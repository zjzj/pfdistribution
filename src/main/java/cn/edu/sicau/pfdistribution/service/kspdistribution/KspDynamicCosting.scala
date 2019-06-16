package cn.edu.sicau.pfdistribution.service.kspdistribution

import org.springframework.stereotype.Service

import scala.collection.mutable
import java.io._

import scala.collection.JavaConverters._
import cn.edu.sicau.pfdistribution.entity.{DirectedEdge, StationAndSectionPassengers, StationAndSectionRisk}
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge
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

  def perceivedCosts(beforeSite: String, currentSite: String):Double = {//站台感知费用
  /*
  具体计算预留
   */
  //val perceived:Double=0
    val mapTime:mutable.Map[Integer, java.util.List[String]]=getLineID.sectionTime().asScala
    val mapTimeList:mutable.Buffer[String]=mapTime(beforeSite.toInt).asScala
    val mapTimeList1:mutable.Buffer[String]=mapTime(currentSite.toInt).asScala
    val beforeTime=mapTimeList.tail.head
    val currentTime=mapTimeList1.head
    val t=changeTime(beforeTime,currentTime)
    val mapSa:mutable.Map[String, java.util.List[String]]=stationAndSectionPassengers.getStationP.asScala
    val mapSaList:mutable.Buffer[String]=mapSa(currentSite).asScala
    val crowded_degree=mapSaList.head
    val stationPassengers = mapSaList.tail.head.toDouble
    val mapSe:mutable.Map[String, java.util.List[String]]=stationAndSectionPassengers.getSectionP.asScala
    val mapSeList:mutable.Buffer[String]=mapSe(currentSite).asScala
    val sectionPassengers = mapSeList.tail.head.toDouble
    val I:Double=360
    val Fl:Double=1/I
    val Cl:Double=500
    val a = getParameter.getA()
    val b = getParameter.getB() //校正系数
    var Cost:Double=0
    if(Cl-sectionPassengers/(Fl*t)>= stationPassengers)
      Cost=0.5*I
    else if(Cl-sectionPassengers/(Fl*t)< stationPassengers)
      Cost=0.5*I+a*Math.pow((stationPassengers/t)/(Fl*Cl-sectionPassengers/t),b)
    val inPlatformCost = 5*60 //进站站台感知费用
    return inPlatformCost
  }

  def cost_Count(kspArray:mutable.Map[Array[DirectedEdge], Double]):mutable.Map[Array[DirectedEdge], Double] = {
    var DynamicArray:mutable.Map[Array[DirectedEdge], Double] = mutable.Map()
    for (key <- kspArray.keys) {
      var count:Double = 0
      var n:Int=1
      //判断路径有无换乘和换乘次数
      val CZMap:mutable.Map[Integer, Integer]=getLineID.GetCZ_ID().asScala
      for (i <- 0 to (key.length - 2)) {
        val dEdge1: DirectedEdge = key(i)
        val eg1: Edge = dEdge1.getEdge
        val dEdge2: DirectedEdge = key(i+1)
        val eg2: Edge = dEdge2.getEdge
        val a=eg1.getFromNode
        val b=eg2.getToNode
        if(CZMap(a.toInt)!= CZMap(b.toInt)){
          n=n+1
        }
      }
      //计算总费用
      for (i <- 0 to (key.length - 2)) {
        val dEdge3: DirectedEdge = key(i)
        val eg3: Edge = dEdge3.getEdge
        val a=eg3.getFromNode
        val b=eg3.getToNode
        count += stationOperatingCosts(a,b) + n*transferFee() +perceivedCosts(a,b)
      }
      DynamicArray += (key -> count)
    }
    return DynamicArray
  }

  /*def cost_Count1(kspArray:mutable.Map[Array[String],Double]):mutable.Map[Array[String],Double] = {
    var DynamicArray:mutable.Map[Array[String],Double] = mutable.Map()
    for (key <- kspArray.keys) {
      var count:Double = 0
      var n:Int=1
      //判断路径有无换乘和换乘次数
      val CZMap:mutable.Map[Integer, Integer]=getLineID.GetCZ_ID().asScala
      for (i <- 0 to (key.length - 3)) {
        val a=key(i).toInt
        val b=key(i+2).toInt
        if(CZMap(a)!= CZMap(b)){
          n=n+1
        }
      }
      //计算总费用
      for (i <- 0 to (key.length - 2)) {
        count += stationOperatingCosts(key(i),key(i+1)) + n*transferFee() +perceivedCosts(key(i),key(i+1))
      }
      DynamicArray += (key -> count)
    }
    return DynamicArray
  }*/
  def changeTime(before:String,after:String):Double={//格式为小时.分钟.秒的字符串转化为Double类型的秒
  val beforeArray=before.split(".")
    val afterArray=after.split(".")
    val beTime=beforeArray(0).toDouble*3600+beforeArray(1).toDouble*60+beforeArray(2).toDouble
    val afTime=afterArray(0).toDouble*3600+afterArray(1).toDouble*60+afterArray(2).toDouble
    val time =afTime-beTime
    return time
  }
}
