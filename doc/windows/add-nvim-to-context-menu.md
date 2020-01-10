# Add Neovim to context menu

Batch script here:

### Add to context menu

Don’t forget to replace actual path of neovim

```
Windows Registry Editor Version 5.00

[HKEY_CLASSES_ROOT\*\shell\nvim-qt]
@="Edit with Neovim"
"Icon"="\"D:\\Neovim\\bin\\nvim-qt.exe\""

[HKEY_CLASSES_ROOT\*\shell\nvim-qt\command]
@="\"D:\\Neovim\\bin\\nvim-qt.exe\" \"%1\""

[HKEY_CLASSES_ROOT\Directory\shell\nvim-qt]
@="Edit with Neovim"
"Icon"="\"D:\\Neovim\\bin\\nvim-qt.exe\""

[HKEY_CLASSES_ROOT\Directory\shell\nvim-qt\command]
@="\"D:\\tool\\bin\\nvim-qt.exe\" \"%1\""

[HKEY_CLASSES_ROOT\Directory\Background\shell\nvim-qt]
@="Edit with Neovim"
"Icon"="\"D:\\Neovim\\bin\\nvim-qt.exe\""

[HKEY_CLASSES_ROOT\Directory\Background\shell\nvim-qt\command]
@="\"D:\\Neovim\\bin\\nvim-qt.exe\""
```

### Remove from context menu

```
Windows Registry Editor Version 5.00

[-HKEY_CLASSES_ROOT\*\shell\nvim-qt]
[-HKEY_CLASSES_ROOT\Directory\shell\nvim-qt]
[-HKEY_CLASSES_ROOT\Directory\Background\shell\nvim-qt]
```



ref:

https://github.com/neovim/neovim/issues/7222