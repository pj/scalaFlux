package nz.kiwi.johnson.stuff

import scala.scalajs.js
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import scala.concurrent.Future
//import org.scalajs.dom.Event
import scalatags.Text.all._
import nz.kiwi.johnson.virtual_dom.libraryInterface
import nz.kiwi.johnson.virtual_dom.VirtualNode

// built in events
sealed trait ApplicationEvent
case class InitialEvent() extends ApplicationEvent
//case class LocationChangeEvent() extends ApplicationEvent

class EventState[T](val state: T, val event: ApplicationEvent)
class Response[T](val state: T, val tree: VirtualNode)

abstract class App[T >: Null](rootNode: Element) extends js.JSApp() {
  var currentState: Response[T] = new Response[T](null, null)
  
  implicit val executor = JsFuture.InternalCallbackExecutor
  
  def patchTree(response: Future[Response[T]]) {
    response.map {
      response => 
        val patch = libraryInterface.diff(currentState.tree, response.tree)
        
        libraryInterface.patch(rootNode, patch)
        
        currentState = response
    }
  }
  
  def main(): Unit = {
    // Send setup event
    val initialEvent = InitialEvent()
    
    val response = update(new EventState(null, initialEvent))

    patchTree(response)
  }
  
  def runUpdate(event: ApplicationEvent): Unit = {
    val response = update(new EventState(currentState.state, event))
    
    patchTree(response)
  }
  
  def update(state: EventState[T]): Future[Response[T]]
}
