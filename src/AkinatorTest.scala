/**
  * Created by thomas on 12/03/16.
  */


import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import Akinator._

@RunWith(classOf[JUnitRunner])
class AkinatorTest extends FunSuite{

  test("Test fichierToABanimal"){
    val abr = fichierToABanimal("base.txt")
    abr match {
      case Question(q1,Question(q2,Animal(a1),Animal(a2)),Animal(a3)) => {
        assert(q1.equals("Est-ce qu'il a des ailes ?"))
        assert(q2.equals("Est-ce qu'il a des plumes ?"))
        assert(a1.equals("Pélican"))
        assert(a2.equals("Chauve-Souris"))
        assert(a3.equals("Chien"))
      }
      case _ => assert(false)
    }
  }

}


