scalaVersion := "2.12.6"
name := "sbt-classpath-issue"
organization := "com.gihub.ahjohannessen"
version := "1.0"

fork := false

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "1.0.0",
  "org.flywaydb"   % "flyway-core" % "5.1.4",
  "org.postgresql" %  "postgresql" % "42.2.2"
)
