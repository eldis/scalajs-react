package eldis.react.tests.vdom

import scalajs.js
import org.scalatest._

class VDomSpec extends FunSpec with Matchers {

  import eldis.react._
  import eldis.react.vdom._
  import prefix_<^.{ < => <<, _ }

  describe("HTML attributes") {

    it("can be converted to js.Object") {

      val attrs = Attrs(
        ^.className := "test-control",
        false ?= (^.action := "yes, set"),
        true ?= (^.method := "yes, set"),
        ^.style := Style(
          "border" -> "1px solid black",
          false ?= "nop" -> "nop"
        )
      )
      val o = attrs.toJs.asInstanceOf[js.Dynamic]
      o.className shouldBe "test-control"
      o.hasOwnProperty("action") shouldBe false
      o.method shouldBe "yes, set"
      val s = o.style.asInstanceOf[js.Dynamic]
      s.border shouldBe "1px solid black"
      s.hasOwnProperty("nop") shouldBe false
    }

    it("can receive calbacks") {

      var v1 = 0
      var v2 = 0

      val attrs = Attrs(
        "func1" --> (() => v1 = 1),
        "func2" ==> ((v: Int) => v2 = v),
        false ?= "func3" --> (() => ())
      ).toJs.asInstanceOf[js.Dynamic]

      attrs.func1()
      v1 shouldBe 1
      attrs.func2(2)
      v2 shouldBe 2
      attrs.hasOwnProperty("func3") shouldBe false

    }

    it("can receive options as values") {

      var v0 = 0
      val f0 = () => { v0 += 1 }

      var v1 = Seq[String]()
      val f1 = (s: String) => { v1 = v1 :+ s }

      val s = "foo"

      val attrs = Attrs(
        "sSome" :=? Some(s),
        "sNone" :=? (None: Option[String]),
        "f0Some" -->? Some(f0),
        "f0None" -->? (None: Option[f0.type]),
        "f1Some" ==>? Some(f1),
        "f1None" ==>? (None: Option[f1.type])
      ).toJs.asInstanceOf[js.Dynamic]

      attrs.sSome shouldBe s
      attrs.hasOwnProperty("sNone") shouldBe false

      attrs.f0Some()
      v0 shouldBe 1
      attrs.hasOwnProperty("f0None") shouldBe false

      attrs.f1Some("abc")
      v1 shouldBe Seq("abc")
      attrs.hasOwnProperty("f1None") shouldBe false
    }
  }

  describe("HTML tags") {

    val parser = new DOMParser()

    it("can produce react DOM elements") {

      val el = <<.p(
        ^.className := "caption",
        ^.style := Style(
          "border" -> "1px solid black"
        )
      )("Hello, world!")

      val res = parser.parseFromString(ReactDOMServer.renderToStaticMarkup(el), SupportedType.`text/xml`)
      val tag = res.children(0)
      println(ReactDOMServer.renderToStaticMarkup(el))
      tag.tagName shouldBe "p"
      tag.attributes(0).name shouldBe "class"
      tag.attributes(0).value shouldBe "caption"
      tag.attributes(1).name shouldBe "style"
      tag.attributes(1).value shouldBe "border:1px solid black;"
    }

  }

}
