package eldis.react.examples.simple

import scalajs.js
import eldis.react._
import org.scalajs.dom

object Main extends js.JSApp {

  def render(): ReactNode = {
    React.createElement(
      "div", js.undefined,
      React.createElement("p", js.undefined, "Create stateless element:"),
      Stateless.simpleComponent(Wrapped(Stateless.SimpleComponentProp("Hello from simple component"))),
      Stateless.simpleComponentWithChildren(
        Wrapped(Stateless.SimpleComponentProp("Hello from simple component with children")),
        React.createElement("p", js.undefined, "Hi, I'm child")
      ),
      React.createElement("p", js.undefined, "And now create stateful component"),
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
