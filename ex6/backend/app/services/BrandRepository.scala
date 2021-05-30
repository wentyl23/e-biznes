package services

import models.Brand

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BrandRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class BrandTable(tag: Tag) extends Table[Brand](tag, "brand") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def founded = column[Int]("foundation_year")
    def description = column[String]("description")
    def * = (id, name,founded, description) <> ((Brand.apply _).tupled, Brand.unapply)
  }

  val brand = TableQuery[BrandTable]

  def create(name: String, founded: Int, description: String): Future[Brand] = db.run {
    (brand.map(b => (b.name,b.founded, b.description))
      returning brand.map(_.id)
      into { case ((name, founded, description), id) => Brand(id, name, founded, description) }
      ) += (name,founded, description)
  }

  def getById(id: Long): Future[Option[Brand]] = db.run {
    brand.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Brand]] = db.run {
    brand.result
  }

  def update(id:Long, b: Brand): Future[Int] = {
    val updatedBrand: Brand = b.copy(id)
    db.run(brand.filter(_.id === id).update(updatedBrand))
  }

  def delete(id:Long): Future[Int] = db.run(brand.filter(_.id===id).delete)
}
