package com.example.groceryshop.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.Fragments.LangChoice;
import com.example.groceryshop.custom.CustomToast;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {
    private static  String URL_DATA= DBUrl.URL_DATA_Client;
    private View view;
    SaveSharedPreference SSP;
    public EditText username,password;
    private static Button submit;
    private static TextView register,forgetpassword;
   private static  CheckBox showPassword;
    private static LinearLayout loginForm;
    private static Animation shakeAnimation;
    private String passW;
    private String user;
    private String pseudo;
    private String pass;
    private String email,address,phone;
    private Toolbar toolbar;
    private ProgressDialog progressdialog;

    @Override
    public void onBackPressed() {
     //
        finishAffinity();

    }
    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localeCode.toLowerCase()));
        }else
        {
            configuration.locale=new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SSP=new SaveSharedPreference(getApplicationContext());
        setAppLocale(SSP.getLang());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username=findViewById(R.id.usernameText);
        password=findViewById(R.id.passwordText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            username.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        submit=findViewById(R.id.loginBtn);
        loginForm=findViewById(R.id.loginForm);
        register=findViewById(R.id.createAccount);
        forgetpassword=findViewById(R.id.forgot_password);
        showPassword=findViewById(R.id.show_hide_password);
        toolbar = findViewById(R.id.toolbar);
        if(SSP.getLang().equals("ar"))
        {
            toolbar.setTitle("تسجيل الدخول");
        }else
        {
            toolbar.setTitle("CONNECTER");
        }


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgetpassword.setTextColor(csl);
            showPassword.setTextColor(csl);
            register.setTextColor(csl);
        } catch (Exception e) {

        }

        if(SSP.getLoggedSattus(getApplicationContext())){
            finishAffinity();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  );
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
            finish();
        }else{
            loginForm.setVisibility(View.VISIBLE);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loadUrlData();


            }
        });
        setListeners();


    }
    // Set Listeners
    private void setListeners() {

        forgetpassword.setOnClickListener(this);
        register.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password

                showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            showPassword.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                            password.setSelection(password.getText().length());
                        } else {
                            showPassword.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password
                            password.setSelection(password.getText().length());

                        }

                    }
                });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                Intent intent=new Intent(getApplicationContext(),ForgetPasswordActivity.class);
                startActivity(intent);


                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                 Intent intent1=new Intent(getApplicationContext(),SignUpActivity.class);
                 startActivity(intent1);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.change_lang, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.ChangeLang)
        {
            LangChoice langchoice=new LangChoice(this);

            langchoice.show(getSupportFragmentManager(),"change language");
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUrlData() {
        Log.d("username focused"," "+username.isFocused());

        if(username.isFocused())
        {

            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(username.getWindowToken(), 0);
        }else if(password.isFocused())
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(username.getWindowToken(), 0);

        }


        user=username.getText().toString();
        passW=password.getText().toString();

        if(user.trim().length()>0 && passW.trim().length()>0){
            progressdialog = new ProgressDialog(this);
            progressdialog.setMessage("loading..");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.show();

            Log.d("url:"," "+URL_DATA);
            RequestQueue reqeu = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("I'm here", "OnResponse "+response);
                    try {
                        progressdialog.dismiss();
                        JSONArray json = new JSONArray(response);
                        Boolean ClientExist=false;
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject cat = json.getJSONObject(i);
                            pseudo=cat.getString("username");

                            pass=cat.getString("password");
                            Log.d("passclient",""+pass);
                            email=cat.getString("email");
                            address=cat.getString("address");
                            phone=cat.getString("phone");
                            ClientExist=true;


                        }
                    if(ClientExist){
                        Log.d("client exist"," "+pseudo+" "+pass);
                        SSP.createLoginSession(pseudo,passW);
                        finishAffinity();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  );
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity(intent);
                        finish();
                        startActivity(intent);
                    }else{
                        new CustomToast().Show_Toast(getApplicationContext(), view, getString(R.string.login_incorrect));

                    }



                    } catch (JSONException e) {
                        Log.e("my error", "I got error I got hoes", e);
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error",""+error);
                    progressdialog.dismiss();

                    new CustomToast().Show_Toast(getApplicationContext(), view, getString(R.string.ConxBleme));

                }

            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("DBUsername",DBUrl.DBUsername);
                    params.put("DBPassword",DBUrl.DBPassword);
                    params.put("Username",user);
                    params.put("Password",passW);
                    params.put("query","SelectUsername");




                    return params;
                }
            };
            reqeu.add(stringRequest);
        }else{
            loginForm.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getApplicationContext(), view, getString(R.string.AllChamp));
        }




    }
}
