package cn.edu.sicau.pfdistribution.service.kspdistribution


import scala.collection.mutable
import scala.collection.mutable.Map


trait calculateInterface {
  def odDistributionResult(targetOd:String): Any={}
  //调用KSP算法和distribution，迭代计算各个OD对的分配结果
  def distribution(map: mutable.Map[Array[String], Double], x: Int):Any={}
  //计算单个OD对的OD分配结果
  def odRegion(map: mutable.Map[Array[String], Double]): Any={}
  //将分配到各个路径下的结果划分到区间上，返回区间断面图（未考虑时间，每个OD都能到达）
  def odRegionWithTime(map: mutable.Map[Array[String], Double]):Any={}
  //按照不同的时间粒度分配形，生成区间密度断面图
  def intervalResult(odList:List[String]):Any={}
  //返回区间断面的分配结果
}
