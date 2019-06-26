package cn.edu.sicau.pfdistribution.service.kspdistribution

import java.io._
import java.util

import cn.edu.sicau.pfdistribution.entity.{DirectedEdge, DirectedPath, Risk}
import cn.edu.sicau.pfdistribution.service.kspcalculation.{Edge, KSPUtil, ReadExcel}
import cn.edu.sicau.pfdistribution.service.road.KServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.mutable
import scala.collection.JavaConverters._
import scala.collection.mutable.Map

@Service
class CalculateBaseImplementation @Autowired() (val dynamicCosting:KspDynamicCosting,val getParameter:GetParameter,val kServiceImpl: KServiceImpl,val risk: Risk) extends CalculateBaseInterface with Serializable { //,val kServiceImpl:KServiceImpl
 /* @transient
  val readExcel = new ReadExcel()
  @transient
  val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
  val kspUtil = new KSPUtil()
  kspUtil.setGraph(graph)*/
  override def staticOdPathSearch(targetOd: String):mutable.Map[Array[DirectedEdge], Double] = {
   val aList = targetOd.split(" ")
   val sou = aList(0)
   val tar = aList(1)
   /*    val readExcel = new ReadExcel()
       val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
       val kspUtil = new KSPUtil()
       kspUtil.setGraph(graph)
       val ksp = kspUtil.computeODPath(sou,tar,2)*/
   val ksp = kServiceImpl.computeDynamic(sou,tar, "PARAM_NAME", "RETURN_ID",risk)
   val iter = ksp.iterator()
   var text:mutable.Map[Iterator[DirectedEdge], Double] = mutable.Map()
   var text1:mutable.Map[Array[DirectedEdge], Double] = mutable.Map()
   while(iter.hasNext) {
     val p = iter.next()
     //      一条路径的站点构成
     val nodesIter = p.getEdges.iterator()
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
  override def dynamicOdPathSearch(targetOd: String):mutable.Map[Array[DirectedEdge], Double] = {
    val aList = targetOd.split(" ")
    val sou = aList(0)
    val tar = aList(1)
/*    val readExcel = new ReadExcel()
    val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
    val kspUtil = new KSPUtil()
    kspUtil.setGraph(graph)
    val ksp = kspUtil.computeODPath(sou,tar,2)*/
    val ksp = kServiceImpl.computeDynamic(sou,tar, "PARAM_NAME", "RETURN_ID",risk)
    val iter = ksp.iterator()
    var text:mutable.Map[Iterator[DirectedEdge], Double] = mutable.Map()
    var text1:mutable.Map[Array[DirectedEdge], Double] = mutable.Map()
    while(iter.hasNext) {
      val p = iter.next()
      //      一条路径的站点构成
      val nodesIter = p.getEdges.iterator()
      //      println("费用:"  + p.getTotalCost)
      text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
      //      println(text.toList)
    }
    for (key <- text.keys) {
      val myArray = key.toArray
      text1 += (myArray -> text.apply(key))
    }

    return dynamicCosting.cost_Count(text1)
  }

  override def kspDistribution(map: Map[Array[DirectedEdge], Double], x: Int): mutable.Map[Array[DirectedEdge], Double] = {
    val e = Math.E
    val Q = -1*getParameter.getDistributionCoefficient()  //分配系数
    var p = 0.0
    var fenMu = 0.0
    val probability_Passenger = new Array[Double](1000)
    val costMin = map.values.min
    val kspMap = scala.collection.mutable.Map[Array[DirectedEdge], Double]()
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

  override def odDistributionResult(targetOd: String,allKsp:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,Integer]): mutable.Map[Array[DirectedEdge], Double] ={
    val ksp:util.List[DirectedPath] = allKsp(targetOd)
    val passengers:Int = odMap(targetOd).toInt
    val iter = ksp.iterator()
    var text:mutable.Map[Iterator[DirectedEdge], Double] = mutable.Map()
    var text1:mutable.Map[Array[DirectedEdge], Double] = mutable.Map()
    while(iter.hasNext) {
      val p = iter.next()
      //      一条路径的站点构成
      val nodesIter = p.getEdges.iterator()
      //      println("费用:"  + p.getTotalCost)
      text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
      //      println(text.toList)
    }
    for (key <- text.keys) {
      val myArray = key.toArray
      text1 += (myArray -> text.apply(key))
    }
    return kspDistribution(text1, passengers)
  }
  override def odDistributionResultTest(targetOd: String,odMap:mutable.Map[String,Integer]): mutable.Map[Array[DirectedEdge], Double] ={
    /*val OD = targetOd.split(" ")
    val O:String = OD(0)
    val D:String = OD(1)*/
    val OD:Map[String, String] = Map(targetOd -> targetOd)
    val ksp1:util.Map[String, util.List[DirectedPath]] = kServiceImpl.computeStatic(OD.asJava, "PARAM_NAME", "RETURN_ID")
    val ksp:util.List[DirectedPath] = ksp1.get(targetOd)
    val passengers:Int = odMap(targetOd).toInt
      val iter = ksp.iterator()
      var text:mutable.Map[Iterator[DirectedEdge], Double] = mutable.Map()
      var text1:mutable.Map[Array[DirectedEdge], Double] = mutable.Map()
      while(iter.hasNext) {
        val p = iter.next()
        //      一条路径的站点构成
        val nodesIter = p.getEdges.iterator()
        //      println("费用:"  + p.getTotalCost)
        text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
      }
      //      println(text.toList)
      for (key <- text.keys) {
        val myArray = key.toArray
        text1 += (myArray -> text.apply(key))
      }
      return kspDistribution(text1, passengers)
  }

  //动态路径分配
  override def dynamicOdDistributionResult(targetOd: String,odMap:mutable.Map[String,Integer]): mutable.Map[Array[DirectedEdge], Double] ={
    val OD:Map[String, String] = Map(targetOd -> targetOd)
    val ksp1:util.Map[String, util.List[DirectedPath]] = kServiceImpl.computeDynamic(OD.asJava, "PARAM_NAME", "RETURN_ID",risk)
    val ksp:util.List[DirectedPath] = ksp1.get(targetOd)
    val passengers:Int = odMap(targetOd).toInt
    val iter = ksp.iterator()
    var text:mutable.Map[Iterator[DirectedEdge], Double] = mutable.Map()
    var text1:mutable.Map[Array[DirectedEdge], Double] = mutable.Map()
    while(iter.hasNext) {
      val p = iter.next()
      //      一条路径的站点构成
      val nodesIter = p.getEdges.iterator()
      //      println("费用:"  + p.getTotalCost)
      text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
      //      println(text.toList)
    }
    for (key <- text.keys) {
      val myArray = key.toArray
      text1 += (myArray -> text.apply(key))
    }
    return kspDistribution(dynamicCosting.cost_Count(text1), passengers)
  }
  override def tongHaoStaticOdDistributionResult(targetOd: String,odMap:mutable.Map[String,String]): mutable.Map[Array[DirectedEdge], Double] ={
    val OD:Map[String, String] = Map(targetOd -> targetOd)
    val ksp1:util.Map[String, util.List[DirectedPath]] = kServiceImpl.computeStatic(OD.asJava, "PARAM_ID", "RETURN_ID")
    val ksp:util.List[DirectedPath] = ksp1.get(targetOd)
    val passengers:Int = odMap(targetOd).toInt
    val iter = ksp.iterator()
    var text:mutable.Map[Iterator[DirectedEdge], Double] = mutable.Map()
    var text1:mutable.Map[Array[DirectedEdge], Double] = mutable.Map()
    while(iter.hasNext) {
      val p = iter.next()
      //      一条路径的站点构成
      val nodesIter = p.getEdges.iterator()
      //      println("费用:"  + p.getTotalCost)
      text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
      //      println(text.toList)
    }
    for (key <- text.keys) {
      val myArray = key.toArray
      text1 += (myArray -> text.apply(key))
    }
    return kspDistribution(text1, passengers)
  }
  override def tongHaoDynamicOdDistributionResult(targetOd: String,odMap:mutable.Map[String,String]): mutable.Map[Array[DirectedEdge], Double] ={
    val OD:Map[String, String] = Map(targetOd -> targetOd)
    val ksp1:util.Map[String, util.List[DirectedPath]] = kServiceImpl.computeDynamic(OD.asJava, "PARAM_ID", "RETURN_ID",risk)
    val ksp:util.List[DirectedPath] = ksp1.get(targetOd)
    if(ksp == null){
      println("错误OD"+targetOd)
    }
    val passengers:Int = odMap(targetOd).toDouble.toInt
    val iter = ksp.iterator()
    var text:mutable.Map[Iterator[DirectedEdge], Double] = mutable.Map()
    var text1:mutable.Map[Array[DirectedEdge], Double] = mutable.Map()
    while(iter.hasNext) {

      val p = iter.next()
      //      一条路径的站点构成
      val nodesIter = p.getEdges.iterator()
      //      println("费用:"  + p.getTotalCost)
      text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
      //      println(text.toList)
    }
    for (key <- text.keys) {
      val myArray = key.toArray
      text1 += (myArray -> text.apply(key))
    }
    return kspDistribution(dynamicCosting.cost_Count(text1), passengers)
  }

  override def odRegion(map: mutable.Map[Array[DirectedEdge], Double]): mutable.Map[String, Double] = {
    val odMap = scala.collection.mutable.Map[String, Double]()
    for (key <- map.keys) {
      for (i <- 0 to (key.length - 1)) {
        val dEdge: DirectedEdge = key(i)
        val edge: Edge = dEdge.getEdge
        val str =  edge.getFromNode + " " + edge.getToNode
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

  override def odRegionWithTime(map: mutable.Map[Array[DirectedEdge], Double], interval:Int):mutable.Map[String, Double] = {
    val odMap = scala.collection.mutable.Map[String, Double]()
    val intervalTime = interval * 60
    for (key <- map.keys) {
      var count = 0
      var i = 0
      //        for (i <- 0 to (key.length - 2)) {
      while (i < key.length - 1) {
        if (count <= intervalTime) {  //满足条件的区间就累加
          val dEdge: DirectedEdge = key(i)
          val edge: Edge = dEdge.getEdge
          val str =  edge.getFromNode + " " + edge.getToNode
          count += getParameter.getTwoSiteTime(edge.getFromNode, edge.getToNode) + getParameter.getSiteStopTime(edge.getToNode)
          if (odMap.contains(str)) {
            odMap += (str -> (map(key) + odMap(str)))
          }
          else {
            odMap += (str -> map(key))
          }
          i += 1
        } else
          i = key.length  //退出循环的条件
      }
    }
    return odMap
  }
}
