package scala_flux

import scala.scalajs.js
import org.scalajs.dom.Element
import org.scalajs.dom.document
import scalatags.Text.all._
import scalatags.VirtualNode
import scalatags.libraryInterface

sealed trait Response
case class NullResponse() extends Response
case class ImmediateResponse[T](val state: T) extends Response
case class ImmediateTreeResponse[T](val state: T, val tree: VirtualNode) extends Response

abstract class App[T >: Null] extends js.JSApp {
  var currentState: T = null
  var currentTree: VirtualNode = libraryInterface.h("div", js.Object(), "test")
  
  var rootNode: Element = null
  var patchNode: Element = null
  
  def getRootNode: Element = document.getElementById(rootId)
  
  def rootId: String

  private[this] def patchTree(nextState: T, nextTree: VirtualNode) {
	val patch = libraryInterface.diff(currentTree, nextTree)

	val newRootNode = libraryInterface.patch(patchNode, patch)
	patchNode = newRootNode
    currentState = nextState
    currentTree = nextTree
  }
  
  private[this] def handleResponse(response: Response) {
    response match {
      case ImmediateResponse(nextState: T) => patchTree(nextState, defaultView(nextState))
      case ImmediateTreeResponse(nextState: T, tree) => patchTree(nextState, tree)
      case NullResponse() => Unit
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
    
    handleResponse(response)
  }
  
  def runUpdate(event: DetailsEvent[_]): Unit = {
    val response = try {
    	update(currentState, event)
    } catch {
      case x => {
        update(currentState, ExceptionEvent(x, event))
      }
    }
    
    handleResponse(response)
  }
  
  def update(state: T, event: DetailsEvent[_]): Response
  
  // A default view function so that it doesn't have to be passed every time
  def defaultView(state: T): VirtualNode
  
//  // Response helpers - generate different response types
//  implicit def response(state: T): Response = {
//    ImmediateResponse(state)
//  }
//  
//  def response(state: T, tree: VirtualNode): Response = {
//    ImmediateTreeResponse(state, tree)
//  }
//  
//  def response(state: T, tree: VirtualDom.TypedTag[VirtualNode]): Unit = {
//    val node = tree.render
//    
//    ImmediateTreeResponse(state, tree)
//  }
}

// Nasty stuff to create singleton of current App so events can easily call it.
object App {
  var currentAppInstance: App[_] = null
  
  def currentApp[T >: Null]: App[T] = currentAppInstance.asInstanceOf[App[T]]
  
  def currentState[T >: Null]: T = currentAppInstance.asInstanceOf[App[T]].currentState
}
