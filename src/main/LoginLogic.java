import java.sql.*;      //javaでSQLを用いてデータベースを使うためのモジュール
import java.util.Objects;

public class LoginLogic {

    //SQLサーバのアドレス、SQLのユーザ名、SQLのパスワードを宣言
        /*　解説
        "jdbc:postgresql://"はプロトコル名。"https://"みたいなやつ。
        localhostはサーバ名。今回は自分自身のPCにPostgresqlをインストールして利用している。
        5432はpostgresqlのデフォルトサービスポート。
        account_passはデータベース名
        */
    private static final String url="jdbc:postgresql://localhost:5432/ACCOUNT_PATH"; //書換
    private static final String user="USERNAME"; //書換
    private static final String pass="PASSWORD"; //書換

    /*ユーザIDとパスワードを入れると、データベースに存在するかチェックして、あればTrue、無いならFalseを返す関数
     つまり、Trueだとログイン成功。Falseだとログイン失敗
     */
    protected static boolean login_sql(String input_id, String input_password){

        //Postgresqlへの接続情報    java.sql関連の変数宣言
        Connection con = null;
        Statement st;

        //JDBCドライバの定義(JAVAでSQLを操作するドライバ)
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();        //例外処理
        }

        //宣言した内容でPostgresqlサーバに接続
        try {
            con=DriverManager.getConnection(url,user,pass);
        } catch (SQLException e) {
            e.printStackTrace();        //例外処理
        }
        try {
            st= Objects.requireNonNull(con).createStatement();  //データベースを開いてstに格納する
        } catch (SQLException e) {
            e.printStackTrace();        //例外処理
        }


        //SQL文を使う
        String sql = "SELECT * FROM user_account_pass WHERE user_name = ? and password = ?;";

        try {
            PreparedStatement ps= con.prepareStatement(sql);        //SQL文をJAVAで扱えるようにしてpsに格納


            ps.setString(1, input_id);  //?の部分に文字列を代入

            ps.setString(2, input_password);    //?の部分に文字列を代入

            ResultSet rs;
            rs = ps.executeQuery();     //SQL文を実行して結果をrsに格納。ユーザ名とパスワードが一致すると返ってくる。


            if (rs.next()) {
                return true;    //ユーザ認証できたらTrueを返す
            } else {
                return false;   //ユーザ認証できなかったらFalseを返す
            }
        }catch(SQLException e) {
            e.printStackTrace();
            return false;       //例外が起こったらFalseを返す
        }
    }
}
