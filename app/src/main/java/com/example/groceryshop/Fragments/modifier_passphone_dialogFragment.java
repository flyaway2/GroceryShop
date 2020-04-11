package com.example.groceryshop.Fragments;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.example.groceryshop.custom.Utils;
import com.example.groceryshop.custom.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class modifier_passphone_dialogFragment extends DialogFragment {
    EditText typed_value,password;
    Button annuler,valider;
    String info,value,passwordCheck,URL_DATA;
    SaveSharedPreference SSP;
    private DialogInterface.OnDismissListener onDismissListener;
    Boolean annuler_clicked;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.modifier_popup_passphone,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        annuler_clicked=false;
        SSP=new SaveSharedPreference(getActivity());
        typed_value=v.findViewById(R.id.typed_value);
        password=v.findViewById(R.id.old_password);


        annuler=v.findViewById(R.id.annuler);
        valider=v.findViewById(R.id.valider);
        info=getArguments().getString("info");

        URL_DATA= DBUrl.URL_DATA.concat("modifierProfile.php?");
        switch (info){
            case "phone":
                typed_value.setHint("Numéro de Tel");
                password.setHint("mot de passe");
                typed_value.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phone,0,0,0);

                break;
            case "password":
                typed_value.setHint("Nouveau mot de passe");
                password.setHint("Ancien mot de passe");

                typed_value.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                annuler_clicked=true;
                getDialog().cancel();

            }
        });
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=typed_value.getText().toString();
                passwordCheck=password.getText().toString();
                if(!(value.equals("") || passwordCheck.equals(""))){
                   switch (info) {
                       case "phone":
                           Pattern p=Pattern.compile(Utils.regExPhone);
                           Matcher m=p.matcher(value);
                           if(m.matches()){
                              if(passwordCheck.equals(SSP.getPassword())){
                                  modifierProfile();
                              }else{
                                  Toast.makeText(getActivity(),"mot de passe incorrect",Toast.LENGTH_LONG).show();
                              }
                           }else{
                               Toast.makeText(getActivity(),"Numéro de Tel incorrect",Toast.LENGTH_LONG).show();
                           }

                           break;
                       case "password":
                           if(passwordCheck.equals(SSP.getPassword())){
                                modifierProfile();
                           }else{
                               Toast.makeText(getActivity(),"mot de passe incorrect",Toast.LENGTH_LONG).show();
                           }

                           break;
                   }

                }else{
                    Toast.makeText(getActivity(),"champ vide",Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if(!annuler_clicked){
            super.onDismiss(dialog);
            if (onDismissListener != null) {
                onDismissListener.onDismiss(dialog);
            }
        }

    }

    public void modifierProfile(){
        RequestQueue reqeu = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        getDialog().dismiss();


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
                params.put("username",SSP.getUsername());
                switch (info){
                    case "phone":
                        params.put("iden","phone");
                        params.put("value",value);



                        break;
                    case "password":
                        params.put("iden","password");
                        params.put("value",value);
                        SSP.setPassword(value);

                        break;

                }



                return params;
            }
        };
        reqeu.add(postRequest);

    }


}
