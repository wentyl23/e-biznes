package controllers.forms

import models.{Brand, Category, Product}
import play.api.data.Form
import play.api.data.Forms._

import javax.inject._
import play.api.mvc._
import services.{BrandRepository, CategoryRepository, ProductRepository}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class FormProductController @Inject()(productRepository: ProductRepository,brandRepository: BrandRepository, catRepository: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/product/list"
  var brandList: Seq[Brand] = Seq[Brand]()
  var categoryList: Seq[Category] = Seq[Category]()
  fetchLists()

  def fetchLists(): Unit ={
    brandRepository.list().onComplete {
      case Success(brands) => brandList = brands
      case Failure(err) => print("error: fetching brands failed", err)
    }

    catRepository.list().onComplete {
      case Success(category) => categoryList = category
      case Failure(err) => print("error: fetching categories failed", err)
    }
  }

  def getProducts: Action[AnyContent] = Action.async {
    implicit request =>
      productRepository.list().map(products => Ok(views.html.forms.get_products(products)))
  }

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "categoryId" -> longNumber,
      "brandId" -> longNumber,
      "name" -> nonEmptyText,
      "amount" -> nonEmptyText,
      "unitPrice" -> nonEmptyText,
      "description" -> nonEmptyText
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val updateProductForm: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> longNumber,
      "categoryId" -> longNumber,
      "brandId" -> longNumber,
      "name" -> nonEmptyText,
      "amount" -> nonEmptyText,
      "unitPrice" -> nonEmptyText,
      "description" -> nonEmptyText
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }

  def addProduct(): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val categories = Await.result(catRepository.list(), 1.second)
      val brands = brandRepository.list()
      brands.map(brand => Ok(views.html.forms.add_product(productForm, categories, brand)))
  }

  def createProductHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      productForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.add_product(errorForm, categoryList, brandList))
          )
        },
        product => {
          productRepository.create(product.categoryId, product.brandId, product.name, product.amount, product.unitPrice, product.description).map { _ =>
            Redirect(url).flashing("success" -> "product.created")
          }
        }
      )
  }

  def updateProduct(id: Long): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val product = productRepository.getById(id)
      product.map(
        product => {
          val productForm = updateProductForm.fill(UpdateProductForm(product.get.id, product.get.categoryId, product.get.brandId, product.get.name, product.get.amount, product.get.unitPrice, product.get.description))
          Ok(views.html.forms.update_product(productForm, categoryList, brandList))
        })
  }

  def updateProductHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      updateProductForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.update_product(errorForm, categoryList, brandList))
          )
        },
        product => {
          productRepository.update(product.id, Product(product.id, product.categoryId, product.brandId, product.name, product.amount, product.unitPrice, product.description)).map { _ =>
            Redirect(url).flashing("success" -> "product.created")
          }
        }
      )
  }

  def deleteProduct(id: Long): Action[AnyContent] = Action {
    productRepository.delete(id)
    Redirect(url)
  }
}


case class CreateProductForm(categoryId: Long, brandId: Long, name: String, amount: String, unitPrice: String, description: String)

case class UpdateProductForm(id: Long, categoryId: Long, brandId: Long, name: String, amount: String, unitPrice: String, description: String)



