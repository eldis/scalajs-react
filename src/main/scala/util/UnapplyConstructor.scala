package eldis.react.util

import eldis.react.Identity

/**
 * A helper to guide prop wrapper inference.
 *
 * High-priority implicit unapplies a top constructor, low-priority
 * one unapplies [[Identity]].
 */
sealed trait UnapplyConstructor[FA] {

  type F[_]
  type A

  // It would be better to have Leibniz here, but we don't want scalaz dependency.
  val evidence: FA =:= F[A]

  def apply(fa: FA): F[A] = evidence(fa)

  def inConstructor[G[_]](gfa: G[FA]): G[F[A]] = gfa.asInstanceOf[G[F[A]]]
}

sealed trait LowPriorityUnapplyConstructors {
  implicit def identityUnapplyConstructor[A0]: UnapplyConstructor.Aux[A0, Identity, A0] =
    UnapplyConstructor.mkUnapplyConstructor[Identity, A0]
}

object UnapplyConstructor extends LowPriorityUnapplyConstructors {

  type Aux[FA, F0[_], A0] = UnapplyConstructor[FA] {
    type F[X] = F0[X]
    type A = A0
  }

  def mkUnapplyConstructor[F0[_], A0]: Aux[F0[A0], F0, A0] =
    new UnapplyConstructor[F0[A0]] {
      type F[X] = F0[X]
      type A = A0
      val evidence = implicitly[F0[A0] =:= F0[A0]]
    }

  implicit def simpleUnapplyConstructor[F0[_], A0]: Aux[F0[A0], F0, A0] =
    mkUnapplyConstructor[F0, A0]
}
