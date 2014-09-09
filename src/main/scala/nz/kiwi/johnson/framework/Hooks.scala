package nz.kiwi.johnson.framework

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

@JSExport
abstract class BaseHook {
    @JSExport
    def hook(element: Element, name: String, previous: BaseHook): Unit
}

class ToggleAttr[E] extends VirtualDom.AttrValue[CheckboxToggleEvent[E]] {
  def apply(t: VBuilder, a: Attr, event: CheckboxToggleEvent[E]): Unit = {
    val eventName = a.name.splitAt(2)._2
    val hook = new BaseHook {
    	def hook(element: Element, name: String, previous: BaseHook): Unit = {
    	  if (previous == null) {
    		element.addEventListener(eventName, { 
    			jsEvent: Event => {
    			  val value = jsEvent.target.asInstanceOf[js.Dynamic].selectDynamic("checked").asInstanceOf[Boolean]
    			  
    			  App.currentApp.runUpdate(event.copy(toggleState=value))
    			}
    		})
    	  }
    	}
    }
    
    t.properties.put(a.name, hook.asInstanceOf[js.Any])
  }
}

object ToggleAttr {
    implicit def toggleAttr[E]: generic.AttrValue[VBuilder, CheckboxToggleEvent[E]] = new ToggleAttr[E]()
}

class EventAttr[E] extends VirtualDom.AttrValue[OnClickEvent[E]] {
  def apply(t: VBuilder, a: Attr, event: OnClickEvent[E]): Unit = {
    val eventName = a.name.splitAt(2)._2
    val hook = new BaseHook {
    	def hook(element: Element, name: String, previous: BaseHook): Unit = {
    	  if (previous == null) {
    		element.addEventListener(eventName, { 
    			jsEvent: Event => {
    			  App.currentApp.runUpdate(event)
    			}
    		})
    	  }
    	}
    }
    
    t.properties.put(a.name, hook.asInstanceOf[js.Any])
  }
}

object EventAttr {
  implicit def eventAttr[E]: generic.AttrValue[VBuilder, OnClickEvent[E]] = new EventAttr[E]()
}

class KeyUpAttr[E] extends VirtualDom.AttrValue[KeyUpEvent[E]] {
  def apply(t: VBuilder, a: Attr, event: KeyUpEvent[E]): Unit = {
    val eventName = a.name.splitAt(2)._2
    val hook = new BaseHook {
    	def hook(element: Element, name: String, previous: BaseHook): Unit = {
    	  if (previous == null) {
    		element.addEventListener(eventName, { 
    			jsEvent: Event => {
    			  val value = jsEvent.asInstanceOf[js.Dynamic].selectDynamic("keyCode").asInstanceOf[Int]
    			  
    			  App.currentApp.runUpdate(event.copy(key=value.toChar))
    			}
    		})
    	  }
    	}
    }
    
    t.properties.put(a.name, hook.asInstanceOf[js.Any])
  }
}

object KeyUpAttr {
    implicit def keyAttr[E]: generic.AttrValue[VBuilder, KeyUpEvent[E]] = new KeyUpAttr[E]()
}

class EnteredAttr[E] extends VirtualDom.AttrValue[EnteredEvent[E]] {
  def apply(t: VBuilder, a: Attr, event: EnteredEvent[E]): Unit = {
    val eventName = a.name.splitAt(2)._2
    val hook = new BaseHook {
    	def hook(element: Element, name: String, previous: BaseHook): Unit = {
    	  if (previous == null) {
    		element.addEventListener(eventName, { 
    			jsEvent: Event => {
    			  val value = jsEvent.asInstanceOf[js.Dynamic].selectDynamic("keyCode").asInstanceOf[Int]
    			  
    			  if (value == '\r') {
    			     val text = jsEvent.target.asInstanceOf[js.Dynamic].selectDynamic("value").asInstanceOf[String]
    			     App.currentApp.runUpdate(event.copy(text=text))  
    			     jsEvent.target.asInstanceOf[js.Dynamic].updateDynamic("value")("")
    			  }
    			}
    		})
    	  }
    	}
    }
    
    t.properties.put(a.name, hook.asInstanceOf[js.Any])
  }
}

object EnteredAttr {
    implicit def enteredAttr[E]: generic.AttrValue[VBuilder, EnteredEvent[E]] = new EnteredAttr[E]()
}

class DeleteAttr extends VirtualDom.AttrValue[Delete] {
  def apply(t: VBuilder, a: Attr, event: Delete): Unit = {
    val eventName = a.name.splitAt(2)._2
    val hook = new BaseHook {
    	def hook(element: Element, name: String, previous: BaseHook): Unit = {
    	  if (previous == null) {
    		element.addEventListener(eventName, { 
    			jsEvent: Event => {
    			  App.currentApp.runUpdate(event)
    			}
    		})
    	  }
    	}
    }
    
    t.properties.put(a.name, hook.asInstanceOf[js.Any])
  }
}

object DeleteAttr {
    implicit def deleteAttr: generic.AttrValue[VBuilder, Delete] = new DeleteAttr()
}