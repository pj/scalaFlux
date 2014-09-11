package examples.todomvc

sealed trait FilterState

case class All() extends FilterState

case class Active() extends FilterState

case class Completed() extends FilterState

case class Todo(val text: String,
                val id: String,
                val visible: Boolean = true,
                val completed: Boolean = false) {
}

case class TodoState(
                      val todos: List[Todo],
                      val filter: FilterState = All(),
                      val currentNumber: Int = 0,
                      val toggle: Boolean = false,
                      val editing: Option[String] = None,
                      val loading: Boolean = false) {
}