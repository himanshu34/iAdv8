package com.agl.product.adw8_new.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.agl.product.adw8_new.model.PermissionData;
import com.agl.product.adw8_new.model.Usuario;

import java.util.HashMap;

public class Session {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "iAdv8RecipeManager";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_IS_PAID = "IS_PAID";
    public static final String KEY_IS_LMS = "IS_LMS";
    public static final String KEY_IS_SEO = "IS_SEO";
    public static final String KEY_IS_BILLING = "IS_BILLING";
    public static final String KEY_IS_ANALYTICS = "IS_ANALYTICS";
    public static final String KEY_IS_TASK = "IS_TASK";

    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_AGENCY_CLIENT_ID = "AGENCY_CLIENT_ID";
    public static final String KEY_AGENCY_ID = "AGENCY_ID";
    public static final String KEY_USERNAME = "USERNAME";
    public static final String KEY_EMAIL = "USER_EMAIL";
    public static final String KEY_USER_ROLE = "USER_ROLE";
    public static final String KEY_STATUS = "STATUS";
    public static final String KEY_PASSWORD = "USER_PASSWORD";
    public static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String KEY_HRM_USER_ID = "HRM_USER_ID";
    public static final String KEY_PROFILE_ID = "PROFILE_ID";

    // Constructor
    public Session(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create Customer login session
     * */
    public void createLoginSession(PermissionData permissionData, Usuario usuario) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putBoolean(KEY_IS_PAID, permissionData.isPAID());
        editor.putBoolean(KEY_IS_LMS, permissionData.isLMS());
        editor.putBoolean(KEY_IS_SEO, permissionData.isSEO());
        editor.putBoolean(KEY_IS_BILLING, permissionData.isBILLING());
        editor.putBoolean(KEY_IS_ANALYTICS, permissionData.isANALYTICS());
        editor.putBoolean(KEY_IS_TASK, permissionData.isTASK());

        editor.putString(KEY_USER_ID, usuario.getId());
        editor.putString(KEY_AGENCY_CLIENT_ID, usuario.getAgency_client_id());
        editor.putString(KEY_AGENCY_ID, usuario.getAgency_id());
        editor.putString(KEY_USERNAME, usuario.getUser_name());
        editor.putString(KEY_EMAIL, usuario.getUser_email());
        editor.putString(KEY_USER_ROLE, usuario.getUser_role());
        editor.putString(KEY_STATUS, usuario.getStatus());
        editor.putString(KEY_PASSWORD, usuario.getUser_password());
        editor.putString(KEY_ACCESS_TOKEN, usuario.getAccess_token());
        editor.putString(KEY_HRM_USER_ID, usuario.getHrm_user_id());
        editor.putString(KEY_PROFILE_ID, usuario.getProfile_id());

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session permission data
     * */
    public HashMap<String, Boolean> getPermissionDetails(){
        HashMap<String, Boolean> user = new HashMap<String, Boolean>();
        user.put(KEY_IS_PAID, pref.getBoolean(KEY_IS_PAID, false));
        user.put(KEY_IS_LMS, pref.getBoolean(KEY_IS_LMS, false));
        user.put(KEY_IS_SEO, pref.getBoolean(KEY_IS_SEO, false));
        user.put(KEY_IS_BILLING, pref.getBoolean(KEY_IS_BILLING, false));
        user.put(KEY_IS_ANALYTICS, pref.getBoolean(KEY_IS_ANALYTICS, false));
        user.put(KEY_IS_TASK, pref.getBoolean(KEY_IS_TASK, false));

        // return customer
        return user;
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUsuarioDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_AGENCY_CLIENT_ID, pref.getString(KEY_AGENCY_CLIENT_ID, null));
        user.put(KEY_AGENCY_ID, pref.getString(KEY_AGENCY_ID, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_USER_ROLE, pref.getString(KEY_USER_ROLE, null));
        user.put(KEY_STATUS, pref.getString(KEY_STATUS, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_ACCESS_TOKEN, pref.getString(KEY_ACCESS_TOKEN, null));
        user.put(KEY_HRM_USER_ID, pref.getString(KEY_HRM_USER_ID, null));
        user.put(KEY_PROFILE_ID, pref.getString(KEY_PROFILE_ID, null));

        // return customer
        return user;
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            return false;

        } else {
            return true;
        }
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}