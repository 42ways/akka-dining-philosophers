package ws.fortytwo.experimental.akka.philosophers

import scala.concurrent.duration._

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

object PhilosopherWorksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val system = ActorSystem("philosophers")        //> system  : akka.actor.ActorSystem = akka://philosophers
  val p = system.actorOf(Props(classOf[Philosopher], "aristoteles"), "aristoteles")
                                                  //> java.lang.IllegalArgumentException: no matching constructor found on class w
                                                  //| s.fortytwo.experimental.akka.philosophers.Philosopher for arguments [class j
                                                  //| ava.lang.String]
                                                  //| 	at akka.util.Reflect$.error$1(Reflect.scala:82)
                                                  //| 	at akka.util.Reflect$.findConstructor(Reflect.scala:106)
                                                  //| 	at akka.actor.ArgsReflectConstructor.<init>(Props.scala:350)
                                                  //| 	at akka.actor.IndirectActorProducer$.apply(Props.scala:309)
                                                  //| 	at akka.actor.Props.producer(Props.scala:176)
                                                  //| 	at akka.actor.Props.<init>(Props.scala:189)
                                                  //| 	at akka.actor.Props$.apply(Props.scala:93)
                                                  //| 	at ws.fortytwo.experimental.akka.philosophers.PhilosopherWorksheet$$anon
                                                  //| fun$main$1.apply$mcV$sp(ws.fortytwo.experimental.akka.philosophers.Philosoph
                                                  //| erWorksheet.scala:13)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)


	println("created actor")

 	p ! Philosopher.Think

	println("finished")
}