package models

import play.api.libs.json._

case class Payment(id: Long=0, value: Int, nameOnCart: String, cardNumber: String, cvCode: String, expDate: String)

object Payment {
  implicit val paymentFormat: OFormat[Payment] = Json.using[Json.WithDefaultValues].format[Payment]
}
