/**
  * Created by thomas on 12/03/16.
  */


import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import Akinator._

import scala.io.Source

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

  test("ABanimalToFichier"){
    ABanimalToFichier("basecopie.txt",fichierToABanimal("base.txt"))
    val li = Source.fromFile("base.txt").getLines.toList
    val lo = Source.fromFile("basecopie.txt").getLines.toList
    (li,lo).zipped.map{ (a,b) => assert(a.equals(b)) }
  }

  test("jeuSimple gagnant"){
    val ret = jeuSimple(fichierToABanimal("base.txt"),List("o","o","o").iterator)
    assert(ret)
  }

  test("jeuSimple perdant"){
    val ret = jeuSimple(fichierToABanimal("base.txt"),List("n","n").iterator)
    assert(!ret)
  }

  test("jeuLog"){
    val entry = List("o","n","o")
    val ret = jeuLog(fichierToABanimal("base.txt"),entry.iterator)
    assert(entry.equals(ret))
  }
}


