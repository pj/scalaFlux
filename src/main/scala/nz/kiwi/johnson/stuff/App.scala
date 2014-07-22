package nz.kiwi.johnson.stuff

import scala.scalajs.js
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import scala.concurrent.Future
import org.scalajs.dom.Event
import scalatags.Text.all._
import nz.kiwi.johnson.stuff.virtualDom
import nz.kiwi.johnson.stuff.VirtualNode

// built in events
sealed trait Event
case class InitialEvent() extends Event
case class LocationChangeEvent() extends Event

class AppState[T](val tree: VirtualNode, val state: T)
class EventState[T](val state: AppState[T], val event: Event)

abstract class App[T >: Null](rootNode: Element) extends js.JSApp() {
  var currentState: AppState[T] = new AppState[T](null, null)
  
  implicit val executor = JsFuture.InternalCallbackExecutor
  
  def patchTree(response: Future[AppState[T]]) {
    response.map {
      appState => 
        val patch = virtualDom.diff(currentState.tree, appState.tree)
        
        virtualDom.patch(rootNode, patch)
        
        currentState = appState
    }
  }
  
  def main(): Unit = {
    // Send setup event
    val initialEvent = InitialEvent()
    
    val response = update(new EventState(null, initialEvent))

    patchTree(response)
  }
  
  def runUpdate(event: Event): Unit = {
    val response = update(new EventState(currentState, event))
    
    patchTree(response)
  }
  
  def update(state: EventState[T]): Future[AppState[T]]
}
