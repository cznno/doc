#RedHat用户最大进程数引起的问题

## 环境

Red Hat Enterprise Linux Server release 6.8 (Santiago)

## 现象

-  通过ssh登录失败, 提示

```shell
packet_write_wait: Connection to xxx.xxx.xxx.xxx port xxxx: Broken pipe
```

- 物理机tty1没有登录界面
- 切换到tty2, 虽然输入了正确的用户名密码, 但是立刻返回到login界面(没有错误提示)
- 使用root登录成功

## 尝试

- 怀疑执行了某些脚本

  - 查看/home/username/.bash_history 没有发现问题
  - 查看/etc/profile 没有问题
  - 查看/home/username/.bashrc 没有问题

- 检查权限

  - /home 没有问题
  - / 没有问题 (* https://serverfault.com/questions/535105/)

- 查看/var/log/secure, 发现

  ```
  login: LOGIN ON tty2 BY username
  login: setuid() failed
  login: pam_unix(login:session): session closed for user username
  ```

  问题在login: setuid() failed, 但是并不能确定原因与解决方法

- root下尝试su username

  失败, 提示

  ```
  cannot set user id resource temporarily unavailable
  ```

## 解决

查看/etc/security/limits.d/90-nproc.conf, 发现最大进程数是1024

修改为更大值后解决

ref: http://blog.51cto.com/12410094/1905156