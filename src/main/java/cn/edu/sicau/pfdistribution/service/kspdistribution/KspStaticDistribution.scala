package cn.edu.sicau.pfdistribution.service.kspdistribution

import java.io.{File, PrintWriter}

import cn.edu.sicau.pfdistribution.service.kspcalculation.{KSPUtil, ReadExcel}
import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * @author tanhubo
  * @date 2019/4/21
  */
case class KspStaticDistribution() {
//  def kspDistributionTest(): Unit = {
//    var oDMap:mutable.Map[Array[String],Double] = mutable.Map()
//    oDMap = odDistributionResult("二桥公园-_南京地铁1号线 珠江路-_南京地铁1号线")
//    val writer = new PrintWriter(new File("G:/工作室/铁路客流预测/distributionTest.txt" ))
//
//    for(key <- oDMap.keys){
//      while(key.hasNext){
////        print(key.next())
////        print("+")
//        writer.write(key.next())
//        writer.write(" ")
//
//      }
////      println(oDMap(key))
//      writer.write(oDMap(key).formatted("%.0f"))
//      writer.write("\n")
//    }
//    writer.close()
//  }
//  def distribution(map:mutable.Map[Array[String],Double], x:Int): mutable.Map[Array[String],Double] = {
//    val e=Math.E
//    val Q= -3.2
//    var p=0.0
//    var fenMu=0.0
//    val probability_Passenger= new Array[Double](10)
//    val costMin=map.values.min
//    var kspMap=scala.collection.mutable.Map[Array[String],Double]()
//    for(value <- map.values){//分配概率
//      fenMu = fenMu + Math.pow(e, Q*value / costMin)
//    }
//    var count =0
//    for(value <- map.values){
//      count +=1
//      p = Math.pow(e, Q * value / costMin) / fenMu
//      val kspPassenger = x.asInstanceOf[Double] * p//计算人数
//      probability_Passenger(count)= kspPassenger
//    }
//    val keys = map.keySet
//    var count1=0
//    for(key <- keys){
//      count1 += 1
//      kspMap += (key -> probability_Passenger(count1))
//    }
//    return kspMap
//    //    kspMap.keys.foreach{ i =>
//    //      print( "Key = " + i )
//    //      println(" Value = " + kspMap(i) )}
//  }
//
//  def odDistributionResult(targetOd:String): mutable.Map[Array[String],Double] ={
//    // val ksp = EppsteinUtil.getOneODPair("data/cd.txt", "一品天下-2_7", "天府广场-4_1", 2)
//    val aList = targetOd.split(" ")
//    val sou = aList(0)
//    val tar = aList(1)
//    val readExcel = new ReadExcel()
//    val graph = readExcel.buildGrapgh("data/stationLine.xls", "data/edge.xls")
//    val kspUtil = new KSPUtil()
//    kspUtil.setGraph(graph)
//    val ksp = kspUtil.computeODPath(sou,tar,2)
//    val iter = ksp.iterator()
//    val passenger = 1000 //OD对的总人数，暂为所有OD设置为1000
//    var text:mutable.Map[Iterator[String],Double] = mutable.Map()
//    var text1:mutable.Map[Array[String],Double] = mutable.Map()
//    while(iter.hasNext) {
//      val p = iter.next()
//      //      一条路径的站点构成
//      val nodesIter = p.getNodes.iterator()
//      //      println("费用:"  + p.getTotalCost)
//      text += (nodesIter.asScala -> p.getTotalCost)   //静态费用
//      //      println(text.toList)
//    }
//    for (key <- text.keys) {
//      val myArray = key.toArray
//      text1 += (myArray -> text.apply(key))
//    }
//    return distribution(text1, passenger)
//
//  }
}
