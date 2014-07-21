package nz.kiwi.johnson.stuff

import scala.scalajs.js
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import scala.concurrent.Future
import org.scalajs.dom.Event
import scalatags.Text.all._

class Todo(val text: String, val completed: Boolean)
class TodoState(val todos: js.Array[Todo])

class TestJS(rootNode: Element) extends App[TodoState](rootNode) {
  // custom events
  
  // Main event method
  def update(currentState: EventState[TodoState]) = {
    currentState.event match {
      case InitialEvent() => {
        val vnode = virtualDom.h("div", js.Object(), "Hello World")
        
        val newState = new TodoState(js.Array())
        
        JsFuture.successful(new AppState(vnode, newState))
      }
    }
  }
  
  // exported methods for issuing events
  
  // methods for generating visuals using scalatags and virtualdom helper
}