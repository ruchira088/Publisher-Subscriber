package playground

import play.api.libs.json.Json
import services.messaging.kafka.KafkaService

object Main
{
  def main(args: Array[String]): Unit =
  {
    KafkaService.send("my-sample-topic", Json.obj("name" -> "John"))
  }
}
