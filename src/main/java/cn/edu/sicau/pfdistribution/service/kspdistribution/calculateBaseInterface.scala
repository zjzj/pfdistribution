package cn.edu.sicau.pfdistribution.service.kspdistribution


import scala.collection.mutable
import scala.collection.mutable.Map


trait calculateBaseInterface {
  //计算单个OD的k路径搜索结果
  def odPathSearch(targetOd:String):mutable.Map[Array[String],Double]

  //获得换乘次数（集体内容待定）
  def getTransferTimes(targetOd:String):Int

  //计算单个OD对的OD分配结果
  def distribution(map: Map[Array[String], Double], x: Int): Map[Array[String], Double]

  //调用KSP算法和distribution，迭代计算各个OD对的分配结果
  def odDistributionResult(targetOd:String):mutable.Map[Array[String],Double]

  //将分配到各个路径下的结果划分到区间上，返回区间断面图（未考虑时间，每个OD都能到达）
  def odRegion(map: mutable.Map[Array[String], Double]): mutable.Map[String, Double]

  //按照时间粒度，将分配到各个路径下的结果划分到区间上，返回区间断面图
  def odRegionWithTime(map: mutable.Map[Array[String], Double]):mutable.Map[String, Double]

}
