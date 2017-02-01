package eldis.react.examples.todomvc

import scalajs.js
import js.annotation._
import eldis.react._

case class FooterProps(
  currentFilter: TodoFilter,
  onChangeFilter: TodoFilter => Unit,
  onClearCompleted: () => Unit,
  activeCount: Int,
  completedCount: Int
)

@ScalaJSDefined
class Footer extends Component[Wrapped[FooterProps]]("Footer") {

  import js.Dynamic.{ literal => JSObj }

  def filterLink(f: TodoFilter) = {
    val p = this.props.get
    React.createElement(
      "li",
      React.createElement(
        "a",
        JSObj(
          className = if (p.currentFilter == f) "selected" else js.undefined,
          onClick = if (p.currentFilter != f) () => p.onChangeFilter(f) else js.undefined,
          href = "#"
        ),
        f.title
      )
    )
  }

  def clearButton() = {
    val p = this.props.get
    React.createElement(
      "button",
      JSObj(
        className = "clear-completed",
        onClick = p.onClearCompleted,
        style = JSObj(
          visibility = if (p.completedCount > 0) js.undefined else "hidden"
        )
      ),
      "Clear completed"
    )
  }

  def render() = {
    val p = this.props.get
    React.createElement(
      "footer",
      JSObj(className = "footer"),
      React.createElement(
        "span",
        JSObj(className = "todo-count"),
        React.createElement("strong", p.activeCount.toString),
        s" ${if (p.activeCount == 1) "item" else "items"} left"
      ),
      React.createElement(
        "ul",
        JSObj(className = "filters"),
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
