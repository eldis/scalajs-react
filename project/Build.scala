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
    val circe = "0.7.0"
  }

  object JsVersions {
    val htmlWebpackPlugin = "~2.26.0"
    val htmlLoader = "~0.4.3"

    val react = "~15.4.2"
  }

  object Dependencies {
    lazy val scalaJsReact = "com.github.japgolly.scalajs-react" %%%! "core" % Versions.scalaJsReact


    lazy val scalatest = "org.scalatest" %%%! "scalatest" % Versions.scalatest % "test"

    lazy val jsReact = Seq(
      "react" -> JsVersions.react,
      "react-dom" -> JsVersions.react
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

    lazy val scalaJs = project.in(file("."))
      .configure(
        Settings.scalajsProject, Settings.jsBundler, Settings.publish, Settings.react(true)
      )
      .settings(
        name := "scalajs-react",
        libraryDependencies ++= Seq(
          "io.circe" %%% "circe-core" % Versions.circe,
          "io.circe" %%% "circe-generic" % Versions.circe,
          "io.circe" %%% "circe-parser" % Versions.circe,
          "io.circe" %%% "circe-scalajs" % Versions.circe
        )
      )

    lazy val exSimple = project
      .configure(
        Settings.exampleProject(
          "simple",
          useReact = true)
      )
      .dependsOn(scalaJs)

    lazy val exHi = project
      .configure(
        Settings.exampleProject(
          "hi",
          useReact = true)
      )
      .dependsOn(scalaJs)

  }

}
