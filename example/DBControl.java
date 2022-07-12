import java.sql.*;      //SQL関連のモジュール

public class DBControl {
    public static void main(String[] args) throws Exception{
        //Postgresqlへの接続情報    java.sql関連の変数宣言
        Connection con;
        Statement st;
        ResultSet rs;

        //SQLサーバのアドレス、SQLのユーザ名、SQLのパスワードを宣言
        /*　解説
        "jdbc:postgresql://"はプロトコル名。"https://"みたいなやつ。
        localhostはサーバ名。今回は自分自身のPCにPostgresqlをインストールして利用している。
        5432はpostgresqlのデフォルトサービスポート。
        account_passはデータベース名
        */
        String url="jdbc:postgresql://localhost:5432/account_pass";
        String user="postgres";
        String pass="PASSWORDTESTTESTTEST";

        //JDBCドライバの定義(JAVAでSQLを操作するドライバ)
        Class.forName("org.postgresql.Driver");

        //宣言した内容でPostgresqlサーバに接続
        con=DriverManager.getConnection(url,user,pass);
        st=con.createStatement();

        //SQL文を使う
        String sql="SELECT * FROM user_account_pass;";   //データベースを表示するだけ

        rs = st.executeQuery(sql);  //SQLクエリを実行して、変数rsに処理内容を格納する。


        //結果の表示
        //SQLの実行結果を格納したと言っても、javaでは1行つづしか読み出せないから、ループで読み出す。
        //SQLの実行結果を順番に読み出すループ。結果の次の要素がなくなれば終了。
        while(rs.next()) {      
            System.out.print(rs.getString("user_name") + ", ");
            System.out.println(rs.getString("password"));
        }

        //Postgresqlの接続を切断
        rs.close();
        st.close();
        con.close();


    }
}
