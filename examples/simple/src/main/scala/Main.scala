package eldis.react.examples.simple

import scalajs.js
import eldis.react._
import org.scalajs.dom

object Main extends js.JSApp {

  def main(): Unit = {
    ReactDOM.render(
      React.createElement(
        "p",
        js.undefined.asInstanceOf[js.Object],
        "Hello, world!"
      ),
      dom.document.getElementById("root")
    )
  }

}
