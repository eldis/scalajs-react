package eldis.react.util

import eldis.react._

/**
 * A helper to guide type inference for [[NativeComponentType]].
 *
 * High-priority implicit infers [[NativeComponentType.WithChildren]],
 * low-priority one uses [[NativeComponentType]] instead.
 */
sealed trait ComponentLike[In] {
  type C[_]
  type P

  val evidence: C[P] <:< NativeComponentType[P]

  def apply(in: In): C[P]
}

sealed trait LowPriorityComponentLikes {

  implicit def simpleConverter[In, P](
    implicit
    convert: In => NativeComponentType[P]
  ): ComponentLike.Aux[In, NativeComponentType, P] =
    ComponentLike.mkConverter[In, NativeComponentType, P]
}

object ComponentLike extends LowPriorityComponentLikes {
  type Aux[In, C0[_], P0] = ComponentLike[In] {
    type C[X] = C0[X]
    type P = P0
  }

  def mkConverter[In, C0[_], P0](
    implicit
    convert: In => C0[P0],
    ev: C0[P0] <:< NativeComponentType[P0]
  ): Aux[In, C0, P0] = new ComponentLike[In] {
    type C[X] = C0[X]
    type P = P0

    val evidence = ev
    def apply(in: In) = convert(in)
  }

  implicit def withChildrenConverter[In, P](
    implicit
    convert: In => NativeComponentType.WithChildren[P]
  ): Aux[In, NativeComponentType.WithChildren, P] =
    mkConverter[In, NativeComponentType.WithChildren, P]
}
