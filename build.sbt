name := "urlShortener"

version := "1.0"

lazy val `urlshortener` =
  (project in file("."))
    .enablePlugins(PlayScala)

scalaVersion := "3.8.2"
val pekkoVersion = "1.4.0"

libraryDependencies ++= Seq(

  "com.kenshoo" %% "metrics-play" % "2.7.3_0.8.2",

  "org.webjars" %% "webjars-play" % "3.0.10",
  "org.webjars" % "swagger-ui" % "5.31.2",

  "commons-codec" % "commons-codec" % "1.21.0",

  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test,
//
//  "org.apache.pekko" %% "pekko-actor"                 % pekkoVersion,
//  "org.apache.pekko" %% "pekko-stream"                % pekkoVersion,
//  "org.apache.pekko" %% "pekko-slf4j"                 % pekkoVersion,
//  "org.apache.pekko" %% "pekko-serialization-jackson" % pekkoVersion,
//  "org.apache.pekko" %% "pekko-actor-typed"           % pekkoVersion,
//  jdbc,
//  ehcache,
  ws, // keep uncommented - required by DI/Juice
  specs2 % Test,
  guice
)
