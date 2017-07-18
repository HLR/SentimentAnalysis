name := "SentimentAnalysis"

version := "1.0"

scalaVersion := "2.12.2"

resolvers += "CogcompSoftware" at "http://cogcomp.cs.illinois.edu/m2repo/"
libraryDependencies ++= Seq("edu.illinois.cs.cogcomp" % "saul_2.11" % "0.5.7",
  "com.twitter" % "hbc-core" % "2.2.0"
)