package eldis

package object react {

  import scalajs.js
  import js.annotation.JSImport
  import japgolly.scalajs.{ react => impl }

  /**
   * Scalajs-react searches its dependencies in the global namespace,
   * so we must provide them to it.
   */
  private object Dependencies {

    @JSImport("react", JSImport.Namespace)
    @js.native
    object React extends js.Object {}

    @JSImport("react-dom", JSImport.Namespace)
    @js.native
    object ReactDOM extends js.Object {}

    def setup = {
      js.Dynamic.global.React = React
      js.Dynamic.global.ReactDOM = ReactDOM
    }
  }

  // Initialize dependencies
  Dependencies.setup

  type ReactNode = impl.ReactNode
  type ReactDOMElement = impl.ReactDOMElement

  object React {
    @inline def createElement(
      tag: String,
      props: js.UndefOr[js.Object],
      children: ReactNode*
    ): ReactDOMElement =
      impl.React.createElement(
        tag,
        props.asInstanceOf[js.Object],
        children: _*
      )
  }
  val ReactDOM = impl.ReactDOM

}
