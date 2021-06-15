package controllers

import models.Cart
import play.api.libs.json.{JsValue, Json}

import javax.inject._
import play.api.mvc._
import services.CartRepository

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CartController @Inject()(val cartRepository: CartRepository,cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addCart(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Cart].map {
        cart =>
          cartRepository.create(cart.orderId, cart.productId, cart.amount).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

  def getCarts: Action[AnyContent] = Action.async {
    val carts = cartRepository.list()
    carts.map {
      carts => Ok(Json.toJson(carts))
    }
  }

  def getCart(id: Long): Action[AnyContent] = Action.async {
    println("getCart:",id)
    val cart = cartRepository.getById(id)
    cart.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("There is no cart with that id")
    }
  }

  def deleteCart(id:Long): Action[AnyContent] = Action.async{
    println("deleteCart:",id)
    cartRepository.delete(id).map {
      res=>
        Ok(Json.toJson(res))
    }
  }

  def updateCart(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      println("updateCart:",request.body)
      request.body.validate[Cart].map {
        cart =>
          cartRepository.update(cart.id, cart).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

}


