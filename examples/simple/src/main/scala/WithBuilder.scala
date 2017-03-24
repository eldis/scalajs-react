package eldis.react.examples.simple

import eldis.react.FunctionalComponent
import eldis.react.ReactNode
import eldis.react.vdom.prefix_<^._
import eldis.react.util.ElementBuilder

object WithBuilder {

  case class Props(
    greeting: String = "Hi",
    name: String = "me"
  )

  val component: FunctionalComponent.WithChildren[Props] =
    FunctionalComponent.withChildren[Props]("WithBuilder") {
      (props, children) =>
        <.div()(
          (List[ReactNode](
            props.greeting,
            " from ",
            props.name,
            " "
          ) ++: children)
        )
    }

  def apply(): ElementBuilder[component.type, Props] =
    ElementBuilder(component, Props())

  implicit class BuilderOps(
      val self: ElementBuilder[WithBuilder.component.type, Props]
  ) extends AnyVal {
    def greeting(g: String) = self.map(_.copy(greeting = g))
    def name(n: String) = self.map(_.copy(name = n))
  }
}
