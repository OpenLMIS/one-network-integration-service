# one-network-integration-service

## Downloading SFTP Host Key
SFTP Host key is needed for application to run.
It's possible to download it via terminal using command:
> ssh-keyscan -t rsa host-address.com > file

Copy this key from the file (without `host-address.com ssh-rsa`) and save as `sftpHostKey.txt`
