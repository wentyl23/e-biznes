package controllers.forms

import models.{Order, User, Payment, Voucher}
import play.api.data.Form
import play.api.data.Forms._

import javax.inject._
import play.api.mvc._
import services.{OrderRepository, PaymentRepository, UserRepository, VoucherRepository}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class FormOrderController @Inject()(orderRepository: OrderRepository, userRepository: UserRepository, paymentRepository: PaymentRepository, voucherRepository: VoucherRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/order/list"
  var userList: Seq[User] = Seq[User]()
  var paymentList: Seq[Payment] = Seq[Payment]()
  var voucherList: Seq[Voucher] = Seq[Voucher]()

  fetchLists()

  def fetchLists(): Unit ={
    userRepository.getAll.onComplete {
      case Success(users) => userList = users
      case Failure(err) => print("error: fetching users failed", err)
    }

    paymentRepository.list().onComplete {
      case Success(payments) => paymentList = payments
      case Failure(err) => print("error: fetching payments failed", err)
    }

    voucherRepository.list().onComplete {
      case Success(vouchers) => voucherList = vouchers
      case Failure(err) => print("error: fetching vouchers failed", err)
    }

  }

  def getOrders: Action[AnyContent] = Action.async {
    implicit request =>
      orderRepository.list().map(orders => Ok(views.html.forms.get_orders(orders)))
  }

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "userId" -> longNumber,
      "paymentId" -> longNumber,
      "voucherId" -> longNumber
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "id" -> longNumber,
      "userId" -> longNumber,
      "paymentId" -> longNumber,
      "voucherId" -> longNumber
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
  }

  def addOrder(): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val users = Await.result(userRepository.getAll, 1.second)
      val payments = Await.result(paymentRepository.list(), 1.second)
      val vouchers = voucherRepository.list()

      vouchers.map(voucher => Ok(views.html.forms.add_order(orderForm, users, payments, voucher)))
  }

  def createOrderHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      orderForm.bindFromRequest.fold(
        errorForm => {
          Future.successful(
            BadRequest(views.html.forms.add_order(errorForm, userList, paymentList, voucherList))
          )
        },
        order => {
          orderRepository.create(order.userId, order.paymentId, order.voucherId).map { _ =>
            Redirect(url).flashing("success" -> "order.created")
          }
        }
      )
  }

  def updateOrder(id: Long): Action[AnyContent] = Action.async {
    implicit request: MessagesRequest[AnyContent] =>
      val order = orderRepository.getById(id)
      order.map(
        order => {
          val orderForm = updateOrderForm.fill(UpdateOrderForm(order.get.id,  order.get.userId, order.get.paymentId, order.get.voucherId))
          Ok(views.html.forms.update_order(orderForm, userList, paymentList, voucherList))
        })
  }

  def updateOrderHandle(): Action[AnyContent] = Action.async {
    implicit request =>
      updateOrderForm.bindFromRequest.fold(
        errorForm => {

          Future.successful(
            BadRequest(views.html.forms.update_order(errorForm, userList, paymentList, voucherList))
          )
        },
        order => {
          orderRepository.update(order.id, Order(order.id, order.userId, order.paymentId, order.voucherId)).map { _ =>
            Redirect(url).flashing("success" -> "order.updated")
          }
        }
      )
  }

  def deleteOrder(id: Long): Action[AnyContent] = Action {
    orderRepository.delete(id)
    Redirect(url)
  }
}


case class CreateOrderForm( userId: Long, paymentId: Long, voucherId: Long)

case class UpdateOrderForm(id: Long, userId: Long, paymentId: Long, voucherId: Long)





