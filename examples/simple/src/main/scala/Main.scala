package eldis.react.examples.simple

import scalajs.js
import eldis.react._
import vdom.prefix_<^._
import org.scalajs.dom

object Main extends js.JSApp {

  def render(): ReactNode = {
    <.div()(
      <.p()("Create stateless element:"),
      Stateless.nativeComponent(Stateless.NativeProps("Hello from native stateless component")),
      Stateless.scalaComponent(Stateless.ScalaProps("Hello from scala stateless component")),
      Stateless.scalaComponentWithChildren(
        Stateless.ScalaProps("Hello from scala stateless component with children"),
        <.p()("Hi, I'm child")
      ),
      <.p()("And now create stateful component"),
      Stateful.NativeComponent(Stateful.NativeProps("Hello from native stateful component")),
      Stateful.ScalaComponent(Stateful.ScalaProps("Hello from scala stateful component"))
    )
  }

  def main(): Unit = {

    ReactDOM.render(
      render(),
      dom.document.getElementById("root")
    )
  }

}
