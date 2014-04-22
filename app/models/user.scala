package models

import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocument

case class User( _id: Option[BSONObjectID],
				 phoneNumber: String,
                 firstName: String,
                 lastName: String,
                 active: Boolean
            	)

object User {
  	import play.api.libs.json.Json  

  	// Generates Writes and Reads for Feed and User thanks to Json Macros
  	implicit val userFormat = Json.format[User]

	  /** serialize a User into a BSON */
  	implicit object UserBSONWriter extends BSONDocumentWriter[User] {
	    def write(user: User): BSONDocument =
      	BSONDocument(
	        "_id" -> user._id.getOrElse(BSONObjectID.generate),
        	"phoneNumber" -> user.phoneNumber,
        	"firstName" -> user.firstName,
        	"lastName" -> user.lastName,
        	"active" -> user.active)
  	}

  	/** deserialize a User from a BSON */
  	implicit object UserBSONReader extends BSONDocumentReader[User] {
	  	def read(doc: BSONDocument): User =
   			User(
		        doc.getAs[BSONObjectID]("_id"),
		        doc.getAs[String]("phoneNumber").get,
	        	doc.getAs[String]("firstName").get,
	        	doc.getAs[String]("lastName").get,
	        	doc.getAs[Boolean]("active").get
	        	)
	}	


}
