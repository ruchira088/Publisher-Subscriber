package controllers.requests

import play.api.libs.json._

case class SubscriberRequest(subscriberId: String, topics: Seq[String], postUrl: String)

object SubscriberRequest
{
  implicit val jsonFormat = Json.format[SubscriberRequest]
}