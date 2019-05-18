package cn.edu.sicau.pfdistribution.service.kspdistribution


import cn.edu.sicau.pfdistribution.service.kspcalculation.{KSPUtil, ReadExcel}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.JavaConverters._
import scala.collection.mutable.Map
import scala.util.control.Breaks.break

class interfaceImplementation() extends calculateBaseInterface{

  val dynamicCosting = new kspDynamicCosting
  override def odPathSearch(targetOd: String):mutable.Map[Array[String],Double] = {
    val aList = targetOd.split(" ")
    val sou = aList(0)
    val tar = aList(1)
    val readExcel = new ReadExcel()
    val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
    val kspUtil = new KSPUtil()
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

  override def getTransferTimes(targetOd: String):Int={
    val aList = targetOd.split(" ")
    val sou = aList(0)
    val tar = aList(1)
    val readExcel = new ReadExcel()
    val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
    val kspUtil = new KSPUtil()
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

  override def distribution(map: Map[Array[String], Double], x: Int): Map[Array[String], Double] = {
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

  override def odDistributionResult(targetOd: String): mutable.Map[Array[String],Double] ={
    // val ksp = EppsteinUtil.getOneODPair("data/cd.txt", "一品天下-2_7", "天府广场-4_1", 2)
    val aList = targetOd.split(" ")
    val sou = aList(0)
    val tar = aList(1)
    val readExcel = new ReadExcel()
    val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
    val kspUtil = new KSPUtil()
    kspUtil.setGraph(graph)
    val ksp = kspUtil.computeODPath(sou,tar,getTransferTimes(targetOd))
    val iter = ksp.iterator()
    val passenger = 1000 //OD对的总人数，暂为所有OD设置为1000
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

  //动态路径分配
  def dynamicOdDistributionResult(targetOd: String): mutable.Map[Array[String],Double] ={
    // val ksp = EppsteinUtil.getOneODPair("data/cd.txt", "一品天下-2_7", "天府广场-4_1", 2)
    val aList = targetOd.split(" ")
    val sou = aList(0)
    val tar = aList(1)
    val readExcel = new ReadExcel()
    val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
    val kspUtil = new KSPUtil()
    kspUtil.setGraph(graph)
    val ksp = kspUtil.computeODPath(sou,tar,getTransferTimes(targetOd))
    val iter = ksp.iterator()
    val passenger = 1000 //OD对的总人数，暂为所有OD设置为1000
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
    return distribution(dynamicCosting.cost_Count(text1), getPassengers(targetOd))
  }

  override def odRegion(map: mutable.Map[Array[String], Double]): mutable.Map[String, Double] = {
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

  override def odRegionWithTime(map: mutable.Map[Array[String], Double]):mutable.Map[String, Double] = {
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

  override def kspDistributionResult():mutable.Map[Array[String], Double] = {
    val conf = new SparkConf().setAppName("kspDistributionResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => odDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    return rddIntegration
  }

  override def kspCalculateResult():mutable.Map[Array[String], Double] = {
    @transient
    val conf = new SparkConf().setAppName("kspDistributionResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => odPathSearch(String)) //各个OD的路径搜索结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y) //对OD分配结果的RDD的整合
    return rddIntegration
  }

  override def intervalResult():mutable.Map[String, Double] = {
    val conf = new SparkConf().setAppName("intervalResult").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val odList= getOdList()
    val rdd = sc.makeRDD(odList)
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => odDistributionResult(String))   //各个OD的分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = odRegion(rddIntegration)                              //各个区间的加和结果
    return regionMap
  }

  override def intervalResultWithTimeResult(): mutable.Map[String, Double] = {
    val conf = new SparkConf().setAppName("intervalResultWithTime").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(getOdList())
    //od对，起点与终点与用空格连接
    val odDistributionRdd = rdd.map(String => dynamicOdDistributionResult(String))   //各个OD的路径分配结果
    val rddIntegration = odDistributionRdd.reduce((x, y) => x ++ y)      //对OD分配结果的RDD的整合
    val regionMap = odRegionWithTime(rddIntegration)
    return regionMap
  }


  //获得当前OD的客流人数
  def getPassengers(odStr:String):Int={
    val passengers:Int = 1000
    return passengers
  }

  //获得分配系数
  def getDistributionCoefficient():Double={
    val coeff:Double = 3.2
    return coeff
  }

  //获得OD列表
  def getOdList():List[String] ={
    val odList:List[String] = List("二桥公园-_南京地铁1号线 珠江路-_南京地铁1号线","吉祥庵-_南京地铁1号线 花神庙-_南京地铁1号线  1.0","雨润大街-_南京地铁二号线 孝陵卫-_南京地铁二号线  1.0")
    return odList
  }
  def getTime():Int={   //返回定义的区间粒度时间
    val int_Time: Int = 15
    return int_Time
  }
  def getTwoSiteTime(siteId1:String,siteId2:String):Int={  //返回两个站点的运行时间
    val twoSiteTime:Int =5
    return twoSiteTime
  }
  def getSiteStopTime(siteId:String):Int={   //获得两站间停止时间
    val siteStopTime:Int =2
    return siteStopTime
  }


}
