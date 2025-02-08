ThisBuild / scalaVersion := "3.3.3"

ThisBuild / name    := "Iron-demo"
ThisBuild / version := "1.0"
ThisBuild / scalacOptions ++= Seq(
  "-no-indent",
  "-deprecation", // Warns about deprecated APIs
  "-feature",
  // "-Wunused:imports",     // Warns about advanced language features
  "UTF-8",
  "-unchecked"
)

val ironVersion = "2.6.0"

def iron(artifact: String): ModuleID =
  "io.github.iltotore" %% s"iron-$artifact" % ironVersion

val ironSkunk = iron("skunk")

val ironPureconfig = iron("pureconfig")

val ironDoobie = iron("doobie")

val ironCirce = iron("circe")
val ironCats  = iron("cats")

val ironCore   = "io.github.iltotore"    %% "iron"            % ironVersion
val pureconfig = "com.github.pureconfig" %% "pureconfig-core" % "0.17.7"

val cats = "org.typelevel" %% "cats-core" % "2.8.0"

ThisBuild / bspEnabled := true

ThisBuild / semanticdbEnabled := true

ThisBuild / cancelable := true

ThisBuild / usePipelining := true

libraryDependencies ++= Seq(
  ironCore,
  ironCats,
  ironCirce,
  ironDoobie,
  ironSkunk,
  // ironPureconfig,
  pureconfig,
  cats
)
