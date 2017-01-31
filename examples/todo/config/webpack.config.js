var HtmlWebpackPlugin = require('html-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var cfg = require('./scalajs.webpack.config');
var path = require('path');
var rootDir = path.dirname(path.dirname(path.dirname(path.dirname(__dirname))));
var nodeModulesDir = path.join(__dirname, 'node_modules');

cfg.output.path = path.join(rootDir, 'build');
cfg.output.filename = 'js/index.js';

cfg.module = cfg.module || {};
cfg.module.loaders = cfg.module.loaders || [];
cfg.module.loaders.push({
  test: /\.html$/,
  loader: 'html'
});

cfg.plugins = cfg.plugins || [];
cfg.plugins.push(new CopyWebpackPlugin([
  { from: path.join(nodeModulesDir, 'todomvc-common/base.css'), to: 'css/base.css' },
  { from: path.join(nodeModulesDir, 'todomvc-app-css/index.css'), to: 'css/index.css' },
]));
cfg.plugins.push(new HtmlWebpackPlugin({
  template: path.join(rootDir, 'src/main/assets/index.html')
}));

module.exports = cfg;
