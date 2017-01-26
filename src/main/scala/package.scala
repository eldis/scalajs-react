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

  val React = impl.React
  val ReactDOM = impl.ReactDOM

}
