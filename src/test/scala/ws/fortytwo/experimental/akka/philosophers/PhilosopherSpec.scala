package ws.fortytwo.experimental.akka.philosophers

import org.scalatest.{ BeforeAndAfterAll, WordSpecLike }
import akka.actor.{ Actor, ActorSystem, Props }
import akka.testkit.{ ImplicitSender, TestActorRef, TestKit }
import scala.concurrent.duration._

class PhilosopherSpec extends TestKit(ActorSystem("ChopstickSpec")) with ImplicitSender with WordSpecLike with BeforeAndAfterAll {
  import Philosopher._

  override def afterAll() { system.shutdown() }

  def actor() = {
    val a = TestActorRef[Philosopher](Props(classOf[Philosopher], testActor, testActor))
    (a, a.underlyingActor)
  }

  "Philosopher" should {
    "try to take chopsticks after thinking for a while" in {
      val (p, _) = actor()
      p ! Think
      expectMsg(5.seconds, Chopstick.Take)
      expectMsg(Chopstick.Take)
    }

    "try to take chopsticks when getting hungry" in {
      val (p, real) = actor()
      real.context.become(real.thinking)
      p ! Eat
      expectMsg(Chopstick.Take)
      expectMsg(Chopstick.Take)
    }

    "put back both chopsticks when getting a denial of the first chopstick" in {
      val (p, real) = actor()
      real.context.become(real.hungry)
      p ! Chopstick.ChopstickInUse
      expectMsg(Chopstick.Put)
      expectMsg(Chopstick.Put)
    }

    "put back both chopsticks when getting a denial of the second chopstick" in {
      val (p, real) = actor()
      real.context.become(real.waitingForOtherChopstick)
      p ! Chopstick.ChopstickInUse
      expectMsg(Chopstick.Put)
      expectMsg(Chopstick.Put)
    }

  }

}