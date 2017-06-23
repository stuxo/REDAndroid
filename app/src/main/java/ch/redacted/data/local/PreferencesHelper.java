package ch.redacted.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import ch.redacted.injection.ApplicationContext;

@Singleton public class PreferencesHelper {

	public static final String PREF_FILE_NAME = "android_boilerplate_pref_file";
	public static final String PREF_COOKIE_FILE_NAME = "CookiePersistence";
	public static final String PREF_AUTH_KEY = "pref_auth_key";
	public static final String PREF_PASS_KEY = "pref_pass_key";
	public static final String PREF_COOKIE_KEY = "pref_cookie_set_key";
	public static final String PREF_USER_ID_KEY = "pref_user_id_key";

	public static final String PREF_WM_HOST_KEY = "pref_wm_host";
	public static final String PREF_WM_PASSWORD_KEY = "pref_wm_password";
	public static final String PREF_WM_PORT_KEY = "pref_wm_port";
	public static final String PREF_WM_USER_KEY = "pref_wm_user";

	public static final String PREF_LOAD_IMAGES = "pref_load_images";


	public static final String PREF_DEFAULT_DOWNLOAD_KEY = "pref_default_download_method";
	public static final String PREF_WM_SESSION_KEY = "pref_wm_session";

	private final SharedPreferences mPref;
	private final SharedPreferences mCookiePreference;

	@Inject public PreferencesHelper(@ApplicationContext Context context) {
		mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		mCookiePreference = context.getSharedPreferences(PREF_COOKIE_FILE_NAME, Context.MODE_PRIVATE);
	}

	public void clear() {
		mPref.edit().clear().apply();
	}

	public void setAuth(String auth) {
		mPref.edit().putString(PREF_AUTH_KEY, auth).apply();
	}

	public String getAuth() {
		return mPref.getString(PREF_AUTH_KEY, "");
	}

	public void setUserId(int id) {
		mPref.edit().putInt(PREF_USER_ID_KEY, id).apply();
	}

	public int getUserId() {
		return mPref.getInt(PREF_USER_ID_KEY, 0);
	}

	public String getPass() {
		return mPref.getString(PREF_PASS_KEY, "");
	}

	public void setPass(String pass) {
		mPref.edit().putString(PREF_PASS_KEY, pass).apply();
	}

    public void putCookieSet(HashSet<String> cookies) {
		mCookiePreference.edit().putStringSet(PREF_COOKIE_KEY, cookies).apply();
	}

	public HashSet<String> getCookies() {
		return (HashSet<String>) mCookiePreference.getStringSet(PREF_COOKIE_KEY, new HashSet<String>());
	}

	public void clearCookies() {
		mCookiePreference.edit().remove(PREF_COOKIE_KEY).apply();
	}

    public void setWmHost(String wmHost) {
        mPref.edit().putString(PREF_WM_HOST_KEY, wmHost).apply();
    }

	public void setWmPassword(String wmPassword) {
		mPref.edit().putString(PREF_WM_PASSWORD_KEY, wmPassword).apply();
	}

	public void setWmPort(String wmPort) {
		mPref.edit().putString(PREF_WM_PORT_KEY, wmPort).apply();
	}

	public void setWmUser(String wmUser) {
		mPref.edit().putString(PREF_WM_USER_KEY, wmUser).apply();
	}

	public String getWmHost(){
		return String.format("%s:%s", mPref.getString(PREF_WM_HOST_KEY, ""), mPref.getString(PREF_WM_PORT_KEY, ""));
	}

	public String getWmUser() {
		return mPref.getString(PREF_WM_USER_KEY, "");
	}

	public String getWmPassword() {
		return mPref.getString(PREF_WM_PASSWORD_KEY, "");
	}

	public String getDownloadMethod() {
		return mPref.getString(PREF_DEFAULT_DOWNLOAD_KEY, "");
	}

	public void setDownloadMethod(String method) {
		mPref.edit().putString(PREF_DEFAULT_DOWNLOAD_KEY, method).apply();
	}

	public String getWmSession() {
		return mPref.getString(PREF_WM_SESSION_KEY, "");
	}

	public void setWmSession(String session) {
		mPref.edit().putString(PREF_WM_SESSION_KEY, session).apply();
	}

	public boolean getLoadImages() {
		return mPref.getBoolean(PREF_LOAD_IMAGES, true);
	}

	public void setLoadImages(boolean loadImages) {
		mPref.edit().putBoolean(PREF_LOAD_IMAGES, loadImages).apply();
	}
}
