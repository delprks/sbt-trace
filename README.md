# sbt-trace

[![License](http://img.shields.io/:license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.delprks/sbt-trace/badge.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.delprks%22%20AND%20a%3A%22sbt-trace%22)

`sbt-trace` is a SBT plugin that can find traces of the client or library in different applications.

It does this by using the GitHub's Search API and locating usage of client or library in the `build.sbt` of applications.

<h2>Usage</h2>

<h3>Add to project</h3>

Add the following to `project/build.properties`:

```scala
addSbtPlugin("com.delprks" % "sbt-trace" % "1.0.0")
```

If you want to define the search scope in the library itself, you can add it to `build.sbt`:

```scala
val traceSettings = Seq(
  traceUser := "delprks"
)

lazy val `project-name`: Project = project.in(file("."))
  .enablePlugins(SbtTrace)
  .settings(traceSettings)

```

Or you can define it as an environment variable:

```
export TRACE_GIT_USER=delprks
```

<h3>Run</h3>

If you are using the plugin to search private repositories, you would need to provide a GitHub-generated access token which can retrieve repository information.

You should then export it as an environment variable:

```bash
export TRACE_GIT_TOKEN=<token>
```

You can run the following in client or libraries to find their usage:

```bash
sbt trace
```

Output:

```bash
Found 7 traces of rms-sounds-metadata in:
 |-rms-deftones
 |-rms-nirvana
 |-rms-programme-metadata-client
 |-rms-green-day
 |-rms-sounds-activities-client
 |-rms-sounds-metadata-client
 |-rms-blur
 +-rms-sounds-metadata
```

# License

`sbt-trace` is open source software released under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0).
