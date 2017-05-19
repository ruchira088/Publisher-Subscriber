package controllers

import javax.inject.{Inject, Singleton}

import controllers.requests.SubscriptionRequest
import models.Subscription
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.ReactiveMongoApi
import services.database.{DatabaseCollection, MongoCollection}
import utils.ControllerUtils._
import utils.RequestUtils

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SubscriptionController @Inject()(implicit val reactiveMongoApi: ReactiveMongoApi) extends Controller
{
  val SUBSCRIPTION_COLLECTION_NAME = "subscriptions"

  lazy val subscriptionCollection: DatabaseCollection[Subscription] = MongoCollection(SUBSCRIPTION_COLLECTION_NAME)

  def create(topicName: String) = Action.async {
    implicit request =>
    {
      val SubscriptionRequest(subscriberId, postUrl) = jsonBody.as[SubscriptionRequest]

      subscriptionCollection.insert(Subscription(subscriberId, topicName, postUrl))
        .map { RequestUtils.handleDatabaseInsertResult[Subscription](Ok(_), InternalServerError(_)) }
    }
  }
}
