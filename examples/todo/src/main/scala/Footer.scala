package eldis.react.examples.todomvc

import scalajs.js
import js.annotation._
import eldis.react._
import vdom._
import vdom.prefix_<^._

case class FooterProps(
  currentFilter: TodoFilter,
  onChangeFilter: TodoFilter => Unit,
  onClearCompleted: () => Unit,
  activeCount: Int,
  completedCount: Int
)

@ScalaJSDefined
class Footer extends Component[Wrapped[FooterProps]]("Footer") {

  type State = Nothing

  def filterLink(f: TodoFilter) = {
    val p = this.props.get
    <.li()(
      <.a(
        p.currentFilter == f ?= (^.className := "selected"),
        p.currentFilter != f ?= (^.onClick --> (() => p.onChangeFilter(f))),
        ^.href := "#"
      )(f.title)
    )
  }

  def clearButton() = {
    val p = this.props.get
    <.button(
      ^.className := "clear-completed",
      ^.onClick --> p.onClearCompleted,
      ^.style := Style(
        p.completedCount > 0 ?= "visibility" -> "hidden"
      )
    )("Clear completed")
  }

  def render() = {
    val p = this.props.get

    <.footer(^.className := "footer")(
      <.span(^.className := "todo-count")(
        <.strong()(p.activeCount.toString),
        s" ${if (p.activeCount == 1) "item" else "items"} left"
      ),
      <.ul(^.className := "filters")(
        TodoFilter.values.map(this.filterLink _): _*
      ),
      clearButton()
    )
  }

}

@ScalaJSDefined
object Footer extends Footer {
  @JSName("createWrapped")
  def apply(p: FooterProps): ReactDOMElement = apply(Wrapped(p))
}
