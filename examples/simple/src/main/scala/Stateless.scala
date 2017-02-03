package eldis.react.examples.simple

import eldis.react._
import vdom.prefix_<^._
import scalajs.js
import js.annotation.JSName

object Stateless {

  @js.native
  trait NativeProps extends js.Object {
    val message: String = js.native
  }

  object NativeProps {
    def apply(message: String): NativeProps =
      js.Dynamic.literal(
        message = message
      ).asInstanceOf[NativeProps]
  }

  val nativeComponent = NativeFunctionalComponent("Stateless.native") { p: NativeProps =>
    <.p()(p.message)
  }

  case class ScalaProps(
    message: String
  )

  val scalaComponent = FunctionalComponent("Stateless.scala") { p: ScalaProps =>
    <.p()(p.message)
  }

  val scalaComponentWithChildren = FunctionalComponent.withChildren("Stateless.scalaWithChildren") { (p: ScalaProps, ch: PropsChildren) =>
    <.div()(
      scalaComponent(p),
      ch
    )
  }

}
