package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class PaymentController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addPayment(): Action[AnyContent] = Action.async {
    implicit request =>
      println("addPayment:",request.body)
      Future {
        Ok("")
      }
  }

  def getPayments: Action[AnyContent] = Action.async {
    implicit request =>
      println("getPayments")
      Future {
        Ok("")
      }
  }

  def getPayment(id: Long): Action[AnyContent] = Action.async {
    println("getPayment:",id)
    Future{
      Ok("")
    }
  }

  def deletePayment(id:Long): Action[AnyContent] = Action.async{
    println("deletePayment:",id)
    Future{
      Ok("")
    }
  }

  def updatePayment(): Action[AnyContent] = Action.async {
    implicit request =>
      println("updatePayment:",request.body)
      Future {
        Ok("")
      }
  }

}


