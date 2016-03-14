package com.yat3s.runn1nginstaller;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Yat3s on 10/19/15.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */

public class CacheUtil {
    public static final String FILENAME = "yat3s";

    public static void putData(String key, Object object) {
        put(FILENAME, key, object);
    }

    public static Object getData(String key, Object object) {
        return get(FILENAME, key, object);
    }

    // Basic functions
    public static void put(String filename, String key, Object object) {
        if (null == object) {
            return;
        }
        SharedPreferences sp = App.getContext().getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    public static Object get(String filename, String key, Object defaultObject) {
        SharedPreferences sp = App.getContext().getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    public static void remove(String filename, String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    public static void clear(String filename) {
        SharedPreferences sp = App.getContext().getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    public static boolean contains(String filename, String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    public static Map<String, ?> getAll(String filename) {
        SharedPreferences sp = App.getContext().getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}