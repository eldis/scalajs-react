package eldis.react

import scalajs.js

@js.native
trait FunctionalComponent[P] extends js.Any

object FunctionalComponent {

  def apply[P](name: String)(f: Function1[P, ReactNode]): FunctionalComponent[P] = {
    var jf = ((p: Wrapped[P]) => f(p.get)): js.Function1[Wrapped[P], ReactNode]
    jf.asInstanceOf[js.Dynamic].displayName = name
    jf.asInstanceOf[FunctionalComponent[P]]
  }

  def apply[P](f: Function1[P, ReactNode]): FunctionalComponent[P] =
    apply("FunctionalComponent")(f)

  @inline implicit class FunctionalComponentOps[P](private val f: FunctionalComponent[P]) {
    def withKey(key: js.Any)(p: P) =
      React.createElement[Wrapped[P], Identity](f, Wrapped(p, key))
    def apply(p: P) = React.createElement(f, p)
  }

  @js.native
  trait WithChildren[P] extends js.Any

  def withChildren[P](name: String)(f: Function2[P, PropsChildren, ReactNode]): WithChildren[P] = {
    var proxy = ((p: Wrapped[P]) => {
      val children = p.asInstanceOf[js.Dynamic].children.asInstanceOf[PropsChildren]
      f(p.get, if (js.Array.isArray(children)) children else js.Array(children.asInstanceOf[ReactNode]))
    }): js.Function1[Wrapped[P], ReactNode]
    proxy.asInstanceOf[js.Dynamic].displayName = name
    proxy.asInstanceOf[WithChildren[P]]
  }

  def withChildren[P](f: Function2[P, PropsChildren, ReactNode]): WithChildren[P] =
    withChildren("FunctionalComponent.withChildren")(f)

  import scala.language.implicitConversions

  @inline
  implicit def functionalComponentIsNativeComponentType[P](
    c: FunctionalComponent[P]
  ): NativeComponentType[Wrapped[P]] =
    c.asInstanceOf[NativeComponentType[Wrapped[P]]]

  @inline
  implicit def functionalComponentWithChildrenIsNativeComponentTypeWithChildren[P](
    c: FunctionalComponent.WithChildren[P]
  ): NativeComponentType.WithChildren[Wrapped[P]] =
    c.asInstanceOf[NativeComponentType.WithChildren[Wrapped[P]]]

  @inline implicit class FunctionalComponentWithChildrenOps[P](private val f: WithChildren[P]) {
    def withKey(key: js.Any)(p: P, ch: ReactNode*) =
      React.createElement[Wrapped[P], Identity](f, Wrapped(p, key), ch)
    def apply(p: P, ch: ReactNode*) = React.createElement(f, p, ch)
  }

}

@js.native
trait NativeFunctionalComponent[P] extends js.Any

object NativeFunctionalComponent {
  def apply[P](name: String)(f: Function1[P, ReactNode])(implicit ev: P <:< js.Any): NativeFunctionalComponent[P] = {
    var jf = f: js.Function1[P, ReactNode]
    jf.asInstanceOf[js.Dynamic].displayName = name
    jf.asInstanceOf[NativeFunctionalComponent[P]]
  }

  def apply[P](f: Function1[P, ReactNode])(implicit ev: P <:< js.Any): NativeFunctionalComponent[P] =
    apply("NativeFunctionalComponent")(f)

  @inline implicit class NativeFunctionalComponentOps[P <: js.Any](private val f: NativeFunctionalComponent[P]) {
    def apply(p: P) = React.createElement[P, Identity](f, p)
  }

  @js.native
  trait WithChildren[P] extends js.Any

  def withChildren[P](name: String)(f: Function2[P, PropsChildren, ReactNode])(implicit ev: P <:< js.Any): WithChildren[P] = {
    var proxy = ((p: P) => {
      val children = p.asInstanceOf[js.Dynamic].children.asInstanceOf[PropsChildren]
      f(p, if (js.Array.isArray(children)) children else js.Array(children.asInstanceOf[ReactNode]))
    }): js.Function1[P, ReactNode]
    proxy.asInstanceOf[js.Dynamic].displayName = name
    proxy.asInstanceOf[WithChildren[P]]
  }

  def withChildren[P](f: Function2[P, PropsChildren, ReactNode])(implicit ev: P <:< js.Any): WithChildren[P] =
    withChildren("NativeFunctionalComponent.withChildren")(f)

  import scala.language.implicitConversions

  @inline
  implicit def nativeFunctionalComponentIsNativeComponentType[P <: js.Any](
    c: NativeFunctionalComponent[P]
  ): NativeComponentType[Identity[P]] =
    c.asInstanceOf[NativeComponentType[P]]

  @inline
  implicit def nativeFunctionalComponentWithChildrenIsNativeComponentTypeWithChildren[P <: js.Any](
    c: NativeFunctionalComponent.WithChildren[P]
  ): NativeComponentType.WithChildren[P] =
    c.asInstanceOf[NativeComponentType.WithChildren[P]]

  @inline implicit class NativeFunctionalComponentWithChildrenOps[P <: js.Any](private val f: WithChildren[P]) {
    def apply(p: P, ch: ReactNode*) = React.createElement[P, Identity](f, p, ch)
  }
}
