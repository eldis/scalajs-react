package eldis.react.vdom

import scalajs.js

import eldis.react._

class Tag(name: String, tagAttrs: List[Attr] = Nil) {
  def apply(attrs: Attr*)(children: ReactNode*): ReactDOMElement = React.createElement(
    name,
    Attrs((tagAttrs ++ attrs): _*).toJs,
    children: _*
  )
}

