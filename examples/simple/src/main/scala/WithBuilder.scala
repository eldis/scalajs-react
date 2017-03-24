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

  def apply(): ElementBuilder[component.type, Props, Unit] =
    ElementBuilder(component, Props())

  def withChildren(children: ReactNode*): ElementBuilder[component.type, Props, Seq[ReactNode]] =
    ElementBuilder(component, Props(), children)

  implicit class BuilderOps[CH](
      val self: ElementBuilder[WithBuilder.component.type, Props, CH]
  ) extends AnyVal {
    def greeting(g: String) = self.mapProps(_.copy(greeting = g))
    def name(n: String) = self.mapProps(_.copy(name = n))
  }
}
