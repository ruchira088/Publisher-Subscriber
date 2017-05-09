package utils

import play.api.libs.json.JsValue
import play.api.mvc.{AnyContent, Request}

object ControllerUtils
{
  def jsonBody(implicit request: Request[AnyContent]): JsValue =
  {
    request.body.asJson match
    {
      case Some(json) => json
      case None => throw new Error("Unable to parse JSON")
    }
  }
}
