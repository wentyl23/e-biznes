package models

import play.api.libs.json._

case class Product(id: Long=0, categoryId: Long, brandId: Long, name: String, amount: String, unitPrice: String, description: String)

object Product {
  implicit val productFormat: OFormat[Product] = Json.using[Json.WithDefaultValues].format[Product]
}
