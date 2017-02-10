# Spark + Neo4J Skeleton code (Scala)

This repo contains the base codeline in scala to start using Spark and Neo4j

It should contain a step-by-Step guide in order to start using Neo4J integrated 
with Spark.

Feel free to use the code at your own risk :)

# Intro

This guid would be made with Spark version 1.6.0 due to local enviroment constraints, if you are following this guide you should use a newer version.

## Spark

Spark is a fast and general processing engine compatible with Hadoop data. It can run in Hadoop clusters through YARN or Spark's standalone mode, and it can process data in HDFS, HBase, Cassandra, Hive, and any Hadoop InputFormat. It is designed to perform both batch processing (similar to MapReduce) and new workloads like streaming, interactive queries, and machine learning.

Check the Official Page for more info on Spark: http://spark.apache.org/faq.html

## Neo4j

Neo4j is a highly scalable native graph database that leverages data relationships as first-class entities, helping enterprises build intelligent applications to meet today’s evolving data challenges.

Check the Official site for more information https://neo4j.com/

# Instalation dependencies 

Lets first start by downloading Spark and Neo4J in case we don't have it yet.

Download prebuilt version, i'm using 1.6.0 for this guide from:

* http://spark.apache.org/downloads.html

Untar the package
```
mkdir /opt/spark
tar xvfz spark-1.6.0-bin-hadoop2.6.tgz -C /opt/spark
```

Add to your bash enviroment

```
export PATH=$PATH:/opt/spark/spark-1.6.0-bin-hadoop2.6/bin
export SPARK_HOME=/opt/spark/spark-1.6.0-bin-hadoop2.6
export 
```

You should open a new terminal an execute the command `spark-shell` If you get a prompt like the following you are ready.

```
$ spark-shell 
log4j:WARN No appenders could be found for logger (org.apache.hadoop.metrics2.lib.MutableMetricsFactory).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Using Spark's repl log4j profile: org/apache/spark/log4j-defaults-repl.properties
To adjust logging level use sc.setLogLevel("INFO")
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 1.6.0
      /_/

Using Scala version 2.10.5 (OpenJDK 64-Bit Server VM, Java 1.8.0_121)
Type in expressions to have them evaluated.
Type :help for more information.
Spark context available as sc.
17/02/09 21:25:22 WARN Connection: BoneCP specified but not present in CLASSPATH (or one of dependencies)
17/02/09 21:25:22 WARN Connection: BoneCP specified but not present in CLASSPATH (or one of dependencies)
17/02/09 21:25:26 WARN ObjectStore: Version information not found in metastore. hive.metastore.schema.verification is not enabled so recording the schema version 1.2.0
17/02/09 21:25:26 WARN ObjectStore: Failed to get database default, returning NoSuchObjectException
17/02/09 21:25:28 WARN Connection: BoneCP specified but not present in CLASSPATH (or one of dependencies)
17/02/09 21:25:28 WARN Connection: BoneCP specified but not present in CLASSPATH (or one of dependencies)
17/02/09 21:25:32 WARN ObjectStore: Version information not found in metastore. hive.metastore.schema.verification is not enabled so recording the schema version 1.2.0
17/02/09 21:25:32 WARN ObjectStore: Failed to get database default, returning NoSuchObjectException
SQL context available as sqlContext.

scala> 

```


Download Neo4j Community version from and unpack:

* https://neo4j.com/download/community-edition/

Untar the package

```
mkdir /opt/neo4j
tar xvfz neo4j-community-3.1.1-unix.tar.gz -C /opt/neo4j/
```

Start a new console and type

```
neo4j start
```

You should get a similar output
```
Starting Neo4j.
WARNING: Max 1024 open files allowed, minimum of 40000 recommended. See the Neo4j manual.
Started neo4j (pid 22041). By default, it is available at http://localhost:7474/
There may be a short delay until the server is ready.
See /opt/neo4j/neo4j-community-3.1.1/logs/neo4j.log for current status.
```

If you access http://localhost:7474/ on you browser you could start the inital setup.

For simplification i just put password, please don't do that in production :D

Lets' populate Neo4J with some demo data. In the console write 

```
:play movie graph
```

Select to the rigth and you'll see a code block you can import , just click it and run in the conole. 

A nice coloring graph should appear in the interface.

# Neo4j Data

Lets start by cleaning any existing data we have

```
MATCH (n)
OPTIONAL MATCH (n)-[r]-()
DELETE n,r
```

