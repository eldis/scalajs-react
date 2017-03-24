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
 *
 * CH is generally assumed to either be a Unit or Seq[ReactNode], though
 * other types are also an option (but not supported by default implicits)
 */
final case class ElementBuilder[C, P, CH](
  component: C,
  props: P,
  children: CH = ()
)

object ElementBuilder {

  implicit def unitBuilderToReactNode[C, P, F[_], FP](
    self: ElementBuilder[C, P, Unit]
  )(
    implicit
    ev: C => NativeComponentType[FP],
    unapply: UnapplyConstructor.Aux[FP, F, P],
    wrapper: Wrapper[F, P]
  ): ReactDOMElement =
    React.createElement(ev(self.component), self.props)

  implicit def childrenBuilderToReactNode[C, P, F[_], FP](
    self: ElementBuilder[C, P, Seq[ReactNode]]
  )(
    implicit
    ev: C => NativeComponentType.WithChildren[FP],
    unapply: UnapplyConstructor.Aux[FP, F, P],
    wrapper: Wrapper[F, P]
  ): ReactDOMElement =
    React.createElement(ev(self.component), self.props, self.children)

  implicit class Ops[C, P, CH](val self: ElementBuilder[C, P, CH]) extends AnyVal {

    def apply(
      children: ReactNode*
    )(implicit ev: CH =:= Unit): ElementBuilder[C, P, Seq[ReactNode]] =
      // We only transform builder to ReactNode implicitly!
      ElementBuilder(self.component, self.props, children)

    def mapProps[X](f: P => X): ElementBuilder[C, X, CH] =
      ElementBuilder(self.component, f(self.props), self.children)
  }
}
