package examples.todomvc

import scalatags.VirtualNode
import scalatags.VirtualDom.all._
import scalatags.VirtualDom._
import scalatags.VirtualDom.tags2.section
import scala_flux._
import scala_flux.EventAttr._

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
            onkeypress:=EnteredEvent("main")
            )
        )
  }
  
  val hookAttr = scalatags.generic.Attr("hookAttr")
  
  def todoItem(state: TodoState, todo: Todo) = {
    val editing = state.editing.map (_ == todo.id) getOrElse (false)
    
    li(
        `class`:=(if (editing) "editing" else ""),
        div(
            `class`:="view",
            input(
                `class`:="toggle", 
                `type`:="checkbox", 
                checked:=todo.completed,
                onclick:=CheckboxToggleEvent(todo.id)),
            label(
                todo.text,
                onclick:=StartEdit(todo.id)
            ),
            button(
                `class`:="destroy",
                onclick:=Delete(todo.id),
                id:=todo.id)
        ),
        input(
            `class`:="edit", 
            value:=todo.text,
            onkeypress:=EnteredEventNoClear(todo.id),
            onblur:=StopEdit(todo.id),
            hookAttr:=FocusHook(editing)
            )
    )
  }
  
  def appMain(state: TodoState) = {
    val todos = state.todos.filter {
      todo=> 
        state.filter match {
          case All() => true
          case Completed() if todo.completed => true
          case Active() if !todo.completed => true
          case _ => false
        }
    } map {
      todo => todoItem(state, todo)
    }
    
    val x = ul(id:="todo-list")(todos: _*)
    val z = section(
        id:="main",
        input(
            id:="toggle-all",
            `type`:="checkbox",
            onclick:=CheckboxToggleEvent("all"),
            checked:=state.toggle
            ),
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
    
    footer(
        id:="footer",
        if (length == 0) display.none else UnitNode(Unit),
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
            s"Clear completed ($completed)",
            onclick:=ClearCompleted(),
            if (completed == 0) display.none else UnitNode(Unit)
        )
    )
  }
}