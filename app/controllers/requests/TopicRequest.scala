package controllers.requests

import play.api.libs.json.{Json, OFormat}

case class TopicRequest(topicName: String)

object TopicRequest
{
  implicit val jsonFormat: OFormat[TopicRequest] = Json.format[TopicRequest]
}