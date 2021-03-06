package com.speful.demo

import com.speful.spark.utils.SimpleSpark

object OnlineBlackListFilter extends App with SimpleSpark{

  override def second: Int = 2

  val blackList = sc.makeRDD(Array(
    ("Spy" , true),
    ("Cheater" , true)
  ))

  val stream = ssc.socketTextStream("localhost" , 9999)




  stream.filter(_.length > 0).map { adv =>
    adv.split(" ")(1) -> adv
  }
    .transform {
      _.leftOuterJoin(blackList)
        .filter(!_._2._2.getOrElse(false))
        .map(_._2._1)
    }.print


  ssc.start
  ssc.awaitTermination

}
