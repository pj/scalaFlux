package examples.todomvc

import scala_flux._

// Type parameter is the applications state class
object TodoApp extends App[TodoState] {
  // id of an element to put the view into.
  def rootId = "main"

  // default view for the project.
  def defaultView(state: TodoState) = {
    TodoHTML.todoView(state)
  }

  // dispatch method, takes the current state and event
  def update(currentState: TodoState, event: DetailsEvent[_]) = {
    event match {
      // Create Initial blank state
      case InitEvent() =>
        val initialState = TodoState(List(), loading = true)

        val timeoutFuture = Stream.timeout[TodoState](10) {
          (state, _) =>
            println("success!")
            state
        }

        //    	val p = JsPromise[Unit]()
        //
        //    	global.setTimeout{
        //    		p.complete(Success(Unit)).asInstanceOf[js.Any]
        //    	}
        //    	p.future.map {
        //    	  _ => println("hello world")
        //    	}

        SequenceResponse(
          List(
            ImmediateResponse(initialState),
            timeoutFuture
          )
        )

      // Handle filter change
      case Filter(filterState) =>
        val todos = filterState match {
          case All() => currentState.todos.map(_.copy(visible = true))
          case Active() => currentState.todos.map {
            todo => todo.copy(visible = !todo.completed)
          }
          case Completed() => currentState.todos.map {
            todo => todo.copy(visible = todo.completed)
          }
        }

        val nextState = currentState.copy(filter = filterState, todos = todos)

        ImmediateResponse(nextState)

      case Delete(id) =>
        val todos = currentState.todos.filter(_.id != id)
        val nextState = currentState.copy(todos = todos)

        ImmediateResponse(nextState)

      case ClearCompleted() =>
        val todos = currentState.todos.filter(!_.completed)
        val nextState = currentState.copy(todos = todos, toggle = false)

        ImmediateResponse(nextState)

      // Toggle All checkbox
      case CheckboxToggleEvent("all", toggle) =>
        val nextState = if (currentState.todos.length == 0) {
          currentState.copy(toggle = false)
        } else {
          val todos = currentState.todos.map(_.copy(completed = toggle))
          currentState.copy(todos = todos, toggle = toggle)
        }

        ImmediateResponse(nextState)

      case CheckboxToggleEvent(id, toggle) =>
        val todos = currentState.todos.map {
          todo =>
            if (todo.id == id) {
              todo.copy(completed = toggle)
            } else {
              todo
            }
        }
        val nextState = currentState.copy(todos = todos)

        ImmediateResponse(nextState)

      // Called when return is called on textbox
      case EnteredEvent("main", text) =>
        if (text != "") {
          val id = currentState.currentNumber
          val newTodos = currentState.todos :+ new Todo(text, id.toString)
          val nextState = currentState.copy(todos = newTodos, currentNumber = id + 1)

          ImmediateResponse(nextState)
        } else {
          NullResponse()
        }

      // Start edit on id
      case StartEdit(id) =>
        val nextState = currentState.copy(editing = Some(id))

        ImmediateResponse(nextState)

      // Blur edit box
      case StopEdit(id) =>
        val nextState = currentState.copy(editing = None)

        ImmediateResponse(nextState)

      // Update todo
      case EnteredEventNoClear(id, text) =>
        if (text != "") {
          val todos = currentState.todos.map { todo =>
            if (todo.id == id) {
              todo.copy(text = text)
            } else {
              todo
            }
          }

          val nextState = currentState.copy(todos = todos, editing = None)

          ImmediateResponse(nextState)
        } else {
          NullResponse()
        }


      case ExceptionEvent(exception, underlying) => {
        println(s"Something bad happened ${exception.toString} ${underlying.toString}")

        ImmediateResponse(currentState)
      }
    }
  }
}