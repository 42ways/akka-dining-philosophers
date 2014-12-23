package ws.fortytwo.experimental.akka.philosophers

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.actorRef2Scala

object Chopstick {
  // Messages for Chopstick and client of Chopstick
  case object Take
  case object Put
  case object ChopstickInUse
  case object ChopstickTaken
}

class Chopstick extends Actor with ActorLogging {
  import Chopstick._
  import Philosopher._

  def name = self.path.name

  def available: Receive = {
    case Take =>
      log debug ("Chopstick %s is taken by Philosopher %s".format(name, sender.path.name))
      context.become(taken(sender))
      sender ! ChopstickTaken
  }

  def taken(philosopher: ActorRef): Receive = {
    case Take =>
      log debug ("Chopstick %s cannot be taken by Philosopher %s because it is in use by %s".format(name, sender.path.name, philosopher.path.name))
      sender ! ChopstickInUse
    case Put =>
      if (sender == philosopher) {
        log debug ("Chopstick %s is put by Philosopher %s".format(name, sender.path.name))
        context.become(available)
      }
  }

  def receive = available
}