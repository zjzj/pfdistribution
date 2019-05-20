package cn.edu.sicau.pfdistribution.service.kspdistribution

import java.util.Map
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.mutable

//该段代码把Object改成Class定义
@Service
case class MainDistribution @Autowired() (val calBase: CalculateBaseImplementation) {

//该段代码移植到KafkaReceiver中
  def triggerTask(args: Map[String,String]): Unit = {
    val result = intervalResultWithTimeResult()
    result.keys.foreach { i =>
      print("Key = " + i)
      println(" Value = " + result(i))
    }
  }

  //各个OD的路径搜索结果
  def kspCalculateResult():mutable.Map[Array[String], Double] = {
    val conf = new SparkConf().setAppName("kspDistributionResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(calBase.getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.odPathSearch(String)) //各个OD的路径搜索结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y) //对OD分配结果的RDD的整合
    return rddIntegration
  }
  //各个OD的路径分配结果
  def kspDistributionResult():mutable.Map[Array[String], Double] = {
    val conf = new SparkConf().setAppName("kspDistributionResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(calBase.getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.odDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    return rddIntegration
  }

  //返回区间断面的分配结果（静态）
  def intervalResult():mutable.Map[String, Double] = {
    val conf = new SparkConf().setAppName("intervalResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
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
    val conf = new SparkConf().setAppName("intervalResultWithTimeResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(calBase.getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => calBase.dynamicOdDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = calBase.odRegionWithTime(rddIntegration)
    return regionMap
  }

}
