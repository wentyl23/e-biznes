package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OrderController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addOrder(): Action[AnyContent] = Action.async {
    implicit request =>
      println("addOrder:",request.body)
      Future {
        Ok("")
      }
  }

  def getOrders: Action[AnyContent] = Action.async {
    implicit request =>
      println("getOrders")
      Future {
        Ok("")
      }
  }

  def getOrder(id: Long): Action[AnyContent] = Action.async {
    println("getOrder:",id)
    Future{
      Ok("")
    }
  }

  def deleteOrder(id:Long): Action[AnyContent] = Action.async{
    println("deleteOrder:",id)
    Future{
      Ok("")
    }
  }

  def updateOrder(): Action[AnyContent] = Action.async {
    implicit request =>
      println("updateOrder:",request.body)
      Future {
        Ok("")
      }
  }

}

