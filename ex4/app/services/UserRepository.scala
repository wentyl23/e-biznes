package services

import models.User
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "app_user") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("email")
    def login = column[String]("login")
    def password = column[String]("password")
    def * = (id, email, login, password) <> ((User.apply _).tupled, User.unapply)
  }

  val user = TableQuery[UserTable]

  def create(email:String, login: String, password: String): Future[User] = db.run {
    (user.map(u => (u.email, u.login, u.password))
      returning user.map(_.id)
      into { case ((orderId, productId, amount), id) => User(id, orderId, productId, amount) }
      ) += (email, login, password)
  }

  def getById(id: Long): Future[Option[User]] = db.run {
    user.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[User]] = db.run {
    user.result
  }

  def update(id:Long, u: User): Future[Int] = {
    val updatedUser: User = u.copy(id)
    db.run(user.filter(_.id === id).update(updatedUser))
  }

  def delete(id:Long): Future[Int] = db.run(user.filter(_.id===id).delete)
}

