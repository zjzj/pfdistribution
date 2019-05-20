package cn.edu.sicau.pfdistribution.service.kspdistribution

import cn.edu.sicau.pfdistribution.dao.mysqlsave.RegionSaveInterface
import cn.edu.sicau.pfdistribution.service.kafka.sender.KafkaSender
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.mutable

class DataDeal {

  @Autowired
  val save:RegionSaveInterface = null

  @Autowired
  val sender:KafkaSender = null

  def kspDistributionDataSave(data:mutable.Map[Array[String], Double])= {
    for (key <- data.keys) {
      var str: String = ""
      for (i <- 0 to (key.length - 1)) {
        str = str + "," + key(i)
      }
      save.kspregionadd(str, data(key))
    }
  }
  def intervalDataSave(data:mutable.Map[String, Double])= {
    for (key <- data.keys) {
      save.kspregionadd(key, data(key))
    }
  }

  def intervalDataSend(data:mutable.Map[String, Double])= {
    for (key <- data.keys) {
      val a:Int = data(key).toInt
      val message:String = key+","+a.toString
      sender.send(message)
    }
  }

}
