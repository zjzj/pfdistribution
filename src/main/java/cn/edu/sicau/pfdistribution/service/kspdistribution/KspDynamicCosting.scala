package cn.edu.sicau.pfdistribution.service.kspdistribution

import org.springframework.stereotype.Service

import scala.collection.mutable
import java.io._
import scala.collection.JavaConverters._
import cn.edu.sicau.pfdistribution.entity.{StationAndSectionPassengers, StationAndSectionRisk}
import org.springframework.beans.factory.annotation.Autowired

@Service
class KspDynamicCosting @Autowired()(val getParameter:GetParameter,val stationAndSectionPassengers:StationAndSectionPassengers,val getLineID:GetLineID)extends Serializable {

  def stationOperatingCosts(beforeSite: String, currentSite: String): Double = {
    val a = getParameter.getA()
    val b = getParameter.getB() //校正系数
    val str=beforeSite+ " "+currentSite
    val seat = getParameter.getSeat() //列车座位数
    val max_p = getParameter.getMaxPassengers() //列车最大乘客数
    val mapPassengers:mutable.Map[String, java.util.List[String]]=stationAndSectionPassengers.getSectionP.asScala
    val mapList:mutable.Buffer[String]=mapPassengers(str).asScala
    val s:String=mapList.head
    val passengers=s.toDouble//区间载客量
    //val passengers = getParameter.intervalPassenger()
    var crowded_degree:Double = 0
    //拥挤度
    var cost:Double = 0
    if (passengers <= seat)
      crowded_degree = 0
    else if (passengers >= seat && passengers <= max_p)
      crowded_degree = a * (passengers - seat) / seat
    else
      crowded_degree = a * (passengers - seat) / seat + b * (seat - max_p) / max_p
    cost = getParameter.getTwoSiteTime(beforeSite,currentSite) * (1 + crowded_degree) + getParameter.getSiteStopTime(beforeSite) * (1 + crowded_degree)
    return cost
  }

  def transferFee() : Double = {
    var cost = 0 //换乘费用
    cost= getParameter.transferTime()
    return cost
  }

  def perceivedCosts(currentSite: String):Double = {//站台感知费用
    /*
    具体计算预留
     */
    //val perceived:Double=0
    val map:mutable.Map[String, java.util.List[String]]=stationAndSectionPassengers.getSectionP.asScala
    val mapList:mutable.Buffer[String]=map(currentSite).asScala
    val crowded_degree=mapList.head
    val passengers = mapList.tail.head
    val inPlatformCost = 5*60 //进站站台感知费用
    return inPlatformCost
  }

  def cost_Count(kspArray:mutable.Map[Array[String],Double]):mutable.Map[Array[String],Double] = {
    var DynamicArray:mutable.Map[Array[String],Double] = mutable.Map()
    for (key <- kspArray.keys) {
      var count:Double = 0
      var n:Int=1
      //判断路径有无换乘和换乘次数
      val CZMap:mutable.Map[Integer, Integer]=getLineID.GetCZ_ID().asScala
      for (i <- 0 to (key.length - 2)) {
        val a=key(i).toInt
        val b=key(i+1).toInt
        if(CZMap(a)!= CZMap(b)){
          n=n+1
        }
      }
      //计算总费用
      for (i <- 0 to (key.length - 2)) {
        count += stationOperatingCosts(key(i),key(i+1)) + n*transferFee() +perceivedCosts(key(i+1))
      }
      DynamicArray += (key -> count)
    }
    return DynamicArray
  }

}
