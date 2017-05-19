package controllers.requests

import play.api.libs.json.{JsValue, Json, OFormat}

case class MessageRequest(message: JsValue)

object MessageRequest
{
  implicit val jsonFormat: OFormat[MessageRequest] = Json.format[MessageRequest]
}
