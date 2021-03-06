# 物品管理アプリ

データベースを構築してから実行してください。データベースの構築や触り方は別のテキストファイルに書いてます。

プログラムは`src`フォルダに入っている。
必要なライブラリは`module`フォルダに入っている。
javaの開発環境とかで、`module`フォルダの中身をすべてインポートするとsrcのプログラムが動きます。



## 処理内容
実行すると、コンソールにIDとパスワードを入力することができます。
ユーザ認証情報は`user_pass.sql`ファイルに記載されているものが使えます。
ユーザ認証できなかった場合は、プログラムが終了する感じになっています。

ユーザ認証に成功すると、「持ち出し」「返却」ボタンのウインドウが表示されます。
ボタンを押すと、どちらもQRコード読み取りのカメラ画像が表示されます。パソコンで実行する場合はWebカメラが必要です。
2つの画像ファイルは`a`と`b`の文字列が入っているQRコードです。動作テスト的に作りました。

追加で物品を追加する場合は、再度QRリーダが起動します。
物品を追加しない場合は、ログインしたユーザIDと物品の情報がメールで送信されるようになっています。



#### ※注意

ユーザ認証のためのデータベースにはパスワードはハッシュ化した文字列を格納しておいて、認証時にクライアント側で入力したパスワードの文字列をハッシュ化して、データベースのハッシュ化されたパスワードと比較するのが安全なやり方ですが、完全に手抜きです。データベースにそのままパスワードが入っています。
もしもデータベースサーバが攻撃されて、データベースが盗まれた場合、ユーザ名とパスワードが流出します。
パスワードをハッシュ化しておけば、生のパスワードはわかりません。


### 使用ライブラリ
- QRコードの処理
ZXing
https://github.com/zxing/zxing

- Webカメラを使う
Webcam Capture
http://www.webcam-capture.sarxos.pl/

- Webcam Captureに必要なライブラリ
slf4j関連
bridj関連

- メール送信機能
javax.mail

- データベース操作
postgresql-42.2.23.jar

### 開発環境
- OS: Windows 10 21H1 x64
- IDE: IntelliJ IDEA 2021.2 (Community Edition)
- Java:  Java SE Development Kit 16.0.2 x64
- RDBMS: PostgreSQL Database 13.4 x64
