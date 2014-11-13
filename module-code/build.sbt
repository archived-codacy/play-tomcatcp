name := "play-tomcatcp"

version := "1.0.1.SNAPSHOT"

scalaVersion := "2.11.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  jdbc,
  "org.apache.tomcat" % "tomcat-jdbc" % "8.0.11"
)

resolvers ++= Seq(
  Resolver.typesafeRepo("releases")
)

organization := "com.codacy"

organizationName := "Codacy"

organizationHomepage := Some(new URL("https://www.codacy.com"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false}

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

startYear := Some(2014)

description := "TomcatCP Plugin for Play Framework 2.3.x"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("http://codacy.github.io/play-tomcatcp/"))

pomExtra :=
  <scm>
    <url>https://github.com/codacy/play-tomcatcp</url>
    <connection>scm:git:git@github.com:codacy/play-tomcatcp.git</connection>
    <developerConnection>scm:git:https://github.com/codacy/play-tomcatcp.git</developerConnection>
  </scm>
    <developers>
      <developer>
        <id>mrfyda</id>
        <name>Rafael</name>
        <email>rafael [at] codacy.com</email>
        <url>https://github.com/mrfyda</url>
      </developer>
    </developers>

scalacOptions := Seq("-deprecation", "-feature", "-unchecked", "-Ywarn-adapted-args", "-Xlint")
