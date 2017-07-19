name := "SentimentAnalysis"

version := "1.0"

scalaVersion := "2.12.2"

//resolvers += "CogcompSoftware" at "http://cogcomp.cs.illinois.edu/m2repo/"
libraryDependencies ++= Seq("edu.illinois.cs.cogcomp" % "saul_2.11" % "0.5.8-SNAPSHOT",
  "edu.illinois.cs.cogcomp" % "saul-examples_2.11" % "0.5.8-SNAPSHOT",
  "com.twitter" % "hbc-core" % "2.2.0"
)
resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.ivy2/repository"
