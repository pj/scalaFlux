package nz.kiwi.johnson.stuff

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
import nz.kiwi.johnson.framework.OnClickEvent
import nz.kiwi.johnson.framework.App
import nz.kiwi.johnson.framework.EventAttr._
import nz.kiwi.johnson.framework.EnteredEvent
import nz.kiwi.johnson.framework.CheckboxToggleEvent

object TodoHTML {
  // methods for generating visuals using scalatags and virtualdom helper
  def todoView(state: TodoState): VirtualNode = {
    section(
        id:="todoapp"
        ,appHeader(state)
        ,appMain(state)
        ,appFooter(state)
        ).render.asInstanceOf[VirtualNode]
  }
  
  def appHeader(state: TodoState) = {
    header(
        id:="header",
        h1("todos"),
        input(
            id:="new-todo",
            placeholder:="What needs to be done?",
            autofocus:="true",
            `type`:="text",
            value:=state.entryText,
            onkeypress:=EnteredEvent("main")
            )
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
  
  def todoItem(state: TodoState, todo: Todo) = {
    li(
        `class`:=todo.completedClass,
        div(
            `class`:="view",
            input(
                `class`:="toggle", 
                `type`:="checkbox", 
                checked:=todo.completed,
                onclick:=CheckboxToggleEvent(todo.id)),
            label(todo.text),
            button(
                `class`:="destroy",
                onclick:=Delete(todo.id),
                id:=todo.id)
        ),
        input(`class`:="edit", value:=todo.text)
    )
  }
  
  def appMain(state: TodoState) = {
    val todos = state.todos.map {
      todo: Todo => todoItem(state, todo)
    }
    
    val x = ul(id:="todo-list")(todos: _*)
    val z = section(
        id:="main",
        input(
            id:="toggle-all",
            `type`:="checkbox",
            onclick:=CheckboxToggleEvent("all")),
        label(
            `for`:="toggle-all",
            "Mark all as complete")
        ,x
    )
    
    z
  }
  
  def filterClass(state: TodoState, filterKey: FilterState): String = {
    if (state.filter == filterKey) {
      "selected"
    } else {
      ""
    }
  }

  def appFooter(state: TodoState) = {
    val length = state.todos.length
    val completed = state.todos.foldLeft(0) {
      case (sum, todo) => if (todo.completed) sum + 1 else sum 
    }
    val f = footer(
        id:="footer",
        span(
            id:="todo-count",
            strong(length-completed),
            if (length-completed > 1) " items left" else " item left"),
        ul(
            id:="filters",
            li(
                a(
                    `class`:=filterClass(state, All()), 
                    href:="#", 
                    "All",
                    onclick:=Filter(All())
                )
            ),
            li(
                a(
                    `class`:=filterClass(state, Active()), 
                    href:="#/active", 
                    "Active",
                    onclick:=Filter(Active())
                )
            ),
            li(
                a(
                    `class`:=filterClass(state, Completed()),
                    href:="#/completed", 
                    "Completed",
                    onclick:=Filter(Completed())
                )
            )
        ),
        button(
            id:="clear-completed", 
            s"Clear completed ($completed)"
        )
    )
    
    if (length < 0) f.modifiers :+ (display.none)

    f
  }
}