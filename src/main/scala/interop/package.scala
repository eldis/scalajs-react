package eldis.react

import scalajs.js
import js.annotation._
import org.scalajs.dom

package object interop {

  @js.native
  trait ReactNode extends js.Object

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

  //TODO: Fix the type
  type Ref[C <: ReactNode] = js.Any

  @js.native
  trait React extends js.Object {

    def createElement(
      tag: String,
      props: js.UndefOr[js.Object],
      children: ReactNode*
    ): ReactDOMElement = js.native

    def createElement[P <: js.Any](
      f: FunctionalComponent[P],
      props: P
    ): ReactDOMElement = js.native

    def createElement[P <: js.Any](
      f: FunctionalComponent.WithChildren[P],
      props: P,
      children: ReactNode*
    ): ReactDOMElement = js.native

    def createElement[P <: js.Any](
      c: js.Dynamic,
      props: P,
      children: ReactNode*
    ): ReactDOMElement = js.native

  }

  @JSImport("react", JSImport.Namespace)
  @js.native
  object React extends React

  @js.native
  trait ReactDOM extends js.Object {

    def render[N <: ReactNode](
      node: N,
      container: dom.Node,
      callback: js.UndefOr[js.ThisFunction] = js.undefined
    ): Ref[N] = js.native

  }

  @JSImport("react-dom", JSImport.Namespace)
  @js.native
  object ReactDOM extends ReactDOM

  // Implicits
  @inline implicit def reactNodeFromString(s: String) = s.asInstanceOf[ReactNode]
  @inline implicit def reactNodeFromNodeArray(a: js.Array[ReactNode]) = a.asInstanceOf[ReactNode]

}
