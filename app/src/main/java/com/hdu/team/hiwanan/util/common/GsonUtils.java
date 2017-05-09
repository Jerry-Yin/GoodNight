package com.hdu.team.hiwanan.util.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.hdu.team.hiwanan.util.HiLog;
import com.koushikdutta.async.http.body.JSONArrayBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 3/11/17.
 */

public class GsonUtils {

    /**
     * 将Json对象（或者Object对象）转化成string
     * @param json
     * @return
     */
    public static String parseJsonToString(Object json){
        Gson gson = new Gson();
        String data = gson.toJson(json);
        return data;
    }



    /**
     * 将 String  的json数据转化为对应的实体类对象
     * @param jsonData
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T parseJsonToClass(String jsonData, Class<T> tClass){
        Gson gson = new Gson();
//        T object = gson.fromJson(jsonData, tClass);
        T object = gson.fromJson(jsonData, tClass);
        return object;
    }


    public static <T> List<T> parseJsonArrayToList(String array, Class<T> tClass){
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        HiLog.d("json = ", array);
//        JSONArray jsonArray = JSONArray.fromObject(array);
//        for (int i=0; i<jsonArray.size(); i++){
//            list.add(gson.fromJson(jsonArray.get(i).toString(), tClass));
//        }
        return list;
    }
}
