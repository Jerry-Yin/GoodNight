package com.hdu.team.hiwanan.util.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by JerryYin on 8/18/16.
 */
public class HashMapUtil {

    /**
     * 根据value获取单个map的key
     * @param map
     * @param value
     * @return
     */
    public static String getKey(HashMap<String, Object> map, Object value){
        String key = null;
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getValue().equals(value)){
                String k = (String) entry.getKey();
                key = k;
            }
        }
        return key;
    }



}
