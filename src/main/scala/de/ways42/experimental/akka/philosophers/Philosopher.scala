package de.ways42.experimental.akka.philosophers

import scala.concurrent.duration._
import akka.actor._

object Philosopher {
  // Messages for Philosopher
  case object Eat
  case object Think
  case object ChopstickInUse
  case class ChopstickTaken(chopstick: ActorRef)
}

class Philosopher(val name: String, val leftChopstick: ActorRef, val rightChopstick: ActorRef) extends Actor with ActorLogging {
  import Philosopher._
  import Chopstick._

  override def toString = name

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
    log info (s"Philosopher $name got a ChopstickInUse from $chopstick")
    thinkFor(retryTime)
  }

  def hungry: Receive = {
    case ChopstickInUse =>
      handleMissingChopstick(sender)
    case ChopstickTaken(chopstick) =>
      log info (s"Philosopher $name took $chopstick, waiting for the other one")
      context.become(waitingForOtherChopstick)
  }

  def waitingForOtherChopstick: Receive = {
    case ChopstickInUse =>
      handleMissingChopstick(sender)
    case ChopstickTaken(chopstick) =>
      log info (s"Philosopher $name took $chopstick and can now eat!")
      context.become(eating)
      context.system.scheduler.scheduleOnce(eatingTime, self, Think)
  }

  def thinking: Receive = {
    case Eat =>
      log info (s"Philosopher $name wants to eat and becomes hungry")
      context.become(hungry)
      takeChopsticks
  }

  def eating: Receive = {
    case Think =>
      log info (s"Philosopher $name is full and wants to think again")
      thinkFor(thinkingTime)
  }

  def receive: Receive = {
    case Think =>
      log info (s"Philosopher $name starts thinking")
      thinkFor(thinkingTime)
  }

}

