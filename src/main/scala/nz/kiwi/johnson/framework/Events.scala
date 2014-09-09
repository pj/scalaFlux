package nz.kiwi.johnson.framework

import scalatags.Text.all._
import scalatags.VirtualNode
import scalatags.VirtualDom.all._
import scalatags.vdom.Frag
import scalatags.VirtualDom.tags2.section
import org.scalajs.jquery.jQuery
import scalatags.generic
import scalatags.`package`.Companion
import scalatags.libraryInterface
import scalatags.VirtualDom
import org.scalajs.dom.{Node, Element, Event}
import scalajs.js
import scala.scalajs.js.annotation.JSExport
import nz.kiwi.johnson.stuff.Filter
import scalatags.vdom.VBuilder
import nz.kiwi.johnson.stuff.Delete
import scala.util.Random
import org.scalajs.jquery.jQuery

// built in events
abstract class DetailsEvent[E](val identifier: E) {
  var eventName: String = ""
  
  val listener = {jsEvent: Event =>
	  App.currentApp.runUpdate(this)
  }
  
  @JSExport
  def hook(element: Element, name: String, previous: js.Any): Unit = {
    jQuery(element).off(this.eventName)
    jQuery(element).on(this.eventName, listener)
//    js.Dynamic.global.virtualDom.removeAddListener(this.asInstanceOf[js.Any], element, this.eventName, listener)
//    element.removeEventListener(this.eventName, listener)
//    element.addEventListener(this.eventName, listener)
  }
}

case class InitEvent() 
	extends DetailsEvent(false)
	
case class ExceptionEvent[E <: Throwable](val exception: E) 
	extends DetailsEvent(exception)

class OnClickEvent[E](override val identifier: E, val path: Seq[VirtualNode] = null) 
	extends DetailsEvent[E](identifier) {
}

case class CheckboxToggleEvent[E](override val identifier: E, val toggleState: Boolean = false) 
  extends DetailsEvent[E](identifier) {
  
  override val listener = {jsEvent: Event =>
    val value = jsEvent.target.asInstanceOf[js.Dynamic].selectDynamic("checked").asInstanceOf[Boolean]
  
    App.currentApp.runUpdate(this.copy(toggleState=value))
  }
}

//case class KeyUpEvent[E](override val details: E, val key: Char = 'x') extends DetailsEvent[E](details)

// when a text box has return on it
case class EnteredEvent[E](override val identifier: E, val text: String = "") 
  extends DetailsEvent[E](identifier) {
  
  override val listener = {jsEvent: Event =>
	  val value = jsEvent.asInstanceOf[js.Dynamic].selectDynamic("keyCode").asInstanceOf[Int]
	  
	  if (value == '\r') {
	     val text = jsEvent.target.asInstanceOf[js.Dynamic].selectDynamic("value").asInstanceOf[String]
	     App.currentApp.runUpdate(this.copy(text=text))  
	     jsEvent.target.asInstanceOf[js.Dynamic].updateDynamic("value")("")
	  }
	}
}

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
