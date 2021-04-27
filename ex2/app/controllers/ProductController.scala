package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ProductController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addProduct(): Action[AnyContent] = Action.async {
    implicit request =>
      println("addProduct:",request.body)
      Future {
        Ok("")
      }
  }

  def getProducts: Action[AnyContent] = Action.async {
    implicit request =>
      println("getProducts")
      Future {
        Ok("")
      }
  }

  def getProduct(id: Long): Action[AnyContent] = Action.async {
    println("getProduct:",id)
    Future{
      Ok("")
    }
  }

  def deleteProduct(id:Long): Action[AnyContent] = Action.async{
    println("deleteProduct:",id)
    Future{
      Ok("")
    }
  }

  def updateProduct(): Action[AnyContent] = Action.async {
    implicit request =>
      println("updateProduct:",request.body)
      Future {
        Ok("")
      }
  }

}

