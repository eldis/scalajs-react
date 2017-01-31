import sbt._
import Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object ScalaJSReact {

  object Versions {
    val scala = "2.11.8"
    val scalaJsReact = "0.11.3"

    val scalatest = "3.0.1"
  }

  object JsVersions {
    val htmlWebpackPlugin = "~2.26.0"
    val htmlLoader = "~0.4.3"

    val react = "~15.4.2"

    val todomvcAppCss = "^2.0.1"
    val todomvcCommon = "^1.0.2"

    val copyWebpackPlugin = "~4.0.1"
  }

  object Dependencies {
    lazy val scalaJsReact = "com.github.japgolly.scalajs-react" %%%! "core" % Versions.scalaJsReact

    lazy val scalatest = "org.scalatest" %%%! "scalatest" % Versions.scalatest % "test"

    lazy val jsReact = Seq(
      "react" -> JsVersions.react,
      "react-dom" -> JsVersions.react
    )

    lazy val jsTodoExample = jsReact ++ Seq(
      "todomvc-app-css" -> JsVersions.todomvcAppCss,
      "todomvc-common" -> JsVersions.todomvcCommon
    )

    lazy val jsTodoExampleDev = Seq(
      "copy-webpack-plugin" -> JsVersions.copyWebpackPlugin
    )
  }

  object Settings {
    type PC = Project => Project

    def commonProject: PC =
      _.settings(
        scalaVersion := Versions.scala,
        organization := "com.github.eldis"
      )

    def scalajsProject: PC =
      _.configure(commonProject)
      .enablePlugins(ScalaJSPlugin)
      .settings(
        requiresDOM in Test := true
      )

    def jsBundler: PC =
      _.enablePlugins(ScalaJSBundlerPlugin)
      .settings(
        enableReloadWorkflow := false,
        libraryDependencies += Dependencies.scalatest
      )

    def react(dev: Boolean = false): PC =
      _.settings(
        libraryDependencies += Dependencies.scalaJsReact,
        if(dev)
          npmDevDependencies in Compile ++= Dependencies.jsReact
        else
          npmDependencies in Compile ++= Dependencies.jsReact
      )

    def exampleProject(prjName: String, useReact: Boolean = false): PC = { p: Project =>
      p.in(file("examples") / prjName)
        .configure(scalajsProject, jsBundler)
        .settings(
          name := prjName,

          npmDevDependencies in Compile ++= Seq(
            "html-webpack-plugin" -> JsVersions.htmlWebpackPlugin,
            "html-loader" -> JsVersions.htmlLoader
          ),

          webpackConfigFile in fastOptJS := Some(baseDirectory.value / "config" / "webpack.config.js"),
          webpackConfigFile in fullOptJS := Some(baseDirectory.value / "config" / "webpack.config.js")
        )
      } compose { pc =>
        if(useReact)
          pc.configure(react())
        else
          pc
      }

    def publish: PC =
      _.settings(
        publishMavenStyle := true,
        publishTo := {
          val nexus = "https://oss.sonatype.org/"
          if (isSnapshot.value)
            Some("snapshots" at nexus + "content/repositories/snapshots")
          else
            Some("releases"  at nexus + "service/local/staging/deploy/maven2")
        }
      )
  }

  object Projects {

    lazy val scalaJsReact = project.in(file("."))
      .configure(
        Settings.scalajsProject, Settings.jsBundler, Settings.publish, Settings.react(true)
      )
      .settings(
        name := "scalajs-react"
      )

    lazy val exSimple = project
      .configure(
        Settings.exampleProject(
          "simple",
          useReact = true)
      )
      .dependsOn(scalaJsReact)

    lazy val exTodomvc = project
      .configure(
        Settings.exampleProject(
          "todo",
          useReact = true
        )
      )
      .settings(
        npmDependencies in Compile ++= Dependencies.jsTodoExample,
        npmDevDependencies in Compile ++= Dependencies.jsTodoExampleDev
      )
      .dependsOn(scalaJsReact)

  }

}
