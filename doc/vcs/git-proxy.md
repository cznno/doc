# Git proxy git代理
## HTTP/HTTPS protocol
```bash
本地配置（in git repo）
http proxy
git config http.proxy http://host:port
socks proxy
git config http.proxy socks5://host:port
git config http.proxy socks5h://host:port (dns resolved on server. haven't tested)
全局配置
git config --global http.proxy http://host:port
git config --global http.proxy socks5://host:port
```
## ssh protocol
```
windows (tested on windows 10 1909)
Host gitlab.com
  ProxyCommand connect -S host:port -a none %h %p
  IdentityFile "~\.ssh\id_rsa"
  ForwardAgent yes
```
