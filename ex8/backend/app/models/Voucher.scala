package models

import play.api.libs.json._

case class Voucher(id: Long=0, code: String, value: String)

object Voucher {
  implicit val voucherFormat: OFormat[Voucher] = Json.using[Json.WithDefaultValues].format[Voucher]
}

