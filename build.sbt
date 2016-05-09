name := "hello-slick"

mainClass in Compile := Some("com.leonardoz.starred.Main")

libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "3.1.0-RC2",
  "org.slf4j" % "slf4j-nop" % "1.7.10",
  "com.h2database" % "h2" % "1.4.187",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

fork in run := true
