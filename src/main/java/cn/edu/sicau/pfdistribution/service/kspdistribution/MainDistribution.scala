package cn.edu.sicau.pfdistribution.service.kspdistribution


import java.io._
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import scala.collection.JavaConverters._

import scala.collection.mutable


//该段代码把Object改成Class定义
@Service
case class MainDistribution @Autowired() (val calBase:CalculateBaseInterface,val getOdList: GetOdList)extends Serializable { //,val getOdList: GetOdList

  @transient
  val conf = new SparkConf().setAppName("PfAllocationApp").setMaster("local[*]")
  @transient
  val sc = new SparkContext(conf)

//该段代码移植到KafkaReceiver中
  def triggerTask(args: Map[String,String]): java.util.Map[String, String]= {
    val command:String = args("command")
    val time  = args("timeInterval")
    val odListObtain:scala.collection.mutable.Buffer[String] = getOdList.getList(args("startTime"),args("timeInterval").toLong).asScala
    val odList = odListObtain.toList
    if(command.equals("static")){
      return mapTransfer(intervalResult(odList)).asJava
    }else
      return mapTransfer(intervalResultWithTimeResult(odList,time.toInt)).asJava
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
    return result.map{case(k,v)=>(k,v)}.asJava;
  }

  //各个OD的路径分配结果
  def kspDistributionResult(odList:List[String]):mutable.Map[Array[String], Double] = {
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    return rddIntegration
  }

  //返回区间断面的分配结果（静态）
  def intervalResult(odList:List[String]):mutable.Map[String, Double] = {
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String))   //各个OD的分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegion(rddIntegration)                              //各个区间的加和结果
    return regionMap
  }

  //按照不同的时间粒度分配形，生成区间密度(动态)
  def intervalResultWithTimeResult(odList:List[String],interval:Int): mutable.Map[String, Double] = {
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.dynamicOdDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegionWithTime(rddIntegration,interval:Int)
    return regionMap
  }

}
