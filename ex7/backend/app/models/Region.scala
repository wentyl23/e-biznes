package models

import play.api.libs.json._

case class Region(id: Long=0, userId: Long, address: String, city: String, zip: String, state: String, country: String)

object Region {
  implicit val regionFormat: OFormat[Region] = Json.using[Json.WithDefaultValues].format[Region]
}