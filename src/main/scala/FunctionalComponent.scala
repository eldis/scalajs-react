package eldis.react

import scalajs.js

@js.native
trait FunctionalComponent[-P] extends js.Any

object FunctionalComponent {

  def apply[P](name: String)(f: Function1[P, ReactNode]): FunctionalComponent[P] = {
    var jf = ((p: Wrapped[P]) => f(p.get)): js.Function1[Wrapped[P], ReactNode]
    jf.asInstanceOf[js.Dynamic].displayName = name
    jf.asInstanceOf[FunctionalComponent[P]]
  }

  def apply[P](f: Function1[P, ReactNode]): FunctionalComponent[P] =
    apply("FunctionalComponent")(f)

  @inline implicit class FunctionalComponentOps[P](private val f: FunctionalComponent[P]) {
    def apply(p: P) = JSReact.createElement(f, Wrapped(p))
  }

  @js.native
  trait WithChildren[-P] extends js.Any

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

  @inline implicit class FunctionalComponentWithChildrenOps[P](private val f: WithChildren[P]) {
    def apply(p: P, ch: ReactNode*) = JSReact.createElement(f, Wrapped(p), ch: _*)
  }

}

@js.native
trait NativeFunctionalComponent[-P <: js.Any] extends js.Any

object NativeFunctionalComponent {
  def apply[P <: js.Any](name: String)(f: Function1[P, ReactNode]): NativeFunctionalComponent[P] = {
    var jf = f: js.Function1[P, ReactNode]
    jf.asInstanceOf[js.Dynamic].displayName = name
    jf.asInstanceOf[NativeFunctionalComponent[P]]
  }

  def apply[P <: js.Any](f: Function1[P, ReactNode]): NativeFunctionalComponent[P] =
    apply("NativeFunctionalComponent")(f)

  @inline implicit class NativeFunctionalComponentOps[P <: js.Any](private val f: NativeFunctionalComponent[P]) {
    def apply(p: P) = JSReact.createElement(f, p)
  }

  @js.native
  trait WithChildren[-P] extends js.Any

  def withChildren[P <: js.Any](name: String)(f: Function2[P, PropsChildren, ReactNode]): WithChildren[P] = {
    var proxy = ((p: P) => {
      val children = p.asInstanceOf[js.Dynamic].children.asInstanceOf[PropsChildren]
      f(p, if (js.Array.isArray(children)) children else js.Array(children.asInstanceOf[ReactNode]))
    }): js.Function1[P, ReactNode]
    proxy.asInstanceOf[js.Dynamic].displayName = name
    proxy.asInstanceOf[WithChildren[P]]
  }

  def withChildren[P <: js.Any](f: Function2[P, PropsChildren, ReactNode]): WithChildren[P] =
    withChildren("NativeFunctionalComponent.withChildren")(f)

  @inline implicit class NativeFunctionalComponentWithChildrenOps[P <: js.Any](private val f: WithChildren[P]) {
    def apply(p: P, ch: ReactNode*) = JSReact.createElement(f, p, ch: _*)
  }
}
