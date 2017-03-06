package com.hdu.team.hiwanan.model;

/**
 * Created by JerryYin on 10/14/16.
 */

public class HiMaps {

    private String key;
    private String value;

    public HiMaps(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
