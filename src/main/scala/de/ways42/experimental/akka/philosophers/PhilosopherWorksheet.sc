package de.ways42.experimental.akka.philosophers

import scala.concurrent.duration._

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

object PhilosopherWorksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val system = ActorSystem("philosophers")        //> system  : akka.actor.ActorSystem = akka://philosophers
  val p = system.actorOf(Props(classOf[Philosopher], "aristoteles"), "aristoteles")
                                                  //> p  : akka.actor.ActorRef = Actor[akka://philosophers/user/aristoteles#-91535
                                                  //| 0416]


	println("created actor")                  //> created actor

 	p ! Philosopher.Think

	println("finished")                       //> finished/
}