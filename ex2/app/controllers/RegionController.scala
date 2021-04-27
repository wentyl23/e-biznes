package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class RegionController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addRegion(): Action[AnyContent] = Action.async {
    implicit request =>
      println("addRegion:",request.body)
      Future {
        Ok("")
      }
  }

  def getRegions: Action[AnyContent] = Action.async {
    implicit request =>
      println("getRegions")
      Future {
        Ok("")
      }
  }

  def getRegion(id: Long): Action[AnyContent] = Action.async {
    println("getRegion:",id)
    Future{
      Ok("")
    }
  }

  def deleteRegion(id:Long): Action[AnyContent] = Action.async{
    println("deleteRegion:",id)
    Future{
      Ok("")
    }
  }

  def updateRegion(): Action[AnyContent] = Action.async {
    implicit request =>
      println("updateRegion:",request.body)
      Future {
        Ok("")
      }
  }

}
