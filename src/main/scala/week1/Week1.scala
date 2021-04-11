package week1

/**
  * Recap on functions and pattern matching
  */
object Week1 extends App {
  val json: JSON = JObj(
    Map(
      "firstname" -> JString("John"),
      "lastName" -> JString("Smith"),
      "address" -> JObj(
        Map(
          "streetAddress" -> JString("21 2nd Street"),
          "state" -> JString("NY"),
          "postalCode" -> JNum(10021)
        )
      ),
      "phoneNumbers" -> JSeq(
        List(
          JObj(
            Map("type" -> JString("home"), "number" -> JString("212 555 -1234"))
          ),
          JObj(
            Map("type" -> JString("fax"), "number" -> JString("646 555 -4567"))
          )
        )
      )
    )
  )
  println(json.show)
}

trait JSON {
  def show: String =
    this match {
      case JSeq(elems) =>
        "[" + (elems map(_.show) mkString ", ") + "]"
      case JObj(bindings) =>
        val assocs = bindings map {
          case (key, value) => "\"" + key + "\": " + value.show
        }
        "{" + (assocs mkString ", ") + "}"
      case JNum(num)    => num.toString
      case JString(str) => '\"' + str + '\"'
      case JBool(b)     => b.toString
      case JNull        => "null"
    }

}

case class JSeq(elems: List[JSON]) extends JSON

case class JBool(b: Boolean) extends JSON

case class JString(s: String) extends JSON

case class JNum(n: Double) extends JSON

case class JObj(bindings: Map[String, JSON]) extends JSON

case object JNull extends JSON

object Ex {
  val x =
    """
{ ” firstName ” : ” John ” ,
  ” lastName ” : ” Smith ” ,
  ” address ”: {
  ” streetAddress ”: ”21 2 nd Street ” ,
  ” state ”: ” NY ” ,
  ” postalCode ”: 10021
},
  ” phoneNumbers ”: [
{ ” type ”: ” home ” , ” number ”: ”212 555 -1234” } ,
{ ” type ”: ” fax ” , ” number ”: ”646 555 -4567” }
  ]
}
"""
}
