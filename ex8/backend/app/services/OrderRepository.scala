package services

import models.Order
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepo: UserRepository, val paymentRepo: PaymentRepository, val voucherRepo: VoucherRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OrderTable(tag: Tag) extends Table[Order](tag, "shop_order") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Long]("user_id")
    def paymentId = column[Long]("payment_id")
    def voucherId = column[Long]("voucher_id")
    def paymentKey = foreignKey("payment_key", paymentId, payment)(_.id)
    def userKey = foreignKey("user_key", userId, app_user)(_.id)
    def voucherKey = foreignKey("voucher_key", voucherId, voucher)(_.id)
    def * = (id, userId, paymentId, voucherId) <> ((Order.apply _).tupled, Order.unapply)
  }

  import paymentRepo.PaymentTable
  import userRepo.UserTable
  import voucherRepo.VoucherTable

  val voucher = TableQuery[VoucherTable]
  val app_user = TableQuery[UserTable]
  val payment = TableQuery[PaymentTable]
  val shop_order = TableQuery[OrderTable]

  def create(userId:Long, paymentId:Long, voucherId:Long): Future[Order] = db.run {
    (shop_order.map(o => (o.userId, o.paymentId, o.voucherId))
      returning shop_order.map(_.id)
      into { case ((userId, paymentId, voucherId), id) => Order(id, userId, paymentId, voucherId) }
      ) += (userId, paymentId, voucherId)
  }

  def getById(id: Long): Future[Option[Order]] = db.run {
    shop_order.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Order]] = db.run {
    shop_order.result
  }

  def update(id:Long, o: Order): Future[Int] = {
    val updatedOrder: Order = o.copy(id)
    db.run(shop_order.filter(_.id === id).update(updatedOrder))
  }

  def delete(id:Long): Future[Int] = db.run(shop_order.filter(_.id===id).delete)
}


