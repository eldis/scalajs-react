package eldis.react.examples.todomvc

import scalajs.js
import js.JSApp
import org.scalajs.dom

import eldis.react._

object Main extends JSApp {

  def main(): Unit = {
    ReactDOM.render(
      TodoList(),
      dom.document.getElementsByClassName("todoapp")(0)
    )
  }

}
