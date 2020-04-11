package com.example.groceryshop.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.groceryshop.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;
    private ProgressBar pb;
    private String getUsername;
    private String getNom;
    private String getPrenom;
    private String getPhone;
    private String getPassword;
    private String getEmail;
    private String getAddress;
    private String getLatitude;
    private String getLongitude;
    SaveSharedPreference SSP;

    private static String URL_DATA=DBUrl.URL_DATA.concat("signup.php?");
    @Override
    public void onBackPressed() {
        //

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SSP=new SaveSharedPreference(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        Intent intent=getIntent();

        getUsername=intent.getStringExtra("username");
        getPrenom=intent.getStringExtra("prenom");
        getNom=intent.getStringExtra("nom");
        getPassword=intent.getStringExtra("password");
        getPhone=intent.getStringExtra("phone");
        getEmail=intent.getStringExtra("email");
        getAddress=intent.getStringExtra("address");
        getLongitude=intent.getStringExtra("longitude");
        getLatitude=intent.getStringExtra("latitude");


        //initializing objects
        mAuth = FirebaseAuth.getInstance();

         pb=findViewById(R.id.progressbar);
         
        //getting mobile number from the previous activity
        //and sending the verification code to the number

        String mobile = intent.getStringExtra("phone");
        sendVerificationCode(mobile);
        editTextCode=findViewById(R.id.editTextCode);

        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well

    private void sendVerificationCode(String mobile) {

        Log.d("mobile "," "+mobile.replaceFirst("0",""));
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+213" + mobile.replaceFirst("0",""),
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        Log.d("codeVer"," "+code);
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Log.d("onComplete","successful");
                            /*Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/
                            insertClient();
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
    public void insertClient(){
        RequestQueue reqeu = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        SSP.createLoginSession(getUsername,getPassword);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response","");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", getUsername);
                params.put("Nom", getNom);
                params.put("Prenom", getPrenom);
                params.put("phone", getPhone);
                params.put("address", getAddress);
                params.put("password", getPassword);
                params.put("email",getEmail);
                params.put("longitude",getLongitude);
                params.put("latitude",getLatitude);


                return params;
            }
        };
        reqeu.add(postRequest);

    }


}
