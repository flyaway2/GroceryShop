package com.example.groceryshop.custom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import static com.example.groceryshop.custom.PreferencesUtility.LOGGED_IN_PREF;

public class SaveSharedPreference {
    Context context;
    SharedPreferences pref;
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
    SharedPreferences.Editor editor;

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    public static final String KEY_Lang= "Lang";

    // password address (make variable public to access from outside)
    public static final String KEY_password = "password";

    public SaveSharedPreference(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    static SharedPreferences getPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static void setLoggedIn(Context context,boolean LoggedIn){
        SharedPreferences.Editor editor=getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF,LoggedIn);
        editor.apply();
    }
    public void setLang(String Lang)
    {
        editor.putString(KEY_Lang,Lang);
        editor.commit();
    }
    public String getLang()
    {
        return pref.getString(KEY_Lang,"en_us");
    }
    public boolean getLoggedSattus(Context context){
        Log.d("islogin"," "+pref.getBoolean(IS_LOGIN,false));
        return pref.getBoolean(IS_LOGIN,false);

    }

    public void createLoginSession(String name, String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        Log.d("createislogin"," "+pref.getBoolean(IS_LOGIN,false));
        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing password in pref
        editor.putString(KEY_password, password);

        // commit changes
        editor.commit();
    }
    public void setPassword(String password){
        editor.putString(KEY_password,password).commit();
    }
    public String getUsername(){
        return pref.getString(KEY_NAME,"empty");

    }
    public String getPassword(){
        Log.d("passZ",""+PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_password,"fuckyou"));
        return pref.getString(KEY_password,"empty");

    }
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN,false);

        editor.remove(KEY_NAME);
        editor.remove(KEY_password);
        editor.commit();
        Log.d("logout"," editor:"+editor+" key name:"+getPreferences(context).getString(KEY_NAME,"name")+" "+
        getPreferences(context).getBoolean(IS_LOGIN,false));

    }
}
