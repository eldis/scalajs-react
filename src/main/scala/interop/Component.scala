package eldis.react.interop

import scalajs.js
import js.annotation._

@JSImport("react", "Component")
@js.native
abstract class Component[Props <: js.Any] extends js.Object {

  type State

  def state: State = js.native

  def setState(s: State): Unit = js.native

  val render: js.Function0[ReactNode]

}
