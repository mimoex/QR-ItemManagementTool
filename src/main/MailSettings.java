import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.List;

public class MailSettings {

    /*SMTPサーバの設定を行う関数。設定内容を返す*/
    public MimeMessage smtp_set(){
        Properties props = new Properties();
//SMTPサーバの設定。"smtp.test.com"に自分のメールサーバの設定を記述
        props.setProperty("mail.smtp.host", "smtp.test.com");   //書換
//SSL用にポート番号を変更
        props.setProperty("mail.smtp.port", "465");   //書換
//タイムアウト設定
        props.setProperty("mail.smtp.connectiontimeout", "60000");
        props.setProperty("mail.smtp.timeout", "60000");

//認証
        props.setProperty("mail.smtp.auth", "true");
//SSLを使用するとこはこの設定が必要
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", "465");

//propsに設定した情報を使用して、sessionの作成
        final Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                //送信者のメールアカウント情報を記載
                return new PasswordAuthentication("sender@test.com", "sendermailpassword");   //書換
            }
        });

        //先ほどの変数sessionを設定する

        return new MimeMessage(session);

    }

    /* 引数listをメールとして送信する関数*/
    public void send_mail(MimeMessage contentMessage, List list){
        //メールのコンテンツ情報
        try {
            //送信元アドレス、表示名、文字コードを設定
            Address addFrom = new InternetAddress("sender@test.com", "`物品管理通知システム`", "UTF-8");   //書換
            contentMessage.setFrom(addFrom);
            //送信先アドレス、表示名、文字コードを設定
            Address addTo = new InternetAddress("reciver@test.com");   //書換
            contentMessage.addRecipient(Message.RecipientType.TO, addTo);   //送信先を設定。

            contentMessage.setSubject("物品管理通知", "UTF-8");       //件名を設定
            contentMessage.setText(String.valueOf(list), "UTF-8");      //メールの内容
            contentMessage.setSentDate(new Date());     //日付等の設定
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //メール送信
        try {
            //先ほどのcontentMessageを設定する
            Transport.send(contentMessage);
        } catch (MessagingException e) {
            //認証失敗
            e.printStackTrace();
        }//smtpサーバへの接続失敗

    }
}
