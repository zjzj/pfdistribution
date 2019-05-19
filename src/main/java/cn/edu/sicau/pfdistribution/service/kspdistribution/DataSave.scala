package cn.edu.sicau.pfdistribution.service.kspdistribution

import cn.edu.sicau.pfdistribution.dao.mysqlsave.regionSaveInterface
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.mutable

class DataSave {

  @Autowired
  val save:regionSaveInterface = null

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
