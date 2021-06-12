package controllers

import models.Brand
import javax.inject._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import scala.concurrent.{ExecutionContext, Future}
import services.BrandRepository

@Singleton
class BrandController @Inject()(val brandRepository: BrandRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addBrand(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Brand].map {
        brand =>
          brandRepository.create(brand.name, brand.founded, brand.description).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

  def getBrands: Action[AnyContent] = Action.async {
      val brands = brandRepository.list()
      brands.map {
        brands => Ok(Json.toJson(brands))
      }
  }

  def getBrand(id: Long): Action[AnyContent] = Action.async {
    println("getBrand:",id)
    val brand = brandRepository.getById(id)
    brand.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("There is no brand with that id")
    }
  }

  def deleteBrand(id:Long): Action[AnyContent] = Action.async{
    println("deleteBrand:",id)
    brandRepository.delete(id).map {
      res=>
        Ok(Json.toJson(res))
    }
  }

  def updateBrand(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      println("updateBrand:",request.body)
      request.body.validate[Brand].map {
        brand =>
          brandRepository.update(brand.id, brand).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

}
