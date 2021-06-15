package controllers

import models.Category
import play.api.libs.json.{JsValue, Json}

import javax.inject._
import play.api.mvc._
import services.CategoryRepository

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CategoryController @Inject()(val categoryRepository: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addCategory(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Category].map {
        cat =>
          System.out.println(cat.name)
          categoryRepository.create(cat.name, cat.description).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

  def getCategories: Action[AnyContent] = Action.async {
    val categories = categoryRepository.list()
    categories.map {
      categories => Ok(Json.toJson(categories))
    }
  }

  def getCategory(id: Long): Action[AnyContent] = Action.async {
    println("getBrand:",id)
    val cat = categoryRepository.getById(id)
    cat.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("There is no category with that id")
    }
  }

  def deleteCategory(id:Long): Action[AnyContent] = Action.async{
    println("deleteCategory:",id)
    categoryRepository.delete(id).map {
      res=>
        Ok(Json.toJson(res))
    }
  }

  def updateCategory(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      println("updateCat:",request.body)
      request.body.validate[Category].map {
        cat =>
          categoryRepository.update(cat.id, cat).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

}

