package services.database

import play.api.libs.json.{JsObject, OWrites, Reads}

import scala.concurrent.Future
import scala.util.Try

trait DatabaseCollection[A]
{
  def insert(item: A)(implicit writer: OWrites[A]): Future[Try[A]]

  def find(query: JsObject)(implicit reader: Reads[A]): Future[List[A]]
}
