package models

import play.api.libs.json._

case class Category(id: Long=0, name: String, description: String)

object Category {
  implicit val categoryFormat: OFormat[Category] = Json.using[Json.WithDefaultValues].format[Category]
}
