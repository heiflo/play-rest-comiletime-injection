package models

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

case class Person(id: Int, name: String, email: String)

trait PersonService {
  def list: Seq[Person]
  def update(person: Person): Int
}

object PersonService {
  val list: ListBuffer[Person] = new ListBuffer[Person] += Person(1, "Leon Frischauf", "leon.frischauf@s-itsolutions.at")
}

class PersonCacheService extends PersonService {

  def list = {
    PersonService.list.toList
  }

  def update(person: Person): Int = {
    val index = PersonService.list.indexOf(person)
    if(index != -1) {
      PersonService.list.update(index, person)
      person.id
    } else {
      -1
    }
  }

}
