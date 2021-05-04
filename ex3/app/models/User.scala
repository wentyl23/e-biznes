package models

import play.api.libs.json._

case class User(id: Long=0, email: String, login: String, password: String)

object User {
  implicit val userFormat: OFormat[User] = Json.format[User]
}
