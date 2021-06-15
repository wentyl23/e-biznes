package services

import models.Cart
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val orderRepo: OrderRepository, val productRepo: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CartTable(tag: Tag) extends Table[Cart](tag, "cart") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def orderId = column[Long]("order_id")
    def productId = column[Long]("product_id")
    def amount = column[Int]("amount")
    def orderKey = foreignKey("order_key", orderId, shop_order)(_.id)
    def productKey = foreignKey("product_key", productId, product)(_.id)
    def * = (id, orderId, productId, amount) <> ((Cart.apply _).tupled, Cart.unapply)
  }

  import orderRepo.OrderTable
  import productRepo.ProductTable

  val cart = TableQuery[CartTable]
  val shop_order = TableQuery[OrderTable]
  val product = TableQuery[ProductTable]

  def create(orderId:Long, productId: Long, amount: Int): Future[Cart] = db.run {
    (cart.map(c => (c.orderId, c.productId, c.amount))
      returning cart.map(_.id)
      into { case ((orderId, productId, amount), id) => Cart(id, orderId, productId, amount) }
      ) += (orderId, productId, amount)
  }

  def getById(id: Long): Future[Option[Cart]] = db.run {
    cart.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Cart]] = db.run {
    cart.result
  }

  def update(id:Long, c: Cart): Future[Int] = {
    val updatedCart: Cart = c.copy(id)
    db.run(cart.filter(_.id === id).update(updatedCart))
  }

  def delete(id:Long): Future[Int] = db.run(cart.filter(_.id===id).delete)
}

