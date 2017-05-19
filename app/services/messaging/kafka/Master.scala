package services.messaging.kafka

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.SmallestMailboxPool

class Master(kafkaUrl: String, numOfWorkers: Int) extends Actor
{
  val workerRouter: ActorRef = context.actorOf(
    Props(new Worker(KafkaService.createProducer(kafkaUrl))).withRouter(SmallestMailboxPool(numOfWorkers)),
    name = "workerRouter"
  )

  override def receive: Receive =
  {
    case kafkaMessage: KafkaMessage => {
      workerRouter ! kafkaMessage
    }
  }
}
