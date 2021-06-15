package controllers.forms

import models.Category
import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.mvc._
import services.CategoryRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FormCategoryController @Inject()(categoryRepo: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/category/list"

  def getCategories: Action[AnyContent] = Action.async {
    implicit request =>
      categoryRepo.list().map(categories => Ok(views.html.forms.get_categories(categories)))
  }

  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateCategoryForm: Form[UpdateCategoryForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }

  def addCategory(): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepo.list()
    categories.map(_ => Ok(views.html.forms.add_category(categoryForm)))
  }

  def createCategoryHandle(): Action[AnyContent] = Action.async {
    implicit request =>
    categoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.forms.add_category(errorForm))
        )
      },
      category => {
        categoryRepo.create(category.name, category.description).map { _ =>
          Redirect(url).flashing("success" -> "product.created")
        }
      }
    )
  }

  def updateCategory(id: Long): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
    val category = categoryRepo.getById(id)
    category.map(
      category => {
      val catForm = updateCategoryForm.fill(UpdateCategoryForm(category.get.id, category.get.name, category.get.description))
      Ok(views.html.forms.update_category(catForm))
    })
  }

  def updateCategoryHandle(): Action[AnyContent] = Action.async {
    implicit request =>
    updateCategoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.forms.update_category(errorForm))
        )
      },
      category => {
        categoryRepo.update(category.id, Category(category.id, category.name, category.description)).map { _ =>
          Redirect(url).flashing("success" -> "product.created")
        }
      }
    )
  }

  def deleteCategory(id: Long): Action[AnyContent] = Action {
    categoryRepo.delete(id)
    Redirect(url)
  }
}


case class CreateCategoryForm(name: String, description: String)

case class UpdateCategoryForm(id: Long, name: String, description: String)