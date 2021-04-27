package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class VoucherController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addVoucher(): Action[AnyContent] = Action.async {
    implicit request =>
      println("addVoucher:",request.body)
      Future {
        Ok("")
      }
  }

  def getVouchers: Action[AnyContent] = Action.async {
    implicit request =>
      println("getVouchers")
      Future {
        Ok("")
      }
  }

  def getVoucher(id: Long): Action[AnyContent] = Action.async {
    println("getVoucher:",id)
    Future{
      Ok("")
    }
  }

  def deleteVoucher(id:Long): Action[AnyContent] = Action.async{
    println("deleteVoucher:",id)
    Future{
      Ok("")
    }
  }

  def updateVoucher(): Action[AnyContent] = Action.async {
    implicit request =>
      println("updateVoucher:",request.body)
      Future {
        Ok("")
      }
  }

}

