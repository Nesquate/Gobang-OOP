## 五子棋
這是 2020 上學期物件導向程式設計的作業，包含 Client 端和 Server 端，詳細規格可以查看 [說明文件](DOCS.md)

## 啟動方式
*需要 JDK 11 或更高版本*

Clone 此專案或下載此專案為 zip 檔案解壓縮後，執行下列指令

```shell
# Compile Client & Server 
javac src/Core/*.java # Client
javac src/Server/*.java

# Change Directory to ./src
cd ./src

# Run client 
java Gobang.Core.Main

# Run Server
java Gobang.Server.Main
```

## 使用技術
Client: Java Swing + Socket  
Server: Socket
