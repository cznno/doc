# SVN迁移到Gitlab

## 从SVN仓库转换成Git仓库

主要参考了：

- https://docs.microsoft.com/en-us/azure/devops/articles/perform-migration-from-svn-to-git?view=vsts#advanced-migrations
- https://www.lovelucy.info/codebase-from-svn-to-git-migration-keep-commit-history.html
- https://my.oschina.net/u/199525/blog/1556989

操作环境环境是在win下

### 处理Subversion的用户名

用powershell运行

```powershell
svn.exe log --quiet | ? { $_ -notlike '-*' } | % { ($_ -split ' \| ')[1] } | Select-Object -Unique
```

把得到的用户名保存在一个文件里，比如authors-transform.txt

然后把这些用户名处理成git的格式，如：

`jamal = Jamal Hartnett <jamal@fabrikam-fiber.com>`

### 用git-svn clone Subversion的仓库

基本指令

```powershell
git svn clone ["SVN repo URL"] --prefix=svn/ --no-metadata --authors-file "authors-transform.txt" --stdlayout c:\mytempdir
```

- authors-file的值是之前设置用户名的文件路径

- c:\mytempdir是clone到本地的路径

- `-prefix=svn/`让工具可以识别svn的版本号(?)

  似乎只是设置了branch, 默认的prefix是origin

- stdlayout是通过svn默认的目录配置读取仓库, 即按照: trunk, branches和tags读取. 因为公司的svn目录是以trunk, branch命名的, 所以需要用-b branch指定目录. 大概的参数是这样

  `git svn clone [thepath] -T trunk -b branches -t tags`

### 转换所有的svn分支到git分支

完成git svn clone之后, svn上的分支会在remote上. 可以通过powershell命令在本地创建. 同时,这个命令会删除remote上的所有分支.

```powershell
git for-each-ref --format='%(refname)' refs/remotes | % { $_.Replace('refs/remotes/','') } | % { git branch "$_" "refs/remotes/$_"; git branch -r -d "$_"; }
```

## 从旧仓库合并到新仓库

把之前从svn上迁移成git的仓库称为old-repo, 在运维系统会自动在gitlab上init一个仓库, 称为new-repo

把new-repo clone到本地, 设置git remote为old-repo, 然后从old-repo拉取分支, 最后push到gitlab



