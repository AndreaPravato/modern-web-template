package controllers

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.Future
import reactivemongo.api.Cursor
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import org.slf4j.{LoggerFactory, Logger}
import javax.inject.Singleton
import play.api.mvc._
import play.api.libs.json._

/**
 * The Users controllers encapsulates the Rest endpoints and the interaction with the MongoDB, via ReactiveMongo
 * play plugin. This provides a non-blocking driver for mongoDB as well as some useful additions for handling JSon.
 * @see https://github.com/ReactiveMongo/Play-ReactiveMongo
 */
@Singleton
class Users extends Controller with MongoController {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Users])

  /*
   * Get a JSONCollection (a Collection implementation that is designed to work
   * with JsObject, Reads and Writes.)
   * Note that the `collection` is not a `val`, but a `def`. We do _not_ store
   * the collection reference to avoid potential problems in development with
   * Play hot-reloading.
   */
  def collection: JSONCollection = db.collection[JSONCollection]("users")

  // ------------------------------------------ //
  // Using case classes + Json Writes and Reads //
  // ------------------------------------------ //

  import play.modules.reactivemongo.json.BSONFormats._
  import reactivemongo.bson.BSONDocument
  import models._
  import models.User._
  import reactivemongo.bson.BSONObjectID

  def createUser = Action.async(parse.json) {
    request =>
    /*
     * request.body is a JsValue.
     * There is an implicit Writes that turns this JsValue as a JsObject,
     * so you can call insert() with this JsValue.
     * (insert() takes a JsObject as parameter, or anything that can be
     * turned into a JsObject using a Writes.)
     */
    
    //BEFORE it was
    //     request.body.validate[User].map {
    //       user =>
    //       // `user` is an instance of the case class `models.User`
    //         collection.insert(user).map {
    //           lastError =>
    //             logger.debug(s"Successfully inserted with LastError: $lastError")
    //             Created(s"User Created")
    //         }
    //     }.getOrElse(Future.successful(BadRequest("invalid json")))
    // }

      val phoneNumber = request.body.\("phoneNumber").toString().replace("\"", "")   // if simple number instead, can use .as[Int]
      val firstName = request.body.\("firstName").toString().replace("\"", "")
      val lastName = request.body.\("lastName").toString().replace("\"", "")
      val active = request.body.\("active").as[Boolean]
      val user = User(Option(BSONObjectID.generate), phoneNumber, firstName, lastName, active) // create the celebrity
      collection.insert(user).map(
        _ => Ok(Json.toJson(user)))
    }

  def findUsers = Action.async {
    // let's do our query
    val cursor: Cursor[User] = collection.
      find(BSONDocument()).
      // // find all
      // find(Json.obj("active" -> true)).
      // // sort them by creation date
      // sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[User]

      //BEFORE it was
      // // gather all the JsObjects in a list
      // val futureUsersList: Future[List[User]] = cursor.collect[List]()

      // // transform the list into a JsArray
      // val futurePersonsJsonArray: Future[JsArray] = futureUsersList.map { users =>
      //   Json.arr(users)
      // }
      // // everything's ok! Let's reply with the array
      // futurePersonsJsonArray.map {
      //   users =>
      //     Ok(users(0))
      // }

      // gather all the JsObjects in a list
      val futureList = cursor.collect[List]() 
      // Returns the list transformed into a JsArray (by toJson; alternative would be: Json.arr(users))
      futureList.map { users => Ok(Json.toJson(users)) }  

  }

  // sends back JSON about a user corresponding to given id 
  def getUser(id: String) = Action.async {
    val objectID = new BSONObjectID(id) // from id to BSONObjectID  
    val futureUser = collection.find(BSONDocument("_id" -> objectID)).one[User]
    futureUser.map { user => Ok(Json.toJson(user)) }
  }

}
