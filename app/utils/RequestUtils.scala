package utils

import play.api.libs.json.{JsValue, Json, OFormat}
import play.api.mvc.Result
import services.database.DbErrorResult

import scala.util.{Failure, Success, Try}

object RequestUtils
{
  type ResponseHandler = JsValue => Result

  def handleDatabaseInsertResult[A]
  (successHandler: ResponseHandler, errorHandler: ResponseHandler)
    (result: Try[A])(implicit format: OFormat[A]): Result =
  {
    result match {
      case Success(item) => successHandler(Json.toJson(item))
      case Failure(error: DbErrorResult) => errorHandler(Json.toJson(error))
    }
  }

}
