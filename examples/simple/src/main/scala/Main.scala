package eldis.react.examples.simple

import scalajs.js
import eldis.react.interop._
import org.scalajs.dom

object Main extends js.JSApp {

  def render(): ReactNode = {
    React.createElement(
      "div", js.undefined,
      React.createElement("p", js.undefined, "Create stateless element:"),
      React.createElement(
        Stateless.simpleComponent,
        Stateless.SimpleComponentProp("Hello from simple component")
      ),
      React.createElement(
        Stateless.simpleComponentWithChildren,
        Stateless.SimpleComponentProp("Hello from simple component with children"),
        React.createElement("p", js.undefined, "Hi, I'm child")
      )
    )
  }

  def main(): Unit = {

    ReactDOM.render(
      render(),
      dom.document.getElementById("root")
    )
  }

}
