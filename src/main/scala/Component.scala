package eldis.react

import scalajs.js
import js.annotation._

@JSImport("react", "Component")
@js.native
abstract class JSComponent[P <: js.Any] extends js.Object {

  type Props = P
  type State

  def props: Props = js.native
  @JSName("state")
  protected def stateRaw: Wrapped[State] = js.native

  @JSName("setState")
  def setStateRaw(s: Wrapped[State]): Unit = js.native

  def initialState: State = js.native
  def render(): ReactNode
  def componentDidMount(): Unit = js.native
  def componentWillUnmount(): Unit = js.native
  //TODO: Remove wrapper object
  def componentWillUpdate(nextProps: Props, nextState: Wrapped[State]): Unit = js.native
  //TODO: Remove wrapper object
  def componentDidUpdate(prevProps: Props, prevState: Wrapped[State]): Unit = js.native
}

@ScalaJSDefined
abstract class Component[P <: js.Any] extends JSComponent[P] {

  def this(name: String) {
    this()
    this.asInstanceOf[js.Dynamic].constructor.displayName = name
  }

  @JSName("stateImpl")
  def state: State = {
    if (stateRaw.asInstanceOf[js.Any] == null && this.asInstanceOf[js.Dynamic].initialState != null) {
      this.asInstanceOf[js.Dynamic].state = Wrapped(initialState.asInstanceOf[js.Any])
    }
    stateRaw.get
  }

  @JSName("setStateImpl")
  def setState(s: State): Unit = {
    setStateRaw(Wrapped(s))
  }

  @JSName("createElement")
  def apply(p: Props, children: ReactNode*): ReactDOMElement = {
    val c = this.asInstanceOf[js.Dynamic].constructor
    JSReact.createElement(c, p, children: _*)
  }

  @JSName("createElementNoProps")
  def apply(children: ReactNode*): ReactDOMElement = {
    val c = this.asInstanceOf[js.Dynamic].constructor
    JSReact.createElement(c, (), children: _*)
  }

}

