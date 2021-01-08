package com.asmatullah.spaceapp.common.core.db.local.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreferenceSettings implements SharedPreferences.Editor {

    protected Context mContext;
    private SharedPreferences mSP;
    private String mPrefName;
    private SharedPreferences.Editor mEditor;

    private static List<String> allPrefs = new ArrayList<>();

    public PreferenceSettings(Context context, String prefName) {
        mContext = context.getApplicationContext();
        mPrefName = prefName;

        allPrefs.add(prefName);
    }

    private SharedPreferences getSP() {
        if (mSP == null) {
            mSP = mContext.getSharedPreferences(mPrefName, Context.MODE_PRIVATE);
        }
        return mSP;
    }

    @SuppressLint("CommitPrefEdits")
    private SharedPreferences.Editor getEditor() {
        if (mEditor == null) {
            mEditor = getSP().edit();
        }
        return mEditor;
    }

    @Override
    public SharedPreferences.Editor putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value);
        return this;
    }

    @Override
    public SharedPreferences.Editor putInt(String key, int value) {
        getEditor().putInt(key, value);
        return this;
    }

    @Override
    public SharedPreferences.Editor putLong(String key, long value) {
        getEditor().putLong(key, value);
        return this;
    }

    @Override
    public SharedPreferences.Editor putFloat(String key, float value) {
        getEditor().putFloat(key, value);
        return this;
    }

    @Override
    public SharedPreferences.Editor putString(String key, String value) {
        getEditor().putString(key, value);
        return this;
    }

    @Override
    public SharedPreferences.Editor putStringSet(String key, @Nullable Set<String> values) {
        getEditor().putStringSet(key, values);
        return this;
    }

    public SharedPreferences.Editor putLongSet(String key, @Nullable Set<Long> values) {
        Set<String> stringSet = new HashSet<>();
        for (Long num : values) {
            stringSet.add(String.valueOf(num));
        }
        getEditor().putStringSet(key, stringSet);
        return this;
    }

    public boolean contains(String key) {
        return getSP().contains(key);
    }

    @Override
    public SharedPreferences.Editor clear() {
        getEditor().clear();
        return this;
    }

    public static void clearAll(Context context) {
        for (String prefName : allPrefs) {
            context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit().clear();
        }

        allPrefs.clear();
    }

    @Override
    public boolean commit() {
        boolean success = false;
        SharedPreferences.Editor mEditor = this.mEditor;
        if (mEditor != null) {
            success = mEditor.commit();
        }
        this.mEditor = null;
        return success;
    }

    @Override
    public void apply() {
        commit();
    }

    public boolean getBoolean(String key) {
        return getSP().getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getSP().getBoolean(key, defValue);
    }

    public int getInt(String key) {
        return getSP().getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        return getSP().getInt(key, defValue);
    }

    public long getLong(String key) {
        return getSP().getLong(key, 0);
    }

    public long getLong(String key, long defValue) {
        return getSP().getLong(key, defValue);
    }

    public float getFloat(String key) {
        return getSP().getFloat(key, 0);
    }

    public float getFloat(String key, long defValue) {
        return getSP().getFloat(key, defValue);
    }

    public String getString(String key) {
        return getSP().getString(key, null);
    }

    public String getString(String key, String defValue) {
        return getSP().getString(key, defValue);
    }

    public Set<String> getStringSet(String key) {
        return getSP().getStringSet(key, new HashSet<>());
    }

    @Override
    public PreferenceSettings remove(String key) {
        getEditor().remove(key);
        return this;
    }

    public Map<String, ?> getAll() {
        return getSP().getAll();
    }

    public Set<Long> getLongSet(String key) {
        Set<Long> longSet = new HashSet<>();

        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher numberMatcher = numberPattern.matcher("");

        for (String str : getSP().getStringSet(key, new HashSet<>())) {
            if (str != null && !str.isEmpty() && numberMatcher.reset(str).matches()) {
                longSet.add(Long.valueOf(str));
            }
        }

        return longSet;
    }
}