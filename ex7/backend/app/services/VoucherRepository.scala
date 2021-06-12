package services

import models.Voucher
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VoucherRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class VoucherTable(tag: Tag) extends Table[Voucher](tag, "voucher") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def code = column[String]("code")
    def value = column[String]("value")
    def * = (id, code, value) <> ((Voucher.apply _).tupled, Voucher.unapply)
  }


  val voucher = TableQuery[VoucherTable]

  def create(code: String, value: String): Future[Voucher] = db.run {
    (voucher.map(v => (v.code, v.value))
      returning voucher.map(_.id)
      into { case ((code, value), id) => Voucher(id, code, value) }
      ) += (code, value)
  }

  def getById(id: Long): Future[Option[Voucher]] = db.run {
    voucher.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Voucher]] = db.run {
    voucher.result
  }

  def update(id:Long, v: Voucher): Future[Int] = {
    val updatedVoucher: Voucher = v.copy(id)
    db.run(voucher.filter(_.id === id).update(updatedVoucher))
  }

  def delete(id:Long): Future[Int] = db.run(voucher.filter(_.id===id).delete)
}

