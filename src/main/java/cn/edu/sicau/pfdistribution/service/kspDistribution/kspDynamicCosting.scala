package cn.edu.sicau.pfdistribution.service.kspDistribution

import cn.edu.sicau.pfdistribution.dao.yxt.YxtCalcInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class kspDynamicCosting {
  @Autowired var yxtCalcInterface: YxtCalcInterface = null

  def main(ars: Array[String]): Unit = {

  }
//  def stationOperatingCosts(passengers: Int, beforeSiteId: Int, currentSiteId: Int): Double = {
//    val a = 1
//    val b = 1 //校正系数
//    val seat = 50 //列车座位数
//    val max_p = 80 //列车最大乘客数
//    var crowded_degree = 0
//    //拥挤度
//    var cost = 0
//    if (passengers <= seat)
//      crowded_degree = 0
//    else if (passengers >= seat && passengers <= max_p)
//      crowded_degree = a * (passengers - seat) / seat
//    else
//      crowded_degree = a * (passengers - seat) / seat + b * (seat - max_p) / max_p
//    if (beforeSiteId == 0)
//      cost = yxtCalcInterface.GetDistanceOfTwoStation(beforeSiteId,currentSiteId,)* (1 + crowded_degree)
//    else
//      cost = yxtCalcInterface.GetDistanceOfTwoStation(beforeSiteId,currentSiteId,) * (1 + crowded_degree) + yxtCalcInterface.GetTrainStopTime( ,) * (1 + crowded_degree)
//    return cost
//  }
//
//  def transferFee(currentSiteId:Int) : Double = {
//    var cost = 0 //换乘费用
//    if(yxtCalcInterface.CheckStationIsHcz(currentSiteId)){ //判断是否为换乘站点
//        ...
//    }
//    return cost
//  }
//
//  def perceivedCosts(site:String,headSite:String,lastSite:String,transferSite:Double):Double = {//站台感知费用
//  val inPlatformCost = 5 //进站站台感知费用
//  val outPlatformCost = 2 //换乘站台感知费用
//    if(site == headSite || site == lastSite)
//      return inPlatformCost
//    else if(transferSite != 0)
//      return outPlatformCost
//    else
//      return 0
//  }
//
//  def cost_Count(kspIterator:Iterator[String],passengers:Int,journeyTime:Double,stopTime:Double):Double = {
//    var stationOperatingCostsMap = Map()
//    var transferFeeMap = Map()
//    var perceivedCostsMap = Map()
//    //    val siteList : List[String] = sList
//    var stationOperatingCostCount = 0
//    var transferFeeCount = 0
//    var perceivedCostsCount = 0
//    while(kspIterator.hasNext){
//      val site = kspIterator.next()
//      stationOperatingCostsMap += (site -> stationOperatingCosts(passengers,journeyTime,stopTime,siteList.head,site))
//    }
//    for(i <- 1 to siteList.length-2){
//      transferFeeMap += (siteList(i) -> transferFee(siteList(i-1),siteList(i),siteList(i+1)))
//    }
//    for(site <- siteList){
//      perceivedCostsMap += (site -> perceivedCosts(site,siteList.head,siteList.last,transferFeeMap(site)))
//    }
//    stationOperatingCostCount = stationOperatingCostsMap.reduceLeft((_,_) => _ + _)
//    transferFeeCount = transferFeeMap.reduceLeft((_,_) => _ + _)
//    perceivedCostsCount = perceivedCostsMap.reduceLeft((_,_) => _ + _)
//    stationOperatingCostCount + transferFeeCount + perceivedCostsCount
//  }
}
