package com.delprks.trace.search

import org.specs2.mutable.Specification

class SearchClientTest extends Specification {
  "extractDependencies" should {
    "extract the correct dependencies where Search API has returned results" in {
      val searchClient = new SearchClient()

      val searchResult =
        """
          |"repository":{"id":1021637,"name":"repo-name-1","full_name":"org-name/repo-name-1","owner":
          |"repository":{"id":1021157,"name":"repo-name-2","full_name":"org-name/repo-name-2","owner":
        """.stripMargin

      searchClient.extractDependencies(searchResult, "org-name") must be equalTo List(
        "repo-name-1",
        "repo-name-2"
      )
    }

    "extract distinct dependencies where Search API has returned results" in {
      val searchClient = new SearchClient()

      val searchResult =
        """
          |"repository":{"id":1021637,"name":"repo-name-1","full_name":"org-name/repo-name-1","owner":
          |"repository":{"id":1021157,"name":"repo-name-1","full_name":"org-name/repo-name-1","owner":
        """.stripMargin

      searchClient.extractDependencies(searchResult, "org-name") must be equalTo List(
        "repo-name-1"
      )
    }

    "return empty list where Search API has found no matches" in {
      val searchClient = new SearchClient()

      val searchResult =
        """
          |{
          |  "total_count": 0,
          |  "incomplete_results": false,
          |  "items": [
          |  ]
          |}
        """.stripMargin

      searchClient.extractDependencies(searchResult, "org-name") must beEmpty
    }

  }
}
