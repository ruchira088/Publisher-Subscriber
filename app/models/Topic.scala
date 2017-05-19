package models

import play.api.libs.json._

case class Topic(id: String, name: String)

object Topic
{
  implicit val jsonFormat: OFormat[Topic] = Json.format[Topic]
}
