package controllers.forms

import models.Payment
import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.mvc._
import services.PaymentRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FormPaymentController @Inject()(paymentRepository: PaymentRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/payment/list"

  def getPayments: Action[AnyContent] = Action.async {
    implicit request =>
      paymentRepository.list().map(payments => Ok(views.html.forms.get_payments(payments)))
  }

  val paymentForm: Form[CreatePaymentForm] = Form {
    mapping(
      "value" -> number,
      "nameOnCard" -> nonEmptyText,
      "cardNumber" -> nonEmptyText,
      "cvCode" -> nonEmptyText,
      "expDate" -> nonEmptyText
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updatePaymentForm: Form[UpdatePaymentForm] = Form {
    mapping(
      "id" -> longNumber,
      "value" -> number,
      "nameOnCard" -> nonEmptyText,
      "cardNumber" -> nonEmptyText,
      "cvCode" -> nonEmptyText,
      "expDate" -> nonEmptyText
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  def addPayment(): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val payments = paymentRepository.list()
      payments.map(_ => Ok(views.html.forms.add_payment(paymentForm)))
  }

  def createPaymentHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      paymentForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.add_payment(errorForm))
          )
        },
        payment => {
          paymentRepository.create(payment.value, payment.nameOnCard, payment.cardNumber, payment.cvCode, payment.expDate).map { _ =>
            Redirect(url).flashing("success" -> "product.created")
          }
        }
      )
  }

  def updatePayment(id: Long): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val payment = paymentRepository.getById(id)
      payment.map(
        payment => {
          val paymentForm = updatePaymentForm.fill(UpdatePaymentForm(payment.get.id, payment.get.value, payment.get.nameOnCart, payment.get.cardNumber, payment.get.cvCode, payment.get.expDate))
          Ok(views.html.forms.update_payment(paymentForm))
        })
  }

  def updatePaymentHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      updatePaymentForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.update_payment(errorForm))
          )
        },
        payment => {
          paymentRepository.update(payment.id, Payment(payment.id, payment.value, payment.nameOnCard, payment.cardNumber, payment.cvCode, payment.expDate)).map { _ =>
            Redirect(url).flashing("success" -> "product.created")
          }
        }
      )
  }

  def deletePayment(id: Long): Action[AnyContent] = Action {
    paymentRepository.delete(id)
    Redirect(url)
  }
}


case class CreatePaymentForm(value: Int, nameOnCard: String, cardNumber: String, cvCode: String, expDate: String)

case class UpdatePaymentForm(id: Long, value: Int, nameOnCard: String, cardNumber: String, cvCode: String, expDate: String)


