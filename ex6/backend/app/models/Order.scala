package models

import play.api.libs.json._

case class Order(id: Long=0, userId:Long , paymentId: Long, voucherId: Long)

object Order {
  implicit val orderFormat: OFormat[Order] = Json.using[Json.WithDefaultValues].format[Order]
}
