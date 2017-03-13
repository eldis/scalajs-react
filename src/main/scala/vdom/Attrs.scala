package eldis.react.vdom

import scalajs.js

case class AttrValue(value: js.Any)

case class AttrName(value: String) {
  def :=(value: AttrValue): Attr = Attr(this, value)
  def -->(value: js.Function0[_]): Attr = Attr(this, AttrValue(value: js.Function))
  def ==>[T](value: js.Function1[T, _]): Attr = Attr(this, AttrValue(value))

  def :=?[A](value: Option[A])(implicit toAttrValue: A => AttrValue): Attrs =
    value.fold(Attrs.zero)(a => Attrs(this := toAttrValue(a)))

  def -->?[F](value: Option[F])(implicit toJSFunction: F => js.Function0[_]): Attrs =
    value.fold(Attrs.zero)(f => Attrs(this --> toJSFunction(f)))

  def ==>?[T, FT](value: Option[FT])(implicit toJsFunction: FT => js.Function1[T, _]): Attrs =
    value.fold(Attrs.zero)(ft => Attrs(this ==> toJsFunction(ft)))
}

case class Attr(
  val name: AttrName,
  val value: AttrValue
)

class Attrs(private val attrs: Seq[Attr]) {
  def toJs: js.Object =
    js.Dictionary(
      attrs.filter(_.value.value != js.undefined).map(a => a.name.value -> a.value.value): _*
    ).asInstanceOf[js.Object]
}

object Attrs {

  import scala.language.implicitConversions

  def apply(attrs: Attr*): Attrs = new Attrs(attrs)
  def apply(attr0: Attrs, attrs: Attrs*): Attrs = concat(attr0 +: attrs)

  // Attrs is a monoid
  def zero: Attrs = Attrs()

  def append(a: Attrs, b: Attrs): Attrs =
    Attrs(a.attrs ++: b.attrs: _*)

  def concat(as: Seq[Attrs]): Attrs =
    Attrs(as.flatMap(_.attrs): _*)

  implicit def attrToAttrs(attr: Attr): Attrs = Attrs(attr)
}

