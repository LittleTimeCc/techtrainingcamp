package com.example.ByteDanceQAQ.userlogin;
import com.example.ByteDanceQAQ.MainActivity;
import com.example.test_information.R;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etNumber;
    private  EditText etPassword;
    private  String token=null;
    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        String nowTime= Token.getNowDateTime();               //是否token超时
        Map<String,String> map= null;
        try {
            map = Utils.getUserInfo(this,nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        etNumber=(EditText)findViewById(R.id.et_number);
        etPassword=(EditText)findViewById(R.id.et_password);
        findViewById(R.id.send_request).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){                                         //点击按钮函数
        String number = etNumber.getText().toString().trim();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        sendRequestWithOkHttp(number, password, this);
    }

    private void sendRequestWithOkHttp(String username, String password, Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                            .add("username",username)
                            .add("password",password)
                            .build();
                    Request request=new Request.Builder().url("https://vcapi.lvdaqian.cn/login").post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject result = new JSONObject(responseData);
                    String nowTime= Token.getNowDateTime();

                    responseData=result.get("token").toString();
                    boolean isSaveSuccess=Utils.saveUserInfo(context,responseData,nowTime);
                    token=responseData;
                    Looper.prepare();
                    Toast.makeText( context,"登陆成功",Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, MainActivity.class);//从MainActivity页面跳转至显示登录页面
                            LoginActivity.this.startActivity(intent);
                        }
                    });
                    Looper.loop();
                    if (isSaveSuccess==true){
                        Looper.prepare();
                        Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    }else {
                        Looper.prepare();
                        Toast.makeText(context,"保存失败",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}