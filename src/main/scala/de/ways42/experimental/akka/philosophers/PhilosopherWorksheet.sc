package de.ways42.experimental.akka.philosophers

import scala.concurrent.duration._

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

object PhilosopherWorksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val system = ActorSystem("philosophers")        //> system  : akka.actor.ActorSystem = akka://philosophers
  val p = system.actorOf(Props[Philosopher], "aristoteles")
                                                  //> p  : akka.actor.ActorRef = Actor[akka://philosophers/user/aristoteles#-63100
                                                  //| 9259]

 	p ! Philosopher.Speak

	println("created actor")                  //> created actor

 	p ! Philosopher.Speak
// 	p ! Philosopher.Eat
 	p ! Philosopher.Speak

	println("finished")                       //> finished/
}