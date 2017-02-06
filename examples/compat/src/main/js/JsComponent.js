var React = require('react');

module.exports = function(props) {

  return React.createElement(
    "p",
    null,
    "This is the message from native JS component: " + props.message
  );

};
