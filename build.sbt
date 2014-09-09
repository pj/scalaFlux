scalaJSSettings

name := "todomvc-scalajs"

scalaVersion := "2.11.2"

libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"

libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-jquery" % "0.6"

libraryDependencies += "com.scalatags" %%% "scalatags" % "0.4.1" changing()

libraryDependencies += "com.github.japgolly.fork.monocle" %%% "monocle-core" % "0.5.0"

libraryDependencies += "com.scalarx" %%% "scalarx" % "0.2.6"

skip in ScalaJSKeys.packageJSDependencies := false

//relativeSourceMaps := true