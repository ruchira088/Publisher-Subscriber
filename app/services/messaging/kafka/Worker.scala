package services.messaging.kafka

import akka.actor.Actor
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class Worker(kafkaProducer: KafkaProducer[String, String]) extends Actor
{
  override def receive: Receive =
  {
    case KafkaMessage(topicName, message) =>
    {
      kafkaProducer.send(new ProducerRecord[String, String](topicName, message.toString()))
    }
  }
}
