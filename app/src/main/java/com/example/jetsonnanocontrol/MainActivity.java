package com.example.jetsonnanocontrol;

import static android.view.KeyEvent.KEYCODE_ENTER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    ImageButton arrowUp;
    ImageButton arrowDown;
    ImageButton arrowLeft;
    ImageButton arrowRigth;
    ImageButton setting;
    EditText getIP;
    int IPtextVisible = 0;
    int countclick=0;
    WebView webView;
    Socket myappSocket;
    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";
    EditText ipConfig;
    Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /******* Declaración de elementos ******/
        // Declaramos los botones de control
        arrowUp = (ImageButton) findViewById(R.id.arrup);
        arrowDown = (ImageButton) findViewById(R.id.arrdw);
        arrowLeft = (ImageButton) findViewById(R.id.arrlef);
        arrowRigth = (ImageButton) findViewById(R.id.arrrig);
        ipConfig = (EditText) findViewById(R.id.ipaddress);
        connect = (Button) findViewById(R.id.connect);

        // Declaramos el botón de configuración
        setting = (ImageButton) findViewById(R.id.config);

        // Declaramos el cuadro de texto IP
        getIP = (EditText) findViewById(R.id.iptext);
        // Declaramos el getIP como invisible
        getIP.setVisibility(View.INVISIBLE);
        ipConfig.setVisibility(View.INVISIBLE);
        connect.setVisibility(View.INVISIBLE);

        // Declaramos webView
        webView = (WebView) findViewById(R.id.webview);

        /****** WEBVIEW *****/
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setWebViewClient(new MyWebViewClient());

        loadMyUrl("https://google.com");

        getIP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int i, KeyEvent event) {
                if(i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE){
                    loadMyUrl(getIP.getText().toString());
                    return true;
                }
                return false;
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMyUrl(getIP.getText().toString());
            }
        });




        /**** Botón de configuración ****/
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IPtextVisible == 0 && countclick == 0) {// Texto invisible -> texto visible
                    getIP.setVisibility(View.VISIBLE);
                    ipConfig.setVisibility(View.VISIBLE);
                    connect.setVisibility(View.VISIBLE);

                    IPtextVisible = 1;
                    countclick = 1;
                }

                if (IPtextVisible == 1 && countclick == 0) { // Texto visible-> Texto invisible
                    getIP.setVisibility(View.INVISIBLE);
                    ipConfig.setVisibility(View.INVISIBLE);
                    connect.setVisibility(View.INVISIBLE);

                    IPtextVisible = 0;
                    countclick = 1;
                }

                countclick = 0;
            }
        });

        /***** Función controles *****/
        arrowUp.setOnClickListener(new View.OnClickListener() { // Recto
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "S";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });

        arrowUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getIPandPort();
                CMD = "U";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
                return false;
            }
        });



        arrowDown.setOnClickListener(new View.OnClickListener() { // Reversa
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "S";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });

        arrowDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getIPandPort();
                CMD = "R";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
                return false;
            }
        });

        arrowRigth.setOnClickListener(new View.OnClickListener() { // Derecha
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "S";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });

        arrowRigth.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getIPandPort();
                CMD = "D";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
                return false;
            }
        });

        arrowLeft.setOnClickListener(new View.OnClickListener() { // Izquierda
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "S";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });

        arrowLeft.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getIPandPort();
                CMD = "I";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
                return false;
            }
        });

    }

    void loadMyUrl(String url){
        boolean matchUrl = Patterns.WEB_URL.matcher(url).matches();
        if (matchUrl){
            webView.loadUrl(url);
        }
        else {
            webView.loadUrl(url);
        }
    }

    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }

    public void getIPandPort(){
        String iPandPort = ipConfig.getText().toString();
        Log.d("MYTEST", "IP String: " + iPandPort);
        String temp[] = iPandPort.split(":");   // Separamos el texto por los ":"
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);
        Log.d("MYTEST", "IP: "+ wifiModuleIp);
        Log.d("MYTEST", "PORT: " + wifiModulePort);

    }

    public class Soket_AsyncTask extends AsyncTask<Void, Void, Void> {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(MainActivity.wifiModuleIp);
                socket = new java.net.Socket(inetAddress, MainActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
            } catch (UnknownHostException e){e.printStackTrace();} catch (IOException e){e.printStackTrace();}
            return null;
        }
    }
}

