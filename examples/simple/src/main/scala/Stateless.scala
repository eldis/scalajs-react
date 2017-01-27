package eldis.react.examples.simple

import eldis.react.interop._
import scalajs.js
import js.annotation.JSName

object Stateless {

  @js.native
  trait SimpleComponentProp extends js.Object {
    val message: String
  }
  object SimpleComponentProp {
    def apply(message: String) =
      js.Dynamic.literal(message = message).asInstanceOf[SimpleComponentProp]
  }

  val simpleComponent = FunctionalComponent[SimpleComponentProp]("simpleComponent") { p =>
    React.createElement("p", js.undefined, p.message)
  }

  val simpleComponentWithChildren = FunctionalComponent.withChildren("simpleComponentWithChildren") { (p: SimpleComponentProp, ch: PropsChildren) =>
    React.createElement(
      "div",
      js.undefined,
      React.createElement(simpleComponent, p),
      ch
    )
  }

}
