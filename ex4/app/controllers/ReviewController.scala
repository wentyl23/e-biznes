package controllers

import models.Review
import play.api.libs.json.{JsValue, Json}

import javax.inject._
import play.api.mvc._
import services.ReviewRepository

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ReviewController @Inject()(val reviewRepository: ReviewRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addReview(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Review].map {
        review =>
          reviewRepository.create(review.userId, review.productId, review.rating, review.description).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

  def getReviews: Action[AnyContent] = Action.async {
    val reviews = reviewRepository.list()
    reviews.map {
      reviews => Ok(Json.toJson(reviews))
    }
  }

  def getReview(id: Long): Action[AnyContent] = Action.async {
    println("getReview:",id)
    val review = reviewRepository.getById(id)
    review.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("There is no review with that id")
    }
  }

  def deleteReview(id:Long): Action[AnyContent] = Action.async{
    println("deleteReview:",id)
    reviewRepository.delete(id).map {
      res=>
        Ok(Json.toJson(res))
    }
  }

  def updateReview(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      println("updateReview:",request.body)
      request.body.validate[Review].map {
        review =>
          reviewRepository.update(review.id, review).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

}


