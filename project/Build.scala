import sbt._
import Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._
import sbtnexustasks.SbtNexusTasksPlugin
import sbtnexustasks.SbtNexusTasksPlugin.autoImport._
import com.typesafe.sbt.SbtPgp.autoImport._

object ScalaJSReact {

  object Versions {
    val scala = "2.11.8"
    val japgollyScalaJsReact = "0.11.3"
    val scalaJsDom = "0.9.1"

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
    lazy val japgollyScalaJsReact = "com.github.japgolly.scalajs-react" %%%! "core" % Versions.japgollyScalaJsReact

    lazy val scalatest = "org.scalatest" %%%! "scalatest" % Versions.scalatest % "test"

    lazy val scalaJsDom = "org.scala-js" %%%! "scalajs-dom" % Versions.scalaJsDom

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
        if(dev)
          npmDevDependencies in Compile ++= Dependencies.jsReact
        else
          npmDependencies in Compile ++= Dependencies.jsReact
      )

    def exampleProject(prjName: String): PC = { p: Project =>
      p.in(file("examples") / prjName)
        .configure(scalajsProject, jsBundler, react())
        .settings(
          name := prjName,

          npmDevDependencies in Compile ++= Seq(
            "html-webpack-plugin" -> JsVersions.htmlWebpackPlugin,
            "html-loader" -> JsVersions.htmlLoader
          ),

          webpackConfigFile in fastOptJS := Some(baseDirectory.value / "config" / "webpack.config.js"),
          webpackConfigFile in fullOptJS := Some(baseDirectory.value / "config" / "webpack.config.js")
        )
      }

    def publish: PC =
      _.enablePlugins(SbtNexusTasksPlugin)
        .settings(
          publishMavenStyle := true,
          publishTo := {
            val nexus = "https://oss.sonatype.org/"
            if (isSnapshot.value)
              Some("snapshots" at nexus + "content/repositories/snapshots")
            else
              Some("releases"  at nexus + "service/local/staging/deploy/maven2")
          },

        PgpKeys.publishSigned := {
          nexusExpireProxyCache.in(Compile)
            .dependsOn(PgpKeys.publishSigned).value
        }
      )
  }

  object Projects {

    lazy val scalaJsReact = project.in(file("."))
      .configure(
        Settings.scalajsProject, Settings.jsBundler, Settings.publish, Settings.react(true)
      )
      .settings(
        name := "scalajs-react",
        libraryDependencies += Dependencies.scalaJsDom
      )

    lazy val scalaJsReactCompat = project.in(file("compat"))
      .configure(
        Settings.scalajsProject, Settings.jsBundler, Settings.publish, Settings.react(true)
      )
      .settings(
        name := "scalajs-react-compat",
        libraryDependencies += Dependencies.japgollyScalaJsReact
      )
      .dependsOn(scalaJsReact)

    lazy val exSimple = project
      .configure(
        Settings.exampleProject("simple")
      )
      .dependsOn(scalaJsReact)

    lazy val exTodomvc = project
      .configure(
        Settings.exampleProject("todo")
      )
      .settings(
        npmDependencies in Compile ++= Dependencies.jsTodoExample,
        npmDevDependencies in Compile ++= Dependencies.jsTodoExampleDev
      )
      .dependsOn(scalaJsReact)

    lazy val exCompat = project
      .configure(
        Settings.exampleProject("compat")
      )
      .dependsOn(scalaJsReact, scalaJsReactCompat)

  }

}
