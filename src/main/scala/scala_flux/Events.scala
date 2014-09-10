package scala_flux

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.{Element, Event}
import org.scalajs.jquery.jQuery
import scalajs.js
import scalatags.VirtualNode
import scalatags.VirtualDom.all._
import scalatags.VirtualDom
import scalatags.vdom.VBuilder
import scalatags.generic

// Base event attaches listener to element and calls runUpdate with event
abstract class DetailsEvent[E](val identifier: E) {
  var eventName: String = ""
  var underlyingEvent: Event = null
  
  val listener = {jsEvent: Event =>
      underlyingEvent = jsEvent
	  App.currentApp.runUpdate(this)
  }
  
  @JSExport
  def hook(element: Element, name: String, previous: js.Any): Unit = {
    jQuery(element).off(this.eventName)
    jQuery(element).on(this.eventName, listener)
  }
}

// Sent by the App when intialising the application
case class InitEvent() 
	extends DetailsEvent(Unit)
	
// Sent when any error occurs in the application
case class ExceptionEvent[E <: Throwable](val exception: E, val event: DetailsEvent[_]) 
	extends DetailsEvent(exception)

// TODO: Include chain of parents as path value.
case class OnClickEvent[E](override val identifier: E, val path: Seq[VirtualNode] = null) 
	extends DetailsEvent[E](identifier) {
}

// Event that retrieves and sets the value from a checkbox.
case class CheckboxToggleEvent[E](override val identifier: E, val toggleState: Boolean = false) 
  extends DetailsEvent[E](identifier) {
  
  override val listener = {jsEvent: Event =>
    underlyingEvent = jsEvent
    val value = jsEvent.target.asInstanceOf[js.Dynamic].selectDynamic("checked").asInstanceOf[Boolean]
    
    App.currentApp.runUpdate(this.copy(toggleState=value))
  }
}

// Waits for a carriage return on a text input field before issuing the underlying event with the text.
case class EnteredEvent[E](override val identifier: E, val text: String = "") 
  extends DetailsEvent[E](identifier) {
  
  override val listener = {jsEvent: Event =>
    underlyingEvent = jsEvent
	val value = jsEvent.asInstanceOf[js.Dynamic].selectDynamic("keyCode").asInstanceOf[Int]
	  
	if (value == '\r') {
	  val text = jsEvent.target.asInstanceOf[js.Dynamic].selectDynamic("value").asInstanceOf[String]
	  App.currentApp.runUpdate(this.copy(text=text))  
	  jsEvent.target.asInstanceOf[js.Dynamic].updateDynamic("value")("")
	}
  }
}

// Same as above but doesn't clear the text box after issuing the event.
case class EnteredEventNoClear[E](override val identifier: E, val text: String = "") 
  extends DetailsEvent[E](identifier) {
  
  override val listener = {jsEvent: Event =>
    underlyingEvent = jsEvent
	val value = jsEvent.asInstanceOf[js.Dynamic].selectDynamic("keyCode").asInstanceOf[Int]
	  
	if (value == '\r') {
	  val text = jsEvent.target.asInstanceOf[js.Dynamic].selectDynamic("value").asInstanceOf[String]
	  App.currentApp.runUpdate(this.copy(text=text))
	}
  }
}

// Not an event exactly, just sets focus on an element in response to focus being set
// to true.
case class FocusHook(val focus: Boolean) 
	extends DetailsEvent(Unit) {
  
  @JSExport
  override def hook(element: Element, name: String, previous: js.Any): Unit = {
    if (focus) {
      jQuery(element).focus()
    } 
  }
}

// Interface to scalatags - transforms events into properties in underlying library.
class EventAttr[E] extends VirtualDom.AttrValue[DetailsEvent[E]] {
  def apply(t: VBuilder, a: Attr, event: DetailsEvent[E]): Unit = {
    val eventName = a.name.splitAt(2)._2
    event.eventName = eventName
    t.properties.put(a.name, event.asInstanceOf[js.Any])
  }
}

object EventAttr {
  implicit def eventAttr[E]: generic.AttrValue[VBuilder, DetailsEvent[E]] = {
    new EventAttr()
  }
}
