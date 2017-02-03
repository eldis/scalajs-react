package eldis.react.examples.simple

import scalajs.js
import eldis.react._
import vdom.prefix_<^._
import org.scalajs.dom

object Main extends js.JSApp {

  def render(): ReactNode = {
    <.div()(
      <.h4()("Create stateless elements:"),
      Stateless.nativeComponent(Stateless.NativeProps("Hello from native stateless component")),
      Stateless.nativeComponentWithChildren(
        Stateless.NativeProps("Hello from native stateless component with children"),
        <.p()("*** Hi, I'm child")
      ),
      Stateless.scalaComponent(Stateless.ScalaProps("Hello from scala stateless component")),
      Stateless.scalaComponentWithChildren(
        Stateless.ScalaProps("Hello from scala stateless component with children"),
        <.p()("*** Hi, I'm child")
      ),
      <.h4()("And now create stateful elements:"),
      Stateful.NativeComponent(
        Stateful.NativeProps("Hello from native stateful component"),
        <.p()("*** Hi, I'm the child of native statfull component")
      ),
      Stateful.ScalaComponent(
        Stateful.ScalaProps("Hello from scala stateful component with properties"),
        <.p()("*** Hi, I'm the child of scala statfull component")
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
