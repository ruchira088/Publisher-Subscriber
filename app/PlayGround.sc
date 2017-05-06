import play.api.libs.json._

case class Topic(name: String, subscribers: List[String] = List.empty)

object Topic
{
  implicit def jsonFormat: OFormat[Topic] = new OFormat[Topic]
  {
    override def reads(json: JsValue): JsResult[Topic] =
    {
      val name = (json \ "name").as[String]
      val subscribers = (json \ "subscribers").as[JsArray].value.toList.map(_.as[String])

      JsSuccess(Topic(name, subscribers))
    }

    override def writes(topic: Topic): JsObject =
      Json.obj("name" -> topic.name, "subscribers" -> JsArray(topic.subscribers.map(JsString(_))))
  }
}

val topic = Topic("cats", List("1", "2", "3"))

val json: JsValue = Topic.jsonFormat.writes(topic)
Topic.jsonFormat.reads(json).get