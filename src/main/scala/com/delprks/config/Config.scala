package com.delprks.config

import com.delprks.exception.TraceException
import scala.io.Source

trait Config {
  private lazy val configFile = Source.fromURL(getClass.getResource("/trace_plugin.conf")).mkString

  private val userRegex = """user = "([{}A-Z_a-z-0-9]+)"""".r
  private val tokenRegex = """token = "([{}A-Z_a-z-0-9]+)"""".r
  private val repoPrefixRegex = """repo-prefix = "([{}A-Z_a-z-0-9]+)"""".r

  lazy val user: String = userRegex
    .findFirstIn(configFile).map(
    _.replace("\"", "")
      .replace("user = ", "")
  ) match {
    case Some(u) if u == "{{GIT_USER}}" && sys.env.get("TRACE_REPO_USER").nonEmpty => sys.env("TRACE_REPO_USER")
    case Some(u) if u.trim.isEmpty || u == "{{GIT_USER}}" => throw TraceException("You need to specify a GitHub user or organization for sbt-trace to search")
    case Some(u) => u
  }

  lazy val token: Option[String] = tokenRegex
    .findFirstIn(configFile).map(
    _.replace("\"", "")
      .replace("token = ", "")
  ) match {
    case Some(t) if t == "{{GIT_TOKEN}}" => sys.env.get("TRACE_REPO_TOKEN")
    case Some(t) if t.isEmpty => None
    case Some(t) => Some(t)
  }

  lazy val repoPrefix: Option[String] = repoPrefixRegex
    .findFirstIn(configFile).map(
    _.replace("\"", "")
      .replace("repo-prefix = ", "")
  ) match {
    case Some(r) if r == "{{REPO_PREFIX}}" => sys.env.get("TRACE_REPO_PREFIX")
    case Some(r) if r.isEmpty => None
    case Some(r) => Some(r)
  }

}
