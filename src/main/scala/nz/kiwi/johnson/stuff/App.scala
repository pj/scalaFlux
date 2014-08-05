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

abstract class App[T >: Null] extends js.JSApp {
  var currentState: Response[T] = new Response[T](null, libraryInterface.h("div", null, "test"))
  
  var rootNode: Element = null
  
  def getRootNode: Element
  
  implicit val executor = JsFuture.InternalCallbackExecutor
  
  def patchTree(response: Future[Response[T]]) {
    response.map {
      response => 
//         try {
        println(rootNode)
        val patch = libraryInterface.diff(currentState.tree, response.tree)
        
        val x = libraryInterface.patch(rootNode, patch)
        
        currentState = response
//         } catch {
//           case ex: Exception => println(ex.toString())
//         }
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
