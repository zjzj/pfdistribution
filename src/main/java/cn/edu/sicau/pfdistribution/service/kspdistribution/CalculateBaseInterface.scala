package cn.edu.sicau.pfdistribution.service.kspdistribution

import java.util

import cn.edu.sicau.pfdistribution.entity.{DirectedEdge, DirectedPath}
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path

import scala.collection.mutable
import scala.collection.mutable.Map

trait CalculateBaseInterface {
  //计算单个OD的k路径搜索结果
  def staticOdPathSearch(targetOd: String):mutable.Map[Array[DirectedEdge], Double]

  def dynamicOdPathSearch(targetOd: String):mutable.Map[Array[DirectedEdge], Double]

  //计算单个OD对的OD分配结果
  def kspDistribution(map: Map[Array[DirectedEdge], Double], x: Int): mutable.Map[Array[DirectedEdge], Double]

  //调用KSP算法和distribution，迭代计算各个OD对的分配结果
  def odDistributionResult(targetOd: String,allKsp:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,Integer]): mutable.Map[Array[DirectedEdge], Double]
  //调用KSP算法和distribution，迭代计算各个OD对的分配结果(以动态费用分配)
  def dynamicOdDistributionResult(targetOd: String,allKsp:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,Integer]): mutable.Map[Array[DirectedEdge], Double]
  //通号院对接
  def tongHaoStaticOdDistributionResult(targetOd: String,allKsp:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,String]): mutable.Map[Array[DirectedEdge], Double]

  def tongHaoDynamicOdDistributionResult(targetOd: String,allKsp:mutable.Map[String, util.List[DirectedPath]],odMap:mutable.Map[String,String]): mutable.Map[Array[DirectedEdge], Double]

  //将分配到各个路径下的结果划分到区间上，返回区间断面图（未考虑时间，每个OD都能到达）
  def odRegion(map: mutable.Map[Array[DirectedEdge], Double]): mutable.Map[String, Double]

  //按照时间粒度，将分配到各个路径下的结果划分到区间上，返回区间断面图
  def odRegionWithTime(map: mutable.Map[Array[DirectedEdge], Double],interval:Int):mutable.Map[String, Double]


}
