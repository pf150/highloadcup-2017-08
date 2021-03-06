package com.github.evis.highloadcup2017.api

import com.github.evis.highloadcup2017.model._
import spray.json._

trait JsonFormats extends DefaultJsonProtocol {

  implicit val userFormat: JsonFormat[User] = jsonFormat(User.apply,
    "id", "email", "first_name", "last_name", "gender", "birth_date")

  implicit val locationFormat: JsonFormat[Location] = jsonFormat5(Location.apply)

  implicit val visitFormat: JsonFormat[Visit] = jsonFormat(Visit.apply,
    "id", "location", "user", "visited_at", "mark")

  implicit val userUpdateReader: JsonReader[UserUpdate] = json => {
    implicit val fields = json.asJsObject.fields
    UserUpdate(
      getField[String]("email"),
      getField[String]("first_name"),
      getField[String]("last_name"),
      getField[Char]("gender"),
      getField[Int]("birth_date")
    )
  }

  implicit val locationUpdateReader: JsonReader[LocationUpdate] = json => {
    implicit val fields = json.asJsObject.fields
    LocationUpdate(
      getField[String]("place"),
      getField[String]("country"),
      getField[String]("city"),
      getField[Int]("distance")
    )
  }

  implicit val visitUpdateReader: JsonReader[VisitUpdate] = json => {
    implicit val fields = json.asJsObject.fields
    VisitUpdate(
      getField[Int]("location"),
      getField[Int]("user"),
      getField[Int]("visited_at"),
      getField[Int]("mark")
    )
  }

  private def getField[T: JsonReader](name: String)(implicit fields: Map[String, JsValue]) =
    fields.get(name) match {
      case Some(JsNull) => deserializationError("null is forbidden")
      case Some(x) => Some(x.convertTo[T])
      case None => None
    }
}
