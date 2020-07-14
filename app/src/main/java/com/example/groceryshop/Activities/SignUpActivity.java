package com.example.groceryshop.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.custom.CustomToast;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.Utils;
import com.example.groceryshop.R;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private static  String URL_DATA_UserAvail= DBUrl.URL_DATA;
    private static  String URL_DATA_PhoneAvail;
    private static View view;
    private static EditText Nom,Prenom,Username, emailId, mobileNumber,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    String getNom;
    String getPrenom;
    String getEmailId;
    String getMobileNumber;
    String getPassword;
    String getConfirmPassword;
    String getUsername;
    private ProgressDialog progressdialog;
    private NavController navController;
    private static final int SMS_CONSENT_REQUEST = 2;
    // Set to an unused request code
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("broadOnreceive","req ");
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status)extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            /*Start activity to show consent dialog to user within
                             *5 minutes, otherwise you'll receive another TIMEOUT intent
                             */
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        }
                        catch (ActivityNotFoundException e) {
                            // Handle the exception
                            Log.d("broadExcept"," "+e.toString());
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        Log.d("broadExcept"," timeout");
                        break;
                }
            }
        }
    };
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        overridePendingTransition(R.anim.right_enter, R.anim.left_out);

        Nom = findViewById(R.id.Nom);
        Prenom=findViewById(R.id.Prenom);
        Username=findViewById(R.id.Username);
        emailId = findViewById(R.id.userEmailId);
        mobileNumber = findViewById(R.id.mobileNumber);

        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.signUpBtn);
        login = findViewById(R.id.already_user);

        // Setting text selector over textviews
        XmlResourceParser xrp;
        xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
        setListeners();


    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Log.d("onclick sign"," d");
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                int green=Color.parseColor("#009688");
                Nom.setBackgroundColor(green);
                Prenom.setBackgroundColor(green);
                Username.setBackgroundColor(green);
                password.setBackgroundColor(green);
                emailId.setBackgroundColor(green);
                mobileNumber.setBackgroundColor(green);
                confirmPassword.setBackgroundColor(green);


                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                break;
        }

    }
    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts

        getNom = Nom.getText().toString();

        getPrenom = Prenom.getText().toString();
        getUsername=Username.getText().toString();

        getEmailId = emailId.getText().toString();

        getMobileNumber = mobileNumber.getText().toString();


        getPassword = password.getText().toString();

        getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        Pattern phonePattern=Pattern.compile(Utils.regExPhone);
        Matcher m2=phonePattern.matcher(getMobileNumber);
        Log.d("checkvalidation"," ");
        // Check if all strings are null or not
        if (getNom.equals("") || getNom.length() == 0
                || getPrenom.equals("") || getPrenom.length()==0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("") || getConfirmPassword.length() == 0)
        {
            new CustomToast().Show_Toast(getApplicationContext(), view,getString(R.string.AllChamp));
            if(getNom.equals("") || getNom.length() == 0 )
            {
                Nom.setBackgroundColor(Color.RED);
            }
            if(getPrenom.equals("") || getPrenom.length()==0 )
            {
                Prenom.setBackgroundColor(Color.RED);
            }
            if(getEmailId.equals("") || getEmailId.length() == 0)
            {
                emailId.setBackgroundColor(Color.RED);
            }
             if(getMobileNumber.equals("") || getMobileNumber.length() == 0 )
            {
                mobileNumber.setBackgroundColor(Color.RED);
            }
             if(getConfirmPassword.equals("") || getConfirmPassword.length() == 0 )
            {
                confirmPassword.setBackgroundColor(Color.RED);
            }
             if(getPassword.equals("") || getPassword.length() == 0)
            {
                password.setBackgroundColor(Color.RED);
            }

            // Check if email id valid or not
        }

        else if (!m2.matches()){
            new CustomToast().Show_Toast(getApplicationContext(), view,getString(R.string.TelInvalid));
            mobileNumber.setBackgroundColor(Color.RED);
        }


        else if (!m.find()) {
            new CustomToast().Show_Toast(getApplicationContext(), view, getString(R.string.EmailInvalid));
            emailId.setBackgroundColor(Color.RED);

            // Check if both password should be equal
        }
        else if (!getConfirmPassword.equals(getPassword)){
            new CustomToast().Show_Toast(getApplicationContext(), view,getString(R.string.DiffPass));
            password.setBackgroundColor(Color.RED);
        }




            // Else do signup or do your stuff
        else {


            UsernameAvailability();
        }

    }
    private void NextAct(){
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra("nom", getNom);
        intent.putExtra("prenom", getPrenom);
        intent.putExtra("username", getUsername);
        intent.putExtra("password", getPassword);
        intent.putExtra("phone", getMobileNumber);
        intent.putExtra("email", getEmailId);

        startActivity(intent);
    }

    private void UsernameAvailability(){
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("loading...");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
        URL_DATA_UserAvail=DBUrl.URL_DATA_Client;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest getRequest = new StringRequest(Request.Method.POST, URL_DATA_UserAvail,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONArray json = new JSONArray(response);

                        Boolean exist=false;
                        Log.d("reponseUser",""+response.equals("[]"));

                        if(json.length()>0){
                            exist=true;
                        }
                        if(exist){
                            progressdialog.dismiss();
                            new CustomToast().Show_Toast(getApplicationContext(), view,getString(R.string.UsernameExist));
                            Username.setBackgroundColor(Color.RED);
                        }else{
                             PhoneAvailability();
                        }
                        } catch (JSONException e) {
                            Log.d("exception",""+e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        progressdialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Vérifier votre connexion",Toast.LENGTH_LONG);
                        new CustomToast().Show_Toast(getApplicationContext(), view,getString(R.string.ConxBleme));

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","CheckUser");
                params.put("Username",Username.getText().toString());



                return params;
            }
        };
        queue.add(getRequest);

    }
    public void PhoneAvailability(){
        URL_DATA_PhoneAvail=DBUrl.URL_DATA_Client;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest getRequest = new StringRequest(Request.Method.POST, URL_DATA_PhoneAvail,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            progressdialog.dismiss();
                            JSONArray json = new JSONArray(response);

                        Boolean exist=false;
                        if(json.length()>0){
                            exist=true;
                        }
                        if(exist){
                            new CustomToast().Show_Toast(getApplicationContext(), view,getString(R.string.PhoneExist));
                            mobileNumber.setBackgroundColor(Color.RED);
                        }else{
                            NextAct();
                        }
                        } catch (JSONException e) {
                            Log.d("exception",""+e);
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        progressdialog.dismiss();
                        new CustomToast().Show_Toast(getApplicationContext(), view,getString(R.string.ConxBleme));
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","SelectPhone");
                params.put("phone",mobileNumber.getText().toString());



                return params;
            }
        };
        queue.add(getRequest);
    }




}


