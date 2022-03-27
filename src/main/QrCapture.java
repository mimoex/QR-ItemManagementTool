//JFrame関連のライブラリ(カメラの画像を表示するためのもの)
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.util.concurrent.Exchanger;
import javax.swing.JFrame;

//カメラ・QRコード読み取りライブラリ
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


public class QrCapture extends JFrame implements Closeable {

    //シリアライズとか変数の初期化
    private static final long serialVersionUID = 1L;

    private Webcam webcam = null;
    private BufferedImage image = null;
    private Result result = null;
    private Exchanger<String> exchanger = new Exchanger<String>();

    public QrCapture() {

        super();        //JFrame呼び出し

        //選択画面の生成
        setLayout(new FlowLayout());
        setTitle("Capture");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //カメラウィンドウを閉じたらカメラをクローズ
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        webcam = Webcam.getDefault();       //カメラの設定読み込み
        webcam.setViewSize(WebcamResolution.QVGA.getSize());    //カメラの解像度指定
        webcam.open();      //カメラをオープン

        //カメラからの画像を表示するウィンドウ
        add(new WebcamPanel(webcam));

        pack();     //表示するウインドウのサイズの調整。
        setVisible(true);   //ウインドウを表示する。

        //スレッドで実行(カメラを動かしながら、QRコードの文字列を読み込む)
        final Thread daemon = new Thread(new Runnable() {

            @Override
            public void run() {
                while (isVisible()) {    //カメラ画像が読み込まれているとき、QRコードを読み取る。
                    read();
                }
            }
        });
        daemon.setDaemon(true);     //スレッドで処理を開始する。
        daemon.start();
    }

    //画像からQRコードを認識して、バイナリ値を読み込む関数・処理
    private static BinaryBitmap toBinaryBitmap(BufferedImage image) {
        return new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
    }

    //カメラ画像を読み込む関数
    private void read() {

        if (!webcam.isOpen()) {     //カメラが開けないとき
            return;     //終了
        }
        if ((image = webcam.getImage()) == null) {  //カメラから画像を受け取れないとき
            return;     //終了
        }

        try {
            result = new MultiFormatReader().decode(toBinaryBitmap(image));     //QRコードを読み取ってresultに文字列を入れる
        } catch (NotFoundException e) {
            return; // QRコードが見つからないとき　終了
        }

        if (result != null) {       //QRコードが見つからないとき→見つかるまでカメラ画像を読み込む
            try {
                //QRコードがあったとき、カメラを閉じてQRコードの文字列を返す
                close();
                exchanger.exchange(result.getText());

            } catch (InterruptedException e) {
                return; //終了
            } finally {
                dispose();  //カメラ画像とかをメモリから開放する？
            }
        }
    }

    public String getResult() throws InterruptedException {
        return exchanger.exchange(null);
    }

    #Webカメラを閉じる関数
    @Override
    public void close() {
        webcam.close();
    }
}