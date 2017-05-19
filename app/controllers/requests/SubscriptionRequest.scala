package controllers.requests

import play.api.libs.json._

case class SubscriptionRequest(subscriberId: String, postUrl: String)

object SubscriptionRequest
{
  implicit val jsonFormat = Json.format[SubscriptionRequest]
}