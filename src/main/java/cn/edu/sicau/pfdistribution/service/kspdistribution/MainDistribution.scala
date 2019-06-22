package cn.edu.sicau.pfdistribution.service.kspdistribution

import java.io._
import java.util

import cn.edu.sicau.pfdistribution.MysqlGetID
import cn.edu.sicau.pfdistribution.entity._
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge
import cn.edu.sicau.pfdistribution.service.road.KServiceImpl
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._
import scala.collection.mutable


//该段代码把Object改成Class定义
@Service
case class MainDistribution @Autowired() (calBase:CalculateBaseInterface,getOdList: GetOdList,getParameter: GetParameter,kServiceImpl:KServiceImpl,dataDeal: DataDeal,tongHaoReturnResult: TongHaoReturnResult,getLineID:GetLineID,mysqlGetID: MysqlGetID)extends Serializable { //,val getOdList: GetOdList

  @transient
  val conf = new SparkConf().setAppName("PfAllocationApp").setMaster("local[*]")
  @transient
  val sc = new SparkContext(conf)
  val dayT = "20180903"
  val hourT = "6"

//该段代码移植到KafkaReceiver中
  /*
   *intervalTriggerTask方法用于返回静态和动态的区间分配结果
   * args（需要传入的执行命令Map）eg：Map("command", command；"startTime", startTime;"timeInterval", timeInterval;"predictionInterval", predictionInterval)
   * return  ava.util.Map（section->passengers）
   */
  def intervalTriggerTask(args: Map[String,String]): java.util.Map[String, String]= {
    getLineID.setCZ_ID()
    val command:String = args("command")
    val time  = args("timeInterval")
    //从数据库获得AFC历史数据
    //val odMapObtain:mutable.Map[String,Integer] = getOdList.getOdMap(args("startTime"),args("timeInterval").toLong).asScala
    val odMapObtain:mutable.Map[String,Integer] = mysqlGetID.test_CQ_od(dayT,hourT).asScala
    /*//类型转换
    val odListStaticTest = odListTransfer(odMapObtain)*/
    /*//测试od
    val odMapDynamicTest:mutable.Map[String,Integer] = getParameter.getOdMap().asScala*/
    val odMap = odMapTransfer(odMapObtain)
    val allKsp:mutable.Map[String, util.List[DirectedPath]] = kServiceImpl.computeDynamic(odMap, "PARAM_NAME", "RETURN_NAME").asScala
    if(command.equals("static")){
      return mapTransfer(intervalResult(allKsp,odMapObtain)).asJava
    }else
      return mapTransfer(intervalResultWithTimeResult(allKsp,odMapObtain,time.toInt)).asJava
  }
  /*
   *intervalTriggerTask方法用于返回静态和动态的区间分配结果
   * args（默认执行命令Map）eg：Map("command", dynamic；;"predictionInterval", predictionInterval)
   * return  util.ArrayList[path->passengers]
   */
  def triggerTask(args: Map[String,String]):Unit= {
    getLineID.setCZ_ID()
    val command:String = args("command")
    val time  = args("predictionInterval")
    //从数据库获得需要计算的OD矩阵
    val od:scala.collection.mutable.Buffer[String] = getOdList.odFromOracleToList().asScala
    val odList:List[String] = od.toList
    //将OD的列表转换为Map
    val odMap = odListToOdMap(odList)
    //将OD的Map转换为java的Map
    /*val odJavaMap = odMapToJavaOdMap(odMap)
    println("长度"+odJavaMap.keySet().size())*/
    //调用路径搜索方法，获得所有OD的k路径
    val allKspMap:mutable.Map[String, util.List[DirectedPath]] = kServiceImpl.computeDynamic(odMap.asJava,"PARAM_ID", "RETURN_ID").asScala
    if(command.equals("static")){
      tongHaoKspStaticDistributionResult(allKspMap,odMap)
    }else
      tongHaoKspDynamicDistributionResult(allKspMap,odMap)
  }
  //各个OD的路径分配结果
  def kspDistributionResult(allKsp:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,Integer]):mutable.Map[Array[DirectedEdge], Double] = {
    val odList:List[String] = allKsp.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String,allKsp,odMap))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    return rddIntegration
  }

  def tongHaoKspStaticDistributionResult(allKspMap:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,String]):Unit ={
    val odList:List[String] = allKspMap.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.tongHaoStaticOdDistributionResult(String,allKspMap,odMap))   //各个OD的路径分配结果
    //对OD分配结果的RDD的整合
    val rddIntegration:mutable.Map[Array[DirectedEdge], Double] = odDistributionRdd.reduce((x, y) => x ++ y)
    //处理得到tonghao的路径分配结果
    kspDistributionTransfer(rddIntegration)
  }

  def tongHaoKspDynamicDistributionResult(allKspMap:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,String]):Unit={
    val odList:List[String] = allKspMap.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.tongHaoDynamicOdDistributionResult(String,allKspMap,odMap))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    kspDistributionTransfer(rddIntegration)
  }

  //返回区间断面的分配结果（静态）
  def intervalResult(allKsp:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,Integer]):mutable.Map[String, Double] = {
    val odList:List[String] = allKsp.keySet.toList
    val rdd = sc.makeRDD(odList)
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String,allKsp,odMap))   //各个OD的分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegion(rddIntegration) //各个区间的加和结果
    displayResult(regionMap)
    dataDeal.sectionDataSave(regionMap,dayT,hourT)
    return regionMap
  }

  //按照不同的时间粒度分配形，生成区间密度(动态)
  def intervalResultWithTimeResult(allKsp:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,Integer],interval:Int): mutable.Map[String, Double] = {
    val odList:List[String] = allKsp.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.dynamicOdDistributionResult(String,allKsp,odMap))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegionWithTime(rddIntegration,interval:Int)
    displayResult(regionMap)
    return regionMap
  }

  //rest接口调用
  def getDistribution(od:String):Object = {
    val data1:mutable.Map[Array[DirectedEdge], Double] = calBase.staticOdPathSearch(od)
    var result: Map[String, Double] = Map()
    println(data1)
    val minPathMap1:Array[DirectedEdge]=minWeightPath(data1)
    val minDataArray:Array[Array[DirectedEdge]] = Array(minPathMap1)
    for(i <- 0 to (minDataArray.length -1)) {
      val minPath: Array[DirectedEdge] = minDataArray(i)
      val directedEdge: DirectedEdge = minPath(0)
      val edge: Edge = directedEdge.getEdge
      var str = edge.getFromNode
      for (i <- 1 to (minPath.length - 1)) {
        val dEdge: DirectedEdge = minPath(i)
        val eg: Edge = dEdge.getEdge
        str = str + "," + eg.getFromNode
      }
      val dEdge2: DirectedEdge = minPath(minPath.length - 1)
      val eg2: Edge = dEdge2.getEdge
      str = str + "," + eg2.getToNode
      if(i==0)
        result += (str -> data1(minPath))
    }
    return result.map{case(k,v)=>(k,v)}.asJava
  }
 def displayResult(data:mutable.Map[String, Double]):Unit={
   for (key <- data.keys){
     print("section:"+ key)
     println("passengers:"+ data(key))
   }
 }
  //筛选出所有路径中权值和最小的路径
  def minWeightPath(data:mutable.Map[Array[DirectedEdge], Double]):Array[DirectedEdge]={
    var result:mutable.Map[Array[DirectedEdge],Double] = mutable.Map()
    var minPathWeight:Double = data.values.head
    for(key <- data.keys){
      result += (key -> data(key))
      if(data(key)<minPathWeight){
        minPathWeight = data(key)
        if(result.isEmpty)
          result += (key -> data(key))
        else {
          result.clear()
          result += (key -> data(key))
        }
      }
    }
    return result.keys.head
  }

  //返回通号需要的路径结果
  def kspDistributionTransfer(rddIntegration:mutable.Map[Array[DirectedEdge], Double]): Unit = {
    var len: Int = 0
    for (i <- rddIntegration.keys)
      len = len + 1
    val kspArray: util.ArrayList[TongHaoPathType] = new util.ArrayList()
    for (key <- rddIntegration.keys) {
      var ksp:TongHaoPathType = new TongHaoPathType()
      val dEdge: DirectedEdge = key(0)
      val edge: Edge = dEdge.getEdge
      var forMatedPath = edge.getFromNode +"-"+ dEdge.getDirection +"-"+ edge.getToNode
      for (i <- 1 to (key.length - 1)) {
        val dEdg: DirectedEdge = key(i)
        val eg: Edge = dEdg.getEdge
        forMatedPath = forMatedPath + "-"+dEdg.getDirection +"-"+ eg.getToNode
      }
      val P:Int = rddIntegration(key).toInt
      ksp.setPath(forMatedPath)
      ksp.setPassengers(P.toString)
      kspArray.add(ksp)
    }
    tongHaoReturnResult.setPathDistribution(kspArray)
  }

  def odListToOdMap(odList:List[String]):mutable.Map[String, String]={
    var transfer:mutable.Map[String, String] = mutable.Map()
    for (i <- odList.toArray){
      val od = i.split(" ")
      val str = od(0)+" "+od(1)
      if(transfer.contains(str)){
        val v1:Int = transfer(str).toInt
        val v2:Int = od(2).toInt
        val valueAll = v1 + v2
        transfer += (str -> valueAll.toString)
      }
      transfer += (str -> od(2))
    }
    return transfer
  }
  def odMapToJavaOdMap(odMap:mutable.Map[String, String]):java.util.Map[String, String] = {
    var transfer:Map[String, String] = Map()
    for (key <- odMap.keys){
      val od = key.split(" ")
      transfer += (od(0) -> od(1))
    }
    return transfer.asJava
  }

  def odListTransfer(map:mutable.Map[String,Integer]):List[String]={
    var odMap:Map[String,String] = Map()
    for(key <- map.keys){
      val str = key + " " + map(key)
      odMap += (key -> str)
    }
    return odMap.values.toList
  }

  def odMapTransfer(map:mutable.Map[String,Integer]):java.util.Map[String, String] = {
    var transfer:Map[String, String] = Map()
    for (key <- map.keys){
      transfer += (key -> map(key).toString)
    }
    return transfer.asJava
  }
  /*  def odMapTransfer(map:mutable.Map[String, util.List[Path]]):mutable.Map[String, List[Path]] = {
      var transfer:mutable.Map[String, List[Path]] = mutable.Map()
      for (key <- map.keys){
        val v:mutable.Buffer[Path] = map(key).asScala
        val vs:List[Path] = v.toList
        transfer += (key -> vs)
      }
      return transfer
    }*/
  def mapTransfer(map:mutable.Map[String, Double]):mutable.Map[String, String]={
    var transfer:mutable.Map[String, String] = mutable.Map()
    for(key <- map.keys){
      val K:Int = map(key).toInt
      val V:String = K.toString
      transfer += (key -> V)
    }
    return transfer
  }

}
