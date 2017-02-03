package eldis.react.examples.simple

import eldis.react._
import vdom.prefix_<^._
import scalajs.js
import js.annotation.JSName

object Stateless {

  @js.native
  trait NativeProps extends js.Object {
    val message: String = js.native
    val key: js.UndefOr[js.Any] = js.native
  }

  object NativeProps {
    def apply(message: String, key: js.UndefOr[String] = js.undefined): NativeProps =
      js.Dynamic.literal(
        message = message,
        key = key
      ).asInstanceOf[NativeProps]
  }
  val nativeComponent = NativeFunctionalComponent("Stateless.native") { p: NativeProps =>
    <.p()(p.message)
  }

  val nativeComponentWithChildren = NativeFunctionalComponent.withChildren("Stateless.nativeWithChildren") { (p: NativeProps, ch: PropsChildren) =>
    <.div()(
      nativeComponent(p),
      ch
    )
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
