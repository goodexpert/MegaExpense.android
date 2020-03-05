package com.example.apps.mega.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class JsonUtils {
    private final Gson defaultGson;

    public JsonUtils(Gson defaultGson) {
        this.defaultGson = defaultGson;
    }

    public <T extends Object> T getJsonObject(JsonElement element, Type type) {
        T obj = null;
        if (element != null && !(element instanceof JsonNull)) {
            try {
                obj = defaultGson.fromJson(element, type);
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace();
            }
        }
        return obj;
    }

    public <T extends Object> T getJsonObject(String json, Type type) {
        T obj = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                obj = defaultGson.fromJson(json, type);
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace();
            }
        }
        return obj;
    }

    public String toJson(Object obj) {
        String json = null;
        try {
            json = defaultGson.toJson(obj);
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
