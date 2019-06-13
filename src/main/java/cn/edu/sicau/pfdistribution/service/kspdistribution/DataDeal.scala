package cn.edu.sicau.pfdistribution.service.kspdistribution

//import cn.edu.sicau.pfdistribution.dao.mysqlsave.RegionSaveInterface
//import cn.edu.sicau.pfdistribution.service.kafka.sender.KafkaPfAllocationMessageSender
import cn.edu.sicau.pfdistribution.entity.DirectedEdge
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
  def function(data:mutable.Map[Iterator[DirectedEdge], Double]): Unit ={
    for(key <-data.keys){
      var formatedpath:String = ""
      var firstEdge = key.next()
      formatedpath = firstEdge.getEdge.getFromNode
      formatedpath = formatedpath + " " + firstEdge.getDirection +" " +  firstEdge.getEdge.getToNode;
      while(key.hasNext){
        var edge = key.next()
        formatedpath = formatedpath + " "+edge.getEdge.getToNode
      }
      return formatedpath
    }
}
}
