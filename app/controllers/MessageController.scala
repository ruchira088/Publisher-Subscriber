package controllers

import java.net.URL
import java.util.UUID
import javax.inject.{Inject, Singleton}

import models.Topic
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MessageController @Inject() (val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents
{
  case class TopicRequest(topicName: String)

  object TopicRequest
  {
    implicit def jsonFormat: OFormat[TopicRequest] = Json.format[TopicRequest]
  }

  case class SubscribeRequest(topicName: String, subscriberId: UUID, postUrl: Option[URL])

  val TOPICS_COLLECTION_NAME = "topics"

  lazy val getCollection: Future[JSONCollection] = database.map { _.collection[JSONCollection](TOPICS_COLLECTION_NAME) }

  def createTopic = Action.async {
    implicit request => {
      getCollection
        .flatMap {
          collection => {
            val topic = Topic(jsonBody.as[TopicRequest].topicName)
            collection.insert[Topic](topic)
              .map { _ => Ok(Json.toJson[Topic](topic)) }
          }
        }
    }
  }

  def getTopics = Action.async {
    getCollection
      .flatMap {
        _.find(Json.obj()).cursor[Topic]().collect[List]()
      }
      .map {
        topics => Ok(Json.obj("topics" -> JsArray(topics.map(Json.toJson(_)))))
      }
  }

  def jsonBody(implicit request: Request[AnyContent]): JsValue =
  {
    request.body.asJson match
    {
      case Some(json) => json
      case None => throw new Error("Unable to parse JSON")
    }
  }
}
