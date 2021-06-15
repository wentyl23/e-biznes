package services

import models.Category

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "category") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def description = column[String]("description")
    def * = (id, name, description) <> ((Category.apply _).tupled, Category.unapply)
  }

  val category = TableQuery[CategoryTable]

  def create(name: String, description: String): Future[Category] = db.run {
    (category.map(cat => (cat.name, cat.description))
      returning category.map(_.id)
      into { case ((name, description), id) => Category(id, name, description) }
      ) += (name, description)
  }

  def getById(id: Long): Future[Option[Category]] = db.run {
    category.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Category]] = db.run {
    category.result
  }

  def update(id:Long, cat: Category): Future[Int] = {
    val updatedCategory: Category = cat.copy(id)
    db.run(category.filter(_.id === id).update(updatedCategory))
  }

  def delete(id:Long): Future[Int] = db.run(category.filter(_.id===id).delete)
}

