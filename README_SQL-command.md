Windowsに[Postgresql](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)をインストール。

開発時は`Windows x86-64`のバージョン13.4を使いました。
インストーラ通りに進めれば問題なくインストールできると思います。
途中でSQLを使うためのPosgresqlユーザのパスワードを設定する必要があるのですが、好きな文字列で良いです。

インストール先を変えてなければ、次の場所からsqlを実行することができます。

`C:\Program Files\PostgreSQL\13\bin`

------------------------------------------
コマンドからデータベースを操作する。
```
cd C:\Program Files\PostgreSQL\13\bin
```
データベースを作成する
```
psql -U postgres
postgresのパスワードを入力
CREATE DATABASE account_pass;
```
`account_pass`というデータベースを作成。


------------------------------------------
## SQLファイルを読み込む
`user_pass.sql`は完全なパスを指定しないと読み込めないと思います。
```
psql -U postgres -d account_pass -f user_pass.sql
(postgresのパスワードを入力)
```

コマンド解説
```
-U データベースシステムのユーザ名
-d データベース名
-f .sqlファイルの場所
```

こんなのが表示されればOK
```
BEGIN
INSERT 0 1
INSERT 0 1
INSERT 0 1
INSERT 0 1
COMMIT
```

------------------------------------------
PostfreSQLでデータベースが作成されたか確認するコマンド
```
psql -U postgres -d account_pass
(postgresのパスワードを入力)
select * from user_account_pass;
```

こんなリストが出る
```
 user_name | password
-----------+-----------
 1929040   | Pw1929040
 1929041   | Pw1929041
 1929042   | Pw1929042
 1929043   | Pw1929043
(4 行)
```

------------------------------------------
javaからデータベースに接続して確認する方法
・DBControl.javaファイルでコンソールにデータベースの要素を表示することが可能。
・外部ライブラリに"postgresql-バージョン番号.jar"が必須。開発環境で読み込む必要あり。sqlフォルダに入っています。