And populate with the following demo data

```
UNWIND range(1,100) as id
CREATE (p:Person {id:id}) WITH collect(p) as people
UNWIND people as p1
UNWIND range(1,10) as friend
WITH p1, people[(p1.id + friend) % size(people)] as p2
CREATE (p1)-[:KNOWS {years: abs(p2.id - p2.id)}]->(p2)
```

With this data in place let's start preparing our IDE


# IDE configuration

There are several options for IDEs out there, you could choose the one that better suits you. I've choosen IntelliJ because i like the nice integration it has with Scala,SBT.

Download the Community Edition from:

* https://www.jetbrains.com/idea/download

If you don't have the scala plugin just follow the existing get-started-with-sbt guide.

https://www.jetbrains.com/help/idea/2016.3/getting-started-with-sbt.html

The point of this guide is to have a prepared skeleton for intellij so just git clone this repo start from there:

```
git clone https://github.com/rramos/spark-neo4j-skel
```

Make sure you can compile the code and goto the Spark Submit section

If you want to do it from scratch, create a new SBT project 

You are going to have a build.sbt file similar to this one

```
name := "spark-neo4j-skel"

version := "1.0"

scalaVersion := "2.10.5"
```

Let's add some dependencies

Let's add spark dependencies and Neo4j-spark-connector 

```
name := "spark-neo4j-skel"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "1.6.0",
  "org.apache.spark" %% "spark-hive" % "1.6.0",
  "org.apache.spark" %% "spark-streaming" % "1.6.0",
  "org.apache.spark" %% "spark-streaming-kafka" % "1.6.0",
  "org.apache.spark" %% "spark-streaming-flume" % "1.6.0",
  "org.apache.spark" %% "spark-mllib" % "1.6.0"
)

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"
libraryDependencie
```

Refresh the project sbt packages in the SBT Tab or using the command line

```
sbt package
```

This will take a while for the first time

# Initial Code

Let's create a new scala script with the following code

```
import org.apache.spark.{SparkConf, SparkContext}
import org.neo4j.spark.Neo4j
import org.graphframes._

object SparkNeo4jApp {

  def main(args: Array[String]) {
    
    val conf = new SparkConf()
      .setAppName("Test-neo4j-skel")       // App Name
      .setMaster("local[*]")               // local mode

    val sc = new SparkContext(conf)

    // Neo4J Code starting here ...

    val neo = Neo4j(sc)

    val rdd = neo.cypher("MATCH (n:Person) RETURN id(n) as id ").loadRowRdd
    rdd.count

    // inferred schema
    rdd.first.schema.fieldNames
    //   => ["id"]
    rdd.first.schema("id")
    //   => StructField(id,LongType,true)

    neo.cypher("MATCH (n:Person) RETURN id(n)").loadRdd[Long].mean
    //   => res30: Double = 236696.5

    neo.cypher("MATCH (n:Person) WHERE n.id <= {maxId} RETURN n.id").param("maxId", 10).loadRowRdd.count
    //   => res34: Long = 10

    // provide partitions and batch-size
    // neo.nodes("MATCH (n:Person) RETURN id(n) SKIP {_skip} LIMIT {_limit}").partitions(4).batch(25).loadRowRdd.count
    //   => 100 == 4 * 25

    // load via pattern
    neo.pattern("Person",Seq("KNOWS"),"Person").rows(80).batch(21).loadNodeRdds.count
    //   => 80 = b/c 80 rows given

    // load relationships via pattern
    neo.pattern("Person",Seq("KNOWS"),"Person").partitions(12).batch(100).loadRelRdd.count
    //   => 1000

    sc.stop()
  }
```

You can build this code now from the IDE or

```
sbt package
```

If you sucessfully build the code you should have the following jar `target/scala-2.10/spark-neo4j-skel_2.10-1.0.jar`.

Let's try a Spark Submit.

```
spark-submit --conf spark.neo4j.bolt.password=password --packages neo4j-contrib:neo4j-spark-connector:2.0.0-M2,graphframes:graphframes:0.3.0-spark1.6-s_2.10 spark-neo4j-skel_2.10-1.0.jar --class SparkNeo4jApp
```

# Spark Submit

In order to have Spark executing this code one should have the necessary jars files in-place.

Let's go to target dir

# References

* https://neo4j.com/developer/apache-spark/#neo4j-spark-connector
* https://github.com/neo4j-contrib/neo4j-spark-connector
* 