package eldis.react.examples.todomvc

import scalajs.js
import js.annotation._
import eldis.react._
import vdom._
import prefix_<^._
import org.scalajs.dom.ext.KeyCode

@ScalaJSDefined
class TodoList extends ScalaComponent[Nothing]("TodoList") {

  case class State(
    todos: List[Todo],
    editing: Option[TodoId],
    currentFilter: TodoFilter
  )

  def modifyTodos(todos: List[Todo], id: TodoId)(f: Todo => Todo): List[Todo] = {
    todos map {
      case t if t.id == id => f(t)
      case o => o
    }
  }

  def handleNewTodoKeyDown(e: ReactKeyboardEventI): Unit = {
    (e.keyCode, UnfinishedTitle(e.target.value).validated) match {
      case (KeyCode.Enter, Some(title)) => {
        e.target.value = ""
        setState(state.copy(todos = Todo(TodoId.random, title, false) :: state.todos))
      }
      case _ => ()
    }
  }

  override def initialState =
    State(
      todos = List(
        Todo(TodoId.random, Title("Task A"), false),
        Todo(TodoId.random, Title("Task B"), false),
        Todo(TodoId.random, Title("Task C"), false)
      ),
      editing = None,
      currentFilter = TodoFilter.All
    )

  def render = {
    val todos = state.todos
    val filteredTodos = todos filter state.currentFilter.accepts
    val activeCount = todos count TodoFilter.Active.accepts
    val completedCount = todos.length - activeCount

    <.div()(
      <.h1()("todos"),
      <.header(^.className := "header")(
        <.input(
          ^.className := "new-todo",
          ^.placeholder := "What needs to be done?",
          ^.onKeyDown ==> handleNewTodoKeyDown _,
          ^.autoFocus := true
        )()
      ),
      if (todos.nonEmpty) todoList(filteredTodos, activeCount) else EmptyNode,
      if (todos.nonEmpty) footer(activeCount, completedCount) else EmptyNode
    )
  }

  def toggleAll(e: ReactEventI) = {
    setState(state.copy(todos = state.todos.map(todo => todo.copy(isCompleted = e.target.checked))))
  }

  def deleteTodo(id: TodoId) = {
    setState(
      state.copy(
        todos = state.todos.filter(_.id != id)
      )
    )
  }

  def toggleTodo(id: TodoId) = {
    setState(
      state.copy(
        todos = modifyTodos(state.todos, id) { todo => todo.copy(isCompleted = !todo.isCompleted) }
      )
    )
  }

  def startEditing(id: TodoId) = {
    setState(state.copy(editing = Some(id)))
  }

  def updateTitle(id: TodoId)(title: Title) = {
    setState(
      state.copy(
        editing = None,
        todos = modifyTodos(state.todos, id) { _.copy(title = title) }
      )
    )
  }

  def cancelEditing() = {
    setState(state.copy(editing = None))
  }

  def todoList(filteredTodos: Seq[Todo], activeCount: Int) = {
    <.section(^.className := "main")(
      <.input.checkbox(
        ^.className := "toggle-all",
        ^.checked := activeCount == 0,
        ^.onChange ==> toggleAll _
      )(),
      <.ul(^.className := "todo-list")(
        filteredTodos.map({ todo =>
          TodoItem(
            TodoItemProps(
              todo = todo,
              isEditing = state.editing.contains(todo.id),
              onToggle = () => toggleTodo(todo.id),
              onDelete = () => deleteTodo(todo.id),
              onStartEditing = () => startEditing(todo.id),
              onUpdateTitle = updateTitle(todo.id),
              onCancelEditing = () => cancelEditing()
            )
          )
        }): _*
      )
    )
  }

  def onChangeFilter(f: TodoFilter) = {
    setState(state.copy(currentFilter = f))
  }

  def clearCompleted() = {
    setState(state.copy(todos = state.todos.filter(!_.isCompleted)))
  }

  def footer(activeCount: Int, completedCount: Int) = {
    Footer(FooterProps(state.currentFilter, onChangeFilter _, () => clearCompleted(), activeCount, completedCount))
  }

}

@ScalaJSDefined
object TodoList extends TodoList
