package controllers

import models.Payment
import play.api.libs.json.{JsValue, Json}

import javax.inject._
import play.api.mvc._
import services.PaymentRepository

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class PaymentController @Inject()(val paymentRepository: PaymentRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addPayment(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Payment].map {
        payment =>
          paymentRepository.create(payment.value, payment.nameOnCart, payment.cardNumber, payment.cvCode, payment.expDate).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

  def getPayments: Action[AnyContent] = Action.async {
    val payments = paymentRepository.list()
    payments.map {
      payments => Ok(Json.toJson(payments))
    }
  }

  def getPayment(id: Long): Action[AnyContent] = Action.async {
    println("getPayment:",id)
    val payment = paymentRepository.getById(id)
    payment.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("There is no payment with that id")
    }
  }

  def deletePayment(id:Long): Action[AnyContent] = Action.async{
    println("deletePayment:",id)
    paymentRepository.delete(id).map {
      res=>
        Ok(Json.toJson(res))
    }
  }

  def updatePayment(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      println("updatePayment:",request.body)
      request.body.validate[Payment].map {
        payment =>
          paymentRepository.update(payment.id, payment).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

}


