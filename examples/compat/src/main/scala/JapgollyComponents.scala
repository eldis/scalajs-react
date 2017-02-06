package eldis.react.examples.compat

import japgolly.scalajs.react._
import vdom.prefix_<^._

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

}
