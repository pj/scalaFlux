package nz.kiwi.johnson.stuff

import scala.scalajs.js
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import org.scalajs.dom.window
import scala.scalajs.js.Math
import scala.concurrent.Future

import scalatags.Text.all._
import nz.kiwi.johnson.virtual_dom.libraryInterface
import nz.kiwi.johnson.virtual_dom.VirtualNode

import org.scalajs.jquery.jQuery
import org.scalajs.jquery.JQueryEventObject

import rx.Rx
import rx.Var
import rx.Obs
import rx.core.Emitter

object FRPApp extends js.JSApp {
  
  var currentTree: VirtualNode = null
  var currentState: TodoState = null
  var rootNode: Element = null
  
  // state update callback
  def update(newState: TodoState) {
    if (newState == currentState) {
      return
    }
    
    // generate new tree
    println(newState.entryText)
    val newTree = TodoHTML.todoApp(newState)
    
    val patch = libraryInterface.diff(currentTree, newTree)
        
    val x = libraryInterface.patch(rootNode, patch)
    
    currentState = newState
    currentTree = newTree
  }
  
  def initial() {
    // create initial node
    
    currentState = new TodoState(js.Array[Todo]())
    
    currentTree = TodoHTML.todoApp(currentState)
    
    // create initial node
    rootNode = libraryInterface.createElement(currentTree, null)
    
    document.getElementById("main").appendChild(rootNode)
  }

  def eventType(typeString: String, selector: String): Var[JQueryEventObject] = {
    val clickVar: Var[JQueryEventObject] = Var(null)
    
    jQuery(selector).on(typeString, { event: JQueryEventObject =>
      clickVar() = event
    })
      
    clickVar
  }

  def Click(selector: String): Var[JQueryEventObject] = {
     eventType("click", selector)
  }
  
  def Change(selector: String): Var[JQueryEventObject] = {
     eventType("change", selector)
  }
  
  def DoubleClick(selector: String): Var[JQueryEventObject] = {
     eventType("dblclick", selector)
  }
  
  def KeyUp(selector: String): Var[JQueryEventObject] = {
     eventType("keyup", selector)
  }
  
  def FocusOut(selector: String): Var[JQueryEventObject] = {
     eventType("focusout", selector)
  }
  
  def HashChange: Var[JQueryEventObject] = {
    val clickVar: Var[JQueryEventObject] = Var(null)
    
    jQuery(window).on("hashchange", { event: JQueryEventObject =>
      clickVar() = event
    })
      
    clickVar
  }
  
  // generates Rx that finds the clicked items index
  def FindItemIndex(sourceVar: Var[JQueryEventObject]) = {
    Rx {
      val target = sourceVar().target
      
      val id = jQuery(target).closest("li").data("id")
      
      currentState.todos.indexWhere( todo => todo.id == id)
    }
  }
  
  val ENTER = 13
  val ESCAPE = 27
  
  def main(): Unit = {
    initial()
    
    // State var
//    val state = Var(currentState)
    
    // Event vars
    val newTodo = KeyUp("#new-todo")
    val toggleAllChange = Change("#toggle-all")
    val clearCompleted = Click("#clear-completed")
    val toggleChange = Change(".toggle")
    val labelClick  = DoubleClick("label")
    val editKey   = KeyUp(".edit")
    val editFocusOut = FocusOut(".edit")
    val destroy = Click(".destroy")
    
    // filtering
    val filterChange = Rx {
      val change = HashChange()
      
      currentState
    }
    
    // State RXs
    
    val deleteIndex = FindItemIndex(destroy)
    
    val delete = Rx {
      val index = deleteIndex()
      
      val newTodos = currentState.todos.drop(index)
      
      new TodoState(newTodos)
    }
    
    val add = Rx {
      val event = newTodo()
      
      val text = jQuery(event.target).value().asInstanceOf[String].trim()
      
      if (event.which == ENTER) {
        val todoId = Math.random().toString()
        val todo = new Todo(text, false, todoId)

        jQuery("#new-todo").value("")
        
        new TodoState(currentState.todos :+ todo, entry=None)
      } else {
          currentState
      }
    }
    
    // FIXME: adjust focus
//    val edit = Rx {
//      val event = editKey()
//      
//      val id = jQuery(event.target).closest("li").data("id")
//      
//      val index = state().todos.indexWhere( todo => todo.id == id)
//      
//      val focusTodo = state().todos(index)
//      
//      event.which match {
//        case ENTER  => {
//          
//        }
//        case ESCAPE => {
//          
//        }
//        case _      => state()
//      }
//    }
    
    val editFocus = Rx {
      val event = editFocusOut()
      
      val text = jQuery(event.target).value().asInstanceOf[String].trim()
      
      val id = jQuery(event.target).closest("li").data("id")
      
      val index = currentState.todos.indexWhere( todo => todo.id == id)
      
      if (text == "") {
        new TodoState(currentState.todos.drop(index))
      } else {
        val currentTodo = currentState.todos(index)
        currentState.todos.update(index, new Todo(currentTodo.text, currentTodo.completed, currentTodo.id))
        
        currentState
      }
    }
    
    val clear = Rx {
      clearCompleted()
      
      val todos = currentState.todos.filter {
        todo: Todo => todo.completed
      }
      
      new TodoState(todos)
    }

    val toggle = Rx {
      val target = toggleChange().target
      
      val id = jQuery(target).closest("li").data("id")
      
      val index = currentState.todos.indexWhere( todo => todo.id == id)
      val checked = jQuery(target).attr("checked").asInstanceOf[Boolean]
      
      val todos = currentState.todos
      
      val currentTodo = todos(index)
      
      todos(index) = new Todo(currentTodo.text, checked, currentTodo.id)
      
      new TodoState(todos)
    }
    
    val toggleAll = Rx {
      val target = toggleAllChange().target
      val checked = jQuery(target).attr("checked").asInstanceOf[Boolean]
      
      val newTodos = currentState.todos.map {
        todo: Todo => new Todo(todo.text, checked, todo.id)
      }
      
      new TodoState(newTodos)
    }
    
    val filter = Rx {
      val event = filterChange()
      
    }
    
    // adjust focus
    val labelIndex = FindItemIndex(labelClick)
    
    val label = Rx {
      val index = labelIndex()
      
      val currentTodo = currentState.todos(index)
      
      currentState.todos.update(index, new Todo(currentTodo.text, currentTodo.completed, 
                                           currentTodo.id))
                                                       
      new TodoState(currentState. todos)
    }
    
    // Update observers
    Obs(delete, skipInitial=true) {
      val state = delete()
      
      update(state)
    }
    
    Obs(add, skipInitial=true) {
      val state = add()
      
      update(state)
    }
    
//    Obs(edit) {
//      val state = add()
//      
//      update(state)
//    }
    
    Obs(editFocus, skipInitial=true) {
      val state = editFocus()
      
      update(state)
    }
    
    Obs(clear, skipInitial=true) {
      val state = clear()
      
      update(state)
    }
    
    Obs(toggle, skipInitial=true) {
      val state = toggle()
      
      update(state)
    }
    
    Obs(toggleAll, skipInitial=true) {
      val state = toggleAll()
      
      update(state)
    }
    
    Obs(filterChange, skipInitial=true) {
      val state = filterChange()
      
      update(state)
    }
    
    Obs(label, skipInitial=true) {
      val state = label()
      
      update(state)
    }
    
  }
}