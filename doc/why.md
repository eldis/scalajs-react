# Why do I need this project

TODO: Sort the reasons by their priorities.

## The problems of japgolly/scalajs-react

Inability to work with native javascript properties of the components.
`scalajs-react` always wraps your properties and that's the reason that
you can't use your components with another JS libraries.

The absence of optionality in the parameters. For example, [the react
documentation](https://facebook.github.io/react/docs/react-api.html#createelement)
says that `props` parameter is optional. But not in scalajs-react. And
this is not a single case of the problem.
