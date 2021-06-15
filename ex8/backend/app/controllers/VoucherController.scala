package controllers

import models.Voucher
import javax.inject._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import scala.concurrent.{ExecutionContext, Future}
import services.VoucherRepository

@Singleton
class VoucherController @Inject()(val voucherRepository: VoucherRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addVoucher(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Voucher].map {
        voucher =>
          voucherRepository.create(voucher.code, voucher.value).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

  def getVouchers: Action[AnyContent] = Action.async {
    val vouchers = voucherRepository.list()
    vouchers.map {
      vouchers => Ok(Json.toJson(vouchers))
    }
  }

  def getVoucher(id: Long): Action[AnyContent] = Action.async {
    println("getVoucher:",id)
    val voucher = voucherRepository.getById(id)
    voucher.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("There is no voucher with that id")
    }
  }

  def deleteVoucher(id:Long): Action[AnyContent] = Action.async{
    println("deleteVoucher:",id)
    voucherRepository.delete(id).map {
      res=>
        Ok(Json.toJson(res))
    }
  }

  def updateVoucher(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Voucher].map {
        voucher =>
          voucherRepository.update(voucher.id, voucher).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

}

