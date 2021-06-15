package services

import models.Payment
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class PaymentTable(tag: Tag) extends Table[Payment](tag, "payment") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def value = column[Int]("value")
    def nameOnCard = column[String]("name_on_card")
    def cardNumber = column[String]("card_number")
    def cvCode = column[String]("cv_code")
    def expDate = column[String]("exp_date")
    def * = (id, value,nameOnCard,cardNumber, cvCode,expDate) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  val payment = TableQuery[PaymentTable]

  def create(value: Int, nameOnCard: String, cardNumber: String, cvCode: String, expDate: String): Future[Payment] = db.run {
    (payment.map(p => (p.value, p.nameOnCard, p.cardNumber, p.cvCode, p.expDate))
      returning payment.map(_.id)
      into { case ((value, nameOnCard, cardNumber, cvCode, expDate), id) => Payment(id, value, nameOnCard, cardNumber, cvCode, expDate) }
      ) += (value, nameOnCard, cardNumber, cvCode, expDate)
  }

  def getById(id: Long): Future[Option[Payment]] = db.run {
    payment.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }

  def update(id:Long, c: Payment): Future[Int] = {
    val updatedPayment: Payment = c.copy(id)
    db.run(payment.filter(_.id === id).update(updatedPayment))
  }

  def delete(id:Long): Future[Int] = db.run(payment.filter(_.id===id).delete)
}


