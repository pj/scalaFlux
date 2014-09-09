package nz.kiwi.johnson.framework

import scalatags.Text.all._
import scalatags.VirtualNode

// built in events
trait ApplicationEvent

case class InitEvent() 
	extends ApplicationEvent

class DetailsEvent[E](val details: E) extends 
	ApplicationEvent
	
class OnClickEvent[E](override val details: E, val path: Seq[VirtualNode]) 
	extends DetailsEvent[E](details)

case class ExceptionEvent[E <: Throwable](val exception: E, val path: Seq[VirtualNode]) 
	extends ApplicationEvent

case class CheckboxToggleEvent[E](override val details: E, val toggleState: Boolean = false) extends DetailsEvent[E](details)

case class KeyUpEvent[E](override val details: E, val key: Char = 'x') extends DetailsEvent[E](details)

// when a text box has return on it
case class EnteredEvent[E](override val details: E, val text: String = "") extends DetailsEvent[E](details)