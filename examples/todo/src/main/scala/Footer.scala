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
class Footer extends Component[FooterProps]("Footer") {

  type State = Unit

  def initialState = ()

  def filterLink(f: TodoFilter) = {
    val p = this.props
    <.li()(
      <.a(
        p.currentFilter == f ?= (^.className := "selected"),
        p.currentFilter != f ?= (^.onClick --> (() => p.onChangeFilter(f))),
        ^.href := "#"
      )(f.title)
    )
  }

  def clearButton() = {
    val p = this.props
    <.button(
      ^.className := "clear-completed",
      ^.onClick --> p.onClearCompleted,
      ^.style := Style(
        p.completedCount == 0 ?= "visibility" -> "hidden"
      )
    )("Clear completed")
  }

  def render() = {
    val p = this.props

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
object Footer extends Footer
