package cn.edu.sicau.pfdistribution.service.kspdistribution

//import cn.edu.sicau.pfdistribution.dao.mysqlsave.RegionSaveInterface
//import cn.edu.sicau.pfdistribution.service.kafka.sender.KafkaPfAllocationMessageSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.mutable


@Service
class DataDeal @Autowired()(val getOdList: GetOdList)extends Serializable {
/*  def tongHaoKspDataSave(kspData:mutable.Map[Array[String], Double]): Unit ={
    getOdList.deleteAllKspRegion()
//    getOdList.createKspRegionTable()
    for(key <- kspData.keys){
      var str:String = key(0)
      for (i <- 1 to (key.length - 1)) {
        str = str + "," + key(i)
      }
        getOdList.kspRegionAdd(str,kspData(key).toInt)
    }
  }*/ //od矩阵处理测试

}
