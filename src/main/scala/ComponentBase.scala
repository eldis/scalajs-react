package eldis.react

import scala.language.higherKinds

import scala.scalajs.js
import js.annotation._

@JSImport("react", "Component")
@js.native
abstract class RawComponent extends js.Any {

  type State

  @JSName("props")
  def propsNative: js.Any = js.native

  @JSName("state")
  protected def stateRaw: Wrapped[State] = js.native

  @JSName("setState")
  def setStateRaw(s: Wrapped[State]): Unit = js.native

  def initialState: State

  def render(): ReactNode

  protected def componentWillUpdate(nextProps: js.Any, nextState: Wrapped[State]): Unit = js.native
  protected def componentDidUpdate(prevProps: js.Any, prevState: Wrapped[State]): Unit = js.native
  protected def componentWillMount(): Unit = js.native
  protected def componentDidMount(): Unit = js.native
  protected def componentWillUnmount(): Unit = js.native
}

@ScalaJSDefined
abstract class ComponentBase[F[_], P](
    implicit
    wrapper: Wrapper[F, P]
) extends RawComponent {

  type Props = P
  type WrappedProps = js.Any with F[P]

  var stateInitialized = false

  @JSName("propsImpl")
  def props: Props = wrapper.unwrap(propsNative.asInstanceOf[F[P]])

  def propsChildren: js.Array[ReactNode] = {
    //TODO: cache it and invalidate when receive new props
    val ch = Option(propsNative.asInstanceOf[js.Dynamic])
      .flatMap(p => Option(p.children.asInstanceOf[js.Any]))
      .map(ch => (if (ch.isInstanceOf[js.Array[_]]) ch.asInstanceOf[js.Array[ReactNode]] else js.Array(ch.asInstanceOf[ReactNode])))
    ch.getOrElse(js.Array[ReactNode]())
  }

  def this(name: String)(
    implicit
    wrapper: Wrapper[F, P]
  ) {
    this()(wrapper)
    this.asInstanceOf[js.Dynamic].constructor.displayName = name
  }

  @JSName("createElement")
  def apply(p: Props, children: ReactNode*): ReactDOMElement = {

    val props: js.Any with F[P] = wrapper.wrap(p)
    React.createElement(ComponentBase.componentBaseIsNativeComponentTypeWithChildren(this), props, children)
  }

  @JSName("createElementNoProps")
  def apply(children: ReactNode*): ReactDOMElement = {
    // TODO: This is unsafe and ugly
    val c = this.asInstanceOf[js.Dynamic].constructor
    JSReact.createElement(c, (), children: _*)
  }

  @JSName("stateImpl")
  def state: State = {
    stateRaw.get
  }

  @JSName("setStateImpl")
  def setState(s: State): Unit = {
    setStateRaw(Wrapped(s))
  }

  @JSName("componentWillUpdate")
  override protected def componentWillUpdate(nextProps: js.Any, nextState: Wrapped[State]): Unit = {
    willUpdate(wrapper.unwrap(nextProps.asInstanceOf[F[P]]), nextState.get)
  }

  @JSName("componentDidUpdate")
  override protected def componentDidUpdate(prevProps: js.Any, prevState: Wrapped[State]): Unit = {
    didUpdate(wrapper.unwrap(prevProps.asInstanceOf[F[P]]), prevState.get)
  }

  @JSName("componentWillMount")
  override protected def componentWillMount(): Unit = {
    if (stateInitialized == false) {
      val s = Wrapped(initialState)
      setStateRaw(s)
      stateInitialized = true
    }
    willMount()
  }

  @JSName("componentDidMount")
  override protected def componentDidMount(): Unit = {
    didMount()
  }

  @JSName("componentWillUnmount")
  override protected def componentWillUnmount(): Unit = {
    willUnmount()
  }

  def willUpdate(nextProps: Props, nextState: State): Unit = {}
  def didUpdate(prevProps: Props, prevState: State): Unit = {}
  def willMount(): Unit = {}
  def didMount(): Unit = {}
  def willUnmount(): Unit = {}
}

object ComponentBase {

  import scala.language.implicitConversions

  @inline
  implicit def componentBaseIsNativeComponentTypeWithChildren[F[_], P](
    c: ComponentBase[F, P]
  // TODO: ComponentBase wrapping synchronization
  ): NativeComponentType.WithChildren[c.WrappedProps] =
    c.asInstanceOf[js.Dynamic].constructor
      .asInstanceOf[NativeComponentType.WithChildren[c.WrappedProps]]

  @inline
  implicit def componentBaseTagIsNativeComponentTypeWithChildren[F[_], P, C <: ComponentBase[F, P]](
    c: js.ConstructorTag[C]
  // TODO: ComponentBase wrapping synchronization
  ): NativeComponentType.WithChildren[C#WrappedProps] =
    c.constructor.asInstanceOf[NativeComponentType.WithChildren[C#WrappedProps]]
}
