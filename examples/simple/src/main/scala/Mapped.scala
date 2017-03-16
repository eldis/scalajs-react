package eldis.react.examples.simple

import eldis.react.Wrapped
import eldis.react.{ FunctionalComponent, NativeFunctionalComponent, MapProps, NativeComponentType }
import scalajs.js
import js.annotation.ScalaJSDefined

import eldis.react.ComponentBase._
import eldis.react.Identity

object Mapped {

  case class ExampleProps(value: String)

  @ScalaJSDefined
  trait NativeExampleProps extends js.Object {
    val value: String
  }

  object NativeExampleProps {
    def apply(value0: String): NativeExampleProps =
      new NativeExampleProps {
        val value = value0
      }
  }

  val component: FunctionalComponent[ExampleProps] =
    MapProps(Stateless.scalaComponent).wrapped(
      (ex: ExampleProps) => Stateless.ScalaProps(ex.value)
    )

  val native: FunctionalComponent[ExampleProps] =
    MapProps(Stateless.nativeComponent).wrapped(
      (ex: ExampleProps) => Stateless.NativeProps(ex.value)
    )

  val withoutWrapping: NativeFunctionalComponent[NativeExampleProps] =
    MapProps(Stateless.scalaComponent).native(
      (ex: NativeExampleProps) => Stateless.ScalaProps(ex.value)
    )

  val withChildren: FunctionalComponent.WithChildren[ExampleProps] =
    MapProps(Stateless.scalaComponentWithChildren).wrappedWithChildren(
      (ex: ExampleProps) => Stateless.ScalaProps(ex.value)
    )
}
