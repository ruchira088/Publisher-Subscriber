package models

import controllers.requests.SubscriberRequest
import play.api.libs.json._

case class Subscriber(id: String, topics: Seq[String], postUrl: String)

object Subscriber
{
  def createFromRequest(subscriberRequest: SubscriberRequest) =
    Subscriber(subscriberRequest.subscriberId, subscriberRequest.topics, subscriberRequest.postUrl)

  implicit val jsonFormat: OFormat[Subscriber] = Json.format[Subscriber]
}
