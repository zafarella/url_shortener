name := "url_shortener"

version := "1.0"

lazy val `url_shortener` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(

//  "com.kenshoo" %% "metrics-play" % "2.6.2_0.6.1",

  "io.swagger" %% "swagger-play2" % "1.6.0" exclude("org.reflections", "reflections"),
  "org.reflections" % "reflections" % "0.9.8" notTransitive(),
  "org.webjars" % "swagger-ui" % "3.8.0",

  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,

  jdbc, ehcache, ws, specs2 % Test, guice
)


