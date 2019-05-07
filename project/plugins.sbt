import sbt._

resolvers := Seq(DefaultMavenRepository, Resolver.jcenterRepo, Resolver.sonatypeRepo("releases"))

ivyLoggingLevel := UpdateLogging.Full

// Comment to get more information during initialization
logLevel := Level.Info

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.2")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.11")

addSbtPlugin("com.codacy" % "codacy-sbt-plugin" % "15.0.0")
