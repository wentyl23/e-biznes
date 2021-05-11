package controllers

import models.User
import javax.inject._
import play.api.mvc._
import services.UserRepository
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserController @Inject()(val userRepository: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

    def addUser(): Action[JsValue] = Action.async(parse.json) {
      implicit request =>
        request.body.validate[User].map {
          user =>
            userRepository.create(user.email, user.login, user.password).map {
              res=>
                Ok(Json.toJson(res))
            }
        }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
    }

    def getUsers: Action[AnyContent] = Action.async {
      val users = userRepository.list()
      users.map {
        users => Ok(Json.toJson(users))
      }
    }

    def getUser(id: Long): Action[AnyContent] = Action.async {
        println("getUser:",id)
      val user = userRepository.getById(id)
      user.map {
        case Some(res) => Ok(Json.toJson(res))
        case None => NotFound("There is no user with that id")
      }
    }

    def deleteUser(id:Long): Action[AnyContent] = Action.async{
        println("deleteUser:",id)
      userRepository.delete(id).map {
        res=>
          Ok(Json.toJson(res))
      }
    }

    def updateUser(): Action[JsValue] = Action.async(parse.json) {
      implicit request =>
        request.body.validate[User].map {
          user =>
            userRepository.update(user.id, user).map {
              res=>
                Ok(Json.toJson(res))
            }
        }.getOrElse(Future.successful(BadRequest("Error: Bad Data")))
    }

}
