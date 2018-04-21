import sbt.Keys.resolvers

import scala.util.Properties

val ScalaVersion = "2.10.7"

lazy val applicationSettings = Seq(
  name := "sbt-trace",
  organization := "com.delprks",
  scalaVersion := ScalaVersion,
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  sbtPlugin := true,
  libraryDependencies ++= dependencies,
  resolvers ++= Seq("Scala SBT" at "https://dl.bintray.com/typesafe/ivy-releases/")
)

lazy val dependencies = Seq(
  "org.scalaj" %% "scalaj-http" % "2.3.0"
)

lazy val publishSettings = Seq(
  version := Properties.envOrElse("BUILD_VERSION", "0.1-SNAPSHOT"),
  publishMavenStyle := true,
  makePomConfiguration ~= { (mpc: MakePomConfiguration) =>
    mpc.copy(file = file("pom.xml"))
  },
  pomExtra := <url>https://github.com/delprks/sbt-trace</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:delprks/sbt-trace.git</url>
      <connection>scm:git@github.com:delprks/sbt-trace.git</connection>
    </scm>
    <developers>
      <developer>
        <id>delprks</id>
        <name>Daniel Parks</name>
        <url>http://github.com/delprks</url>
      </developer>
    </developers>
)

lazy val `sbt-trace`: Project = project.in(file("."))
  .settings(applicationSettings)
  .settings(publishSettings)
