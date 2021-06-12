package controllers

import play.api.libs.json.{JsValue, Json}

import models.Product
import javax.inject._
import play.api.mvc._
import services.ProductRepository

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ProductController @Inject()(val productRepository: ProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addProduct(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Product].map {
        product =>
          productRepository.create(product.categoryId, product.brandId, product.name, product.amount, product.unitPrice, product.description).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

  def getProducts: Action[AnyContent] = Action.async {
    val products = productRepository.list()
    products.map {
      products => Ok(Json.toJson(products))
    }
  }

  def getProduct(id: Long): Action[AnyContent] = Action.async {
    println("getProduct:",id)
    val product = productRepository.getById(id)
    product.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("There is no product with that id")
    }
  }

  def deleteProduct(id:Long): Action[AnyContent] = Action.async{
    println("deleteProduct:",id)
    productRepository.delete(id).map {
      res=>
        Ok(Json.toJson(res))
    }
  }

  def updateProduct(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      println("updateProduct:",request.body)
      request.body.validate[Product].map {
        product =>
          productRepository.update(product.id, product).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

}

