// For sbt-nexus-tasks
resolvers += Opts.resolver.sonatypeSnapshots

addSbtPlugin("org.ensime" % "sbt-ensime" % "1.11.3")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.14")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.5.0")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")
addSbtPlugin("com.github.eldis" % "sbt-nexus-tasks" % "0.1.0-SNAPSHOT")
