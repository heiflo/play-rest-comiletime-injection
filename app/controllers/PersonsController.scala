package controllers

import models.{Person, PersonService}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc._

case class BadRequestJson(status: String, message: String, errors: Seq[ValidationErrorJson])
case class ValidationErrorJson(field: String, message: String)

class PersonsController(val personService: PersonService) extends Controller {

  protected implicit val personReads = Json.reads[Person]
  protected implicit val personWrites = Json.writes[Person]

  implicit val validationErrorWrites: Writes[ValidationErrorJson] = (
    (JsPath \ "field").write[String] and
      (JsPath \ "message").write[String]
    )(unlift(ValidationErrorJson.unapply))

  implicit val badRequestWrites: Writes[BadRequestJson] = (
    (JsPath \ "status").write[String] and
      (JsPath \ "message").write[String] and
      (JsPath \ "errors").write[Seq[ValidationErrorJson]]
    )(unlift(BadRequestJson.unapply))

  def list = Action { implicit request =>
    val persons = personService.list
    val json = Json.prettyPrint(Json.toJson(persons))
    Ok(json)
  }

  def update = Action(BodyParsers.parse.json) { implicit request =>
    val requestData = request.body.validate[Person]
    requestData.fold(
      errors => {
        BadRequest(Json.prettyPrint(Json.toJson(BadRequestJson("KO", "Validation failed", errors.map(validationError =>
          ValidationErrorJson(validationError._1.toString(), validationError._2(0).message))))))
      },
      person => {
        personService.update(person)
        NoContent
      }
    )
  }



}

