package eldis.react

import scala.scalajs.js
import util.{ ComponentLike, UnapplyConstructor }

object MapProps {

  final class Factory[F[_], A, C[_]](val c: C[F[A]]) {

    def wrapped[B](f: B => A)(
      implicit
      C: C[F[A]] <:< NativeComponentType[F[A]],
      wrapper: Wrapper[F, A]
    ): FunctionalComponent[B] = {
      def worker(b: B): ReactNode = {
        React.createElement[A, F](C(c), f(b))
      }
      FunctionalComponent("MapProps")(worker)
    }

    def wrappedWithChildren[B](f: B => A)(
      implicit
      C: C[F[A]] <:< NativeComponentType.WithChildren[F[A]],
      wrapper: Wrapper[F, A]
    ): FunctionalComponent.WithChildren[B] = {
      def worker(b: B, children: PropsChildren): ReactNode =
        React.createElement[A, F](C(c), f(b), children)
      FunctionalComponent.withChildren("MapProps")(worker)
    }

    def native[B](f: B => A)(
      implicit
      C: C[F[A]] <:< NativeComponentType[F[A]],
      wrapper: Wrapper[F, A],
      B: B <:< js.Any
    ): NativeFunctionalComponent[B] = {
      def worker(b: B): ReactNode =
        React.createElement[A, F](C(c), f(b))
      NativeFunctionalComponent("MapProps")(worker)
    }

    def nativeWithChildren[B](f: B => A)(
      implicit
      C: C[F[A]] <:< NativeComponentType.WithChildren[F[A]],
      wrapper: Wrapper[F, A],
      B: B <:< js.Any
    ): NativeFunctionalComponent.WithChildren[B] = {
      def worker(b: B, children: PropsChildren): ReactNode =
        React.createElement[A, F](C(c), f(b), children)
      NativeFunctionalComponent.withChildren("MapProps")(worker)
    }
  }

  @inline
  def apply[C0, C[_], FA](c0: C0)(
    implicit
    convert: ComponentLike.Aux[C0, C, FA],
    unapply: UnapplyConstructor[FA]
  ): Factory[unapply.F, unapply.A, C] = {
    val c: C[unapply.F[unapply.A]] =
      unapply.inConstructor(convert(c0))
    new Factory[unapply.F, unapply.A, convert.C](c)
  }
}
