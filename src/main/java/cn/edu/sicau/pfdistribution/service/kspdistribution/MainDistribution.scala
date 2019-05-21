package cn.edu.sicau.pfdistribution.service.kspdistribution


import java.io._

import java.util
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import scala.collection.JavaConverters._

import scala.collection.mutable


//该段代码把Object改成Class定义
@Service
case class MainDistribution @Autowired() (val calBase:CalculateBaseImplementation)extends Serializable { //,val getOdList: GetOdList

  @transient
  val conf = new SparkConf().setAppName("PfAllocationApp").setMaster("local[*]")
  @transient
  val sc = new SparkContext(conf)

//该段代码移植到KafkaReceiver中
  def triggerTask(args: Map[String,String]): java.util.Map[String, String]= {
//    val messages: scala.collection.mutable.Map[String, String] = args
    val command:String = args("command")
//    val odList:scala.collection.mutable.Buffer[String] = getOdList.getList(args("startTime"),args("timeInterval").toLong).asScala
    if(command.equals("static")){
      val result:mutable.Map[String, Double] = intervalResult()
      val abc:mutable.Map[String, String] = mapTransfer(result)
      return abc.asJava
    }else
      return mapTransfer(intervalResultWithTimeResult()).asJava
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
  def getDistribution(od:String):util.Map[Array[String], Double] = {
      return calBase.dynamicOdDistributionResult(od).asJava
  }


  //各个OD的路径搜索结果
  def kspCalculateResult():mutable.Map[Array[String], Double] = {
    val rdd = sc.makeRDD(calBase.getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.odPathSearch(String)) //各个OD的路径搜索结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y) //对OD分配结果的RDD的整合
    return rddIntegration
  }
  //各个OD的路径分配结果
  def kspDistributionResult():mutable.Map[Array[String], Double] = {
    val rdd = sc.makeRDD(calBase.getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    return rddIntegration
  }

  //返回区间断面的分配结果（静态）
  def intervalResult():mutable.Map[String, Double] = {
    val odList= calBase.getOdList()
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String))   //各个OD的分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegion(rddIntegration)                              //各个区间的加和结果
    return regionMap
  }

  //按照不同的时间粒度分配形，生成区间密度断面图
  def intervalResultWithTimeResult(): mutable.Map[String, Double] = {
    val rdd = sc.makeRDD(calBase.getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.dynamicOdDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegionWithTime(rddIntegration)
    return regionMap
  }

}
