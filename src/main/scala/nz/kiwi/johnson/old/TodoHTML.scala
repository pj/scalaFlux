package nz.kiwi.johnson.old

import org.scalajs.jquery.jQuery
import scalatags.VirtualNode
import scalatags.VirtualDom.all._
import scalatags.VirtualDom.tags2.section

object TodoHTML {
  // methods for generating visuals using scalatags and virtualdom helper
  def todoApp(state: TodoState): VirtualNode = {
    section(
        id:="todoapp"
        ,appHeader(state)
        ,appMain(state)
        ,appFooter(state)
        ).render
  }
  
  def appHeader(state: TodoState) = {
    header(
        id:="header",
        h1("todos"),
        input(
            id:="new-todo"
            ,placeholder:="What needs to be done?"
            ,autofocus:="true"
            ,`type`:="text"
            ,value:=state.entryText
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
            input(`class`:="toggle", `type`:="checkbox", checked:=todo.completedChecked),
            label(todo.text),
            button(`class`:="destroy")
        ),
        input(`class`:="edit", value:=todo.text)
    )
  }
  
  def appMain(state: TodoState) = {
    val todos = state.todos.map {
      todo: Todo => todoItem(state, todo)
    }
    
    val x = ul(id:="todo-list")(todos: _*)
    println(x)
    val z = section(
        id:="main",
        input(
            id:="toggle-all",
            `type`:="checkbox"),
        label(
            `for`:="toggle-all",
            "Mark all as complete")
        ,x
    )
    
    z
  }
  
  def appFooter(state: TodoState) = {
    val length = state.todos.length
    val f = footer(
        id:="footer",
        span(
            id:="todo-count",
            strong(length),
            if (length > 1) " items left" else " item left"),
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
            s"Clear completed ($length)"
        )
    )
    
    if (length < 0) f.modifiers :+ (display.none)

    f
  }
}