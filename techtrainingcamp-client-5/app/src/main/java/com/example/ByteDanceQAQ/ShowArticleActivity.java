package com.example.ByteDanceQAQ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test_information.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowArticleActivity extends AppCompatActivity {
    private String article;
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);
        //接收传递过来的参数
        final Intent intent = getIntent();
        article = intent.getStringExtra("article");

        System.out.println(article);
        LinearLayout ll_content;
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        TextView tv = (TextView) findViewById(R.id.wenzhang);
        TextView vt = (TextView) findViewById(R.id.title);
        TextView vtime = (TextView) findViewById(R.id.tx_news_simple_photos_time);
        TextView va = (TextView) findViewById(R.id.tx_news_simple_photos_author);
        vt.setText(intent.getStringExtra("title"));
        va.setText(intent.getStringExtra("author"));
        vtime.setText(intent.getStringExtra("publish_time"));
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        imageButton=(ImageButton) findViewById(R.id.button1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //在当前onClick方法中监听点击Button的动作
            public void onClick(View v) {
                System.out.println("我的Button被点击了");
                ShowArticleActivity.this.finish();
            }
        });
        try {
            System.out.println(article);
            JSONObject obj = new JSONObject(article); //json转换
            String data = obj.getString("data");
            System.out.println(data);
            String str = new String();
            int end = 0;
            for (int i = 0; i < data.length(); i++) {
                if (data.charAt(i) == '!' && i + 1 < data.length() && data.charAt(i + 1) == '[') { //提取图片信息
                    if (i > end) {
                        TextView textView = new TextView(ShowArticleActivity.this);
                        textView.setText(data.substring(end, i - 1));//设置文本框里面要显示的文字
                        ll_content.addView(textView);
                    }
                    int k = i + 1, l = 0, r = 0, f = 0;
                    while (k < data.length()) {
                        if (f == 1) break;
                        if (data.charAt(k) == '(') {
                            l = ++k;
                            k++;
                            while (k < data.length()) {
                                if (data.charAt(k) == ')') {
                                    r = k;
                                    k++;
                                    f = 1;
                                    break;
                                }
                                k++;
                            }
                        }
                        k++;
                    }
                    i = k;
                    String s = data.substring(l, r);
                    s = s.substring(0, s.lastIndexOf("."));
                    s = s.toLowerCase();
                    System.out.println(s);
                    int imageId = getResources().getIdentifier(s, "drawable", "com.example.test_information");
                    LinearLayout limage = new LinearLayout(ShowArticleActivity.this);
                    ImageView imageView = new ImageView(ShowArticleActivity.this);
                    limage.addView(imageView);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageResource(imageId);
                    ll_content.addView(limage);
                    end = k;
                }
                if (data.charAt(i) == 'h' && i + 2 < data.length() && data.charAt(i + 1) == 't' && data.charAt(i + 2) == 't') {                 //提取http网页链接
                    if (i > end) {
                        TextView textView = new TextView(ShowArticleActivity.this);
                        textView.setText(data.substring(end, i - 1));//设置文本框里面要显示的文字
                        ll_content.addView(textView);
                    }
                    int k = i + 1, l = 0, r = 0;
                    while (k < data.length()) {
                        if (data.charAt(k) == '\n' || data.charAt(k)==' ') {
                              break;
                        }
                        k++;
                    }
                    String s = data.substring(i, k);
                    System.out.println(s);
                    TextView textView = new TextView(ShowArticleActivity.this);
                    textView.setTextSize(12);
                    textView.setText(Html.fromHtml("<a href="+"'"+s+"'"+">"+s+"</a>"));
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    ll_content.addView(textView);
                    end = k;
                }
            }
            if (end < data.length()) {
                TextView textView = new TextView(ShowArticleActivity.this);
                textView.setText(data.substring(end, data.length() - 1));//设置文本框里面要显示的文字
                ll_content.addView(textView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}