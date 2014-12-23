package ws.fortytwo.experimental.akka.philosophers

import org.scalatest.{ BeforeAndAfterAll, WordSpecLike }
import akka.actor.{ Actor, ActorSystem, Props }
import akka.testkit.{ ImplicitSender, TestActorRef, TestKit }
import scala.concurrent.duration._

class PhilosopherSpec extends TestKit(ActorSystem("ChopstickSpec")) with ImplicitSender with WordSpecLike with BeforeAndAfterAll {
  import Philosopher._

  override def afterAll() { system.shutdown() }

  "Philosopher" should {
    "try to take chopsticks after thinking for a while" in {
      val p = TestActorRef(Props(classOf[Philosopher], testActor, testActor))
      p ! Think
      expectMsg(Chopstick.Put)
      expectMsg(Chopstick.Put)
      expectMsg(5.seconds, Chopstick.Take)
      expectMsg(Chopstick.Take)
    }
  }
}