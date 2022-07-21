const { defineConfig } = require('@vue/cli-service')
const path = require("path");
const CompressionPlugin = require('compression-webpack-plugin')
function resolve(dir) {
  return path.join(__dirname, dir)
}

const name = process.env.VUE_APP_TITLE || 'XENON' // 网页标题
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  publicPath: process.env.NODE_ENV === 'production'? '/': '/',
  productionSourceMap: false,
  css: {
    loaderOptions: {
      sass: {
        sassOptions: { outputStyle: "expanded" }
      }
    }
  }
})
