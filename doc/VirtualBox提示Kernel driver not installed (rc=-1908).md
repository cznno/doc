# VirtualBox提示Kernel driver not installed (rc=-1908)

## 环境

4.16.7-rt1-MANJARO 

## 解决

- 寻找对应内核版本的 [linux-headers](https://www.archlinux.org/packages/?name=linux-headers) 或 [linux-lts-headers](https://www.archlinux.org/packages/?name=linux-lts-headers) 并安装

- 运行

  ```shell
  $ modprobe vboxdrv
  ```

ref:

https://wiki.archlinux.org/index.php/VirtualBox#Installation_steps_for_Arch_Linux_hosts