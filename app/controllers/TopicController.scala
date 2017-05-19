package controllers

import java.util.UUID
import javax.inject.{Inject, Singleton}

import controllers.requests.TopicRequest
import models.Topic
import play.api.libs.json._
import play.api.mvc.{Action, _}
import play.modules.reactivemongo.ReactiveMongoApi
import services.database.{DatabaseCollection, MongoCollection}
import utils.ControllerUtils._
import utils.RequestUtils

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TopicController @Inject()(implicit val reactiveMongoApi: ReactiveMongoApi) extends Controller
{
  val TOPICS_COLLECTION_NAME = "topics"

  lazy val databaseCollection: DatabaseCollection[Topic] = MongoCollection(TOPICS_COLLECTION_NAME)

  def createTopic = Action.async {
    implicit request => {
      databaseCollection.insert(Topic(id = UUID.randomUUID().toString, name = jsonBody.as[TopicRequest].topicName))
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
