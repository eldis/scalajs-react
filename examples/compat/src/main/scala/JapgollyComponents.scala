package eldis.react.examples.compat

import scalajs.js
import js.annotation.JSImport
import japgolly.scalajs.react._
import vdom.prefix_<^._
import org.scalajs.dom.raw.HTMLElement

object JapgollyComponents {

  val functionalComponent = FunctionalComponent[String] { message =>
    <.p()("Functional component with message: " + message)
  }

  val component = ReactComponentB[String]("JapgollyComponent")
    .render { scope =>
      <.div()(
        <.p()("Component with message: " + scope.props.toLowerCase + ", and the children:"),
        scope.propsChildren
      )
    }.build

  @js.native
  trait JsProps extends js.Any {
    val message: String = js.native
  }

  object JsProps {
    def apply(message: String): JsProps =
      js.Dynamic.literal(
        message = message
      ).asInstanceOf[JsProps]
  }

  @JSImport("JsComponent", JSImport.Namespace)
  @js.native
  object FromJs extends JsComponentType[JsProps, js.Any, HTMLElement]

  val fromJs = React.createFactory(FromJs)
}
