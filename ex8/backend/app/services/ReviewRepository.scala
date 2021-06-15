package services

import models.Review
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReviewRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepo: UserRepository, val productRepo: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ReviewTable(tag: Tag) extends Table[Review](tag, "review") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def productId = column[Long]("product_id")
    def userId = column[Long]("user_id")
    def rating = column[String]("rating")
    def description = column[String]("description")
    def userKey = foreignKey("user_key", userId, app_user)(_.id)
    def productKey = foreignKey("product_key", productId, product)(_.id)
    def * = (id,userId, productId,rating,description) <> ((Review.apply _).tupled, Review.unapply)
  }

  import userRepo.UserTable
  import productRepo.ProductTable

  val review = TableQuery[ReviewTable]
  val app_user = TableQuery[UserTable]
  val product = TableQuery[ProductTable]

  def create(userId:Long, productId:Long,rating:String,description:String): Future[Review] = db.run {
    (review.map(r => (r.userId, r.productId,r.rating,r.description))
      returning review.map(_.id)
      into { case ((userId, productId,rating,description), id) => Review(id, userId, productId,rating,description) }
      ) += (userId, productId,rating,description)
  }

  def getById(id: Long): Future[Option[Review]] = db.run {
    review.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Review]] = db.run {
    review.result
  }

  def update(id:Long, r: Review): Future[Int] = {
    val updatedReview: Review = r.copy(id)
    db.run(review.filter(_.id === id).update(updatedReview))
  }

  def delete(id:Long): Future[Int] = db.run(review.filter(_.id===id).delete)
}


