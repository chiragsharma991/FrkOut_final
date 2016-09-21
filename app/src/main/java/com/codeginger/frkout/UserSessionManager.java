package com.codeginger.frkout;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

// Created by Pratik Mehta

public class UserSessionManager
{
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "FrkOutPref";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedInSystem";

    public static final String KEY_NAME = "name";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID = "id";
    public static final String KEY_USERREG_ID = "userregId";
    public static final String KEY_IMEI_NO = "imei";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_URL = "url";


    // OTP ID
    public static final String KEY_OTP_ID = "otpId";
    //Login
    public static final String KEY_IS_USER_LOGIN = "is_user_login";
    public static final String KEY_IS_USER_PROFILE_INFO_SETUP = "is_user_profile_info_setup";
    public static final String KEY_IS_USER_PROFILE_INTEREST_SETUP = "is_user_profile_interest_setup";
    // Google
    public static final String KEY_GOOGLE_LOGIN = "google_login";
    // Twitter
    public static final String KEY_TWITTER_OAUTH_TOKEN = "twitter_oauth_token";
    public static final String KEY_TWITTER_OAUTH_SECRET = "twitter_oauth_secret";
    public static final String KEY_TWITTER_LOGIN = "twitter_login";
    // Facebook
    public static final String KEY_FACEBOOK_ACCESS_TOKEN = "facebook_access_token";
    public static final String KEY_FACEBOOK_ACCESS_EXPIRES = "facebook_access_expires";
    public static final String KEY_FACEBOOK_LOGIN = "facebook_login";

    //Chat
    public static final String KEY_GCM_REG_ID = "gcm_reg_id";
    public static final String KEY_GCM_CURRENT_ACTIVE = "gcm_current_active";
    public static final String KEY_GCM_REG_FROM = "gcm_reg_from";
    public static final String KEY_GCM_FROM_NAME = "gcm_from_name";

    // Create Folder
    public static final String KEY_IS_FOLDER_CREATED = "is_folder_created";

    // Broadcast
    public static final String KEY_BROADCAST_ID = "broadcast_id";

    // Constructor
    public UserSessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    // Get stored session data
    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_BIRTHDAY, pref.getString(KEY_BIRTHDAY, null));
        user.put(KEY_URL, pref.getString(KEY_URL, null));
        user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_USERREG_ID, pref.getString(KEY_USERREG_ID, null));
        user.put(KEY_IMEI_NO, pref.getString(KEY_IMEI_NO, null));
        user.put(KEY_OTP_ID, pref.getString(KEY_OTP_ID, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));
        //Chat Module
        user.put(KEY_GCM_REG_ID, pref.getString(KEY_GCM_REG_ID, null));
        user.put(KEY_GCM_CURRENT_ACTIVE, pref.getString(KEY_GCM_CURRENT_ACTIVE, null));
        user.put(KEY_GCM_REG_FROM, pref.getString(KEY_GCM_REG_FROM, null));
        user.put(KEY_GCM_FROM_NAME, pref.getString(KEY_GCM_FROM_NAME, null));
        // Broadcast
        user.put(KEY_BROADCAST_ID, pref.getString(KEY_BROADCAST_ID, null));
        return user;
    }

    // Check for User login
    public boolean isUserLogin()
    {
        return pref.getBoolean(KEY_IS_USER_LOGIN, false);
    }

    // Check for Facebook login
    public boolean isFacebookLoggedInAlready()
    {
        return pref.getBoolean(KEY_FACEBOOK_LOGIN, false);
    }

    // Check for Google login
    public boolean isGoogleLoggedInAlready()
    {
        return pref.getBoolean(KEY_GOOGLE_LOGIN, false);
    }

    // Check for Twitter login
    public boolean isTwitterLoggedInAlready()
    {
        return pref.getBoolean(KEY_TWITTER_LOGIN, false);
    }

    // Check for User Profile Info SetUP
    public boolean isUserProfileInfoSetup()
    {
        return pref.getBoolean(KEY_IS_USER_PROFILE_INFO_SETUP, false);
    }

    // Check for User Profile Interest SetUP
    public boolean isUserProfileInterestSetup()
    {
        return pref.getBoolean(KEY_IS_USER_PROFILE_INTEREST_SETUP, false);
    }

    // Clear session details
    public void Clear()
    {
        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    // Broadcast Clear session details
    public void BroadcastClear()
    {
        // Clearing all user data from Shared Preferences
        editor.putString(KEY_BROADCAST_ID, null);
        editor.commit();
    }


    // Check for Folder Created
    public boolean isFolderCreated()
    {
        return pref.getBoolean(KEY_IS_FOLDER_CREATED, false);
    }


    // Check for User login
    public boolean isUserLoggedIn()
    {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    // Create login session
    public void createUserLoginSession(String name, String email)
    {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    // Check login method will check user login status  If false it will redirect user to login page Else do anything
    public boolean checkLogin()
    {
        // Check login status
        if(!this.isUserLoggedIn())
        {
            Intent i = new Intent(_context, SplashScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return true;
        }
        return false;
    }

    // Clear session details
    public void logoutUser()
    {
        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context,  SplashScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }


}