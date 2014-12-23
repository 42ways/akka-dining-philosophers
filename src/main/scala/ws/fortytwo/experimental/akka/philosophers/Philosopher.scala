package ws.fortytwo.experimental.akka.philosophers

import scala.concurrent.duration._
import akka.actor._

object Philosopher {
  // Messages for Philosopher
  case object Eat
  case object Think
}

class Philosopher(val leftChopstick: ActorRef, val rightChopstick: ActorRef) extends Actor with ActorLogging {
  import Philosopher._
import Chopstick._

  def name = self.path.name

  implicit val executionContext = context.dispatcher

  val eatingTime = 1500.millis
  val thinkingTime = 3500.millis
  val retryTime = 10.millis

  private def takeChopsticks = {
    leftChopstick ! Take
    rightChopstick ! Take
  }

  private def putChopsticks = {
    leftChopstick ! Put
    rightChopstick ! Put
  }

  private def thinkFor(duration: FiniteDuration) = {
    putChopsticks
    context.become(thinking)
    context.system.scheduler.scheduleOnce(duration, self, Eat)
  }

  private def handleMissingChopstick(chopstick: ActorRef) = {
    log debug ("Philosopher %s got a ChopstickInUse from %s".format(name, chopstick.path.name))
    thinkFor(retryTime)
  }

  def hungry: Receive = {
    case ChopstickInUse =>
      handleMissingChopstick(sender)
    case ChopstickTaken =>
      log debug ("Philosopher %s took %s, waiting for the other one".format(name, sender.path.name))
      context.become(waitingForOtherChopstick)
  }

  def waitingForOtherChopstick: Receive = {
    case ChopstickInUse =>
      handleMissingChopstick(sender)
    case ChopstickTaken =>
      log debug ("Philosopher %s took %s and can now eat!".format(name, sender.path.name))
      log info ("Philosopher %s starts to eat with %s and %s".format(name, leftChopstick.path.name, rightChopstick.path.name))
      context.become(eating)
      context.system.scheduler.scheduleOnce(eatingTime, self, Think)
  }

  def thinking: Receive = {
    case Eat =>
      log debug ("Philosopher %s wants to eat and becomes hungry".format(name))
      context.become(hungry)
      takeChopsticks
  }

  def eating: Receive = {
    case Think =>
      log debug ("Philosopher %s is full and starts thinking again".format(name))
      log info ("Philosopher %s puts %s and %s down and starts to think".format(name, leftChopstick.path.name, rightChopstick.path.name))
      thinkFor(thinkingTime)
  }

  def receive: Receive = {
    case Think =>
      log info ("Philosopher %s starts to think".format(name))
      thinkFor(thinkingTime)
  }

}

