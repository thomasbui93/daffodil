package controllers.api

case class SearchResult[A](list: Seq[A], count: Int)
