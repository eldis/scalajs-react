package eldis

import scala.reflect.ClassTag
import scalajs.js
import js.annotation._
import js.|
import org.scalajs.{ dom => jsdom }

package object react extends PropsImplicits {

  @js.native
  trait ReactNode extends js.Object

  val EmptyNode = js.undefined.asInstanceOf[ReactNode]

  @js.native
  trait ReactElement extends js.Object with ReactNode {
    def key: js.UndefOr[String] = js.native
    def ref: js.UndefOr[String] = js.native
  }

  /** A React virtual DOM element, such as 'div', 'table', etc. */
  @js.native
  trait ReactDOMElement extends ReactElement {
    def `type`: String = js.native
    def props: js.UndefOr[js.Object] = js.native
  }

  type PropsChildren = js.Array[ReactNode]

  @js.native
  trait JSReact extends js.Object {

    def createElement[P](
      el: String | js.Any,
      props: js.UndefOr[P],
      children: ReactNode*
    ): ReactDOMElement = js.native
  }

  @JSImport("react", JSImport.Namespace)
  @js.native
  object JSReact extends JSReact

  /** Native component type */
  @js.native
  trait JSComponent[P <: js.Any] extends js.Object

  object React {

    def createElement(
      tag: String,
      props: js.UndefOr[js.Object],
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(tag, props, children: _*)

    def createElement(
      tag: String,
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(tag, js.undefined, children: _*)

    def createElement[P](
      f: FunctionalComponent[P],
      props: P
    ): ReactDOMElement = JSReact.createElement(f, Wrapped(props))

    def createElement[P](
      f: FunctionalComponent.WithChildren[P],
      props: P,
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(f, Wrapped(props), children: _*)

    def createElement[P <: js.Any](
      f: NativeFunctionalComponent[P],
      props: P
    ): ReactDOMElement = JSReact.createElement(f, props)

    def createElement[P <: js.Any](
      f: NativeFunctionalComponent.WithChildren[P],
      props: P,
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(f, props, children: _*)

    def createElement[P: WrapToNative, F[_]: UnwrapNative, C <: ComponentBase[F, P]](
      tag: js.ConstructorTag[C],
      props: P,
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(tag.constructor, implicitly[WrapToNative[P]].wrap(props), children: _*)

    def createElement[F[_], C <: ComponentBase[F, Nothing]](
      tag: js.ConstructorTag[C],
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(tag.constructor, js.undefined, children: _*)

    def createElement[P <: js.Any](
      c: JSComponent[P],
      props: P,
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(c, props, children: _*)

  }

  @js.native
  trait ReactDOM extends js.Object {

    def render[N <: ReactNode](
      node: N,
      container: jsdom.Node,
      callback: js.UndefOr[js.ThisFunction] = js.undefined
    ): Ref[N] = js.native

  }

  @JSImport("react-dom", JSImport.Namespace)
  @js.native
  object ReactDOM extends ReactDOM

  @js.native
  trait ReactDOMServer extends js.Object {
    def renderToString[N <: ReactNode](node: N): String = js.native
    def renderToStaticMarkup[N <: ReactNode](node: N): String = js.native
  }

  @JSImport("react-dom/server", JSImport.Namespace)
  @js.native
  object ReactDOMServer extends ReactDOMServer

  // Implicits
  @inline implicit def reactNodeFromString(s: String) = s.asInstanceOf[ReactNode]
  @inline implicit def reactNodeFromNodeArray(a: js.Array[ReactNode]) = a.asInstanceOf[ReactNode]

  // Wrapper object for scalajs properties
  @js.native
  trait Wrapped[T] extends js.Any {
    @JSName("sjs_p")
    val get: T = js.native
    val key: js.UndefOr[js.Any] = js.native
  }

  object Wrapped {
    def apply[T](value: T, key: js.UndefOr[js.Any] = js.undefined): Wrapped[T] =
      js.Dynamic.literal(
        sjs_p = value.asInstanceOf[js.Any],
        key = key
      ).asInstanceOf[Wrapped[T]]
  }

  type NativeComponent[P <: js.Any] = ComponentBase[Identity, P]
  @ScalaJSDefined
  abstract class Component[P](name: String) extends ComponentBase[Wrapped, P](name) {

    type This = Component[P]

    var key: js.UndefOr[js.Any] = js.undefined

    def withKey(key: js.Any): This = {
      this.key = key
      this
    }

    @JSName("createElement")
    override def apply(p: Props, children: ReactNode*): ReactDOMElement = {
      val c = this.asInstanceOf[js.Dynamic].constructor
      var props = implicitly[WrapToNative[P]].wrap(p).asInstanceOf[js.Dynamic]
      this.key.map(props.key = _)
      JSReact.createElement(c, props, children: _*)
    }

    @JSName("createElementNoProps")
    override def apply(children: ReactNode*): ReactDOMElement = {
      val c = this.asInstanceOf[js.Dynamic].constructor
      var props = js.Dynamic.literal(key = key)
      JSReact.createElement(c, props, children: _*)
    }

  }

}
