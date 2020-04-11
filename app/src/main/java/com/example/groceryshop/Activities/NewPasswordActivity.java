package com.example.groceryshop.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.example.groceryshop.custom.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText New_Pass,New_Pass_Confirm;
    private Button valider;
    private String URL_DATA,getPhone,getUsername;
    private SaveSharedPreference SSP;
    private CheckBox showPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password);
        SSP=new SaveSharedPreference(this);
        URL_DATA= DBUrl.URL_DATA.concat("modifierProfile.php?");

        getPhone=getIntent().getStringExtra("phone");
       getUsername=getIntent().getStringExtra("username");

        New_Pass=findViewById(R.id.New_Pass);
        New_Pass_Confirm=findViewById(R.id.New_Pass_Confirm);
        showPassword=findViewById(R.id.show_pass);


        valider=findViewById(R.id.valider);
        clicking();
    }

    public void clicking(){
        valider.setOnClickListener(this);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    showPassword.setText(R.string.hide_pwd);
                    New_Pass.setInputType(InputType.TYPE_CLASS_TEXT);
                    New_Pass.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    New_Pass_Confirm.setInputType(InputType.TYPE_CLASS_TEXT);
                    New_Pass_Confirm.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());

                }else
                {
                    showPassword.setText(R.string.show_pwd);// change
                    // checkbox
                    // text

                    New_Pass_Confirm.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New_Pass_Confirm.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                    New_Pass.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    New_Pass.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        String newPass= New_Pass.getText().toString();
        String newPassConfirm=New_Pass_Confirm.getText().toString();
        if(newPass.length()==0 || newPassConfirm.length()==0)
        {
            Toast.makeText(this,"Champ vide",Toast.LENGTH_LONG);
        }else{
            if(newPass.equals(newPassConfirm))
            {
                changePassword();
            }else
            {
                Toast.makeText(this,"Mot de passe différent",Toast.LENGTH_LONG);

            }
        }


    }

    public void changePassword()
    {
        RequestQueue reqeu = VolleySingleton.getInstance(this).getRequestQueue();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        SSP.createLoginSession(getUsername,New_Pass.getText().toString());
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
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
                params.put("username",getUsername);
                params.put("iden","password");
                params.put("value",New_Pass.getText().toString());


                return params;
            }
        };
        reqeu.add(postRequest);
    }
}
