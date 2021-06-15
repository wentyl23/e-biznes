package controllers.forms

import models.{Product, Review, User}
import play.api.data.Form
import play.api.data.Forms._

import javax.inject._
import play.api.mvc._
import services.{ProductRepository, ReviewRepository, UserRepository}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class FormReviewController @Inject()(reviewRepository: ReviewRepository, productRepository: ProductRepository, userRepository: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/review/list"
  var productList: Seq[Product] = Seq[Product]()
  var userList: Seq[User] = Seq[User]()
  fetchLists()

  def fetchLists(): Unit ={
    productRepository.list().onComplete {
      case Success(products) => productList = products
      case Failure(err) => print("error: fetching products failed", err)
    }

    userRepository.getAll.onComplete {
      case Success(users) => userList = users
      case Failure(err) => print("error: fetching users failed", err)
    }
  }

  def getReviews: Action[AnyContent] = Action.async {
    implicit request =>
      reviewRepository.list().map(reviews => Ok(views.html.forms.get_reviews(reviews)))
  }

  val reviewForm: Form[CreateReviewForm] = Form {
    mapping(
      "productId" -> longNumber,
      "userId" -> longNumber,
      "rating" -> nonEmptyText,
      "description" -> nonEmptyText
    )(CreateReviewForm.apply)(CreateReviewForm.unapply)
  }

  val updateReviewForm: Form[UpdateReviewForm] = Form {
    mapping(
      "id" -> longNumber,
      "productId" -> longNumber,
      "userId" -> longNumber,
      "rating" -> nonEmptyText,
      "description" -> nonEmptyText
    )(UpdateReviewForm.apply)(UpdateReviewForm.unapply)
  }

  def addReview(): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val products = Await.result(productRepository.list(), 1.second)
      val users = userRepository.getAll
      users.map(user => Ok(views.html.forms.add_review(reviewForm, products, user)))
  }

  def createReviewHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      reviewForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.add_review(errorForm, productList, userList))
          )
        },
        review => {
          reviewRepository.create(review.productId, review.userId, review.rating, review.description).map { _ =>
            Redirect(url).flashing("success" -> "review.created")
          }
        }
      )
  }

  def updateReview(id: Long): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val review = reviewRepository.getById(id)
      review.map(
        review => {
          val reviewForm = updateReviewForm.fill(UpdateReviewForm(review.get.id, review.get.productId, review.get.userId, review.get.rating, review.get.description))
          Ok(views.html.forms.update_review(reviewForm, productList, userList))
        })
  }

  def updateReviewHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      updateReviewForm.bindFromRequest.fold(
        errorForm => {

          Future.successful(
            BadRequest(views.html.forms.update_review(errorForm, productList, userList))
          )
        },
        review => {
          reviewRepository.update(review.id, Review(review.id, review.productId, review.userId, review.rating, review.description)).map { _ =>
            Redirect(url).flashing("success" -> "review.created")
          }
        }
      )
  }

  def deleteReview(id: Long): Action[AnyContent] = Action {
    reviewRepository.delete(id)
    Redirect(url)
  }
}


case class CreateReviewForm(productId: Long, userId: Long, rating: String, description: String)

case class UpdateReviewForm(id: Long, productId: Long, userId: Long, rating: String, description: String)



