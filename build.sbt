organization in ThisBuild := "com.codacy"
version in ThisBuild := "1.0.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.11.11"
scalacOptions in ThisBuild := Seq("-deprecation", "-feature", "-unchecked", "-Ywarn-adapted-args", "-Xlint")
resolvers in ThisBuild += Resolver.typesafeRepo("releases")

lazy val aggregatedProjects: Seq[ProjectReference] = Seq(
  codacyTomcatCP, codacyTomcatCPDropwizard,
  codacyTomcatCPPlay24, codacyTomcatCPPlay26
)

lazy val codacyTomcat = project.in(file("."))
  .settings(name := "root")
  .aggregate(aggregatedProjects: _*)
  .settings(
    publish := {},
    publishLocal := {},
    publishArtifact := false
  )

lazy val codacyTomcatCP = project.in(file("tomcatcp"))
  .settings(name := "codacy-tomcatcp")
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.tomcat" % "tomcat-jdbc" % "8.5.19",
      "org.scalatest" %% "scalatest" % "2.2.4" % "test",
      "com.iheart" %% "ficus" % "1.4.2"
    )
  )

val projectVersion = "1.0.0"

def generatePlayProject(projectName: String, playVersion: String) = {
  Project(projectName, file(s"play/target/$projectName"))
    .settings(
      /**
        * Define the scala sources and resources relative to the sub-directory the project source. The
        * individual projects have their sources defined in `./target/${projectName}`,
        * therefore `./src` lives two directories above base. Also do this for `resources`
        * etc, if needed.
        */
      scalaSource in Compile := baseDirectory.value / ".." / ".." / "src" / "main" / "scala",
      scalaSource in Test := baseDirectory.value / ".." / ".." / "src" / "test" / "scala",
      resourceDirectory in Compile := baseDirectory.value / ".." / ".." / "src" / "main" / "resources",
      resourceDirectory in Test := baseDirectory.value / ".." / ".." / "src" / "test" / "resources",
      libraryDependencies ++= Seq(
        "com.typesafe.play" %% "play" % playVersion,
        "com.typesafe.play" %% "play-jdbc" % playVersion
      )
    )
    .dependsOn(codacyTomcatCP)
    .aggregate(codacyTomcatCP)
}

lazy val codacyTomcatCPPlay24 = generatePlayProject("codacy-tomcatcp-play24", "2.4.11")
lazy val codacyTomcatCPPlay26 = generatePlayProject("codacy-tomcatcp-play26", "2.6.6")

val dropwizardVersion = "3.2.5"

lazy val codacyTomcatCPDropwizard = project.in(file("dropwizard"))
  .settings(name := "codacy-tomcatcp-dropwizard")
  .settings(
    libraryDependencies ++= Seq(
      "io.dropwizard.metrics" % "metrics-core" % dropwizardVersion,
      "io.dropwizard.metrics" % "metrics-healthchecks" % dropwizardVersion
    )
  )
  .dependsOn(codacyTomcatCP)
  .aggregate(codacyTomcatCP)

// Publish Settings

organizationName in ThisBuild := "Codacy"

organizationHomepage in ThisBuild := Some(new URL("https://www.codacy.com"))

publishMavenStyle in ThisBuild := true

publishArtifact in Test := false

pomIncludeRepository in ThisBuild := { _ => false }

publishTo in ThisBuild := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

startYear in ThisBuild := Some(2014)

description in ThisBuild := "TomcatCP Plugin"

licenses in ThisBuild := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage in ThisBuild := Some(url("http://codacy.github.io/play-tomcatcp/"))

pomExtra in ThisBuild :=
  <scm>
    <url>https://github.com/codacy/play-tomcatcp</url>
    <connection>scm:git:git@github.com:codacy/play-tomcatcp.git</connection>
    <developerConnection>scm:git:https://github.com/codacy/play-tomcatcp.git</developerConnection>
  </scm>
    <developers>
      <developer>
        <id>mrfyda</id>
        <name>Rafael Cortês</name>
        <email>rafael [at] codacy.com</email>
        <url>https://github.com/mrfyda</url>
      </developer>
      <developer>
        <id>rtfpessoa</id>
        <name>Rodrigo Fernandes</name>
        <email>rodrigo [at] codacy.com</email>
        <url>https://github.com/rtfpessoa</url>
      </developer>
    </developers>
