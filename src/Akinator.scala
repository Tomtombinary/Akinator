/**
  * Created by thomas on 26/02/16.
  */

import java.io._

import scala.io.Source

object Akinator{
  trait ABanimal
  case class Animal(nom:String) extends ABanimal
  case class Question(q:String,oui:ABanimal,non:ABanimal) extends ABanimal

  /*

  Ancienne version

  def fichierToABanimal(nomf:String): ABanimal = {
    val line = Source.fromFile(nomf).getLines()

    def aux(): ABanimal = {
      val readline = line.next()
      if(readline.contains("q:")) {
        val question = readline.substring(2)
        Question(question, aux(), aux())
      }else
        Animal(readline)
    }

    Question(line.next().substring(2),aux(),aux())
  }
  */

  def fichierToABanimal(nomf:String): ABanimal = {
    val line = Source.fromFile(nomf).getLines().toList

    def aux(l:List[String]): (Integer,ABanimal) = l match {
      case t::q =>
      {
        if (t.contains("q:")) {
          val (i, a) = aux(q)
          val (i2,b) = aux(q.drop(i))
          (1+i+i2,Question(t.substring(2),a,b))
        }else
          (1,Animal(t))
      }
      case List(x) => (1,Animal(x))
    }

    val (i,a) = aux(line)
    a
  }

  def ABanimalToFichier(nomf:String,a:ABanimal) : Unit = {
    val writer = new FileWriter(new File(nomf))
    def aux(toWrite:ABanimal): Unit = toWrite match {
      case Question(q,o,n) => {
        writer.write("q:"+q+"\n")
        aux(o)
        aux(n)
      }
      case Animal(nom) => writer.write(nom+"\n")
    }
    aux(a)
    writer.close()
  }

  def jeuSimple(a:ABanimal,it:Iterator[String]) : Boolean = a match {
    case Question(q,o,n) => {
      println(q)
      val r = it.next()
      if(r.equals("o")) jeuSimple(o,it)
      else if(r.equals("n")) jeuSimple(n,it)
      else {
        println("Repondez par oui (o) ou par non (n) !")
        jeuSimple(a, it)
      }
    }
    case Animal(nom) => {
      println("Pensez-vous à : "+nom)
      val r = it.next()
      if(r.equals("o")) true
      else if(r.equals("n")) false
      else{
        println("Repondez par oui (o) ou par non (n) !")
        jeuSimple(a,it)
      }
    }
  }

  def jeuSimpleJNSP(a:ABanimal,it:Iterator[String]) : Boolean = a match {
    case Question(q,o,n) => {
      println(q)
      val r = it.next()
      if(r.equals("o")) jeuSimpleJNSP(o,it)
      else if(r.equals("n")) jeuSimpleJNSP(n,it)
      else if(r.equals("x"))
      {
        if(jeuSimpleJNSP(o,it)){
          true
        }else
          jeuSimpleJNSP(n,it)
      }

      else {
        println("Repondez par oui (o) ou par non (n) ou je ne sais pas (x) !")
        jeuSimpleJNSP(a, it)
      }
    }
    case Animal(nom) => {
      println("Pensez-vous à : "+nom)
      val r = it.next()
      if(r.equals("o")) true
      else if(r.equals("n")) false
      else{
        println("Repondez par oui (o) ou par non (n) !")
        jeuSimpleJNSP(a,it)
      }
    }
  }


  def jeuLog(a:ABanimal,it:Iterator[String]) : List[String] = {
    def aux(abr:ABanimal,l:List[String]) : List[String] = abr match {
      case Question(q, o, n) => {
        println(q)
        val r = it.next()
        if (r.equals("o"))
          aux(o,l++List(r))
        else if (r.equals("n")){
          aux(n,l++List(r))
        }
        else {
          println("Repondez par oui (o) ou par non (n) !");
          jeuLog(abr, it)
        }
      }
      case Animal(nom) => {
        println("Pensez-vous à : " + nom)
        val r = it.next()
        if (r.equals("o")){
          l++List(r)
        }
        else if (r.equals("n")){
          l++List(r)
        }
        else {
          println("Repondez par oui (o) ou par non (n) !")
          aux(abr,l)
        }
      }
    }
    aux(a,List())
  }

  def jeuApprentissage(a:ABanimal,it:Iterator[String]): ABanimal = a match{
    case Question(q,o,n) => {
      println(q)
      val r = it.next()
      if(r.equals("o")) Question(q,jeuApprentissage(o,it),n)
      else if(r.equals("n")) Question(q,o,jeuApprentissage(n,it))
      else {
        println("Repondez par oui (o) ou par non (n) !")
        jeuApprentissage(a, it)
      }
    }

    case Animal(nom) => {
      println("Pensez-vous à : "+nom)
      val r = it.next()
      if(r.equals("o")) {
        println("J'ai gagné")
        Animal(nom)
      }
      else if(r.equals("n")){
        println("J'ai perdu - quelle est la bonne réponse ?")
        val animal = it.next()
        println("Quelle question permet de différencier "+animal+" de "+nom+" ?")
        val question = it.next()
        println("Quelle est la réponse à cette question pour "+animal+" ?")
        if(it.next().equals("o"))
          Question(question,Animal(animal),Animal(nom))
        else
          Question(question,Animal(nom),Animal(animal))
      }
      else{
        println("Repondez par oui (o) ou par non (n) !")
        jeuApprentissage(a,it)
      }
    }
  }

  def main(args:Array[String]) {
    /* Test unitaire */
    //ABanimalToFichier("jeuapprentissage.txt", fichierToABanimal("base.txt"))
    //println(fichierToABanimal("jeuapprentissage.txt"))
    //val ret = jeuLog(fichierToABanimal("base.txt"), Source.stdin.getLines())
    //println(ret)
    def jouer(fname:String): Unit = {
      ABanimalToFichier(fname,jeuApprentissage(fichierToABanimal(fname),Source.stdin.getLines()))
      println("Voulez vous rejouer ? (o/n)")
      val restart = Source.stdin.getLines().next()
      if(restart.equals("o"))
        jouer(fname)
    }

    jouer("jeuapprentissage.txt")
    //jeuApprentissage(fichierToABanimal("jeuapprentissage.txt"),Source.stdin.getLines())
  }
}