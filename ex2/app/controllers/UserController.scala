package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

    def addUser(): Action[AnyContent] = Action.async {
      implicit request =>
        println("addUser:",request.body)
        Future {
          Ok("")
        }
    }

    def getUsers: Action[AnyContent] = Action.async {
      implicit request =>
        println("getUsers")
        Future {
          Ok("")
        }
    }

    def getUser(id: Long): Action[AnyContent] = Action.async {
        println("getUser:",id)
        Future{
         Ok("")
        }
    }

    def deleteUser(id:Long): Action[AnyContent] = Action.async{
        println("deleteUser:",id)
        Future{
          Ok("")
        }
    }

    def updateUser(): Action[AnyContent] = Action.async {
      implicit request =>
       println("updateUser:",request.body)
       Future {
         Ok("")
       }
    }

}
