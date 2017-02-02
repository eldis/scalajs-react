package eldis

import scala.reflect.ClassTag
import scalajs.js
import js.annotation._
import js.|
import org.scalajs.{ dom => jsdom }

package object react extends ComponentWrappers {

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

    def createElement[P <: js.Any](
      f: FunctionalComponent[P],
      props: P
    ): ReactDOMElement = JSReact.createElement(f, props)

    def createElement[P <: js.Any](
      f: FunctionalComponent.WithChildren[P],
      props: P,
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(f, props, children: _*)

    def createElement[P <: js.Any, F[_], C <: ComponentBase[F, P]](
      tag: js.ConstructorTag[C],
      props: P,
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(tag.constructor, props, children: _*)

    def createElement[F[_], C <: ComponentBase[F, Nothing]](
      tag: js.ConstructorTag[C],
      children: ReactNode*
    ): ReactDOMElement = JSReact.createElement(tag.constructor, js.undefined, children: _*)

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
    val get: T = js.native
    val key: js.UndefOr[String] = js.native
  }

  object Wrapped {
    def apply[T](value: T, key: js.UndefOr[String] = js.undefined): Wrapped[T] =
      js.Dynamic.literal(
        get = value.asInstanceOf[js.Any],
        key = key
      ).asInstanceOf[Wrapped[T]]
  }

  type NativeComponent[P <: js.Any] = ComponentBase[Identity, P]
  type Component[P] = ComponentBase[Wrapped, P]

}
