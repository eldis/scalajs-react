package eldis.react.examples.todomvc

import scalajs.js
import js.annotation._
import eldis.react._
import vdom._
import vdom.prefix_<^._
import org.scalajs.dom.html
import org.scalajs.dom.ext.KeyCode

case class TodoItemProps(
  todo: Todo,
  isEditing: Boolean,
  onToggle: () => Unit,
  onStartEditing: () => Unit,
  onDelete: () => Unit,
  onUpdateTitle: Title => Unit,
  onCancelEditing: () => Unit
)

@ScalaJSDefined
class TodoItem extends Component[TodoItemProps]("TodoItem") {

  case class State(
    editText: UnfinishedTitle
  )

  override def initialState =
    State(
      editText = props.todo.title.editable
    )

  def editFieldSubmit() = {
    state.editText.validated.fold(props.onDelete())(props.onUpdateTitle)
  }

  def editFieldChanged(e: ReactEventI) = {
    setState(state.copy(editText = UnfinishedTitle(e.target.value)))
  }

  def editFieldKeyDown(e: ReactKeyboardEventI) = {
    e.keyCode match {
      case KeyCode.Escape => {
        setState(state.copy(editText = props.todo.title.editable))
        props.onCancelEditing()
      }
      case KeyCode.Enter => editFieldSubmit()
      case _ => ()
    }
  }

  val inputRef = Ref[html.Input]()

  def render = {
    val p = props

    <.li(^.className := classNames(p.todo.isCompleted -> "completed", p.isEditing -> "editing"))(
      <.div(^.className := "view")(
        <.input.checkbox(
          ^.className := "toggle",
          ^.checked := p.todo.isCompleted,
          ^.onChange --> p.onToggle
        )(),
        <.label(^.onDoubleClick --> p.onStartEditing)(p.todo.title.value),
        <.button(
          ^.className := "destroy",
          ^.onClick --> p.onDelete
        )()
      ),
      <.input(
        ^.ref := inputRef(),
        ^.className := "edit",
        ^.onBlur --> editFieldSubmit _,
        ^.onChange ==> editFieldChanged _,
        ^.onKeyDown ==> editFieldKeyDown _,
        ^.value := state.editText.value
      )()
    )
  }

  override def didUpdate(prevProps: Props, prevState: Option[State]) {
    if (props.isEditing && !prevProps.isEditing)
      inputRef.get.map(_.focus())
  }
}

@ScalaJSDefined
object TodoItem extends TodoItem
