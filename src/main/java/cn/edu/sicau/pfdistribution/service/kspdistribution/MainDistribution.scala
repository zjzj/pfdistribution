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
case class MainDistribution @Autowired() (calBase:CalculateBaseInterface,getOdList: GetOdList,getParameter: GetParameter,kServiceImpl:KServiceImpl,dataDeal: DataDeal,tongHaoReturnResult: TongHaoReturnResult,getLineID:GetLineID,mysqlGetID: MysqlGetID

)extends Serializable { //,val getOdList: GetOdList

  @transient
  val conf = new SparkConf().setAppName("PfAllocationApp").setMaster("local[*]")
  @transient
  val sc = new SparkContext(conf)
//该段代码移植到KafkaReceiver中
  /*
   *intervalTriggerTask方法用于返回静态和动态的区间分配结果
   * args（需要传入的执行命令Map）eg：Map("command", command；"startTime", startTime;"timeInterval", timeInterval;"predictionInterval", predictionInterval)
   * return  ava.util.Map（section->passengers）
   */
  val dayT = "20190901"
  val hourT = "6"
  def intervalTriggerTask(args: Map[String,String]): java.util.Map[String, String]= {
    getLineID.setCZ_ID()
    val command:String = args("command")
    val time  = args("timeInterval")
    //从数据库获得AFC历史数据
    val odMapObtain:mutable.Map[String,Integer] = mysqlGetID.test_CQ_od(dayT,hourT).asScala
    val odMap = odMapTransferScala(odMapObtain)
    println("OD条数"+odMap.keys.size)
    if(command.equals("static")){
      return mapTransfer(intervalResultTest(odMap,dayT.toString,hourT.toString)).asJava
    }else
      return mapTransfer(intervalResultWithTimeResult(odMap,time.toInt)).asJava
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
    //val od:scala.collection.mutable.Buffer[String] = getOdList.odFromOracleToList().asScala

    //测试OD
    val od:scala.collection.mutable.Buffer[String] = getOdList.testOdList().asScala
    val odList:List[String] = od.toList
    val odMap = test(odList)
    /*//将OD的列表转换为Map
    val odMap = odListToOdMap(odList)*/
    println("OD条数"+odMap.keys.size)
    //将OD的Map转换为java的Map
    /*val odJavaMap = odMapToJavaOdMap(odMap)
    println("长度"+odJavaMap.keySet().size())*/
    //调用路径搜索方法，获得所有OD的k路径
    //val allKspMap:mutable.Map[String, util.List[DirectedPath]] = kServiceImpl.computeDynamic(odMap.asJava,"PARAM_ID", "RETURN_ID").asScala
    if(command.equals("static")){
      tongHaoKspStaticDistributionResult(odMap)
    }else
      tongHaoKspDynamicDistributionResult(odMap)
  }
  def test(ID:List[String]):mutable.Map[String, String]={
    var transfer:mutable.Map[String, String] = mutable.Map()
    val id:Array[String] = ID.toArray
    for(i<-id){
      for(j<-id){
        val str = i + " " + j
        if(!i.equals(j))
          transfer += (str -> "1000")
      }
    }
    return transfer
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

  def tongHaoKspStaticDistributionResult(odMap:mutable.Map[String,String]):Unit ={
    val odList:List[String] = odMap.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.tongHaoStaticOdDistributionResult(String,odMap))   //各个OD的路径分配结果
    //对OD分配结果的RDD的整合
    val rddIntegration:mutable.Map[Array[DirectedEdge], Double] = odDistributionRdd.reduce((x, y) => x ++ y)
    //处理得到tonghao的路径分配结果
    kspDistributionTransfer(rddIntegration)
  }

  def tongHaoKspDynamicDistributionResult(odMap:mutable.Map[String,String]):Unit={
    val odList:List[String] = odMap.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.tongHaoDynamicOdDistributionResult(String,odMap))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    kspDistributionTransfer(rddIntegration)
  }

  //返回区间断面的分配结果（静态）
  def intervalResult(allKsp:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,Integer]):mutable.Map[String, Double] = {
    val odList:List[String] = allKsp.keySet.toList
    val rdd = sc.makeRDD(odList)
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String,allKsp,odMap))   //各个OD的分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    println("ksp"+rddIntegration.keys.size)
    val regionMap = calBase.odRegion(rddIntegration) //各个区间的加和结果
    println("section:"+regionMap.keys.size)
    displayResult(regionMap)
    return regionMap
  }
  def intervalResultTest(odMap:mutable.Map[String,Integer],dayT:String,hourT:String):mutable.Map[String, Double] = {
    val odList:List[String] = odMap.keySet.toList
      val rdd = sc.makeRDD(odList)
      val odDistributionRdd = rdd.map(String => calBase.odDistributionResultTest(String,odMap))   //各个OD的分配结果
      val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
      println("ksp"+rddIntegration.keys.size)
      val regionMap = calBase.odRegion(rddIntegration) //各个区间的加和结果
      println("section:"+regionMap.keys.size)
      //displayResult(regionMap)
      dataDeal.sectionDataSave(regionMap,dayT,hourT)
      return regionMap
  }

  //按照不同的时间粒度分配形，生成区间密度(动态)
  def intervalResultWithTimeResult(odMap:mutable.Map[String,Integer],interval:Int): mutable.Map[String, Double] = {
    val odList:List[String] = odMap.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.dynamicOdDistributionResult(String,odMap))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegionWithTime(rddIntegration,interval:Int)
    displayResult(regionMap)
    return regionMap
  }

  //rest接口调用
  def getDistribution(od:String):Object = {
    getLineID.setCZ_ID()
    val data1:mutable.Map[Array[DirectedEdge], Double] = calBase.staticOdPathSearch(od)
    val data2:mutable.Map[Array[DirectedEdge], Double] = calBase.dynamicOdPathSearch(od)
    var result: Map[String, Double] = Map()
    val minPathMap1:Array[DirectedEdge]=minWeightPath(data1)
    val minPathMap2:Array[DirectedEdge]=minWeightPath(data2)
    val minDataArray:Array[Array[DirectedEdge]] = Array(minPathMap1,minPathMap2)
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
     println("passengers:"+ data(key).toInt)
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
    for (i <- odList.toArray) {
      val od = i.split(" ")
      val str = od(0) + " " + od(1)
      if (od(0) != od(1)) {
        if (transfer.contains(str)) {
          val v1: Int = transfer(str).toInt
          val v2: Int = od(2).toInt
          val valueAll = v1 + v2
          transfer += (str -> valueAll.toString)
        } else
          transfer += (str -> od(2))
      }
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
      val OD = key.split(" ")
      if(OD(0)!=OD(1))
        if (transfer.contains(key)) {
          val v1: Int = transfer(key).toInt
          val v2: Int = map(key)
          val valueAll = v1 + v2
          transfer += (key -> valueAll.toString)
        } else
          transfer += (key -> map(key).toString)
    }
    return transfer.asJava
  }
  def odMapTransferScala(map:mutable.Map[String,Integer]):mutable.Map[String,Integer] = {
    var transfer:mutable.Map[String, Integer] = mutable.Map()
    for (key <- map.keys){
      val OD = key.split(" ")
      if(OD(0)!=OD(1))
        transfer += (key -> map(key))
    }
    return transfer
  }
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
