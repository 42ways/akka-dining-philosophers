package ws.fortytwo.experimental.akka.philosophers

import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}

class SomeActor extends Actor {
  def receive: Receive = Actor.emptyBehavior
}

class ChopstickSpec extends TestKit(ActorSystem("ChopstickSpec")) with ImplicitSender with WordSpecLike with BeforeAndAfterAll {
  import Chopstick._

  override def afterAll() { system.shutdown() }

  "Chopstick" should {

    "be available if not in use" in {
      val c = TestActorRef(Props[Chopstick])
      c ! Take
      expectMsg(ChopstickTaken)
    }

    "not be taken twice" in {
      val c = TestActorRef(Props[Chopstick])
      c ! Take
      expectMsg(ChopstickTaken)
      c ! Take
      expectMsg(ChopstickInUse)
    }

    "not be available if taken by another actor" in {
      val other = TestActorRef(Props[SomeActor])
      val c = TestActorRef(Props[Chopstick])
      c.!(Take)(other)
      c ! Take
      expectMsg(ChopstickInUse)
    }

    "again be available if given back by the other actor" in {
      val other = TestActorRef(Props[SomeActor])
      val c = TestActorRef(Props[Chopstick])
      c.!(Take)(other)
      c ! Take
      expectMsg(ChopstickInUse)
      c.!(Put)(other)
      c ! Take
      expectMsg(ChopstickTaken)
    }

    "be taken by another actor after given back" in {
      val other = TestActorRef(Props[SomeActor])
      val c = TestActorRef(Props[Chopstick])
      c ! Take
      expectMsg(ChopstickTaken)
      c ! Put
      c.!(Take)(other)
      c ! Take
      expectMsg(ChopstickInUse)
    }

  }

}
