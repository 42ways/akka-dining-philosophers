package ws.fortytwo.experimental.akka.philosophers

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorLogging

object Chopstick {
  // Messages Chopstick receives and sends
  case object Take
  case object Put
  case object ChopstickInUse
  case object ChopstickTaken
}

class Chopstick extends Actor with ActorLogging {
  import Chopstick._

  def name = self.path.name

  def available: Receive = {
    case Take =>
      log debug ("Chopstick %s is taken by Philosopher %s".format(name, sender.path.name))
      sender ! ChopstickTaken
      context.become(taken(sender))
  }

  def taken(philosopher: ActorRef): Receive = {
    case Take =>
      log debug ("Chopstick %s cannot be taken by Philosopher %s because it is in use by %s".format(name, sender.path.name, philosopher.path.name))
      sender ! ChopstickInUse
    case Put if sender == philosopher =>
      log debug ("Chopstick %s is put by Philosopher %s".format(name, sender.path.name))
      context.become(available)
  }

  override def unhandled(msg: Any): Unit = {
    msg match {
      case m =>
        // we get quite a bit unhandled "Put" messages. Change debug to info to see them
        log debug ("Chopstick %s currently doesn't handle %s from %s".format(name, m, sender.path.name))
        super.unhandled(m)
    }
  }

  def receive = available
}
