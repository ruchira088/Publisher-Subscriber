package services.database

import play.api.libs.json._

case class DbErrorResult(errorList: Seq[Throwable]) extends Error

object DbErrorResult
{
  implicit val writes: Writes[DbErrorResult] = new Writes[DbErrorResult]
  {
    override def writes(dbErrorResult: DbErrorResult): JsValue =
      Json.obj("error" -> JsArray(dbErrorResult.errorList.map { error => JsString(error.getMessage) }))
  }
}
