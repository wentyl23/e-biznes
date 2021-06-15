package controllers.forms

import models.Brand
import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.mvc._
import services.BrandRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FormBrandController @Inject()(brandRepo: BrandRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/brand/list"

  def getBrands: Action[AnyContent] = Action.async {
    implicit request =>
      brandRepo.list().map(brands => Ok(views.html.forms.get_brands(brands)))
  }

  val brandForm: Form[CreateBrandForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "founded" -> number,
      "description" -> nonEmptyText,
    )(CreateBrandForm.apply)(CreateBrandForm.unapply)
  }

  val updateBrandForm: Form[UpdateBrandForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "founded" -> number,
      "description" -> nonEmptyText,
    )(UpdateBrandForm.apply)(UpdateBrandForm.unapply)
  }

  def addBrand(): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val brands = brandRepo.list()
      brands.map(_ => Ok(views.html.forms.add_brand(brandForm)))
  }

  def createBrandHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      brandForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.add_brand(errorForm))
          )
        },
        brand => {
          brandRepo.create(brand.name, brand.founded, brand.description).map { _ =>
            Redirect(url).flashing("success" -> "product.created")
          }
        }
      )
  }

  def updateBrand(id: Long): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val brand = brandRepo.getById(id)
      brand.map(
        brand => {
          val catForm = updateBrandForm.fill(UpdateBrandForm(brand.get.id, brand.get.name,brand.get.founded, brand.get.description))
          Ok(views.html.forms.update_brand(catForm))
        })
  }

  def updateBrandHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      updateBrandForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.update_brand(errorForm))
          )
        },
        brand => {
          brandRepo.update(brand.id, Brand(brand.id, brand.name, brand.founded, brand.description)).map { _ =>
            Redirect(url).flashing("success" -> "product.created")
          }
        }
      )
  }

  def deleteBrand(id: Long): Action[AnyContent] = Action {
    brandRepo.delete(id)
    Redirect(url)
  }
}


case class CreateBrandForm(name: String, founded: Int, description: String)

case class UpdateBrandForm(id: Long, name: String, founded: Int, description: String)
