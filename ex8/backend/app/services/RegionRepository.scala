package services

import models.Region
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RegionRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepo: UserRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class RegionTable(tag: Tag) extends Table[Region](tag, "region") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Long]("user_id")
    def address = column[String]("address")
    def city = column[String]("city")
    def zip = column[String]("zip")
    def state = column[String]("state")
    def country = column[String]("country")
    def userKey = foreignKey("user_key", userId, app_user)(_.id)
    def * = (id, userId, address, city, zip, state, country) <> ((Region.apply _).tupled, Region.unapply)
  }

  import userRepo.UserTable
  val app_user = TableQuery[UserTable]
  val region = TableQuery[RegionTable]

  def create(userId:Long, address: String, city: String, zip: String, state:String, country:String): Future[Region] = db.run {
    (region.map(r => (r.userId, r.address, r.city, r.zip, r.state, r.country))
      returning region.map(_.id)
      into { case ((userId, address, city, zip, state, country), id) => Region(id, userId, address, city, zip, state, country) }
      ) += (userId, address, city, zip, state, country)
  }

  def getById(id: Long): Future[Option[Region]] = db.run {
    region.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Region]] = db.run {
    region.result
  }

  def update(id:Long, r: Region): Future[Int] = {
    val updatedRegion: Region = r.copy(id)
    db.run(region.filter(_.id === id).update(updatedRegion))
  }

  def delete(id:Long): Future[Int] = db.run(region.filter(_.id===id).delete)
}

