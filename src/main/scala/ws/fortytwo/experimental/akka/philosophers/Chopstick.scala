package ws.fortytwo.experimental.akka.philosophers

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef

object Chopstick {
  // Messages for Chopstick
  case object Take
  case object Put
}

class Chopstick (val name: String) extends Actor with ActorLogging {
  import Chopstick._
  import Philosopher._

  override def toString = name

  def available: Receive = {
    case Take =>
      log info (s"Chopstick $name is taken by Philosopher $sender")
      context.become(taken(sender))
      sender ! ChopstickTaken(self)
  }

  def taken(philosopher: ActorRef): Receive = {
    case Take =>
      log info (s"Chopstick $name cannot be taken by Philosopher $sender because it is in use by $philosopher")
      sender ! ChopstickInUse
    case Put =>
      if (sender == philosopher) {
        log info (s"Chopstick $name is put by Philosopher $sender")
        context.become(available)
      }
  }

  def receive = available
}