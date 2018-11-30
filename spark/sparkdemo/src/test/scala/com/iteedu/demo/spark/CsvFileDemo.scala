package com.iteedu.demo.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.{ SaveMode, SparkSession }
import org.apache.spark.sql.Column
import java.util.UUID
import org.apache.spark.sql.hive.HiveContext
import scala.io.Source
import java.io.PrintWriter
import java.io.File

/**
 * csv文件读取与操作
 *
 */
object CsvFileDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("spark init test");
    conf.setMaster("local");
    val spark = SparkSession.builder.config(conf).getOrCreate();
    var csv = spark.read.format("csv").option("header", "false").option("delimiter", "|").load("D:/data/advertest/adver_info.csv");
    csv.createOrReplaceTempView("proinfo")
    spark.sql("select * from proinfo").show()
    spark.sql("select * from proinfo where _c1 not rlike '1|0'").show()
    spark.sql("select * from proinfo where _c1 is null").show()
    
    
    //    csv.createOrReplaceTempView("proinfo");
//    val lstR=csv.rdd.map(r=>r.mkString("\001")).collect()
//    val writer = new PrintWriter(new File("D:/test.csv"))
//    writer.println(csv.columns.mkString("\001"))
//    lstR.foreach(r=>writer.println(r))
//    writer.flush()
//    writer.close()
    
  }
}