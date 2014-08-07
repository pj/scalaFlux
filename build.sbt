scalaJSSettings

name := "todomvc-scalajs"

scalaVersion := "2.11.1"

libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"

libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-jquery" % "0.6"

libraryDependencies += "com.scalatags" %%% "scalatags" % "0.3.8"

libraryDependencies += "nz.kiwi.johnson" %%% "scalatags-virtual-dom" % "0.5.1"

skip in ScalaJSKeys.packageJSDependencies := false