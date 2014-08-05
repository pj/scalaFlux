package nz.kiwi.johnson.stuff

import scala.scalajs.js
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import scala.concurrent.Future
import nz.kiwi.johnson.virtual_dom.libraryInterface

import nz.kiwi.johnson.virtual_dom.VirtualDom.all._
import nz.kiwi.johnson.virtual_dom.VirtualDom.tags2.section
import nz.kiwi.johnson.virtual_dom.VirtualNode


//import scalatags.Text.tags2.address

class Todo(val text: String, val completed: Boolean)
class TodoState(val todos: js.Array[Todo])

object TestJS extends App[TodoState] {
  
  def getRootNode: Element = {
    document.getElementById("main")
  }
  
  // custom events
  
  // Main event method
  def update(currentState: EventState[TodoState]) = {
    currentState.event match {
      case InitialEvent() => {
        val newState = new TodoState(js.Array())

        val vnode = todoApp(newState)

        JsFuture.successful(new Response(newState, vnode))
      }
      case _ => throw new Exception("Unknown event")
    }
  }
  
  // exported methods for issuing events
  
  // methods for generating visuals using scalatags and virtualdom helper
  def todoApp(state: TodoState): VirtualNode = {
    section(
        id:="todoapp",
        appHeader(state),
        appMain(state),
        appFooter(state)).render
    
//    libraryInterface.h("div", null, "asdf")
  }
  
  def appHeader(state: TodoState) = {
    header(
        id:="header",
        h1("todos"),
        input(
            id:="new-todo", 
            placeholder:="What needs to be done?", 
            autofocus:="true")
        )
  }

  /* 
	<li class="completed">
		<div class="view">
			<input class="toggle" type="checkbox" checked>
			<label>Create a TodoMVC template</label>
			<button class="destroy"></button>
		</div>
		<input class="edit" value="Create a TodoMVC template">
	</li>
  */
  
  def todo(todo: Todo) = {
    li(
        `class`:="completed",
        div(
            `class`:="view",
            input(`class`:="toggle", `type`:="checkbox", checked:="true"),
            label("Create a TodoMVC template"),
            button(`class`:="destroy")),
        input(`class`:="edit", value:="Create a TodoMVC template")
    )
  }
  
  def appMain(state: TodoState) = {
    val todos = state.todos.map (todo _)
    
    val list = ul(todos: _*)
    
    val blah = list.apply((id:="todo-list"))
    
    section(
        id:="main",
        input(
            id:="toggle-all",
            `type`:="checkbox"),
        label(
            `for`:="toggle-all",
            "Mark all as complete"),
        blah
    )
  }
  
  def appFooter(state: TodoState) = {
    footer(
        id:="footer",
        span(
            id:="todo-count",
            strong(1),
            "item left"),
        ul(
            id:="filters",
            li(
                a(
                    `class`:="selected", 
                    href:="#", 
                    "All"
                )
            ),
            li(
                a(
                    href:="#/active", 
                    "Active"
                )
            ),
            li(
                a(
                    href:="#/completed", 
                    "Completed"
                )
            )
        ),
        button(
            id:="clear-completed", 
            "Clear completed (1)"
        )
    )
  }
}