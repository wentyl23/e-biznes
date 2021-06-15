package controllers.forms

import models.{Product, Cart, Order}
import play.api.data.Form
import play.api.data.Forms._

import javax.inject._
import play.api.mvc._
import services.{CartRepository, OrderRepository, ProductRepository}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class FormCartController @Inject()(cartRepository: CartRepository,orderRepository: OrderRepository, productRepository: ProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/cart/list"
  var orderList: Seq[Order] = Seq[Order]()
  var productList: Seq[Product] = Seq[Product]()
  fetchLists()

  def fetchLists(): Unit ={
    orderRepository.list().onComplete {
      case Success(orders) => orderList = orders
      case Failure(err) => print("error: fetching brands failed", err)
    }

    productRepository.list().onComplete {
      case Success(product) => productList = product
      case Failure(err) => print("error: fetching categories failed", err)
    }
  }

  def getCarts: Action[AnyContent] = Action.async {
    implicit request =>
      cartRepository.list().map(carts => Ok(views.html.forms.get_carts(carts)))
  }

  val cartForm: Form[CreateCartForm] = Form {
    mapping(
      "orderId" -> longNumber,
      "productId" -> longNumber,
      "amount" -> number,
    )(CreateCartForm.apply)(CreateCartForm.unapply)
  }

  val updateCartForm: Form[UpdateCartForm] = Form {
    mapping(
      "id" -> longNumber,
      "orderId" -> longNumber,
      "productId" -> longNumber,
      "amount" -> number,
    )(UpdateCartForm.apply)(UpdateCartForm.unapply)
  }

  def addCart(): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val orders = Await.result(orderRepository.list(), 1.second)
      val products = productRepository.list()
      products.map(product => Ok(views.html.forms.add_cart(cartForm, orders, product)))
  }

  def createCartHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      cartForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.add_cart(errorForm, orderList, productList))
          )
        },
        cart => {
          cartRepository.create(cart.orderId, cart.productId, cart.amount).map { _ =>
            Redirect(url).flashing("success" -> "cart.created")
          }
        }
      )
  }

  def updateCart(id: Long): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val cart = cartRepository.getById(id)
      cart.map(
        cart => {
          val cartForm = updateCartForm.fill(UpdateCartForm(cart.get.id, cart.get.orderId, cart.get.productId, cart.get.amount))
          Ok(views.html.forms.update_cart(cartForm, orderList, productList))
        })
  }

  def updateCartHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      updateCartForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.update_cart(errorForm, orderList, productList))
          )
        },
        cart => {
          cartRepository.update(cart.id, Cart(cart.id, cart.orderId, cart.productId, cart.amount)).map { _ =>
            Redirect(url).flashing("success" -> "cart.created")
          }
        }
      )
  }

  def deleteCart(id: Long): Action[AnyContent] = Action {
    cartRepository.delete(id)
    Redirect(url)
  }
}

case class CreateCartForm(orderId: Long, productId: Long, amount: Int)

case class UpdateCartForm(id: Long, orderId: Long, productId: Long, amount: Int)



