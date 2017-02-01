package eldis.react.examples.simple

import eldis.react._
import scalajs.js
import js.annotation.JSName

object Stateless {

  case class SimpleComponentProp(
    message: String
  )

  val simpleComponent = FunctionalComponent[Wrapped[SimpleComponentProp]]("simpleComponent") { p =>
    React.createElement("p", js.undefined, p.get.message)
  }

  val simpleComponentWithChildren = FunctionalComponent.withChildren("simpleComponentWithChildren") { (p: Wrapped[SimpleComponentProp], ch: PropsChildren) =>
    React.createElement(
      "div",
      js.undefined,
      simpleComponent(p),
      ch
    )
  }

}
