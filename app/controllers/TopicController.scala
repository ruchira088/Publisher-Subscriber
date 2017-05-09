package controllers

import javax.inject.{Inject, Singleton}

import models.Topic
import controllers.requests.TopicRequest
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.ReactiveMongoApi
import services.database.{DatabaseCollection, DbErrorResult, MongoCollection}
import utils.ControllerUtils._
import utils.RequestUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

@Singleton
class TopicController @Inject()(implicit val reactiveMongoApi: ReactiveMongoApi) extends Controller
{
  val TOPICS_COLLECTION_NAME = "topics"

  lazy val databaseCollection: DatabaseCollection[Topic] = MongoCollection(TOPICS_COLLECTION_NAME)

  def createTopic = Action.async {
    implicit request => {
      databaseCollection.insert(Topic(jsonBody.as[TopicRequest].topicName))
        .map { RequestUtils.handleDatabaseInsertResult[Topic](Ok(_), InternalServerError(_)) }
    }
  }

  def getTopic(topicName: String) = Action.async
  {
    databaseCollection.find(Json.obj("name" -> topicName))
      .map {
        results => Ok(Json.toJson(results.head))
      }
  }

  def getTopics = Action.async
  {
    databaseCollection.find(Json.obj())
      .map {
        result => Ok(Json.obj("topics" -> JsArray(result.map(Json.toJson(_)))))
      }
  }

}
