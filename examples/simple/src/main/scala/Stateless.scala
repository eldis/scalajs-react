package eldis.react.examples.simple

import eldis.react._
import vdom.prefix_<^._
import scalajs.js
import js.annotation.JSName

object Stateless {

  case class SimpleComponentProp(
    message: String
  )

  val simpleComponent = FunctionalComponent[Wrapped[SimpleComponentProp]]("simpleComponent") { p =>
    <.p()(p.get.message)
  }

  val simpleComponentWithChildren = FunctionalComponent.withChildren("simpleComponentWithChildren") { (p: Wrapped[SimpleComponentProp], ch: PropsChildren) =>
    <.div()(
      simpleComponent(p),
      ch
    )
  }

}
