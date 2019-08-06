package elope.crazyones.com.coffer.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {
    private static final String PREFERENCE_KEY_USER_ID = "user_id";
    private static final String PREFERENCE_KEY_TOKEN = "verification_token";
    private static final String PREFERENCE_KEY_EMAIL = "user_email";

    private static Context mAppContext;

    // Prevent instantiation
    private SharedPreferenceUtils() {
    }

    public static void init(Context appContext) {
        mAppContext = appContext;
    }

    private static SharedPreferences getSharedPreferences() {
        return mAppContext.getSharedPreferences("elope", Context.MODE_PRIVATE);
    }

    public static void setUserId(String userId) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_USER_ID, userId).apply();
    }

    public static String getUserId() {
        return getSharedPreferences().getString(PREFERENCE_KEY_USER_ID, null);
    }

    public static void setUserToken(String token) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_TOKEN, token).apply();
    }

    public static String getUserToken() {
        return getSharedPreferences().getString(PREFERENCE_KEY_TOKEN, null);
    }

    public static void setUserEmail(String email) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_EMAIL, email).apply();
    }

    public static String getUserEmail() {
        return getSharedPreferences().getString(PREFERENCE_KEY_EMAIL, null);
    }

}
