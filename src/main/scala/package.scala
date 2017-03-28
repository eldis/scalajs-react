package eldis

import scala.reflect.ClassTag
import scalajs.js
import scalajs.js.JSConverters.genTravConvertible2JSRichGenTrav

import js.annotation._
import js.|
import org.scalajs.{ dom => jsdom }
import eldis.react.util.{ ComponentLike, UnapplyConstructor }

package object react extends PropsImplicits {

  import scala.language.implicitConversions

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
      children: Seq[ReactNode]
    ): ReactDOMElement = JSReact.createElement(tag, props, children: _*)

    def createElement(
      tag: String,
      children: Seq[ReactNode]
    ): ReactDOMElement = JSReact.createElement(tag, js.undefined, children: _*)

    private def impl[P](
      c: NativeComponentType[P],
      props: P
    )(ev: P <:< js.Any): ReactDOMElement =
      JSReact.createElement(c, props)

    private def impl[P](
      c: NativeComponentType.WithChildren[P],
      props: P,
      children: Seq[ReactNode]
    )(ev: P <:< js.Any): ReactDOMElement =
      JSReact.createElement(c, props, children: _*)

    def createElement[P, F[_], FP](
      c: NativeComponentType[FP],
      props: P
    )(implicit
      unapply: UnapplyConstructor.Aux[FP, F, P],
      wrapper: Wrapper[F, P]): ReactDOMElement =
      impl[F[P]](unapply.inConstructor(c), wrapper.wrap(props))(wrapper.evidence)

    def createElement[P, F[_], FP](
      c: NativeComponentType.WithChildren[FP],
      props: P,
      children: Seq[ReactNode]
    )(implicit
      unapply: UnapplyConstructor.Aux[FP, F, P],
      wrapper: Wrapper[F, P]): ReactDOMElement =
      impl[F[P]](unapply.inConstructor(c), wrapper.wrap(props), children)(wrapper.evidence)

    // TODO: this is ugly - remove
    def createElement(
      c: NativeComponentType[Nothing]
    ): ReactDOMElement =
      JSReact.createElement(c, js.undefined)

    def createElement(
      c: NativeComponentType.WithChildren[Nothing],
      children: Seq[ReactNode]
    ): ReactDOMElement =
      JSReact.createElement(c, js.undefined, children: _*)
  }

  @js.native
  trait ReactDOM extends js.Object {

    def render(
      node: ReactNode,
      container: jsdom.Node,
      callback: js.UndefOr[js.ThisFunction] = js.undefined
    ): Unit = js.native

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
  @inline implicit def reactNodeFromNodeSeq(a: Seq[ReactNode]): ReactNode = a.toJSArray

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
      var props = Wrapper[Wrapped, P].wrap(p).asInstanceOf[js.Dynamic]
      this.key.foreach(props.key = _)

      val c: NativeComponentType.WithChildren[Wrapped[P]] = this
      React.createElement(c, props.asInstanceOf[Wrapped[Props]], children)
    }

    @JSName("createElementNoProps")
    override def apply(children: ReactNode*): ReactDOMElement = {
      // TODO: This is ugly and unsafe - switch to React.createElement
      val c = this.asInstanceOf[js.Dynamic].constructor
      val props = js.Dynamic.literal(key = key)
      JSReact.createElement(c, props, children: _*)
    }

  }

}
