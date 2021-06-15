package services

import models.Product
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider,  val categoryRepo: CategoryRepository, val brandRepo: BrandRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ProductTable(tag: Tag) extends Table[Product](tag, "product") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def categoryId = column[Long]("category_id")
    def brandId = column[Long]("brand_id")
    def name = column[String]("name")
    def amount = column[String]("amount")
    def unitPrice = column[String]("unit_price")
    def description = column[String]("description")
    def categoryKey = foreignKey("category_key", categoryId, cat)(_.id)
    def brandKey = foreignKey("brand_key", brandId, brand)(_.id)
    def * = (id, categoryId, brandId, name, amount, unitPrice, description) <> ((Product.apply _).tupled, Product.unapply)
  }

  import categoryRepo.CategoryTable
  import brandRepo.BrandTable

  val product = TableQuery[ProductTable]
  val cat = TableQuery[CategoryTable]
  val brand = TableQuery[BrandTable]

  def create(categoryId:Long, brandId:Long, name:String, amount:String, unitPrice:String, description:String): Future[Product] = db.run {
    (product.map(p => (p.categoryId, p.brandId, p.name, p.amount, p.unitPrice, p.description))
      returning product.map(_.id)
      into { case ((categoryId, brandId, name, amount, unitPrice, description), id) => Product(id,categoryId, brandId, name, amount, unitPrice, description) }
      ) += (categoryId, brandId, name, amount, unitPrice, description)
  }

  def getById(id: Long): Future[Option[Product]] = db.run {
    product.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Product]] = db.run {
    product.result
  }

  def update(id:Long, c: Product): Future[Int] = {
    val updatedCart: Product = c.copy(id)
    db.run(product.filter(_.id === id).update(updatedCart))
  }

  def delete(id:Long): Future[Int] = db.run(product.filter(_.id===id).delete)
}


