# Tomcat JDBC Connection Pool Plugin

[![Codacy Badge](https://api.codacy.com/project/badge/grade/b67192ff30fb48bdb4aab2abb486ad26)](https://www.codacy.com/app/Codacy/play-tomcatcp)
[![Codacy Badge](https://api.codacy.com/project/badge/coverage/b67192ff30fb48bdb4aab2abb486ad26)](https://www.codacy.com/app/Codacy/play-tomcatcp)
[![Circle CI](https://circleci.com/gh/codacy/play-tomcatcp.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/codacy/play-tomcatcp)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codacy/codacy-tomcatcp_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.codacy/codacy-tomcatcp_2.11)

This is a generic plugin supporting version `8.8.19` of Tomcat JDBC CP.

### Frameworks:
* PlayFramework `2.4.3`
* PlayFramework `2.4.x`
* PlayFramework `2.5.x`
* PlayFramework `2.6.x`

### Metrics:
* Dropwizard `3.2.x`
* Dropwizard `4.0.x`

## Usage

### Base

Add the dependency to your `build.sbt`:

* `"com.codacy" %% "codacy-tomcatcp" % "<latest-version>"`

### PlayFramework

1. Add the dependency to your `build.sbt`:

* `"com.codacy" %% "codacy-tomcatcp-play243" % "<latest-version>"`
    
**OR**    
    
* `"com.codacy" %% "codacy-tomcatcp-play24" % "<latest-version>"`
    
**OR**    
    
* `"com.codacy" %% "codacy-tomcatcp-play25" % "<latest-version>"`
    
**OR**    
    
* `"com.codacy" %% "codacy-tomcatcp-play26" % "<latest-version>"`

2. Now add the following lines to your `conf/application.conf`:

```
play.modules.disabled += "play.api.db.HikariCPModule"
play.modules.enabled += "com.codacy.tomcatcp.play.TomcatCPModule"
```

>This will disable Hikari and enable the Tomcat plugin.

### Metrics

* `"com.codacy" %% "codacy-tomcatcp-dropwizard3" % "<latest-version>"`
    
**OR**    
    
* `"com.codacy" %% "codacy-tomcatcp-dropwizard4" % "<latest-version>"`

### Custom Options

If you have any problems with the connection validation query, defining a custom validator might help.

For example if you define a `validatorClassName` as described bellow it will be used instead of the default.

We have `com.codacy.tomcatcp.validators.TomcatValidator` that uses a timeout
to avoid blocking for long times while validating the connection.

### Example Configuration:

```
driver = org.example.Driver
url = "jdbc:postgresql://localhost:5432/exampledb"
username = "example"
password = "example"

tomcatcp {
  testOnBorrow = true
  validationInterval = 5000
  testWhileIdle = false
  validatorClassName = "com.codacy.tomcatcp.validators.TomcatValidator"

  initialSize = 2
  minIdle = 2
  maxActive = 5
  maxIdle = 5
}
```

### Additional configuration

For detailed descriptions on each option check the [Tomcat JDBC CP Configuration](https://tomcat.apache.org/tomcat-8.0-doc/jdbc-pool.html#Attributes) page.

## Thanks

The code here is highly inspired by the [HikariCP](http://edulify.github.io/play-hikaricp.edulify.com/) plugin.

## What is Codacy?

[Codacy](https://www.codacy.com/) is an Automated Code Review Tool that monitors your technical debt, helps you improve your code quality, teaches best practices to your developers, and helps you save time in Code Reviews.

### Among Codacy’s features:

 - Identify new Static Analysis issues
 - Commit and Pull Request Analysis with GitHub, BitBucket/Stash, GitLab (and also direct git repositories)
 - Auto-comments on Commits and Pull Requests
 - Integrations with Slack, HipChat, Jira, YouTrack
 - Track issues in Code Style, Security, Error Proneness, Performance, Unused Code and other categories

Codacy also helps keep track of Code Coverage, Code Duplication, and Code Complexity.

Codacy supports PHP, Python, Ruby, Java, JavaScript, and Scala, among others.

### Free for Open Source

Codacy is free for Open Source projects.
