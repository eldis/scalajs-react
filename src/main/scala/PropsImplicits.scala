package eldis.react

import scalajs.js

trait PropsImplicits {

  type Identity[P] = P

  trait Wrapper[F[_], P] {
    type Out = js.Any with F[P]
    def wrap(v: P): Out
    def unwrap(v: F[P]): P
  }

  object Wrapper {

    def apply[F[_], P](implicit w: Wrapper[F, P]): w.type = w

    implicit def identityWrapper[P <: js.Any]: Wrapper[Identity, P] =
      new Wrapper[Identity, P] {
        def wrap(v: P): Out = v
        def unwrap(v: Identity[P]): P = v
      }

    implicit def wrappedWrapper[P]: Wrapper[Wrapped, P] =
      new Wrapper[Wrapped, P] {
        def wrap(v: P): Out = Wrapped(v)
        def unwrap(v: Wrapped[P]): P = v.get
      }
  }
}

