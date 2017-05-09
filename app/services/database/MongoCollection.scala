package services.database

import play.api.libs.json.{JsObject, OWrites, Reads}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class MongoCollection[A] (collectionName: String, val reactiveMongoApi: ReactiveMongoApi)
  extends MongoController with ReactiveMongoComponents with DatabaseCollection[A]
{
  private lazy val getJsonCollection: Future[JSONCollection] = database.map {
    _.collection[JSONCollection](collectionName)
  }

  def insert(item: A)(implicit writer: OWrites[A]): Future[Try[A]] = {
    getJsonCollection
      .flatMap {
        _.insert[A](item)
      }
      .map {
        writeResult => {
          if (writeResult.ok) {
            Success(item)
          } else {
            Failure(
              DbErrorResult(
                writeResult.writeErrors.map {
                  writeError => new Error(writeError.errmsg)
                }
              )
            )
          }
        }
      }
  }

  def find(query: JsObject)(implicit reader: Reads[A]): Future[List[A]] = {
    getJsonCollection.flatMap {
      _.find(query).cursor[A]().collect[List]()
    }
  }
}

object MongoCollection
{
  def apply[A](collectionName: String)(implicit reactiveMongoApi: ReactiveMongoApi): MongoCollection[A]
    = new MongoCollection[A](collectionName, reactiveMongoApi)
}
