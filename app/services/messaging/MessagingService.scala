package services.messaging

import akka.actor.ActorSystem
import play.api.libs.json.JsValue

trait MessagingService
{
  def send(topicName: String, message: JsValue)

}
