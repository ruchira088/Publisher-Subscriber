package services.messaging.kafka

import play.api.libs.json.JsValue

case class KafkaMessage(topicName: String, message: JsValue)
