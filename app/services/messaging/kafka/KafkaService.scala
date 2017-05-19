package services.messaging.kafka

import java.util.Properties

import akka.actor.{ActorRef, ActorSystem, Props}
import org.apache.kafka.clients.producer.ProducerConfig._
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringSerializer
import play.api.libs.json.JsValue
import services.messaging.MessagingService

object KafkaService extends MessagingService
{
  lazy val master: ActorRef = ActorSystem("kafka-service")
    .actorOf(Props(new Master("localhost:9092", 2)), name = "master")

  override def send(topicName: String, message: JsValue): Unit =
  {
    master ! KafkaMessage(topicName, message)
  }

  def createProducer(kafkaUrl: String): KafkaProducer[String, String] =
  {
    val serializerClassName = classOf[StringSerializer].getName

    val properties = Map[String, String](
      BOOTSTRAP_SERVERS_CONFIG -> kafkaUrl,
      ACKS_CONFIG -> "all",
      RETRIES_CONFIG -> "0",
      BATCH_SIZE_CONFIG -> "16384",
      LINGER_MS_CONFIG -> "1",
      BUFFER_MEMORY_CONFIG -> "33554432",
      KEY_SERIALIZER_CLASS_CONFIG -> serializerClassName,
      VALUE_SERIALIZER_CLASS_CONFIG -> serializerClassName
    )

    val kafkaProducerProps = properties.foldLeft(new Properties())((props, entry) => {
      val (key, value) = entry
      props.setProperty(key, value)
      props
    })

    new KafkaProducer[String, String](kafkaProducerProps)
  }
}
