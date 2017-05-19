package models

import play.api.libs.json._

case class Subscription(id: String, topicName: String, postUrl: String)

object Subscription
{
  implicit val jsonFormat: OFormat[Subscription] = Json.format[Subscription]
}
