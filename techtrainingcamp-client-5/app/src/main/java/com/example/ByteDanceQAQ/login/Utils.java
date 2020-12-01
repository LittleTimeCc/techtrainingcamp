package com.example.ByteDanceQAQ.userlogin;


import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static boolean saveUserInfo(Context context, String token,String time) {
        SharedPreferences sp = context.getSharedPreferences("data2", Context.MODE_PRIVATE);//数据自己可用
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("tok", token);
        edit.putString("time",time);
        edit.commit();
        return true;//存储成功
    }

    // 从data2.xml中获取token
    public static Map<String, String> getUserInfo(Context context,String nowTime) throws ParseException {
        SharedPreferences sp = context.getSharedPreferences("data2", Context.MODE_PRIVATE);
        String token = sp.getString("tok", null);
        String time = sp.getString("time", null);
        Map<String, String> userMap = new HashMap<String, String>();
        if(token==null || time==null) return null;
        boolean judge= Token.getDiffTime(time,nowTime);
        if(judge==false){
            SharedPreferences.Editor edit = sp.edit();
            edit.clear();
            edit.commit();
            return null;
        }
        userMap.put("tok",token);
        userMap.put("time", time);
        return userMap;
    }
}