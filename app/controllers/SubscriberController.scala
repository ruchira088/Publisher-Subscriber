package controllers

import javax.inject.{Inject, Singleton}

import controllers.requests.SubscriberRequest
import models.Subscriber
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.ReactiveMongoApi
import services.database.{DatabaseCollection, MongoCollection}
import utils.ControllerUtils._
import utils.RequestUtils

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SubscriberController @Inject()(implicit val reactiveMongoApi: ReactiveMongoApi) extends Controller
{
  val SUBSCRIBER_COLLECTION_NAME = "subscribers"

  lazy val subscriberCollection: DatabaseCollection[Subscriber] = MongoCollection(SUBSCRIBER_COLLECTION_NAME)

  def create = Action.async {
    implicit request =>
    {
      subscriberCollection.insert(Subscriber.createFromRequest(jsonBody.as[SubscriberRequest]))
        .map { RequestUtils.handleDatabaseInsertResult[Subscriber](Ok(_), InternalServerError(_)) }
    }
  }
}
