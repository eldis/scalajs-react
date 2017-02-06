package eldis.react.examples.compat

import scalajs.js
import js.JSApp
import eldis.react._
import compat._
import vdom.prefix_<^._
import org.scalajs.dom

object Main extends JSApp {

  val fc = FunctionalComponent[String]("EldisFC") { message =>
    <.p()(message)
  }

  lazy val app =
    <.div()(
      <.h4()("Japgolly scala.js components:"),
      JapgollyComponents.functionalComponent("Hello from japgolly's scalajs-react"),
      JapgollyComponents.component(
        "Hello from japgolly's scalajs-react",
        fc("Hello from eldis/scalajs-react")
      )
    )

  def main() {
    compat.setupReactGlobals()
    ReactDOM.render(
      app,
      dom.document.getElementById("root")
    )
  }

}
