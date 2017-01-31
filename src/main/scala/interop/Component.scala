package eldis.react.interop

import scalajs.js
import js.annotation._

@JSImport("react", "Component")
@js.native
abstract class JSComponent[Props <: js.Any] extends js.Object {

  type State

  def props: Props = js.native
  def state: State = js.native

  def setState(s: State): Unit = js.native

  def render(): ReactNode

}

@ScalaJSDefined
abstract class Component[Props <: js.Any] extends JSComponent[Props] {

  def this(name: String) {
    this()
    this.asInstanceOf[js.Dynamic].constructor.displayName = name
  }

  @JSName("createElement")
  def apply(p: Props, children: ReactNode*): ReactDOMElement = {
    val c = this.asInstanceOf[js.Dynamic].constructor
    JSReact.createElement(c, p, children: _*)
  }

}

