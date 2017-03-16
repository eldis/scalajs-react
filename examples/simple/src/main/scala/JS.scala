package eldis.react.examples.simple

import scalajs.js
import js.annotation.JSImport
import eldis.react._

object JS {

  @js.native
  trait Props extends js.Any {
    val message: String = js.native
  }

  object Props {
    def apply(message: String): Props =
      js.Dynamic.literal(
        message = message
      ).asInstanceOf[Props]
  }

  @JSImport("JsComponent", JSImport.Namespace)
  @js.native
  object Component extends JSComponent[Props]

  def apply(message: String) = React.createElement[Props, Identity](Component, Props(message))

}
