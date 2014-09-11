package scala_flux

import scala.scalajs.js
import org.scalajs.dom.Element
import org.scalajs.dom.document
import scalatags.VirtualNode
import scalatags.libraryInterface
import cgta.ojs.lang.JsFuture.InternalCallbackExecutor
import scala.concurrent.Future

sealed trait Response

// Do nothing response
case class NullResponse() extends Response

// Return a response immediately
case class ImmediateResponse[T](val state: T) extends Response

case class ImmediateTreeResponse[T](val state: T, val tree: VirtualNode) extends Response

// A series of responses that updates the application state in order.
case class SequenceResponse(val states: List[Response]) extends Response

// A set of responses that updates the application as they arrive.
case class AsSoonAsResponse[T](val states: Seq[Response]) extends Response

// A response that takes a future and a function to update the state with after 
// the future returns
case class FutureResponse[E, T](val future: () => Future[E], val updater: (T, E) => T) extends Response

case class NonImmediateResponse[T](val updater: T => T) extends Response

abstract class App[T >: Null] extends js.JSApp {
  implicit val ec = InternalCallbackExecutor
  var currentState: T = null
  var currentTree: VirtualNode = libraryInterface.h("div", js.Object(), "test")

  var rootNode: Element = null
  var patchNode: Element = null

  def getRootNode: Element = document.getElementById(rootId)

  def rootId: String

  // A default view function so that it doesn't have to be passed every time
  def defaultView(state: T): VirtualNode

  private[this] def patchTree(nextState: T, nextTree: VirtualNode) {
    val patch = libraryInterface.diff(currentTree, nextTree)

    val newRootNode = libraryInterface.patch(patchNode, patch)
    patchNode = newRootNode
    currentState = nextState
    currentTree = nextTree
  }

  private[this] def handleResponse(response: Response) {
    println(response)
    response match {
      case ImmediateResponse(nextState: T) => {
        patchTree(nextState, defaultView(nextState))
      }
      case ImmediateTreeResponse(nextState: T, tree) => patchTree(nextState, tree)
      case NullResponse() => Unit
      case FutureResponse(future, updater: ((T, _) => T)) => {
        future().map { value =>
          val nextState = updater(currentState, value)

          patchTree(nextState, defaultView(nextState))
        }
      }
      case SequenceResponse(response :: responses) => {
        response match {
          case FutureResponse(future, updater: ((T, _) => T)) => {
            future().map { value =>
              val nextState = updater(currentState, value)

              patchTree(nextState, defaultView(nextState))

              if (responses.length != 0) {
                handleResponse(SequenceResponse(responses))
              }
            }
          }
          case x => handleResponse(x)
        }
      }
      case NonImmediateResponse(updater: (T => T)) => {
        val nextState = updater(currentState)
        patchTree(nextState, defaultView(nextState))
      }
      case AsSoonAsResponse(responses) => {
        responses.foreach {
          response => handleResponse(response)
        }
      }
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
}

// Nasty stuff to create singleton of current App so events can easily call it.
object App {
  var currentAppInstance: App[_] = null

  def currentApp[T >: Null]: App[T] = currentAppInstance.asInstanceOf[App[T]]

  def currentState[T >: Null]: T = currentAppInstance.asInstanceOf[App[T]].currentState
}
