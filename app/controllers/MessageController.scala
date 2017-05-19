package controllers

import javax.inject.{Inject, Singleton}

import controllers.requests.MessageRequest
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.messaging.MessagingService
import utils.ControllerUtils._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class MessageController @Inject()(val messagingService: MessagingService) extends Controller
{
  def publishMessage(topicName: String) = Action.async {
    implicit request =>
    {
      Future {
        Ok(Json.toJson[MessageRequest](jsonBody.as[MessageRequest]))
      }
    }
  }

}