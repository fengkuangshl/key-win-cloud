module.exports = {
  outputDir: 'build/' + (process.env.outputDir ? process.env.outputDir : 'dist'),
  devServer: {
    port: 8081,
    proxy: {
      '/api': {
        // target: 'https://192.168.1.147:8443',
        target: 'http://127.0.0.1:9200',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
