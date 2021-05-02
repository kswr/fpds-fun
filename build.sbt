ThisBuild / name := "fpds-fun"

ThisBuild / description := "Experimenting with code examples from Functional Program Design in Scala"

Global / onChangedBuildSource := ReloadOnSourceChanges

val scalaV = "2.13.4"

ThisBuild / scalaVersion := scalaV

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.7",
  "org.scalatest" %% "scalatest" % "3.2.7" % Test
)
