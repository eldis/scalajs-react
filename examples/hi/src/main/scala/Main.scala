package eldis.react.examples.simple

import scalajs.js
import org.scalajs.dom
import io.circe.generic.auto._

import eldis.react._
import eldis.react.{ interop => I }

object Main extends js.JSApp {

  def render(): ReactNode = {
    I.React.createElement(
      "div", js.undefined,
      I.React.createElement("p", js.undefined, "Create stateless element:"),
      Stateless.simpleComponent(Stateless.Message("Hello from simple component")),
      Stateless.simpleComponentWithChildren(
        Stateless.Message("Hello from simple component with children"),
        I.React.createElement("p", js.undefined, "Hi, I'm child")
      )
    )
  }

  def main(): Unit = {
    import io.circe.scalajs._
    import io.circe.generic.auto._
    case class Message(m: String)
    val m = Message("1234")
    val encoded = m.asJsAny
    val decoded = decodeJs[Message](encoded)
    println("message: " + m)
    println("encoded: " + encoded)
    println("decoded: " + decoded)
    println("equal: " + decoded == Right(m))

    I.ReactDOM.render(
      render(),
      dom.document.getElementById("root")
    )
  }

}
