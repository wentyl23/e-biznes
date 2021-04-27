package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CartController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addCart(): Action[AnyContent] = Action.async {
    implicit request =>
      println("addCart:",request.body)
      Future {
        Ok("")
      }
  }

  def getCarts: Action[AnyContent] = Action.async {
    implicit request =>
      println("getCarts")
      Future {
        Ok("")
      }
  }

  def getCart(id: Long): Action[AnyContent] = Action.async {
    println("getCart:",id)
    Future{
      Ok("")
    }
  }

  def deleteCart(id:Long): Action[AnyContent] = Action.async{
    println("deleteCart:",id)
    Future{
      Ok("")
    }
  }

  def updateCart(): Action[AnyContent] = Action.async {
    implicit request =>
      println("updateCart:",request.body)
      Future {
        Ok("")
      }
  }

}


