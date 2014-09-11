package scala_flux

import scala.util.Success
import scalajs.js
import scalajs.js.Dynamic.global
import cgta.ojs.lang.JsPromise

// Various tools for returning future responses
object Stream {
  // returns a stream based on a timer
  def timeout[T](time: Int)(updater: (T, Unit) => T): FutureResponse[Unit, T] = {
    FutureResponse[Unit, T](
    { () =>
      val p = JsPromise[Unit]()

      global.setTimeout {
        p.complete(Success(Unit)).asInstanceOf[js.Any]
      }
      p.future
    },
    updater)
  }
} 