package nz.kiwi.johnson.stuff

import scala.scalajs.js
import scala.scalajs.js.Any.fromFunction1
import scala.scalajs.js.Any.jsArrayOps
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import org.scalajs.jquery.JQueryEventObject
import org.scalajs.jquery.jQuery
import monocle.function._
import monocle.function.HeadOption._
import monocle.syntax.lens._
import nz.kiwi.johnson.framework.App
import nz.kiwi.johnson.framework.ExceptionEvent
import nz.kiwi.johnson.framework.InitEvent
import nz.kiwi.johnson.stuff.TodoHTML.todoView
import nz.kiwi.johnson.stuff.TodoStateLenses._
import scalatags.Text.all._
import nz.kiwi.johnson.framework.ExceptionEvent
import monocle.function.each
import monocle.std.listEach
import nz.kiwi.johnson.framework.CheckboxToggleEvent
import nz.kiwi.johnson.framework.EnteredEvent
import nz.kiwi.johnson.framework.DetailsEvent

object TodoApp extends App[TodoState] {
	def getRootNode = document.getElementById("main")
	
	def defaultView(state: TodoState) = {
	  TodoHTML.todoView(state)
	}
	
	def update(currentState: TodoState, event: DetailsEvent[_]) = {
	  event match {
	  case InitEvent() => 
	    val initialState = TodoState(List())
	    respond(initialState)
	  
	  case Filter(filterState) => 
	    val nextState = filter(currentState, filterState)
	    respond(nextState)
	  
	  case Delete(id: String) => 
	    val todos = currentState.todos.filter(_.id != id)
	    val nextState = currentState.copy(todos=todos)
	    
	    respond(nextState)
	    
	  case ClearCompleted() => 
	    val todos = currentState.todos.filter(!_.completed)
	    val nextState = currentState.copy(todos=todos)
	    
	    respond(nextState)
	  
	  case CheckboxToggleEvent("all", toggle) => 
	    val todos = currentState.todos.map (_.copy(completed = toggle))
	    val nextState = currentState.copy(todos=todos)
	    
	    respond(nextState)
	    
	  case CheckboxToggleEvent(id, toggle) => 
	    val todos = currentState.todos.map {
	      todo => 
	        if (todo.id == id) {
	          todo.copy(completed = toggle)
	        } else {
	          todo
	        }
	    }
	    val nextState = currentState.copy(todos=todos)
	    
	    respond(nextState)
	  
	  case EnteredEvent("main", text) =>
	    val id = currentState.currentNumber
	    val newTodos = currentState.todos :+ new Todo(text, id.toString)
	    val nextState = currentState.copy(todos = newTodos, currentNumber = id+1)
	    
	    respond(nextState)
	    
	  case ExceptionEvent(exception) => {
	    println("Something bad happened" + exception.toString)
	    
	    respond(currentState)
	  }
	}
   }
	
    def filter(currentState: TodoState, filterState: FilterState) = {
	    val filtered = filterLens set(currentState, filterState)
	    
	    filterState match {
	      case All()       => 
	        todosLens composeTraversal each modify(filtered, visibleLens set(_, true))
	      case Active()    => 
	        todosLens composeTraversal each modify(filtered, todo => visibleLens set(todo, !todo.completed))
	      case Completed() => 
	        todosLens composeTraversal each modify(filtered, todo => visibleLens set(todo, todo.completed))
	    }
    }
}