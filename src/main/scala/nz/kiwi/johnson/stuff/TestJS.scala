package nz.kiwi.johnson.stuff

import scala.scalajs.js
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import scala.concurrent.Future

object TestJS extends App[TodoState] {
  
  def getRootNode: Element = {
    document.getElementById("main")
  }
  
  // custom events
  
  // Main event method
  def update(currentState: EventState[TodoState]) = {
    currentState.event match {
      case InitialEvent() => {
        val newState = new TodoState(js.Array())

        val vnode = TodoHTML.todoApp(newState)

        JsFuture.successful(new Response(newState, vnode))
      }
      case _ => throw new Exception("Unknown event")
    }
  }
  
  // exported methods for issuing events
}