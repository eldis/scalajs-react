package eldis.react.vdom

import scalajs.js

import eldis.react._

class Tag(name: String, tagAttrs: List[Attrs] = Nil) {
  def apply(attrs: Attrs*)(children: ReactNode*): ReactDOMElement = React.createElement(
    name,
    Attrs.concat(tagAttrs ++ attrs).toJs,
    children
  )
}

