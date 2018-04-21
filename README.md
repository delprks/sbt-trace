# sbt-trace

[![License](http://img.shields.io/:license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

`sbt-trace` is a SBT plugin that can find traces of the client or library in different applications.

It does this by using the GitHub's Search API and locating usage of client or library in the `build.sbt` of applications.

<h2>Usage</h2>

<h3>Add to project</h3>

Add the following to `project/build.properties`:

```scala
addSbtPlugin("com.delprks" % "sbt-trace" % "1.0.0")
```

And update the `build.sbt`:

```scala
val traceSettings = Seq(
  traceProjectName := name.value,
  traceUser := "delprks"
)

lazy val `project-name`: Project = project.in(file("."))
  .enablePlugins(SbtTrace)
  .settings(traceSettings)

```

<h3>Run</h3>

If you are using the plugin to search private repositories, you would need to provide a GitHub-generated access token which can retrieve repository information.

You should then export it as an environment variable:

```bash
export TRACE_GIT_TOKEN=<token>
```

You can run the following in client or libraries to find their traces:

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
