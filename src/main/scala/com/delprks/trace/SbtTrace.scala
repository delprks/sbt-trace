package com.delprks.trace

import com.delprks.trace.exception.TraceException
import com.delprks.trace.search.SearchClient
import sbt._

object SbtTrace extends AutoPlugin {

  private val searchClient = new SearchClient

  object autoImport {
    val traceProjectName = settingKey[String]("Project name")
    val traceUser = settingKey[String]("GitHub user or organization")
  }

  import autoImport._

  lazy val traceCommand =
    Command.command("trace") { (state: State) =>
      trace(state)
    }

  override def globalSettings: Seq[Setting[_]] = super.globalSettings ++ Seq(
    Keys.commands += traceCommand
  )

  override def projectSettings: Seq[Setting[_]] = Seq()

  private def trace(state: State): State = {
    val projectName: String = valueOfKey[String](state, traceProjectName).getOrElse(
      throw TraceException("Project name not specified.")
    )

    val user: String = valueOfKey[String](state, traceUser).getOrElse(
      throw TraceException("GitHub user or organization not specified.")
    )

    val searchResult = searchClient.search(projectName, user)
    val dependencies = searchClient.extractDependencies(searchResult, user)

    if (dependencies.nonEmpty) {
      println(s"Found ${dependencies.size} traces of $projectName in:")
      dependencies.foreach(dependency => println(s" |-$dependency"))
      println(s" +-$projectName\n")
    } else {
      println(s"Found no trace of $projectName.\n")
    }

    state
  }

  private def valueOfKey[T](state: State, key: SettingKey[T]): Option[T] = {
    val extracted = Project.extract(state)

    import extracted._

    key in currentRef get structure.data
  }

}
