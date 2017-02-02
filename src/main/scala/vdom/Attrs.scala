package eldis.react.vdom

import scalajs.js

case class AttrValue(value: js.Any)

case class AttrName(value: String) {
  def :=(value: AttrValue): Attr = Attr(this, value)
  def -->(value: js.Function0[_]): Attr = Attr(this, AttrValue(value: js.Function))
  def ==>[T](value: js.Function1[T, _]): Attr = Attr(this, AttrValue(value))
}

case class Attr(
  val name: AttrName,
  val value: AttrValue
)

class Attrs(attrs: Seq[Attr]) {
  def toJs: js.Object =
    js.Dictionary(
      attrs.filter(_.value.value != js.undefined).map(a => a.name.value -> a.value.value): _*
    ).asInstanceOf[js.Object]
}

object Attrs {
  def apply(attrs: Attr*): Attrs = new Attrs(attrs)
}

