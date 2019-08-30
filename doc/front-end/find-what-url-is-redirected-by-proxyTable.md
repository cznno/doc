```js
// ...configure

proxyTable: {
  '/foo': {
    changeOrigin: true,
    target: target
    onProxyReq: function (proxyReq, req, res) {
      console.log("original" + req.originalUrl, "proxy：" + req.path)
    }
  }
}
```
