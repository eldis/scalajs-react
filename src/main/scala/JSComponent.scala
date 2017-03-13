package eldis.react

import scala.scalajs.js

/** Native component type */
@js.native
trait JSComponent[P <: js.Any] extends js.Object

object JSComponent {

  @inline
  implicit def jsComponentIsFunctionalComponentWithChildren[P <: js.Any](
    c: JSComponent[P]
  ): NativeComponentType.WithChildren[P] =
    c.asInstanceOf[NativeComponentType.WithChildren[P]]
}
