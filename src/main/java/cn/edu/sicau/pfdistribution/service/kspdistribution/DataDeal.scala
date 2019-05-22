package cn.edu.sicau.pfdistribution.service.kspdistribution

import cn.edu.sicau.pfdistribution.dao.mysqlsave.RegionSaveInterface
import cn.edu.sicau.pfdistribution.service.kafka.sender.KafkaPfAllocationMessageSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.mutable


@Service
class DataDeal @Autowired()(val save:RegionSaveInterface){


  @Autowired
  val sender:KafkaPfAllocationMessageSender = null

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

}
