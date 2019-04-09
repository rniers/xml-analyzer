name := "highfive"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.1"


//util dependencies
libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.11.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"
)