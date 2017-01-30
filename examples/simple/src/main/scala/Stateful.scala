package eldis.react.examples.simple

import scalajs.js
import js.annotation._
import eldis.react.interop._

object Statefull {

  @js.native
  trait SimpleObjectProps extends js.Object {
    val message: String = js.native
  }

  object SimpleObjectProps {
    def apply(message: String): SimpleObjectProps =
      js.Dynamic.literal(
        message = message
      ).asInstanceOf[SimpleObjectProps]
  }

  @ScalaJSDefined
  object SimpleObject extends Component[SimpleObjectProps] {

    type State = Unit

    def render =
      React.createElement(
        "p",
        js.undefined,
        props.message
      )

  }

}
