# SCP通过多节点跳转上传文件

```bash
scp -o ProxyCommand="ssh -p $port_to_jump_host $jump_host nc $host $port_to_host" $local_path $username@$host:$destination_path
```

##Ref

- https://serverfault.com/questions/37629/how-do-i-do-multihop-scp-transfers/815816#815816
- https://www.zhihu.com/question/38216180

