package com.example.groceryshop.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.groceryshop.custom.CustomToast;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.Utils;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private View view;
    private static EditText emailId;
    private static TextView submit, back;
    private String URL_DATAUsername,getUsername,getPhone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.forgetpassword);
        URL_DATAUsername= DBUrl.URL_DATA.concat("getUsername.php?");
        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
        initViews();
        setListeners();

    }
    // Initialize the views
    private void initViews() {
        emailId = (EditText) findViewById(R.id.registered_emailid);
        submit = (TextView) findViewById(R.id.forgot_button);
        back = (TextView) findViewById(R.id.backToLoginBtn);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            back.setTextColor(csl);
            submit.setTextColor(csl);

        } catch (Exception e) {
            Log.d("forgetPassException"," "+e.toString());
        }

    }

    // Set Listeners over buttons
    private void setListeners() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToLoginBtn:

                // Replace Login Fragment on Back Presses
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;

        }

    }

    private void submitButtonTask() {
        String getEmailId = emailId.getText().toString();

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0)

            new CustomToast().Show_Toast(getApplicationContext(), view,
                    "entrer votre Numéro.");

            // Else submit email id and fetch passwod or do your stuff
        else
        {
            getPhone=emailId.getText().toString();
            Pattern p=Pattern.compile(Utils.regExPhone);
            Matcher m=p.matcher(getPhone);
            if(!m.matches()){
                new CustomToast().Show_Toast(getApplicationContext(), view,
                        "Numéro Invalide.");
            }else
            {
                getUsername();
            }


        }


    }

    public void getUsername(){
        RequestQueue reqeu= VolleySingleton.getInstance(this).getRequestQueue();

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATAUsername, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray json = new JSONArray(response);

                    if(json.length()==0){

                        new CustomToast().Show_Toast(getApplicationContext(), view,
                                "Le numéro n'existe pas");
                    }else
                    {

                        JSONObject client = json.getJSONObject(0);
                        getUsername = client.getString("username");
                        Intent intent=new Intent(getApplicationContext(),CheckAccount_Activity.class);
                        intent.putExtra("phone",emailId.getText().toString());
                        intent.putExtra("username",getUsername);
                        startActivity(intent);
                    }


                } catch (Exception e) {
                    Log.d("jsonException"," "+e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorvolley"," "+error.toString());
                new CustomToast().Show_Toast(getApplicationContext(), view,
                        "Vérifier votre connexion");

            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("phone",getPhone);




                return params;
            }};
        reqeu.add(req);

    }
}
