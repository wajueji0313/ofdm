// See LICENSE for license details.

// Provide a managed dependency on X if -DXVersion="" is supplied on the command line.
val defaultVersions = Map(
  "dsptools" -> "1.2-102318-SNAPSHOT",
  "rocket-dsptools" -> "1.2-102318-SNAPSHOT"
)

val commonSettings = Seq(
  organization := "edu.berkeley.cs",
  version      := "0.1",
  scalaVersion := "2.12.7",
  resolvers ++= Seq(
    Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("release")
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-explaintypes",
    "-feature",
    "-language:reflectiveCalls",
    "-unchecked",
    "-Xcheckinit",
    "-Xlint:infer-any",
    "-Xlint:missing-interpolator",
    "-Xsource:2.11",
    "-Ywarn-unused:imports",
    "-Ywarn-unused:locals"
  ),
  // scalacOptions ++= scalacOptions(scalaVersion.value),
  javacOptions ++= javacOptionsVersion(scalaVersion.value)
)

val ofdmSettings = Seq(
  name := "ofdm",
  libraryDependencies ++= Seq("dsptools").map {
    dep: String => "edu.berkeley.cs" %% dep % sys.props.getOrElse(dep + "Version", defaultVersions(dep))
  },
  libraryDependencies += "org.tukaani" % "xz" % "1.0"
)

val ofdmRocketSettings = Seq(
  name := "ofdm-rocket",
  libraryDependencies ++= Seq("rocket-dsptools").map {
    dep: String => "edu.berkeley.cs" %% dep % sys.props.getOrElse(dep + "Version", defaultVersions(dep))
  }
)

lazy val ofdm = (project in file(".")).
  settings(commonSettings: _*).
  settings(ofdmSettings: _*)

lazy val ofdmRocket = (project in file("rocket")).
  settings(commonSettings: _*).
  settings(ofdmRocketSettings: _*).
  dependsOn(ofdm).
  aggregate(ofdm)

def scalacOptionsVersion(scalaVersion: String): Seq[String] = {
  Seq() ++ {
    // If we're building with Scala > 2.11, enable the compile option
    //  switch to support our anonymous Bundle definitions:
    //  https://github.com/scala/bug/issues/10047
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor: Long)) if scalaMajor < 12 => Seq()
      case _ => Seq("-Xsource:2.11")
    }
  }
}

def javacOptionsVersion(scalaVersion: String): Seq[String] = {
  Seq() ++ {
    // Scala 2.12 requires Java 8. We continue to generate
    //  Java 7 compatible code for Scala 2.11
    //  for compatibility with old clients.
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor: Long)) if scalaMajor < 12 =>
        Seq("-source", "1.7", "-target", "1.7")
      case _ =>
        Seq("-source", "1.8", "-target", "1.8")
    }
  }
}

