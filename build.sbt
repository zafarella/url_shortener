name := "url_shortener"

version := "1.0"

lazy val `url_shortener` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc,

  "com.kenshoo" %% "metrics-play" % "2.6.2_0.6.1",
  "io.swagger" %% "swagger-play2" % "1.6.0" exclude("org.reflections", "reflections"),
  "org.reflections" % "reflections" % "0.9.8" notTransitive(),
  "org.webjars" % "swagger-ui" % "2.1.8-M1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,

  ehcache, ws, specs2 % Test, guice)


