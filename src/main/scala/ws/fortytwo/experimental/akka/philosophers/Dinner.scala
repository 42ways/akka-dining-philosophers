package ws.fortytwo.experimental.akka.philosophers

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import akka.actor.Props

object Dinner {

  import Philosopher._

  val system = ActorSystem("dinner")

  def main(args: Array[String]) {
    val chopsticks = for ( i <- 1 to 5 ) yield system.actorOf(Props[Chopstick], s"c$i")
    val philosophers = for ( (name, i) <- List("aristoteles", "plato", "decartes", "kant", "nitzsche").zipWithIndex ) yield system.actorOf(Props(classOf[Philosopher], chopsticks(i), chopsticks((i + 1) % 5)), name)

    philosophers foreach { _ ! Think }

    system.scheduler.scheduleOnce(15.seconds)(system.shutdown())
  }

}