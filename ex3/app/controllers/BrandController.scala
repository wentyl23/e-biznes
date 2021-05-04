package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class BrandController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addBrand(): Action[AnyContent] = Action.async {
    implicit request =>
      println("addBrand:",request.body)
      Future {
        Ok("")
      }
  }

  def getBrands: Action[AnyContent] = Action.async {
    implicit request =>
      println("getBrands")
      Future {
        Ok("")
      }
  }

  def getBrand(id: Long): Action[AnyContent] = Action.async {
    println("getBrand:",id)
    Future{
      Ok("")
    }
  }

  def deleteBrand(id:Long): Action[AnyContent] = Action.async{
    println("deleteBrand:",id)
    Future{
      Ok("")
    }
  }

  def updateBrand(): Action[AnyContent] = Action.async {
    implicit request =>
      println("updateBrand:",request.body)
      Future {
        Ok("")
      }
  }

}
