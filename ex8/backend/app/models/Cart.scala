package models

import play.api.libs.json._

case class Cart(id: Long=0, orderId: Long, productId: Long, amount: Int)

object Cart {
  implicit val cartFormat: OFormat[Cart] = Json.using[Json.WithDefaultValues].format[Cart]
}
