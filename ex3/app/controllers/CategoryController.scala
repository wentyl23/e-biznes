package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CategoryController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addCategory(): Action[AnyContent] = Action.async {
    implicit request =>
      println("addCategory:",request.body)
      Future {
        Ok("")
      }
  }

  def getCategories: Action[AnyContent] = Action.async {
    implicit request =>
      println("getCategories")
      Future {
        Ok("")
      }
  }

  def getCategory(id: Long): Action[AnyContent] = Action.async {
    println("getCategory:",id)
    Future{
      Ok("")
    }
  }

  def deleteCategory(id:Long): Action[AnyContent] = Action.async{
    println("deleteCategory:",id)
    Future{
      Ok("")
    }
  }

  def updateCategory(): Action[AnyContent] = Action.async {
    implicit request =>
      println("updateCategory:",request.body)
      Future {
        Ok("")
      }
  }

}

