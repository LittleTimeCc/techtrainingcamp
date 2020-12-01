package com.example.ByteDanceQAQ;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SaveArticle {
    private static final String IDENTIFY ="data" ;
    public static boolean saveArticleInfo(Context context, String[] values, String id) {
        String regularEx = "#####%%%";
        String str = "";
        SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sharedPref.edit();
            et.putString(id, str);
            et.commit();
        }
        return true;//存储成功
    }

    // 从data.xml中获取values
    public static String[] getArticleInfo(Context context, String id) throws ParseException {
        String regularEx = "#####%%%";
        String[] str = null;
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String values;
        values = sp.getString(id, "");
        str = values.split(regularEx);
        return str;
    }
}
