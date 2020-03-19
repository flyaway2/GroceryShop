package com.example.groceryshop.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.custom.CustomToast;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {
    private static  String URL_DATA= DBUrl.URL_DATA.concat("Client.php?");
    private View view;
    SaveSharedPreference SSP;
    public EditText username,password;
    private static Button submit;
    private static TextView register,forgetpassword;
   private static  CheckBox showPassword;
    private static LinearLayout loginForm;
    private static Animation shakeAnimation;

    @Override
    public void onBackPressed() {
     //

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SSP=new SaveSharedPreference(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username=findViewById(R.id.usernameText);
        password=findViewById(R.id.passwordText);
        submit=findViewById(R.id.loginBtn);
        loginForm=findViewById(R.id.loginForm);
        register=findViewById(R.id.createAccount);
        forgetpassword=findViewById(R.id.forgot_password);
        showPassword=findViewById(R.id.show_hide_password);

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
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
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
                        } else {
                            showPassword.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

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

    private void loadUrlData() {
        String user=username.getText().toString();
        String passW=password.getText().toString();

        if(user.trim().length()>0 && passW.trim().length()>0){
            URL_DATA=URL_DATA.concat("username="+user);
            URL_DATA=URL_DATA.concat("&password="+passW);
            Log.d("url:"," "+URL_DATA);
            RequestQueue reqeu = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("I'm here", "OnResponse");
                    String pseudo="", pass="", email="", address="", phone="";
                    try {
                        JSONArray json = new JSONArray(response);
                        Boolean ClientExist=false;
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject cat = json.getJSONObject(i);
                            pseudo=cat.getString("username");
                            pass=cat.getString("password");
                            email=cat.getString("email");
                            address=cat.getString("address");
                            phone=cat.getString("phone");
                            ClientExist=true;


                        }
                    if(ClientExist){
                        SSP.createLoginSession(pseudo,pass);
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }else{
                        new CustomToast().Show_Toast(getApplicationContext(), view,
                                "Your Email Id is Invalid.");

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
                    Toast.makeText(getApplicationContext(), "vérifier votre connexion" , Toast.LENGTH_LONG).show();

                }

            });
            reqeu.add(stringRequest);
        }else{
            loginForm.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getApplicationContext(), view,
                    "Enter both credentials.");
        }




    }
}
