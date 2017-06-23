package ch.redacted.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hfatih on 22.7.2014.
 */
public class SharedPrefsHelper {

    public static final String FILE_PROFILE = "profile";
    public static final String KEY_PROFILE = "profile";
    private static SharedPrefsHelper helperInstance;
    private Context context;

    private SharedPrefsHelper(Context c) {
        this.context = c;
    }

    public static SharedPrefsHelper getInstance(Context context) {
        if (helperInstance != null) return helperInstance;
        else {
            helperInstance = new SharedPrefsHelper(context);
            return helperInstance;
        }
    }

    public void writeSharedPrefs(String filename, String key, String valuestr, Integer valueint, Boolean valuebool) {
        if (valuestr != null) {
            getSharedPrefsEditor(filename).putString(key, valuestr).commit();
        }
        if (valueint != null) {
            getSharedPrefsEditor(filename).putInt(key, valueint).commit();
        }
        if (valuebool != null) {
            getSharedPrefsEditor(filename).putBoolean(key, valuebool).commit();
        }
    }

    public SharedPreferences.Editor getSharedPrefsEditor(String filename) {
        return getSharedPrefs(filename).edit();
    }

    public SharedPreferences getSharedPrefs(String filename) {
        return context.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    public void deleteSharedPrefs(String filename) {
        getSharedPrefsEditor(filename).clear().commit();
    }

    public String readSharedPrefsString(String filename, String key, String defaultstr) {
        return getSharedPrefs(filename).getString(key, defaultstr);
    }

    public Integer readSharedPrefsInteger(String filename, String key, Integer defaultinteger) {
        return getSharedPrefs(filename).getInt(key, defaultinteger);
    }

    public Boolean readSharedPrefsBoolean(String filename, String key, Boolean defaultboolean) {
        return getSharedPrefs(filename).getBoolean(key, defaultboolean);
    }
}
