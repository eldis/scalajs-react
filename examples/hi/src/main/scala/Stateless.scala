package eldis.react.examples.simple

import scalajs.js
import io.circe.generic.auto._

import eldis.react._
import eldis.react.{ interop => I }

object Stateless {

  case class Message(message: String)

  val simpleComponent = FunctionalComponent[Message](
    "simpleComponent",
    m => I.React.createElement("p", js.undefined, m.message)
  )

  val simpleComponentWithChildren = FunctionalComponent.withChildren[Message](
    "simpleComponentWithChildren",
    (m, ch) => {
      I.React.createElement(
        "div",
        js.undefined,
        simpleComponent(m),
        ch
      )
    }
  )

}
