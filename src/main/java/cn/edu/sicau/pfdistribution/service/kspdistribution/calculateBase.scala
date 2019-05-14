package cn.edu.sicau.pfdistribution.service.kspdistribution


import cn.edu.sicau.pfdistribution.service.kspcalculation.{KSPUtil, ReadExcel}
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters._
import scala.collection.mutable
import util.control.Breaks._
import java.util

import scala.collection.mutable.Map


abstract class calculateBase() extends calculateInterface{

  def odRegionWithTime(map: mutable.Map[Array[String], Double]):mutable.Map[String, Double] = {
    val odMap = scala.collection.mutable.Map[String, Double]()
    val intervalTime = getTime()
    for (key <- map.keys) {
      var count = 0
      var i = 0
      //        for (i <- 0 to (key.length - 2)) {
      while (i < key.length - 2) {
        if (count <= intervalTime) {
          count += getTwoSiteTime(key(i), key(i + 1))
          val str = key(i) + " " + key(i + 1)
          if (odMap.contains(str)) {
            odMap += (str -> (map(key) + odMap(str)))
          }
          else {
            odMap += (str -> map(key))
          }
          i += 1
        } else
          break()
      }
    }
    return odMap
  }

  def getTime():Int={   //返回定义的区间粒度时间
    val int_Time: Int = 15
    return int_Time
  }
  def getTwoSiteTime(siteId1:String,siteId2:String):Int={  //返回两个站点的运行时间
    val twoSiteTime:Int =5
    return twoSiteTime
  }
  def getSiteStopTime(siteId:String):Int={
    val siteStopTime:Int =2
    return siteStopTime
  }
  def intervalResultWithTime(): util.Map[String, Double] = {
    val conf = new SparkConf().setAppName("intervalResultWithTime").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(super.getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => odDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = odRegionWithTime(rddIntegration)
    return regionMap.asJava
  }
}
