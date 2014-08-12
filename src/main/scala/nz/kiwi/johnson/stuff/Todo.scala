package nz.kiwi.johnson.stuff

import scala.scalajs.js

class Todo(val text: String, val completed: Boolean, val id: String) {
  def completedChecked() = {
    if (completed) "true" else "false"
  }  
  
  def completedClass() = {
    if (completed) "completed" else null
  } 
}

class TodoState(val todos: js.Array[Todo], val editing: Option[String] = None, 
    val filter: Option[String] = None, val entry: Option[String] = None) {
  
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