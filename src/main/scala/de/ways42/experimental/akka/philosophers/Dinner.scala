package de.ways42.experimental.akka.philosophers

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import akka.actor.Props

object Dinner {

  import Philosopher._

  val system = ActorSystem("dinner")

  def main(args: Array[String]) {
    val p1 = system.actorOf(Props[Philosopher], "aristoteles")

    p1 ! Speak
    p1 ! Eat

    system.scheduler.scheduleOnce(2.seconds)(p1 ! Speak)
    system.scheduler.scheduleOnce(4.seconds)(p1 ! Speak)
    system.scheduler.scheduleOnce(6.seconds)(p1 ! Speak)
    system.scheduler.scheduleOnce(8.seconds)(p1 ! Speak)
    
    system.scheduler.scheduleOnce(10.seconds)(system.shutdown())
  }

}