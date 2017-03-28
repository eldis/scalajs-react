package eldis.react.vdom

import scalajs.js
import org.scalajs.dom
import dom.html

/*
 Here we using the implementation of events from http://github.com/japgolly/scalajs-react,
 many thanks to him.
*/

trait Events {

  type ReactEvent = SyntheticEvent[dom.Node]
  type ReactKeyboardEvent = SyntheticKeyboardEvent[dom.Node]
  type ReactMouseEvent = SyntheticMouseEvent[dom.Node]

  type ReactEventI = SyntheticEvent[html.Input]
  type ReactKeyboardEventI = SyntheticKeyboardEvent[html.Input]
  type ReactMouseEventI = SyntheticMouseEvent[html.Input]

}

@js.native
trait SyntheticEvent[+DOMEventTarget <: dom.Node] extends js.Object {
  val bubbles: Boolean = js.native
  val cancelable: Boolean = js.native
  val currentTarget: DOMEventTarget = js.native
  def defaultPrevented: Boolean = js.native
  val eventPhase: Double = js.native
  val isTrusted: Boolean = js.native
  val nativeEvent: dom.Event = js.native
  val target: DOMEventTarget = js.native
  val timeStamp: js.Date = js.native
  def preventDefault(): Unit = js.native
  def stopPropagation(): Unit = js.native
  def isPropagationStopped(): Boolean = js.native
  val `type`: String = js.native
  def persist(): Unit = js.native
}

@js.native
trait SyntheticUIEvent[+DOMEventTarget <: dom.Node] extends SyntheticEvent[DOMEventTarget] {
  override val nativeEvent: dom.UIEvent = js.native
  val view: js.Object = js.native
  def detail: Double = js.native
}

@js.native
trait SyntheticKeyboardEvent[+DOMEventTarget <: dom.Node] extends SyntheticUIEvent[DOMEventTarget] {
  override val nativeEvent: dom.KeyboardEvent = js.native
  val location: Double = js.native
  val altKey: Boolean = js.native
  val ctrlKey: Boolean = js.native
  val metaKey: Boolean = js.native
  val shiftKey: Boolean = js.native
  val repeat: Boolean = js.native
  val locale: String = js.native
  def getModifierState(keyArg: String): Boolean = js.native
  val key: String = js.native
  val charCode: Int = js.native
  val keyCode: Int = js.native
  val which: Int = js.native
}

@js.native
trait SyntheticMouseEvent[+DOMEventTarget <: dom.Node] extends SyntheticUIEvent[DOMEventTarget] {
  override val nativeEvent: dom.MouseEvent = js.native
  val ctrlKey: Boolean = js.native
  val shiftKey: Boolean = js.native
  val altKey: Boolean = js.native
  val metaKey: Boolean = js.native
  val button: Short = js.native
  val buttons: Short = js.native
  val clientX: Double = js.native
  val clientY: Double = js.native
  val pageX: Double = js.native
  val pageY: Double = js.native
  val screenX: Double = js.native
  val screenY: Double = js.native
  val relatedTarget: DOMEventTarget = js.native
  def getModifierState(keyArg: String): Boolean = js.native
}
