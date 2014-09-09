package nz.kiwi.johnson.stuff

import scala.scalajs.js
import monocle.syntax._
import monocle.function.HeadOption._
import monocle.std.string._
import monocle.SimpleLens
import monocle.SimpleOptional
import monocle.SimpleTraversal

sealed trait FilterState
case class All() extends FilterState
case class Active() extends FilterState
case class Completed() extends FilterState

case class Todo(val text: String, val id: String, val visible: Boolean = true, val completed: Boolean = false) {
  
  def completedClass() = {
    if (completed) "completed" else ""
  } 
}

case class TodoState(
    val todos: List[Todo], 
    val editing: Option[String] = None, 
    val filter: FilterState = All(), 
    val entry: Option[String] = None,
    val currentNumber: Int = 0,
    val toggle: Boolean = false) {
  
  def editing(todo: Todo): String = {
    editing match {
      case Some(editingId) if editingId == todo.id => "edit"
      case _ => ""
    }
  }
  
  def entryText: String = {
    entry match {
      case Some(text) => text
      case None => ""
    }
  }
}

object TodoStateLenses {
//  val filterLens = SimpleLens[TodoState](_.filter)()
  val filterLens = SimpleLens[TodoState, FilterState](_.filter, (state, value) => state.copy(filter=value))
  
  val todosLens = SimpleLens[TodoState, List[Todo]](_.todos, (state, value) => state.copy(todos=value))
  
  val visibleLens = SimpleLens[Todo, Boolean](_.visible, (state, value) => state.copy(visible=value))
}