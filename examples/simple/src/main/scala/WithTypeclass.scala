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

  type PropsImpl[T] = Tuple2[Props[T], Show[T]]

  @ScalaJSDefined
  class StatefulComponent[T] extends Component[PropsImpl[T]]("WithTypeclass.stateful") {

    type State = Seq[T]

    def initialState = props._1

    def render = {
      val (_, s) = props
      val p = state
      <.ul()(
        p.map(v => <.li()(s.show(v))): _*
      )
    }
  }

  object StatefulComponent {
    def apply[T](p: WithTypeclass.Props[T])(implicit s: Show[T]): ReactDOMElement = {
      val c: NativeComponentType[Wrapped[PropsImpl[T]]] =
        implicitly[js.ConstructorTag[StatefulComponent[T]]]
      React.createElement(
        c,
        (p, s)
      )
    }
  }

  def statelessComponentImpl[T] = FunctionalComponent("WithTypeclass.stateless") { (p: PropsImpl[T]) =>
    val (e, s) = p
    <.ul()(
      e.map(v => <.li()(s.show(v))): _*
    )
  }

  def StatelessComponent[T](p: Props[T])(implicit s: Show[T]): ReactDOMElement =
    React.createElement(
      statelessComponentImpl[T],
      (p, s)
    )

}
