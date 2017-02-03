package eldis.react

import scalajs.js

trait PropsImplicits {

  type Identity[P] = P

  trait UnwrapNative[F[_]] {
    def unwrap[P](v: js.Any): P
  }

  object UnwrapNative {
    implicit val IdentityUnwrapNative: UnwrapNative[Identity] = new UnwrapNative[Identity] {
      def unwrap[P](v: js.Any): P = v.asInstanceOf[P]
    }

    implicit val WrappedUnwrapNative: UnwrapNative[Wrapped] = new UnwrapNative[Wrapped] {
      def unwrap[P](v: js.Any): P = v.asInstanceOf[Wrapped[P]].get
    }
  }

  trait WrapToNative[P] {
    def wrap(v: P): js.Any
  }

  object WrapToNative extends LowPriorityImplicits {
    implicit def identity[P <: js.Any]: WrapToNative[P] = new WrapToNative[P] {
      def wrap(v: P) = v
    }
  }

  trait LowPriorityImplicits {
    implicit def AWrapToNative[P]: WrapToNative[P] = new WrapToNative[P] {
      def wrap(v: P) = Wrapped(v).asInstanceOf[js.Any]
    }
  }

}

