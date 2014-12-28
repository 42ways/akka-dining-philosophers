package ws.fortytwo.experimental.akka.philosophers

import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef

object Philosopher {
  // Messages for Philosopher
  case object Eat
  case object Think
}

class Philosopher(private val leftChopstick: ActorRef, private val rightChopstick: ActorRef) extends Actor with ActorLogging {
  import Philosopher._
  import Chopstick._

  def name = self.path.name

  implicit private val executionContext = context.dispatcher

  private val eatingTime = 1500.millis
  private val thinkingTime = 3500.millis
  private val retryTime = 10.millis

  private def takeChopsticks = {
    leftChopstick ! Take
    rightChopstick ! Take
  }

  private def putChopsticks = {
    leftChopstick ! Put
    rightChopstick ! Put
  }

  private def thinkFor(duration: FiniteDuration) = {
    context.system.scheduler.scheduleOnce(duration, self, Eat)
    context.become(thinking)
  }

  private def handleMissingChopstick(chopstick: ActorRef) = {
    log debug ("Philosopher %s got a ChopstickInUse from %s".format(name, chopstick.path.name))
    // we always put down both chopsticks for simplicity (but this leads to some unhandled messages)
    putChopsticks
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
      log info ("Philosopher %s STARTS TO EAT with %s and %s".format(name, leftChopstick.path.name, rightChopstick.path.name))
      context.system.scheduler.scheduleOnce(eatingTime, self, Think)
      context.become(eating)
  }

  def thinking: Receive = {
    case Eat =>
      log debug ("Philosopher %s wants to eat and becomes hungry".format(name))
      takeChopsticks
      context.become(hungry)
  }

  def eating: Receive = {
    case Think =>
      log debug ("Philosopher %s is full and starts thinking again".format(name))
      log info ("Philosopher %s STARTS TO THINK and puts %s and %s down".format(name, leftChopstick.path.name, rightChopstick.path.name))
      putChopsticks
      thinkFor(thinkingTime)
  }

  override def unhandled(msg: Any): Unit = {
    msg match {
      case m =>
        // we get quite a bit unhandled "ChopstickTaken" and "ChopstickInUse" messages. Change debug to info to see them
        log debug ("Philosopher %s currently doesn't handle %s from %s".format(self.path.name, m, sender.path.name))
        super.unhandled(m)
    }
  }

  def receive: Receive = {
    case Think =>
      log info ("Philosopher %s initially STARTS TO THINK".format(name))
      thinkFor(thinkingTime)
  }

}

