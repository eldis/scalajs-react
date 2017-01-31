package eldis.react.interop

import scalajs.js

class Ref[Node] {
  private var node: Option[Node] = None

  def apply(): js.Function1[Node, Unit] = { n: Node =>
    node = Option(n)
  }

  def get: Option[Node] = node
}

object Ref {
  def apply[Node](): Ref[Node] = new Ref[Node]()
}
