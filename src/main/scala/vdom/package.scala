package eldis.react

import scalajs.js

package object vdom extends vdom.Events {

  import scala.language.implicitConversions

  val Style = Attrs

  object prefix_<^ {

    object ^ extends HtmlAttrs
    object < extends HtmlTags

  }

  @inline def attr(name: String): AttrName = AttrName(name)
  @inline implicit def cvtStringsPairToAttr(p: (String, String)): Attr = AttrName(p._1) := AttrValue(p._2)
  @inline implicit def cvtStringsPairToAttrs(p: (String, String)): Attrs = Attrs(AttrName(p._1) := AttrValue(p._2))

  @inline implicit final class optionalMarkupOps(flag: Boolean) {
    def ?=(attr: Attr): Attrs =
      if (flag)
        attr
      else
        Attrs.zero
  }

  @inline implicit def cvtStringToAttrName(s: String): AttrName = AttrName(s)
  @inline implicit def cvtStringToAttrValue(s: String): AttrValue = AttrValue(s)
  @inline implicit def cvtBoolToAttrValue(v: Boolean): AttrValue = AttrValue(v)
  @inline implicit def cvtIntToAttrValue(v: Int): AttrValue = AttrValue(v)
  @inline implicit def cvtFunctionToAttrValue(v: js.Function): AttrValue = AttrValue(v)
  @inline implicit def cvtAttrsToAttrValue(attrs: Attrs): AttrValue = AttrValue(attrs.toJs)

  @inline def tag(name: String): Tag = new Tag(name)

  // Helper
  def classNames(c: (Boolean, String)*): AttrValue =
    AttrValue(c.filter(_._1).map(_._2).mkString(" "))
}
