package cn.edu.sicau.pfdistribution.service.kspdistribution

import java.io._
import java.util

import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path
import cn.edu.sicau.pfdistribution.service.road.KServiceImpl
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._
import scala.collection.mutable


//该段代码把Object改成Class定义
@Service
case class MainDistribution @Autowired() (calBase:CalculateBaseInterface,getOdList: GetOdList,getParameter: GetParameter,val kServiceImpl:KServiceImpl,val dataDeal: DataDeal)extends Serializable { //,val getOdList: GetOdList

  @transient
  val conf = new SparkConf().setAppName("PfAllocationApp").setMaster("local[*]")
  @transient
  val sc = new SparkContext(conf)

//该段代码移植到KafkaReceiver中

  def intervalTriggerTask(args: Map[String,String]): java.util.Map[String, String]= {
    val command:String = args("command")
    val time  = args("timeInterval")
    val odMapObtain:mutable.Map[String,Integer] = getOdList.getOdMap(args("startTime"),args("timeInterval").toLong).asScala
    val odListStaticTest = odListTransfer(odMapObtain)
    val odMapDynamicTest:mutable.Map[String,Integer] = getParameter.getOdMap().asScala
    val odMap = odMapTransfer(odMapDynamicTest)
    val allKsp:mutable.Map[String, util.List[Path]] = kServiceImpl.computeDynamic(odMap).asScala
    if(command.equals("static")){

      return mapTransfer(intervalResult(odListStaticTest)).asJava
    }else
      return mapTransfer(intervalResultWithTimeResult(allKsp,odMapDynamicTest,time.toInt)).asJava
  }

  def triggerTask(args: Map[String,String]): java.util.Map[String, String]= {
    val command:String = args("command")
    val time  = args("timeInterval")
//
    val od:scala.collection.mutable.Buffer[String] = getOdList.odFromOracleToList().asScala
    val odList:List[String] = od.toList
    val odMap = odListToOdMap(odList)
    val odJavaMap = odMapToJavaOdMap(odMap)
    val allKspMap:mutable.Map[String, util.List[Path]] = kServiceImpl.computeDynamic(odJavaMap).asScala
    if(command.equals("static")){
      tongHaoKspStaticDistributionResult(allKspMap,odMap)
      return Map("动态路径分配测试"->"成功").asJava
    }else
      tongHaoKspDynamicDistributionResult(allKspMap,odMap)
      return Map("动态路径分配测试"->"成功").asJava
  }

  //rest接口调用
  def getDistribution(od:String):Object = {
    val data:mutable.Map[Array[String],Double] = calBase.dynamicOdPathSearch(od)
    var result: Map[String, Double] = Map()
    for (key <- data.keys) {
      var str:String = key(0)
      for (i <- 1 to (key.length - 1)) {
        str = str + "," + key(i)
      }
      result += (str -> data(key))
    }
    return result.map{case(k,v)=>(k,v)}.asJava
  }

  //各个OD的路径分配结果
  def kspDistributionResult(odList:List[String]):mutable.Map[Array[String], Double] = {
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    return rddIntegration
  }

  def tongHaoKspStaticDistributionResult(allKspMap:mutable.Map[String, util.List[Path]],odMap:mutable.Map[String,String]) ={
    val odList:List[String] = allKspMap.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.tongHaoStaticOdDistributionResult(String,allKspMap,odMap))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    dataDeal.tongHaoKspDataSave(rddIntegration)
  }

  def tongHaoKspDynamicDistributionResult(allKspMap:mutable.Map[String, util.List[Path]],odMap:mutable.Map[String,String])={
    val odList:List[String] = allKspMap.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.tongHaoDynamicOdDistributionResult(String,allKspMap,odMap))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    dataDeal.tongHaoKspDataSave(rddIntegration)
  }

  //返回区间断面的分配结果（静态）
  def intervalResult(odList:List[String]):mutable.Map[String, Double] = {
    val rdd = sc.makeRDD(odList)
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String))   //各个OD的分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegion(rddIntegration)                              //各个区间的加和结果
    return regionMap
  }

  //按照不同的时间粒度分配形，生成区间密度(动态)
  def intervalResultWithTimeResult(allKsp:mutable.Map[String, util.List[Path]],odMap:mutable.Map[String,Integer],interval:Int): mutable.Map[String, Double] = {
    val odList:List[String] = allKsp.keySet.toList
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.dynamicOdDistributionResult(String,allKsp,odMap))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegionWithTime(rddIntegration,interval:Int)
    return regionMap
  }




  def odListToOdMap(odList:List[String]):mutable.Map[String, String]={
    var transfer:mutable.Map[String, String] = mutable.Map()
    for (i <- odList.toArray){
      val od = i.split(" ")
      transfer += (od(0)+" "+od(1) -> od(2))
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
      val od = key.split(" ")
      transfer += (od(0) -> od(1))
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
