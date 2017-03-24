package eldis.react.util

import language.{ implicitConversions, higherKinds }

import eldis.react._

/**
 * An utility ADT to support extensible component DSLs.
 *
 * Currently only required for better syntax with
 * scalajs-react-redux-form and scalajs-react-mdl.
 *
 * C and P can be non-unique (e.g. if C is a FunctionalComponent) - if
 * this becomes an issue, use a tagged type as C.
 */
final case class ElementBuilder[C, P](
  component: C,
  props: P
)

object ElementBuilder {

  implicit def builderToReactNode[C, P, F[_], FP](self: ElementBuilder[C, P])(
    implicit
    ev: C => NativeComponentType[FP],
    unapply: UnapplyConstructor.Aux[FP, F, P],
    wrapper: Wrapper[F, P]
  ): ReactDOMElement =
    React.createElement(ev(self.component), self.props)

  implicit class Ops[C, P](val self: ElementBuilder[C, P]) extends AnyVal {

    def apply[F[_], FP](
      children: ReactNode*
    )(implicit
      ev: C => NativeComponentType.WithChildren[FP],
      unapply: UnapplyConstructor.Aux[FP, F, P],
      wrapper: Wrapper[F, P]): ReactDOMElement =
      React.createElement(ev(self.component), self.props, children)

    def map[X](f: P => X): ElementBuilder[C, X] =
      ElementBuilder(self.component, f(self.props))
  }
}
