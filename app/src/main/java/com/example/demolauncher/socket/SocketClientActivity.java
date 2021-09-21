package com.example.demolauncher.socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demolauncher.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SocketClientActivity extends AppCompatActivity {
    private static final String TAG = "SocketClientActivity";
    private Button sentMessageButton;
    private EditText contentEdit;
    private Socket clientSocket;
    private Handler mHandler;
    private Intent serverIntent;
    private TextView contentShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_socket);
        initView();
    }

    private void initView(){
        contentEdit = findViewById(R.id.message_from_client_edit);
        sentMessageButton = findViewById(R.id.sent_message_button);
        contentShow = findViewById(R.id.chart_content_show);
        startServer();
        setViewListener();
        initRes();
    }
    private void initRes(){
        mHandler = new Handler(getMainLooper());
        setTitle("Socket聊天室");
        connectServer();
    }

    private void setViewListener(){
        sentMessageButton.setOnClickListener((View view) -> {
            setMessage();
        });
    }

    private void startServer(){
        serverIntent = new Intent(this , ServerSocketService.class);
        startService(serverIntent);
    }

    private void stopServer(){
        stopService(serverIntent);
    }

    private void connectServer(){
        new Thread(() -> {
            connect();
        }).start();
    }

    private PrintStream ps = null;
    private void connect(){
        Socket socket = null;
        while (socket == null){
            try {
                socket = new Socket(getIPAddressForNetwork() , 6688);
                clientSocket = socket;
                ps = new PrintStream(socket.getOutputStream());
                mHandler.post(() -> {
                    setTitle("Socket聊天室(" + getIPAddressForNetwork() + "/6688)");
                });
                new Thread(() -> {
                    listenerServer(clientSocket);
                }).start();
            }catch (IOException e){
                SystemClock.sleep(1000);
                Log.d(TAG , "connect fail");
                mHandler.post(() -> {
                    setTitle("Socket聊天室(连接失败)");
                });
            }
        }
    }

    String content = null;
    private void listenerServer(Socket socket){
            if (socket != null){
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    content = null;
                    while ((content = br.readLine()) != null){
                        Log.d(TAG , "content=" + content);
                        mHandler.post(() -> {
                            getMessageFromServer(content);
                        });
                    }
                }catch (IOException i){
                    Log.d(TAG , "listener server fail");
                }
          }
    }

    private void getMessageFromServer(String msg){
        if (msg.equals(contentEdit.getText().toString())){
            contentShow.setText(contentShow.getText().toString() + "\n客户端:" + msg);
            contentEdit.setText("");
        }else {
            contentShow.setText(contentShow.getText().toString() + "\n服务器:" + msg);
        }
    }

    private void setMessage(){
        if (ps != null){
            if (!contentEdit.getText().toString().isEmpty()){
                new Thread(() -> {
                    ps.println(contentEdit.getText().toString());
                }).start();
            }else {
                Toast.makeText(this , "请输入内容" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getIPAddressForNetwork() {
        try {
            List<String> ipv4 = new ArrayList<>();
            Enumeration<NetworkInterface> enum1=NetworkInterface.getNetworkInterfaces();
            if (enum1 != null){
                List<NetworkInterface> nilist = Collections.list(enum1);
                for (NetworkInterface ni : nilist) {
                    List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                    Log.d(TAG , "address="  + ialist.size());
                    for (InetAddress address : ialist) {
                        if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                            ipv4.add(address.getHostAddress());
                            Log.d(TAG , "IPv4="  + ipv4);
                        }
                    }
                }
            }
            if (ipv4.size() > 0) return ipv4.get(ipv4.size() - 1);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (clientSocket != null) clientSocket.close();
        }catch (IOException i){
        }
        stopServer();
    }
}
