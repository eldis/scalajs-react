package eldis.react

import scalajs.js
import eldis.{ react => es }
import japgolly.scalajs.{ react => japgolly }

package object compat {

  /**
   *  Initialize global react.js objects
   */
  def setupReactGlobals(): Unit = {
    js.Dynamic.global.React = es.JSReact
    js.Dynamic.global.ReactDOM = es.ReactDOM
  }

  implicit def japgollyDOMElementToEsDOMElement(n: japgolly.ReactDOMElement): es.ReactDOMElement =
    n.asInstanceOf[es.ReactDOMElement]
  implicit def esDOMElementToJapgollyDOMElement(n: es.ReactDOMElement): japgolly.ReactDOMElement =
    n.asInstanceOf[japgolly.ReactDOMElement]

  implicit def japgollyReactComponentUToEsNode(c: japgolly.ReactComponentU[_, _, _, _]): es.ReactNode =
    c.asInstanceOf[es.ReactNode]
}
