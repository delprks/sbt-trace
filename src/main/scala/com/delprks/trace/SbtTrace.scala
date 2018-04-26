package com.delprks.trace

import com.delprks.trace.search.SearchClient
import sbt._

object SbtTrace extends AutoPlugin {

  private val searchClient = new SearchClient

  object autoImport {
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
    val projectName: String = Project.extract(state) get Keys.name

    val user: String = sys.env.get("TRACE_GIT_USER")
      .orElse(valueOfKey[String](state, traceUser))
      .orElse(sys.error(
        """
          |GitHub user or organization not specified. This is needed to limit scope of search. You can either:
          | - Export the GitHub user or organization that you want to search as an env variable: export TRACE_GIT_USER=username
          | - Or add it to all the libraries that use this plugin in settings: traceUser=username
        """.stripMargin)).get

    val searchResult = searchClient.search(projectName, user)
    val dependents = searchClient.extractDependents(searchResult, user)

    if (dependents.nonEmpty) {
      println(s"Found ${dependents.size} traces of $projectName in:")
      dependents.foreach(dependent => println(s" |-$dependent"))
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
