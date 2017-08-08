name := "SentimentAnalysis"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq("edu.illinois.cs.cogcomp" % "saul_2.11" % "0.5.8-SNAPSHOT",
  "edu.illinois.cs.cogcomp" % "saul-examples_2.11" % "0.5.8-SNAPSHOT",
  "com.twitter" % "hbc-core" % "2.2.0"
)