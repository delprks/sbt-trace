package com.delprks

import com.delprks.config.Config
import com.delprks.exception.TraceException
import com.delprks.search.SearchClient
import sbt.AutoPlugin
import sbt._

object SbtTrace extends AutoPlugin with Config {

  private final val DEPENDENCY_REGEX = repoPrefix match {
    case Some(prefix) => s""""$prefix([A-Za-z-0-9]+)"""".r
    case _ => """"([A-Za-z-0-9]+)"""".r
  }

  private val searchClient = new SearchClient

  object autoImport {
    val projectName = settingKey[String]("Project name")
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
    val name: String = valueOfKey[String](state, projectName).getOrElse(
      throw TraceException("Project name not specified.")
    )

    val fullProjectName = repoPrefix match {
      case Some(prefix) => prefix + name
      case _ => name
    }

    val searchResult = searchClient.search(name)
    val dependencies = DEPENDENCY_REGEX.findAllIn(searchResult).toList.map(_.replace("\"", "")).filter(_ != fullProjectName)

    if (dependencies.nonEmpty) {
      println(s"Found ${dependencies.size} traces of $fullProjectName in:")
      dependencies.foreach(dependency => println(s" |-$dependency"))
      println(s" +-$fullProjectName\n")
    } else {
      println(s"Found no trace of $fullProjectName.\n")
    }

    state
  }

  private def valueOfKey[T](state: State, key: SettingKey[T]): Option[T] = {
    val extracted = Project.extract(state)

    import extracted._

    key in currentRef get structure.data
  }

}
