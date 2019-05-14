package cn.edu.sicau.pfdistribution.service.kspdistribution


import java.util

import cn.edu.sicau.pfdistribution.service.kspcalculation.{KSPUtil, ReadExcel}
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters._
import scala.collection.mutable




trait calculateInterface {

  @Autowired
  val kspUtil:KSPUtil
  val readExcel:ReadExcel

  //计算单个OD的k路径搜索结果
  def odPathSearch(targetOd:String):mutable.Map[Array[String],Double] = {
    val aList = targetOd.split(" ")
    val sou = aList(0)
    val tar = aList(1)
    //    val readExcel = new ReadExcel()
    val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
    //    val kspUtil = new KSPUtil()
    kspUtil.setGraph(graph)
    val ksp = kspUtil.computeODPath(sou,tar,getTransferTimes(targetOd))
    val iter = ksp.iterator()
    //    val passenger = 1000 //OD对的总人数，暂为所有OD设置为1000
    var text:mutable.Map[Iterator[String],Double] = mutable.Map()
    var text1:mutable.Map[Array[String],Double] = mutable.Map()
    while(iter.hasNext) {
      val p = iter.next()
      //      一条路径的站点构成
      val nodesIter = p.getNodes.iterator()
      //      println("费用:"  + p.getTotalCost)
      text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
      //      println(text.toList)
    }
    for (key <- text.keys) {
      val myArray = key.toArray
      text1 += (myArray -> text.apply(key))
    }
    return text1
  }

  //获得换乘次数（集体内容待定）
  def getTransferTimes(targetOd:String):Int={
    val aList = targetOd.split(" ")
    val sou = aList(0)
    val tar = aList(1)
    //    val readExcel = new ReadExcel()
    val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
    //    val kspUtil = new KSPUtil()
    kspUtil.setGraph(graph)
    val ksp = kspUtil.computeODPath(sou,tar,1)
    val iter = ksp.iterator()
    var text:mutable.Map[Iterator[String],Double] = mutable.Map()
    var text1:mutable.Map[Array[String],Double] = mutable.Map()
    while(iter.hasNext) {
      val p = iter.next()
      //      一条路径的站点构成
      val nodesIter = p.getNodes.iterator()
      //      println("费用:"  + p.getTotalCost)
      text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
      //      println(text.toList)
    }
    for (key <- text.keys) {
      val myArray = key.toArray
      text1 += (myArray -> text.apply(key))
    }
    /*
     * 需要写对换乘次数的具体判断方法
     */
    val times = 2
    return times
  }

  //获得站点所在路线(暂留)
  def getSiteLine(siteId:Int):Int={
    return 0
  }


  //调用KSP算法和distribution，迭代计算各个OD对的分配结果
  def odDistributionResult(targetOd:String):mutable.Map[Array[String],Double] = {
    val aList = targetOd.split(" ")
    val sou = aList(0)
    val tar = aList(1)
    //    val readExcel = new ReadExcel()
    val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
    //    val kspUtil = new KSPUtil()
    kspUtil.setGraph(graph)
    val ksp = kspUtil.computeODPath(sou,tar,2)
    val iter = ksp.iterator()
//    val passenger = 1000 //OD对的总人数，暂为所有OD设置为1000
    var text:mutable.Map[Iterator[String],Double] = mutable.Map()
    var text1:mutable.Map[Array[String],Double] = mutable.Map()
    while(iter.hasNext) {
      val p = iter.next()
      //      一条路径的站点构成
      val nodesIter = p.getNodes.iterator()
      //      println("费用:"  + p.getTotalCost)
      text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
      //      println(text.toList)
    }
    for (key <- text.keys) {
      val myArray = key.toArray
      text1 += (myArray -> text.apply(key))
    }
    return distribution(text1, getPassengers(targetOd))
  }


  //获得当前OD的客流人数
  def getPassengers(odStr:String):Int={
    val passengers:Int = 1000
    return passengers
  }

  //计算单个OD对的OD分配结果
  def distribution(map: mutable.Map[Array[String], Double], x: Int):mutable.Map[Array[String],Double] = {
    val e = Math.E
    val Q = -1*getDistributionCoefficient()  //分配系数
    var p = 0.0
    var fenMu = 0.0
    val probability_Passenger = new Array[Double](10)
    val costMin = map.values.min
    val kspMap = scala.collection.mutable.Map[Array[String], Double]()
    for (value <- map.values) {
      //分配概率
      fenMu = fenMu + Math.pow(e, (Q * value / costMin))
    }
    var count = 0
    for (value <- map.values) {
      p = Math.pow(e, (Q * value / costMin)) / fenMu
      val kspPassenger = x.asInstanceOf[Double] * p //计算人数
      probability_Passenger(count) = kspPassenger
      count = count + 1
    }
    val keys = map.keySet
    var count1 = 0
    for (key <- keys) {
      kspMap += (key -> probability_Passenger(count1))
      count1 = count1 + 1
    }
    return kspMap
  }

  //获得分配系数
  def getDistributionCoefficient():Double={
    val coeff:Double = 3.2
    return coeff
  }

  //将分配到各个路径下的结果划分到区间上，返回区间断面图（未考虑时间，每个OD都能到达）
  def odRegion(map: mutable.Map[Array[String], Double]): mutable.Map[String, Double] = {
    val odMap = scala.collection.mutable.Map[String, Double]()
    for (key <- map.keys) {
      for (i <- 0 to (key.length - 2)) {
        val str = key(i) + " " + key(i + 1)
        if (odMap.contains(str)) {
          odMap += (str -> (map(key) + odMap(str)))
        }
        else {
          odMap += (str -> map(key))
        }
      }
    }
    return odMap
  }

//  //按照不同的时间粒度分配形，生成区间密度断面图
//  def odRegionWithTime(map: mutable.Map[Array[String], Double]):Any={}

  //各个OD的路径搜索结果
  def kspCalculateResult():util.Map[Array[String], Double] = {
    val conf = new SparkConf().setAppName("kspDistributionResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => odPathSearch(String))   //各个OD的路径搜索结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    return rddIntegration.asJava
  }

  //各个OD的路径分配结果
  def kspDistributionResult():util.Map[Array[String], Double] = {
    val conf = new SparkConf().setAppName("kspDistributionResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => odDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    return rddIntegration.asJava
  }

  //返回区间断面的分配结果
  def intervalResult():util.Map[String, Double] = {
    val conf = new SparkConf().setAppName("intervalResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => odDistributionResult(String))   //各个OD的分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = odRegion(rddIntegration)                              //各个区间的加和结果
    return regionMap.asJava
  }


  def getOdList():List[String] ={
    val odList:List[String] = List{"二桥公园-_南京地铁1号线 珠江路-_南京地铁1号线"}
    return odList
  }
}
