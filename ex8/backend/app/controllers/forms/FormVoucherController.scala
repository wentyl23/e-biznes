package controllers.forms

import models.Voucher
import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.mvc._
import services.VoucherRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FormVoucherController @Inject()(voucherRepo: VoucherRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/voucher/list";

  def getVouchers: Action[AnyContent] = Action.async {
    implicit request =>
      voucherRepo.list().map(vouchers => Ok(views.html.forms.get_vouchers(vouchers)))
  }

  val voucherForm: Form[CreateVoucherForm] = Form {
    mapping(
      "code" -> nonEmptyText,
      "value" -> nonEmptyText,
    )(CreateVoucherForm.apply)(CreateVoucherForm.unapply)
  }

  val updateVoucherForm: Form[UpdateVoucherForm] = Form {
    mapping(
      "id" -> longNumber,
      "code" -> nonEmptyText,
      "value" -> nonEmptyText,
    )(UpdateVoucherForm.apply)(UpdateVoucherForm.unapply)
  }

  def addVoucher(): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val vouchers = voucherRepo.list()
      vouchers.map(_ => Ok(views.html.forms.add_voucher(voucherForm)))
  }

  def createVoucherHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      voucherForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.add_voucher(errorForm))
          )
        },
        voucher => {
          voucherRepo.create(voucher.code, voucher.value).map { _ =>
            Redirect(url).flashing("success" -> "product.created")
          }
        }
      )
  }

  def updateVoucher(id: Long): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val voucher = voucherRepo.getById(id)
      voucher.map(
        voucher => {
          val catForm = updateVoucherForm.fill(UpdateVoucherForm(voucher.get.id, voucher.get.code,voucher.get.value))
          Ok(views.html.forms.update_voucher(catForm))
        })
  }

  def updateVoucherHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      updateVoucherForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.update_voucher(errorForm))
          )
        },
        voucher => {
          voucherRepo.update(voucher.id, Voucher(voucher.id, voucher.code, voucher.value)).map { _ =>
            Redirect(url).flashing("success" -> "product.created")
          }
        }
      )
  }

  def deleteVoucher(id: Long): Action[AnyContent] = Action {
    voucherRepo.delete(id)
    Redirect(url)
  }
}


case class CreateVoucherForm(code: String, value: String)

case class UpdateVoucherForm(id: Long, code: String, value: String)

