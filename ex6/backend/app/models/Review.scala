package models

import play.api.libs.json._

case class Review(id: Long=0, userId: Long, productId: Long, rating: String, description: String)

object Review {
  implicit val reviewFormat: OFormat[Review] = Json.using[Json.WithDefaultValues].format[Review]
}
