package eldis.react

import scala.language.higherKinds

import scalajs.js

trait PropsImplicits {

  type Identity[P] = P

  trait Wrapper[F[_], P] {

    val evidence: F[P] <:< js.Any

    def wrap(v: P): F[P]
    def unwrap(v: F[P]): P
  }

  trait LowPriorityWrappers {

    implicit def identityWrapper[P](implicit ev: P <:< js.Any): Wrapper[Identity, P] =
      new Wrapper[Identity, P] {
        val evidence = ev

        def wrap(v: P): Identity[P] = v
        def unwrap(v: Identity[P]): P = v
      }
  }

  object Wrapper extends LowPriorityWrappers {

    def apply[F[_], P](implicit w: Wrapper[F, P]): Wrapper[F, P] = w

    implicit def wrappedWrapper[P]: Wrapper[Wrapped, P] =
      new Wrapper[Wrapped, P] {

        val evidence = implicitly[Wrapped[P] <:< js.Any]

        def wrap(v: P): Wrapped[P] = Wrapped(v)
        def unwrap(v: Wrapped[P]): P = v.get
      }

    def produceIdentity[F[_], P](
      w: Wrapper[F, P]
    ): Wrapper[Identity, F[P]] = identityWrapper[F[P]](w.evidence)
  }
}
