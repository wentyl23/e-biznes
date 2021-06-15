package controllers

import models.Order
import play.api.libs.json.{JsValue, Json}

import javax.inject._
import play.api.mvc._
import services.OrderRepository

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OrderController @Inject()(val orderRepository: OrderRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  def addOrder(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      System.out.println(request.body)
      request.body.validate[Order].map {
        order =>
          System.out.println("co tam")
          System.out.println(order.userId)
          orderRepository.create(order.userId, order.paymentId, order.voucherId).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

  def getOrders: Action[AnyContent] = Action.async {
    val orders = orderRepository.list()
    orders.map {
      orders => Ok(Json.toJson(orders))
    }
  }

  def getOrder(id: Long): Action[AnyContent] = Action.async {
    println("getOrder:",id)
    val order = orderRepository.getById(id)
    order.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("There is no order with that id")
    }
  }

  def deleteOrder(id:Long): Action[AnyContent] = Action.async{
    println("deleteOrder:",id)
    orderRepository.delete(id).map {
      res=>
        Ok(Json.toJson(res))
    }
  }

  def updateOrder(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      println("updateOrder:",request.body)
      request.body.validate[Order].map {
        order =>
          orderRepository.update(order.id, order).map {
            res=>
              Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
  }

}

