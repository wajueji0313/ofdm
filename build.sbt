// See LICENSE for license details.

name := "ofdm"

organization := "edu.berkeley.eecs"

version := "0.1"

scalaVersion := "2.11.11"

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("release")
)

libraryDependencies ++= Seq("chisel3", "chisel-iotesters", "dsptools").map {
  dep: String => "edu.berkeley.cs" %% dep % sys.props.getOrElse(dep + "Version", defaultVersions(dep))
}

libraryDependencies += ("com.gilt" %% "handlebars-scala" % "2.1.1").exclude("org.slf4j", "slf4j-simple")

libraryDependencies += "org.vegas-viz" %% "vegas" % "0.3.11"

libraryDependencies += "org.tukaani" % "xz" % "1.0"

// Provide a managed dependency on X if -DXVersion="" is supplied on the command line.
val defaultVersions = Map(
  "chisel3" -> "3.0.1",
  "chisel-iotesters" -> "1.1.1",
  "dsptools" -> "1.0.1"
)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:reflectiveCalls")
