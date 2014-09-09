package nz.kiwi.johnson.old

import scala.scalajs.js
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import scala.concurrent.Future
import scalatags.Text.all._
import nz.kiwi.johnson.libs.JsFuture
import scalatags.libraryInterface
import scalatags.VirtualNode

// built in events
sealed trait ApplicationEvent
case class InitialEvent() extends ApplicationEvent
//case class LocationChangeEvent() extends ApplicationEvent

class EventState[T](val state: T, val event: ApplicationEvent)
class Response[T](val state: T, val tree: VirtualNode)

abstract class App[T >: Null] extends js.JSApp {
  var currentState: Response[T] = new Response[T](null, libraryInterface.h("div", js.Object(), "test"))
  
  var rootNode: Element = null
  
  def getRootNode: Element
  
  implicit val executor = JsFuture.InternalCallbackExecutor
  
  def patchTree(response: Future[Response[T]]) {
    response.map {
      response => 
        val patch = libraryInterface.diff(currentState.tree, response.tree)
        
        val x = libraryInterface.patch(rootNode, patch)
        
        currentState = response
    } recover {
      case error => println(error.toString())
    }
  }
  
  def main(): Unit = {
    // create initial node
    rootNode = getRootNode
    
    val element = libraryInterface.createElement(currentState.tree, null)
    
    rootNode.appendChild(element)
    
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
