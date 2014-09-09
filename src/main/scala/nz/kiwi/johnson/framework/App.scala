package nz.kiwi.johnson.framework

import scala.scalajs.js
import org.scalajs.dom.Element
import scala.concurrent.Future
import scalatags.Text.all._
import nz.kiwi.johnson.libs.JsFuture
import scalatags.VirtualNode
import scalatags.libraryInterface
import scalatags.VirtualDom

//case class Request[T, E](val state: T, val event: ApplicationEvent[E])
case class Response[T](val state: T, val tree: VirtualNode)

abstract class App[T >: Null] extends js.JSApp {
  var currentState: T = null
  var currentTree: VirtualNode = libraryInterface.h("div", js.Object(), "test")
  
  var rootNode: Element = null
  var patchNode: Element = null
  
  def getRootNode: Element
  
  implicit val executor = JsFuture.InternalCallbackExecutor
  
  def patchTree(nextState: T, nextTree: VirtualNode) {
	val patch = libraryInterface.diff(currentTree, nextTree)

	val newRootNode = libraryInterface.patch(patchNode, patch)
	patchNode = newRootNode
    currentState = nextState
    currentTree = nextTree
  }
  
  class UnknownEventException(val msg: String, val event: ApplicationEvent) extends Exception(msg)
  
  val unknownFunction: PartialFunction[(T, ApplicationEvent), Unit] = {
	case unknown: ApplicationEvent => {
		val errorEvent = ExceptionEvent(new UnknownEventException("Unknown Event", unknown), null)
		
		val response = update(currentState, errorEvent)
	  }
  }
  
  def main(): Unit = {
    // set instance of App
    App.currentAppInstance = this
    
    // create initial node
    rootNode = getRootNode
    
    val element = libraryInterface.createElement(currentTree, null)
    
    rootNode.appendChild(element)
    
    patchNode = element
    
    // Send setup event
    val initialEvent = InitEvent()
    
    val response = update(null, initialEvent)

//    patchTree(response)
  }
  
  def runUpdate(event: ApplicationEvent): Unit = {
    update(currentState, event)
  }
  
  def update(state: T, event: ApplicationEvent): Unit
  
  
  def defaultView(state: T): VirtualNode
  
  // response helpers
  def respond(state: T): Unit = {
    patchTree(state, defaultView(state))
  }
  
  def respond(state: T, tree: VirtualNode): Unit = {
    patchTree(state, tree)
  }
  
  def respond(state: T, tree: VirtualDom.TypedTag[VirtualNode]): Unit = {
    val node = tree.render

    patchTree(state, node)
  }
}

object App {
  var currentAppInstance: App[_] = null
  
  def currentApp[T >: Null]: App[T] = currentAppInstance.asInstanceOf[App[T]]
  
  def currentState[T >: Null]: T = currentAppInstance.asInstanceOf[App[T]].currentState
}
