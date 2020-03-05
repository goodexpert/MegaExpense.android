package com.example.apps.mega.utils;

import android.content.SharedPreferences;

import com.google.gson.JsonSyntaxException;

import java.util.Set;

public class PreferenceUtils {
    private final SharedPreferences sharedPreferences;
    private final JsonUtils jsonUtils;

    public PreferenceUtils(SharedPreferences sharedPreferences, JsonUtils jsonUtils) {
        this.sharedPreferences = sharedPreferences;
        this.jsonUtils = jsonUtils;
    }

    public Boolean hasPreferences(String key) {
        return sharedPreferences.contains(key);
    }

    public void resetPreferences() {
        sharedPreferences.edit().clear().apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return sharedPreferences.getStringSet(key, defValue);
    }

    public <T extends Object> T getJsonObject(String key, Class<T> clx) {
        T obj = null;

        try {
            String json = sharedPreferences.getString(key, null);
            if (json != null) {
                obj = jsonUtils.getJsonObject(json, clx);
            }
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public void putPreferences(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value != null) {
            editor.putBoolean(key, value);
        } else {
            editor.remove(key);
        }
        editor.apply();
    }

    public void putPreferences(String key, Integer value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value != null) {
            editor.putInt(key, value);
        } else {
            editor.remove(key);
        }
        editor.apply();
    }

    public void putPreferences(String key, Long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value != null) {
            editor.putLong(key, value);
        } else {
            editor.remove(key);
        }
        editor.apply();
    }

    public void putPreferences(String key, Float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value != null) {
            editor.putFloat(key, value);
        } else {
            editor.remove(key);
        }
        editor.apply();
    }

    public void putPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value != null) {
            editor.putString(key, value);
        } else {
            editor.remove(key);
        }
        editor.apply();
    }

    public void putPreferences(String key, Set<String> value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value != null) {
            editor.putStringSet(key, value);
        } else {
            editor.remove(key);
        }
        editor.apply();
    }

    public void putPreferences(String key, Object obj) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (obj != null) {
            String json = jsonUtils.toJson(obj);
            if (json != null) {
                editor.putString(key, json);
            }
        } else {
            editor.remove(key);
        }
        editor.apply();
    }
}
