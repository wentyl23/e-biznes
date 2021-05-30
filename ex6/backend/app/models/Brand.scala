package models

import play.api.libs.json._

case class Brand(id: Long=0, name: String,founded: Int, description:String)

object Brand {
  implicit val brandFormat: OFormat[Brand] = Json.using[Json.WithDefaultValues].format[Brand]
}