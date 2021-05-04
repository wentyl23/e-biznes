package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ReviewController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addReview(): Action[AnyContent] = Action.async {
    implicit request =>
      println("addReview:",request.body)
      Future {
        Ok("")
      }
  }

  def getReviews: Action[AnyContent] = Action.async {
    implicit request =>
      println("getReviews")
      Future {
        Ok("")
      }
  }

  def getReview(id: Long): Action[AnyContent] = Action.async {
    println("getReview:",id)
    Future{
      Ok("")
    }
  }

  def deleteReview(id:Long): Action[AnyContent] = Action.async{
    println("deleteReview:",id)
    Future{
      Ok("")
    }
  }

  def updateReview(): Action[AnyContent] = Action.async {
    implicit request =>
      println("updateReview:",request.body)
      Future {
        Ok("")
      }
  }

}


