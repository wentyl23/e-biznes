package controllers

import models.Region
import play.api.libs.json.{JsValue, Json}

import javax.inject._
import play.api.mvc._
import services.RegionRepository

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class RegionController @Inject()(val regionRepository: RegionRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addRegion(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Region].map {
        region =>
          regionRepository.create(region.userId, region.address, region.city, region.zip, region.state, region.country).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

  def getRegions: Action[AnyContent] = Action.async {
    val regions = regionRepository.list()
    regions.map {
      regions => Ok(Json.toJson(regions))
    }
  }

  def getRegion(id: Long): Action[AnyContent] = Action.async {
    println("getRegion:",id)
    val region = regionRepository.getById(id)
    region.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("There is no region with that id")
    }
  }

  def deleteRegion(id:Long): Action[AnyContent] = Action.async{
    println("deleteRegion:",id)
    regionRepository.delete(id).map {
      res=>
        Ok(Json.toJson(res))
    }
  }

  def updateRegion(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      println("updateRegion:",request.body)
      request.body.validate[Region].map {
        region =>
          regionRepository.update(region.id, region).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

}
