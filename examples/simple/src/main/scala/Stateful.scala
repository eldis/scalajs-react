package eldis.react.examples.simple

import scalajs.js
import js.annotation._
import eldis.react.interop._

object Stateful {

  @js.native
  trait SimpleComponentProps extends js.Object {
    val message: String = js.native
  }

  object SimpleComponentProps {
    def apply(message: String): SimpleComponentProps =
      js.Dynamic.literal(
        message = message
      ).asInstanceOf[SimpleComponentProps]
  }

  @ScalaJSDefined
  object SimpleComponent extends Component[SimpleComponentProps] {

    type State = Unit

    def render =
      React.createElement(
        "p",
        js.undefined,
        props.message
      )
  }

}
