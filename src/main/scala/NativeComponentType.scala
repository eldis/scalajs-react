package eldis.react

import scalajs.js

/**
 * A value that can be used as a first parameter of [[JSReact.createElement]].
 *
 * The contract is as follows: any value implementing this trait
 * can be used as a first argument in [[JSReact.createElement]] with
 * props of type `P` and no children.
 */
@js.native
trait NativeComponentType[-P <: js.Any] extends js.Any

object NativeComponentType {
  /**
   * A value that can be used as a first parameter of [[JSReact.createElement]].
   *
   * The contract is as follows: any value implementing this trait
   * can be used as a first argument in [[JSReact.createElement]] with
   * props of type `P` and some children
   */
  @js.native
  trait WithChildren[-P <: js.Any] extends NativeComponentType[P]
}
