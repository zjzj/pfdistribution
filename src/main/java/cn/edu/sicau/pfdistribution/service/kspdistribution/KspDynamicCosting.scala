package cn.edu.sicau.pfdistribution.service.kspdistribution

import org.springframework.stereotype.Service

import scala.collection.mutable


@Service
class KspDynamicCosting {

  def stationOperatingCosts(beforeSite: String, currentSite: String): Double = {
    val a = getA()
    val b = getB() //校正系数
    val seat = getSeat() //列车座位数
    val max_p = getMaxPassengers() //列车最大乘客数
    val passengers = intervalPassenger()//区间载客量
    var crowded_degree:Double = 0
    //拥挤度
    var cost:Double = 0
    if (passengers <= seat)
      crowded_degree = 0
    else if (passengers >= seat && passengers <= max_p)
      crowded_degree = a * (passengers - seat) / seat
    else
      crowded_degree = a * (passengers - seat) / seat + b * (seat - max_p) / max_p
    cost = getTwoSiteTime(beforeSite,currentSite) * (1 + crowded_degree) + getSiteStopTime(beforeSite) * (1 + crowded_degree)
    return cost
  }

  def transferFee(currentSite: String) : Double = {
    var cost = 0 //换乘费用
    cost=transferTime()
    return cost
  }

  def perceivedCosts(currentSite: String):Double = {//站台感知费用
    /*
    具体计算预留
     */

    val inPlatformCost = 5*60 //进站站台感知费用
    return inPlatformCost
  }

//  def cost_Count(kspArray:mutable.Map[Array[String],Double]):Double = {
//    var stationOperatingCostsMap = Map()
//    var transferFeeMap = Map()
//    var perceivedCostsMap = Map()
//    var DynamicArray:mutable.Map[Array[String],Double] = mutable.Map()
//    val keys = kspArray.keySet
//    stationOperatingCostsMap += (key(i) -> stationOperatingCosts(key(i),key(i+1))
//      transferFeeMap += (key(i) -> transferFee(key(i+1))
//
//      perceivedCostsMap += (site -> perceivedCosts(site,siteList.head,siteList.last,transferFeeMap(site)))
//  }
//  stationOperatingCostCount = stationOperatingCostsMap.reduceLeft((_,_) => _ + _)
//  transferFeeCount = transferFeeMap.reduceLeft((_,_) => _ + _)
//  perceivedCostsCount = perceivedCostsMap.reduceLeft((_,_) => _ + _)
//  stationOperatingCostCount + transferFeeCount + perceivedCostsCount  //    val siteList : List[String] = sList
//    var stationOperatingCostCount:Double = 0
//    var transferFeeCount:Double = 0
//    var perceivedCostsCount:Double = 0
//    for (key <- kspArray.keys) {
//      for (i <- 0 to (key.length - 2)) {
//
//  }
//  }

    def cost_Count(kspArray:mutable.Map[Array[String],Double]):mutable.Map[Array[String],Double] = {
      var DynamicArray:mutable.Map[Array[String],Double] = mutable.Map()
      for (key <- kspArray.keys) {
        var count:Double = 0
        for (i <- 0 to (key.length - 2)) {
          count += stationOperatingCosts(key(i),key(i+1)) + transferFee(key(i+1)) +perceivedCosts(key(i+1))
        }
        DynamicArray += (key -> count)
      }
      return DynamicArray
    }

  //获得校正系数
  def getA():Double={
    val a = 1
    return a
  }
  def getB():Double={
    val b = 1
    return b
  }
  //获得列车最大座位数
  def getSeat():Int = {
    val seat = 200
    return seat
  }
  //列车最大乘客数
  def getMaxPassengers():Int={
    val max_p = 1468
    return max_p
  }
  //获得区间载客量
  def intervalPassenger():Int={
    val passengers = 1000
    return passengers
  }
  //获得站点停站时间
  def getSiteStopTime(currentSite: String):Int={
    val stopTime:Int = 2 * 60 //秒
    return stopTime
  }

  //获得两站间运行时间
  def getTwoSiteTime(beforeSite: String,currentSite: String):Int={
    val time:Int = 5*60
    return time
  }

  //获得换乘时间
  def transferTime():Int={  //具体参数需确认
    val time = 24*60
    return time
  }

  //判断是否换乘
  def DetermineWhetherToTransfer(beforeSite: String,nextSite: String):Boolean={


    return false
  }
}
