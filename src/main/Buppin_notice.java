/*ウインドウを表示するモジュール*/
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
/*リスト形式を使うためのモジュール*/
import java.util.ArrayList;
import java.util.List;

/*ウインドウを表示するモジュール*/
import javax.swing.*;

/*メール送信機能を使うモジュール*/
import javax.mail.internet.MimeMessage;

/*コンソールでキー入力を使うためのモジュール*/
import java.util.Scanner;


public class Buppin_notice extends JFrame {

    //持ち出しリスト宣言
    static List<Object> list = new ArrayList<>();

    /*QRコードを読み取る関数。QrCapture.javaのクラスを使っている。
        持ち出しor返却のアクション名とログインしたユーザIDを引数でもらっている。(表示するため) */
    public void read_qr(String action,String input_id){
        final Thread thread = new Thread(() -> {
            try (QrCapture qr = new QrCapture()) {      //スレッドで実行する。(QRコード読み取りとカメラを開く)
                showMessage(input_id,action,qr.getResult() + "");   //QRコードを見つけた場合、showMessage関数にユーザIDとアクションとQRコードの文字列を渡す。
            } catch (InterruptedException ex) {
                ex.printStackTrace();       //例外エラー発生時。終了
            }
        });
        thread.setDaemon(true); //スレッド処理の開始。
        thread.start();
    }


    //持ち出しor返却を選択するボタンが表示されるウインドウの作成。
    public Buppin_notice(String input_id) {

        /*ウインドウのタイトルとかレイアウトの設定。(JFrameの使い方でググれば色々出てくる。)*/
        setTitle("Main frame");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*持ち出しボタンの生成と押したときの処理内容 */
        add(new JButton(new AbstractAction("持ち出し") {

            @Override
            public void actionPerformed(ActionEvent e) {    //ボタンをクリックしたときのアクションを変数eとしている。
                read_qr(e.getActionCommand(),input_id);  //qrコード読み取り開始。e.getActionCommand()はイベントの名前を取得する(持ち出し)
            }
        }));

        /*返却ボタンの生成と押したときの処理内容 */
        add(new JButton(new AbstractAction("返却") {

            @Override
            public void actionPerformed(ActionEvent e) {    //ボタンをクリックしたときのアクションを変数eとしている。
                read_qr(e.getActionCommand(),input_id);  //qrコード読み取り開始。e.getActionCommand()はイベントの名前を取得する(返却)
            }
        }));

        pack();     //ウインドウのサイズの設定とか
        setVisible(true);   //ウインドウを表示する。
    }


    /*QRコード読み取り後、個数の入力とかを行う関数 */
    private void showMessage(String input_id, String action, String text) {
        MailSettings ms=new MailSettings();     //メールサーバの設定を行う。(処理するタイミング、もっと前でもいいかも…　起動時とか)

        /*個数を入力するウインドウを表示
            Javaで数値だけを入力することができるウインドウの作り方が分からなかったので、アルファベットとかも打てます。(修正するべき点) */
        String kosuu = (JOptionPane.showInputDialog
                (this, text+"の個数"));     //QRコードで読み取った物品名が変数textに入っているので、それの個数を尋ねる表示が出る。

        list.add(action+" "+text+"："+kosuu+"個");  //物品名と個数がリストに追加される。

        String[] y_or_n = {"はい", "いいえ"};        //次のウインドウのための選択肢文字列
        /*続けてアクションするかどうかの確認画面。
            はいを選ぶと、int selectに0が代入される。
            いいえを選ぶと、 selectに1が代入される。 */
        int select = JOptionPane.showOptionDialog(null,
                    "続けて"+action+"しますか？\n",
                    "確認",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,y_or_n,y_or_n[0]);
        
        /*selectの数値によって次の処理を分岐させるswitch文 */
        switch (select) {
            case 0 -> read_qr(action,input_id);     //はいを選んだから再度QRコード読み取り機能を実行する。
            case 1 -> {                             //いいえを選んだから、リストの内容をメールで送信する。
                //list.forEach(System.out::println);    //リスト内容をコンソールに表示する(デバック用)

                MimeMessage contentMessage = ms.smtp_set();     //メールサーバ関連の設定
                ms.send_mail(contentMessage, list);     //メール内容の設定＆送信
                JOptionPane.showMessageDialog(null, "メールを送信しました。");      //メールを送信したことを知らせるウインドウの表示。
            }
        }
    }

    /*メイン関数　ここから実行される */
    public static void main(String[] args) {

        //ユーザ名とパスワードの入力
        Scanner scanner = new Scanner(System.in);

        // キーボード入力を受け付ける
        //IDとパスワードはコンソールに入力する。
        System.out.print("ID：");
        String input_id = scanner.next();
        System.out.print("パスワード：");
        String input_password = scanner.next();

        //データベースにユーザIDとパスワードが存在するか＆一致するかを判定する関数に、入力したIDとパスワードを渡す。
        //ユーザ認証が成功すれば、変数login_checkにはTrue、失敗すればFalseが格納される。
        boolean login_check= LoginLogic.login_sql(input_id,input_password);
        //変数login_checkのTrueかFalseによって処理内容を変える。
        if(login_check){
            System.out.println("認証成功");     //Trueなら認証成功なので、持ち出しor返却のウインドウを表示する。
            list.add("ユーザID："+input_id);    //ユーザ認証に成功したユーザIDをリストに格納する。
            new Buppin_notice(input_id);
        }else{
            System.out.println("IDまたはパスワードが違います。");       //認証失敗。
        }

    }
}