package com.delprks.search

import com.delprks.config.Config
import scalaj.http.Http

class SearchClient extends Config {
  private val url = (projectName: String) => s"https://api.github.com/search/code?q=$projectName+in:file+extension:sbt+user:$user"

  def search(projectName: String): String = token match {
    case Some(authorization) => Http(url(projectName)).header("Authorization", s"token $authorization").asString.body
    case _ => Http(url(projectName)).asString.body
  }
}
