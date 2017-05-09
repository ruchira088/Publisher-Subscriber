package models

import play.api.libs.json._

case class Topic(name: String, subscribers: Seq[String] = List.empty)

object Topic
{
  implicit val jsonFormat: OFormat[Topic] = Json.format[Topic]
}
