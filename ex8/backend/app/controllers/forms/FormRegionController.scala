package controllers.forms

import models.{Region, User}
import play.api.data.Form
import play.api.data.Forms._

import javax.inject._
import play.api.mvc._
import services.{RegionRepository, UserRepository}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class FormRegionController @Inject()(regionRepository: RegionRepository, userRepository: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/region/list"
  var userList: Seq[User] = Seq[User]()
  fetchLists()

  def fetchLists(): Unit ={
    userRepository.getAll.onComplete {
      case Success(users) => userList = users
      case Failure(err) => print("error: fetching users failed", err)
    }
  }

  def getRegions: Action[AnyContent] = Action.async {
    implicit request =>
      regionRepository.list().map(regions => Ok(views.html.forms.get_regions(regions)))
  }

  val regionForm: Form[CreateRegionForm] = Form {
    mapping(
      "userId" -> longNumber,
      "address" -> nonEmptyText,
      "city" -> nonEmptyText,
      "zip" -> nonEmptyText,
      "state" -> nonEmptyText,
      "country" -> nonEmptyText
    )(CreateRegionForm.apply)(CreateRegionForm.unapply)
  }

  val updateRegionForm: Form[UpdateRegionForm] = Form {
    mapping(
      "id" -> longNumber,
      "userId" -> longNumber,
      "address" -> nonEmptyText,
      "city" -> nonEmptyText,
      "zip" -> nonEmptyText,
      "state" -> nonEmptyText,
      "country" -> nonEmptyText
    )(UpdateRegionForm.apply)(UpdateRegionForm.unapply)
  }

  def addRegion(): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val users = userRepository.getAll
      users.map(user => Ok(views.html.forms.add_region(regionForm, user)))
  }

  def createRegionHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      regionForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.add_region(errorForm, userList))
          )
        },
        region => {
          regionRepository.create(region.userId, region.address, region.city, region.zip, region.state, region.country).map { _ =>
            Redirect(url).flashing("success" -> "region.created")
          }
        }
      )
  }

  def updateRegion(id: Long): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val region = regionRepository.getById(id)
      region.map(
        region => {
          val regionForm = updateRegionForm.fill(UpdateRegionForm(region.get.id,  region.get.userId, region.get.address, region.get.city, region.get.zip, region.get.state, region.get.country))
          Ok(views.html.forms.update_region(regionForm, userList))
        })
  }

  def updateRegionHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      updateRegionForm.bindFromRequest.fold(
        errorForm => {

          Future.successful(
            BadRequest(views.html.forms.update_region(errorForm, userList))
          )
        },
        region => {
          regionRepository.update(region.id, Region(region.id, region.userId, region.address, region.city, region.zip, region.state, region.country)).map { _ =>
            Redirect(url).flashing("success" -> "region.created")
          }
        }
      )
  }

  def deleteRegion(id: Long): Action[AnyContent] = Action {
    regionRepository.delete(id)
    Redirect(url)
  }
}


case class CreateRegionForm( userId: Long, address: String, city: String, zip: String, state: String, country: String)

case class UpdateRegionForm(id: Long, userId: Long, address: String, city: String, zip: String, state: String, country: String)




