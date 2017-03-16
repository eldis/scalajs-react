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
trait NativeComponentType[P] extends js.Any

object NativeComponentType {
  /**
   * A value that can be used as a first parameter of [[JSReact.createElement]].
   *
   * The contract is as follows: any value implementing this trait
   * can be used as a first argument in [[JSReact.createElement]] with
   * props of type `P` and some children
   */
  @js.native
  trait WithChildren[P] extends NativeComponentType[P]

  object WithChildren {
    implicit class Ops[A](val self: WithChildren[A]) extends AnyVal {
      def narrow[B](implicit ev: B <:< A): WithChildren[B] =
        self.asInstanceOf[WithChildren[B]]
    }
  }

  implicit class Ops[A](val self: NativeComponentType[A]) extends AnyVal {
    def narrow[B](implicit ev: B <:< A): NativeComponentType[B] =
      self.asInstanceOf[NativeComponentType[B]]
  }
}
