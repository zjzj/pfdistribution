package cn.edu.sicau.pfdistribution.service

import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.stereotype.Service

/**
  * @author Zj
  * @date 2019/4/17 7:55
  */

@Service
class DoWork() {
  def work() = {
    val conf = new SparkConf().
      setMaster("local[2]").
      setAppName("test")
    val sc = new SparkContext(conf)
    val arrayRDD = sc.makeRDD(Array[Int](1,2,3,4))
    println(arrayRDD.count());
  }

}
