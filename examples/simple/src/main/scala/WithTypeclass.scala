package eldis.react.examples.simple

import scalajs.js
import js.annotation._
import eldis.react._
import vdom.prefix_<^._

object WithTypeclass {

  // ``Show`` typeclass
  trait Show[T] {
    def show(v: T): String = v.toString
  }

  type Props[T] = Seq[T]

  trait PropsImpl {
    type PT
    val el: Props[PT]
    val show: Show[PT]
  }

  @ScalaJSDefined
  class StatefulComponent extends Component[PropsImpl]("WithTypeclass.stateful") {

    type State = Unit

    def initialState = ()

    def render = {
      val p = props // We need it because ``props`` is function, and
      // the compiler thinks that the type PropsImpl#PT
      // can be different on each call
      val (e, s) = (p.el, p.show)
      <.ul()(
        e.map(v => <.li()(s.show(v))): _*
      )
    }

    @JSName("createStatefullComponent")
    def apply[T](p: WithTypeclass.Props[T])(implicit s: Show[T]): ReactDOMElement =
      apply(
        new PropsImpl {
          type PT = T
          val el = p
          val show = s
        }
      )
  }

  @ScalaJSDefined
  object StatefulComponent extends StatefulComponent

  val statelessComponentImpl = FunctionalComponent("WithTypeclass.stateless") { (p: PropsImpl) =>
    val (e, s) = (p.el, p.show)
    <.ul()(
      e.map(v => <.li()(s.show(v))): _*
    )
  }

  def StatelessComponent[T](p: Props[T])(implicit s: Show[T]): ReactDOMElement =
    React.createElement(
      statelessComponentImpl,
      new PropsImpl {
        type PT = T
        val el = p
        val show = s
      }
    )

}
