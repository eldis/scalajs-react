package eldis.react.examples.simple

import scalajs.js
import js.annotation._
import eldis.react.{ NativeComponent => NativeComponentBase, _ }
import vdom.prefix_<^._

object Stateful {

  @js.native
  trait NativeProps extends js.Object {
    val message: String = js.native
  }

  object NativeProps {
    def apply(message: String): NativeProps =
      js.Dynamic.literal(
        message = message
      ).asInstanceOf[NativeProps]
  }

  @ScalaJSDefined
  class NativeComponent extends NativeComponentBase[NativeProps]("Stateful.native") {

    type State = Unit

    def initialState = ()

    def render =
      <.div()(
        <.p()(props.message),
        propsChildren
      )
  }

  @ScalaJSDefined
  object NativeComponent extends NativeComponent

  case class ScalaProps(
    message: String
  )

  @ScalaJSDefined
  class ScalaComponent extends Component[ScalaProps]("Stateful.scala") {

    type State = Unit

    def initialState = ()

    def render =
      <.div()(
        <.p()(props.message),
        propsChildren
      )
  }

  @ScalaJSDefined
  object ScalaComponent extends ScalaComponent

}
