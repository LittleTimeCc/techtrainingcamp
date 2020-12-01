package com.example.ByteDanceQAQ;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.example.test_information.R;
import com.example.ByteDanceQAQ.info_implement.MulRecyclerViewAdapter;
import com.example.ByteDanceQAQ.info_implement.NewsPhotoBean;
import com.example.ByteDanceQAQ.info_implement.RecyclerViewDivider;
import com.example.ByteDanceQAQ.userlogin.Token;
import com.example.ByteDanceQAQ.userlogin.LoginActivity;
import com.example.ByteDanceQAQ.userlogin.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;


public class MainActivity extends AppCompatActivity {
    private String Path = "metadata.json";
    private String strJson;
    private RecyclerView recyclerView;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private Handler handler = new Handler();
    private int page = 0;
    private List<NewsPhotoBean> list = new ArrayList<NewsPhotoBean>();
    private MulRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    //private DealArticle dealArticle=new DealArticle();
    private NewsPhotoBean news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            strJson = getJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(strJson);
        // Log.d(strJson, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        ptrClassicFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrClassicFrameLayout.autoRefresh(true);
            }
        }, 150);
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        list.clear();
                        try {
                            //JSONObject jsonObject = new JSONObject(strJson);
                            JSONArray jsonArray = new JSONArray(strJson);
                            for (int i = 0; i < 5; i++) {
                                NewsPhotoBean newsPhotoBean = new NewsPhotoBean();
                                newsPhotoBean.setId(jsonArray.getJSONObject(i).getString("id"));
                                newsPhotoBean.setType(jsonArray.getJSONObject(i).getInt("type"));
                                newsPhotoBean.setTitle(jsonArray.getJSONObject(i).getString("title"));
                                newsPhotoBean.setF_time(jsonArray.getJSONObject(i).getString("publishTime"));
                                newsPhotoBean.setAuthor(jsonArray.getJSONObject(i).getString("author"));
                                String temp = jsonArray.getJSONObject(i).getString("cover");
                                if (temp.charAt(0) == '[') {
                                    temp = temp.substring(1, temp.length() - 1);
                                }
                                StringTokenizer st = new StringTokenizer(temp, ",");
                                List<String> ls = new ArrayList<>();
                                while (st.hasMoreTokens()) {
                                    String t = st.nextToken();
                                    if (t.charAt(0) == '\"') {
                                        t = t.substring(1, t.length() - 1);
                                    }
                                    t = t.toLowerCase();
                                    Log.d(t, "run23: ");
                                    ls.add(t);
                                }
                                for (String s : ls) {
                                    System.out.println(s);
                                }
                                newsPhotoBean.setList(ls);
                                list.add(newsPhotoBean);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.refreshComplete();
                        ptrClassicFrameLayout.setLoadMoreEnable(true);
                        Log.d("TAG", "正在刷新...");
                    }
                }, 1500);
            }
        });
        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // JSONObject jsonObject = new JSONObject(strJson);
                            JSONArray jsonArray = new JSONArray(strJson);
                            for (int i = 0; i < 5; i++) {
                                NewsPhotoBean newsPhotoBean = new NewsPhotoBean();
                                newsPhotoBean.setId(jsonArray.getJSONObject(i).getString("id"));
                                newsPhotoBean.setType(jsonArray.getJSONObject(i).getInt("type"));
                                newsPhotoBean.setTitle(jsonArray.getJSONObject(i).getString("title"));
                                newsPhotoBean.setF_time(jsonArray.getJSONObject(i).getString("publishTime"));
                                newsPhotoBean.setAuthor(jsonArray.getJSONObject(i).getString("author"));
                                List<String> ls = new ArrayList<>();
                                String temp = jsonArray.getJSONObject(i).getString("cover");
                                if (temp.charAt(0) == '[') {
                                    temp = temp.substring(1, temp.length() - 1);
                                }
                                StringTokenizer st = new StringTokenizer(temp, ",");
                                while (st.hasMoreTokens()) {
                                    String t = st.nextToken();
                                    if (t.charAt(0) == '\"') {
                                        t = t.substring(1, t.length() - 1);
                                    }
                                    t = t.toLowerCase();
                                    Log.d(t, "run: ");
                                    ls.add(t);
                                }
                                for (String s : ls) {
                                    System.out.println(s);
                                }
                                newsPhotoBean.setList(ls);
                                list.add(newsPhotoBean);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.loadMoreComplete(true);
                        page++;
                        Log.e("TAG", page + "页");
                        Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });
        adapter = new MulRecyclerViewAdapter(this, list);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                String nowTime= Token.getNowDateTime();
                Map<String,String> map= null;
                try {
                    Context context=MainActivity.this;
                    map = Utils.getUserInfo(context,nowTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(map==null){
                    // System.out.println(map.get("tok"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, LoginActivity.class);//从MainActivity页面跳转至显示登录页面
                            MainActivity.this.startActivity(intent);
                        }
                    });
                }else {
                    Log.d("tag", "position=" + position);
                    Log.d("id", "id=" + list.get(position).getId());
                    String id = new String();
                    id = list.get(position).getId();
                    Context context = MainActivity.this;
                    try {
                        String[] strArray = SaveArticle.getArticleInfo(context, id);                    //看文章是否缓存到过本地
                        System.out.println(strArray.length);
                        if (strArray.length > 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, ShowArticleActivity.class);//从MainActivity页面跳转至显示文章页面
                                    int i = 0;
                                    intent.putExtra("title", strArray[i++]);
                                    intent.putExtra("publish_time", strArray[i++]);
                                    intent.putExtra("author", strArray[i++]);
                                    intent.putExtra("article", strArray[i++]);
                                    MainActivity.this.startActivity(intent);
                                }
                            });
                        } else {
                            NewsPhotoBean item = new NewsPhotoBean();
                            item = list.get(position);
                            requestArticle(id, item, position,map.get("tok").toString());
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void initView() {
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.test_list_view_frame);
        recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new RecyclerViewDivider(MainActivity.this, LinearLayoutManager.VERTICAL));

    }


    public String getJson() throws IOException {  //读取json文件信息
        AssetManager assetManager = getAssets(); //获得assets资源管理器
        InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open("metadata.json"), "UTF-8"); //使用IO流读取json文件内容
        BufferedReader br = new BufferedReader(inputStreamReader);//使用字符高效流
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();
        inputStreamReader.close();
        return builder.toString();
    }

    public void requestArticle(String id, NewsPhotoBean item, int position,String token)  {                   //点击请求文章内容
            Log.d(id, "getArticle: ");
            OkHttpClient client = new OkHttpClient.Builder()
                    .authenticator(new Authenticator() {
                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            return response.request().newBuilder().header("Authorization", "Bearer "+token).build();
                        }
                    })
                    .build();
            Request request = new Request.Builder().url("https://vcapi.lvdaqian.cn/article/" + id + "?markdown=false").build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("google.sang", "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String t = new String(response.body().string());
                        String[] array = {item.getTitle().toString(), item.getF_time().toString(), item.getAuthor().toString(), t};
                        Context context = MainActivity.this;
                        SaveArticle.saveArticleInfo(context, array, item.getId().toString());
                        showResponse(t, item, position);

                    }
                }
            });
    }

    private void showResponse(final String article, final NewsPhotoBean item, final int position) {       //跳转到文章页面并携带信息
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShowArticleActivity.class);//从MainActivity页面跳转至显示文章页面
                intent.putExtra("article", article);    //参数：name   value
                intent.putExtra("author", list.get(position).getAuthor());
                intent.putExtra("publish_time", list.get(position).getF_time());
                intent.putExtra("title", list.get(position).getTitle());
                MainActivity.this.startActivity(intent);
            }
        });
    }


}