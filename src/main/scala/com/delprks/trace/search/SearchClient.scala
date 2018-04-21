package com.delprks.trace.search

import scalaj.http.Http

class SearchClient {
  private val url = (projectName: String, user: String) => s"https://api.github.com/search/code?q=$projectName+in:file+extension:sbt+user:$user"

  def search(projectName: String, user: String): String = sys.env.get("TRACE_GIT_TOKEN") match {
    case Some(authorization) => Http(url(projectName, user)).header("Authorization", s"token $authorization").asString.body
    case _ => Http(url(projectName, user)).asString.body
  }
}
