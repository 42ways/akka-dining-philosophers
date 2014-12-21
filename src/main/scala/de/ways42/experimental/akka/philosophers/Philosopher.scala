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

  val eatingTime = 2000.millis
  val thinkingTime = 5000.millis
  val shutdownDelayTime = 10000.millis

  def receive: Receive = {

    case Eat =>
      println(s"Philosopher wants to eat")
      log info (s"Philosopher wants to eat")  // TODO: Insert name (id) of actor
      //context.system.scheduler.scheduleOnce(eatingTime, self, Think)

    case Think =>
      println(s"Philosopher wants to think")
      log info (s"Philosopher wants to think")  // TODO: Insert name (id) of actor
      //context.system.scheduler.scheduleOnce(thinkingTime, self, Eat)

    case Speak =>
      println(s"Philosopher speaks")
      log info (s"Philosopher speaks")  // TODO: Insert name (id) of actor

    case Bye =>
      println(s"Philosopher says goodbye")
      log info (s"Philosopher says goodbye")  // TODO: Insert name (id) of actor
      context.system.scheduler.scheduleOnce(shutdownDelayTime, self, Shutdown)

    case Shutdown =>
      println(s"Philosopher shuts system down")
      log info (s"Philosopher shuts system down")  // TODO: Insert name (id) of actor
      context.system.shutdown()
      
  }

}

