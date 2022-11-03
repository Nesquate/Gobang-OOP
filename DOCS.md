# 關於本套件
## 五子棋簡介
五子棋是一種兩人對弈的純策略型棋類遊戲，通常雙方分別使用黑白兩色的棋子，輪流下在棋盤直線與橫線的交叉點上，先在橫線、直線或斜對角線上形成5子連線者獲勝。

_來源 : [Wikipedia](https://zh.wikipedia.org/wiki/%E4%BA%94%E5%AD%90%E6%A3%8B)_

## 套件內容物
本 Gobang 套件包含了：  
1. Core : Client，可進行離線遊戲與線上遊戲
2. Server : Server，線上遊戲的伺服器程式

# Core
## 模式
- 本機模式 (預設) : 同一台電腦進行兩人對奕
- 連線模式 : 在兩台不同的電腦上進行兩人對奕

## 勝利條件
- 連五 : 五個連續相同顏色的棋子即獲勝
- 活四 : 四個連續相同顏色的棋子，且前後皆可再下一子即獲勝
- **活四的優先度大於連五**

## 類別架構
- Class Main : 程式啟動接入點
- 本程式採用 MVC (Model-View-Controller) 架構模式設計，其類別與相關意含如下：
    - Class Model : MVC 中的 **Model** ， 負責棋盤的資料儲存與相關操作動作
    - Class Controller : MVC 中的 **Controller** ，負責遊戲邏輯、棋盤操作邏輯、文字版更新邏輯、視窗操作邏輯及連線與更新邏輯。
        - Class UpdateEvent : 負責連線更新邏輯，主要附加在 Class Controller 當中
    - Class View_GUI : MVC 中的 **View**，負責視窗排版與相關操作動作
        - Class MyButton : 負責棋盤按鈕資料的儲存與相關操作動作，主要附加在 Class View_GUI 當中，在 Class Controller 亦有相關操作邏輯 (主要是 `actionLinster` 的部份)
    - Class View_text : MVC 中的 **View**，負責文字版排版與相關操作動作。早期測試使用，**由於後續變動幅度過大，除非自行修改，否則無法正常使用**

## 本機遊戲運作流程
- 程式從 Class Main 開始啟動
    - 生出 Class Model、Class Controller 與 Class View_GUI 相關物件
    - Class Model 建構子不需要填入任何東西
    - Class Controller 建構子需要依序填入 **Class Model 物件**與**連子數量**
    - 使用 `Model.setSize()` 設定棋盤大小
    - Class View_GUI 建構子需要依序填入 **Class Controller 物件**與**棋盤大小**
    - 使用 `Controller.gameStartGUI()` 啟動 GUI (`JFrame.setVisible(true)`)
- 建立 Class View_GUI 物件時，同時建立所有視窗相關物件與註冊 Class Controller 物件
    - 與 Class Controller 註冊是為了要建立 `Controller.actionPerformed()` 的連結
- 按下棋盤按鈕會透過 `Controller.actionPerformed()` 呼叫 :
    - `Controller.whosTurn()` 決定誰下棋
    - `Controller.winIsFour()` 決定是否活四
        - 成立的話會呼叫 `View_GUI.showWinnerOfFour()` 顯示因活四勝利
    - `Controller.winIsFive()` 決定是否連五
        - 成立的話會呼叫 `View_GUI.showWinner()` 顯示勝利
    - `Controller.changeTurn()` 進行雙方互換的動作

## 遠端連線相關程式碼
- 當按下選單的「Connect -> Connect Server」時：
    - 呼叫 `Controller.connectSocket()` 與伺服器連線
    - 啟動 Class UpdateEvent 物件相關執行緒，用於接收狀態碼與更新棋盤
    - 要求伺服器傳送玩家線上 ID 給客戶端
    - 透過 `Controller.sendMsgToServe()` 傳送狀態碼至伺服器
- 當按下選單的「Connect -> Disconnect」時：
    - 呼叫 `Controller.disconnectSocket()`
    - 傳送狀態碼告知其他玩家已離線
    - 嘗試關閉 Class UpdateEvent 相關執行緒
- 當按下選單的「Connect -> Remote Game Reset」時：
    - 重置棋盤，並告知其他玩家一起重置

# Server
## 特色
- 此伺服器可以做到：
    - 透過狀態碼告知即將進行的操作
    - 動態分配玩家 ID
    - 動態調整房間內玩家數量
    - 呼叫特定狀態碼時，主動關閉 Socket 連線
    - 主要以廣播 (broadcast) 方式進行訊息傳送，除了分配玩家 ID 之外

## 狀態碼
- 100 : 玩家 ID 請求
- 200 : 傳送遊戲進行相關資訊
- 300 : 重置遊戲
- 500 : 對方玩家已離線 (包含在狀態 999 內)
- 999 : 告知伺服器關閉 Socket 連線

## 相關程式碼
- Class Main : 程式啟動接入點
- Class ServerUnit : Server 主要邏輯
    - 啟動程式碼在 `ServerUnit.startAndReceive()` 當中
    - 會呼叫 Class ClientConnect 物件處理 Socket 與伺服器間的連線 (新開執行緒)
    - 會呼叫 Class Room 物件處理遊戲加入、移除 Socket 相關管理操作
    - `ServerUnit.sendAll()` : 廣播訊息
- Class ClientConnect : Socket 與伺服器間的操作邏輯
    - 當執行緒啟動時，會透過 Class BufferedReader 物件讀取傳送到伺服器的狀態碼以及相關資料
    - 並且視情況進行相關操作
- Class Room : 處理遊戲加入、移除 Socket 相關管理操作
    - `Room.addToRoom()` : 加入 Socket 至 Class Room 物件內
    - `Room.removeFromRoom()` : 從 Class Room 物件內移除指定 Socket 
    - `Room.resetPlayerID()` : 當接收到 **狀態 500** 時，重新傳送新的玩家 ID 給還在連線的客戶端
    - `Room.getNowID()` : 取得最後進入的玩家 ID
    - `Room.getSize()` : 取得房間大小
    - `Room.getEnter()` : 取得在房間內的玩家數量