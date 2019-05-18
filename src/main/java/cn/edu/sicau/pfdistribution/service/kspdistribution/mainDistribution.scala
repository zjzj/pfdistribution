package cn.edu.sicau.pfdistribution.service.kspdistribution


object mainDistribution {

  val calBase = new interfaceImplementation
  def main(args: Array[String]): Unit = {
    val result = calBase.intervalResult()
    result.keys.foreach { i =>
      print("Key = " + i)
      println(" Value = " + result(i))
    }
  }

}
