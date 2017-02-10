name := "spark-neo4j-skel"

version := "1.0"

scalaVersion := "2.10.5"

val sparkVersion = "1.6.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-flume" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion
)

libraryDependencies += "graphframes" % "graphframes" % "0.3.0-spark1.6-s_2.10"

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"
libraryDependencies += "neo4j-contrib" % "neo4j-spark-connector" % "2.0.0-M2"
