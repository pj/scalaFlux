scalaJSSettings

name := "todomvc-scalajs"

scalaVersion := "2.11.1"

libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"

libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-jquery" % "0.6"

libraryDependencies += "com.scalatags" %%% "scalatags" % "0.3.8"

libraryDependencies += "nz.kiwi.johnson" %%% "scalatags-virtual-dom" % "0.5.9"

libraryDependencies += "com.github.japgolly.fork.monocle" %%% "monocle-core" % "0.5.0"

libraryDependencies += "com.scalarx" %%% "scalarx" % "0.2.6"

skip in ScalaJSKeys.packageJSDependencies := false

//relativeSourceMaps := true