Commande pour compiler et lancer Akinator:
scalac -cp out/production/Akinator/:. src/Akinator.scala -d out/production/Akinator/
scala -cp out/production/Akinator:. Akinator

Commande pour compiler et lancer les Tests:
scalac -cp lib/junit-4.12.jar:lib/scalatest_2.11-2.2.0.jar:out/production/Akinator/:. src/AkinatorTest.scala -d out/production/Akinator/
scala -cp lib/junit-4.12.jar:lib/scalatest_2.11-2.2.0.jar:out/production/Akinator:. org.scalatest.run AkinatorTest