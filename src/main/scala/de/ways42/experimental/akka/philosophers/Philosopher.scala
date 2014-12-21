package de.ways42.experimental.akka.philosophers

import scala.concurrent.duration._
import akka.actor._

object Philosopher {
  case object Eat
  case object Think
  case object Speak
  case object Bye
  case object Shutdown
}

class Philosopher extends Actor with ActorLogging {
  import Philosopher._

  implicit val ec = context.dispatcher

  val eatingTime = 1500.millis
  val thinkingTime = 3500.millis

  def receive: Receive = {

    case Eat =>
      log info (s"Philosopher wants to eat")  // TODO: Insert name (id) of actor
      context.system.scheduler.scheduleOnce(eatingTime, self, Think)

    case Think =>
      log info (s"Philosopher wants to think")  // TODO: Insert name (id) of actor
      context.system.scheduler.scheduleOnce(thinkingTime, self, Eat)

    case Speak =>
      log info (s"Philosopher speaks")  // TODO: Insert name (id) of actor

  }

}

