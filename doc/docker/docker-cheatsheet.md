# Docker Cheatsheet

#### 删除镜像

docker rmi image_id

#### 查看启动错误的日志

docker events查看日志id

docker logs 查看日志

记录一些spring framework中比较有用的类

#### Http的Content-Type

org.springframework.http.MediaType

### 启动docker container

```bash
# bad practice:
# docker ps -a |grep mysql|awk {'print $1'}|xargs docker start
docker ps --format "{{.ID}}" --filter name=mysql|xargs docker start
```

### 接入bash  

```bash
docker exec -it <mycontainer> bash
```
