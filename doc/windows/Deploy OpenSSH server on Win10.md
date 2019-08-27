# Deploy OpenSSH server on Win10

## Resources

https://winscp.net/eng/docs/guide_windows_openssh_server

https://docs.microsoft.com/en-us/windows-server/administration/openssh/openssh_server_configuration

https://github.com/PowerShell/Win32-OpenSSH/wiki

## System detail

- Windows 10 professional 1803

## Install OpenSSH

Since I didn’t find “OpenSSH server” option in *Manage optional features*, I had to download it from github. Version I download was [v8.0.0.0p1-Beta](https://github.com/PowerShell/Win32-OpenSSH/releases/tag/v8.0.0.0p1-Beta)

Installation was quite simple, just unzip file to a folder, and execute `powershell.exe -ExecutionPolicy Bypass -File install-sshd.ps1` under it.

## Configuration

### Basic

- Allow connections in Firewall
- configure automatic start the service

### Set public key authentication

Here is the tricky part, here is the background and how did I fix this:

- I have a domain account and I want to login with this account

- add my public_key to `C:\users\username\.ssh\authorized_keys`

- login with key failed, however password authentication is okay

- and I found this: [Security protection of various files in Win32 OpenSSH](https://github.com/PowerShell/Win32-OpenSSH/wiki/Security-protection-of-various-files-in-Win32-OpenSSH#administrators_authorized_keys)

- check the permissions of authorized_keys, and modified it like

  ```
  authorized_keys NT AUTHORITY\SYSTEM:(F)
                  BUILTIN\Administrators:(F)
                  mydomain\myusername:(F)
  ```

And..still failed

but I found  that users of group Administrators store keys in `C:\ProgramData\ssh\administrators_authorized_keys` 

so check permissions of this file, modified it into:

```
administrators_authorized_keys NT AUTHORITY\SYSTEM:(F)
                               BUILTIN\Administrators:(F)
```

that worked fine for me.

