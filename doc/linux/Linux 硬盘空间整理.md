# Linux 硬盘空间整理

[How to Find Out Top Directories and Files (Disk Space) in Linux](https://www.tecmint.com/find-top-large-directories-and-files-sizes-in-linux/)

上面的网页里用到了命令

`du -hs * | sort -rh | head -5`

文件删除了空间没释放

`lsof |grep delete`

需要kill列出的进程

http://blog.51cto.com/2483526/798379

##压缩
nice -n19 gzip debug
