import akka.actor.Props
import controllers.PersonsController
import models.PersonCacheService
import play.api.ApplicationLoader.Context
import play.filters.HttpFiltersComponents
// import play.api.cache.EhCacheComponents
import play.api.i18n.I18nComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext}
import router.Routes

/**
  * Created by flo on 1.11.16.
  */
class AppLoader extends ApplicationLoader {
  def load(context: Context) = {
    new Components(context).application
  }
}

class Components(context: Context)
  extends BuiltInComponentsFromContext(context) with I18nComponents
                                                with HttpFiltersComponents
                                                // with EhCacheComponents
                                                with controllers.AssetsComponents {

  // Custom error handler
  // override lazy val httpErrorHandler = new ErrorHandler(context.environment, context.initialConfiguration, context.sourceMapper, None)


  lazy val personService = new PersonCacheService
  lazy val personsController = new PersonsController(personService)

  // lazy val assets = new controllers.Assets(httpErrorHandler)
  override lazy val router = new Routes(httpErrorHandler, personsController, assets)
}
