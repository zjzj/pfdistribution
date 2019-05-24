package cn.edu.sicau.pfdistribution.service.kspdistribution

import java.io.Serializable

import org.springframework.stereotype.Service

@Service
class GetParameter extends Serializable{

  //获得分配系数

  def getDistributionCoefficient():Double={
    val coeff:Double = 3.2
    return coeff
  }

  //获得OD列表

  def getOdList():List[String] ={
    val odList:List[String] = List("大坪 牛角沱 1000","较场口 大溪沟 10000")
    return odList
  }

/*  def getTime():Int={   //返回定义的区间粒度时间
    val int_Time: Int = 15
    return int_Time
  }*/

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

  def getTwoSiteTime(beforeSite: String,currentSite: String):Int={
    val time:Int = 5*60
    return time
  }

  //获得换乘时间

  def transferTime():Int={  //具体参数需确认
    val time = 24*60
    return time
  }

  /*  //判断是否换乘
    def DetermineWhetherToTransfer(beforeSite: String,nextSite: String):Boolean={


      return false
    }*/

}
