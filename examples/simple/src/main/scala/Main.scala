package eldis.react.examples.simple

import scalajs.js
import eldis.react._
import vdom.prefix_<^._
import org.scalajs.dom

object Main extends js.JSApp {

  def render(): ReactNode = {
    <.div()(
      <.p()("Create stateless element:"),
      Stateless.simpleComponent(Wrapped(Stateless.SimpleComponentProp("Hello from simple component"))),
      Stateless.simpleComponentWithChildren(
        Wrapped(Stateless.SimpleComponentProp("Hello from simple component with children")),
        <.p()("Hi, I'm child")
      ),
      <.p()("And now create stateful component"),
      Stateful.SimpleComponent(Stateful.SimpleComponentProps("Hello from stateful component"))
    )
  }

  def main(): Unit = {

    ReactDOM.render(
      render(),
      dom.document.getElementById("root")
    )
  }

}
