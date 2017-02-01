package eldis

import scalajs.js
import io.circe.{ Decoder, Encoder }
import io.circe.scalajs._

import eldis.react.{ interop => I }

package object react {
  type ReactNode = I.ReactNode
  type ReactDOMElement = I.ReactDOMElement
  type Children = I.PropsChildren

  case class FunctionalComponent[A](
      name: String,
      f: A => ReactNode
  ) {
    def apply(a: A)(implicit E: Encoder[A], D: Decoder[A]) =
      React.createElement(this, a)
  }

  object FunctionalComponent {
    def withChildren[A](
      name: String,
      f: (A, Children) => ReactNode
    ): FunctionalComponentWithChildren[A] =
      FunctionalComponentWithChildren(name, f)
  }

  case class FunctionalComponentWithChildren[A](
      name: String,
      f: (A, Children) => ReactNode
  ) {
    def apply(a: A, ch: ReactNode*)(implicit E: Encoder[A], D: Decoder[A]) =
      React.createElement(this, a, ch: _*)
  }

  object React {
    def createElement[A: Decoder: Encoder](
      fc: FunctionalComponent[A],
      props: A
    ): ReactDOMElement = {
      // TODO: meaningful error handling
      val f: js.Any => ReactNode =
        j => fc.f(decodeJs[A](j).fold(throw _, identity))

      val fcJs = I.FunctionalComponent(fc.name)(f)
      I.React.createElement(fcJs, props.asJsAny)
    }

    def createElement[A: Decoder: Encoder](
      fc: FunctionalComponentWithChildren[A],
      props: A,
      children: ReactNode*
    ): ReactDOMElement = {
      // TODO: meaningful error handling
      val f: (js.Any, Children) => ReactNode =
        (j, children0) => fc.f(decodeJs[A](j).fold(throw _, identity), children0)

      val fcJs = I.FunctionalComponent.withChildren(fc.name)(f)
      I.React.createElement(fcJs, props.asJsAny, children: _*)
    }
  }
}
