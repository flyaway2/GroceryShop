package com.example.groceryshop.Fragments;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.Activities.MainActivity;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.CustomToast;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.example.groceryshop.custom.Utils;
import com.example.groceryshop.custom.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class modifier_dialogfragment extends DialogFragment {
    Button annuler,valider;
    TextView instruction_msg;
    EditText typed_value;
    String value,info,URL_DATA;
    SaveSharedPreference SSP;
    Boolean annuler_clicked;
    private DialogInterface.OnDismissListener onDismissListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View v=inflater.inflate(R.layout.modifier_popup,container,false);
      getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
      SSP=new SaveSharedPreference(getActivity());
      URL_DATA= DBUrl.URL_DATA.concat("modifierProfile.php?");
      info=getArguments().getString("info");
      annuler_clicked=false;



      annuler=v.findViewById(R.id.annuler);
      valider=v.findViewById(R.id.valider);
      instruction_msg=v.findViewById(R.id.instruction_msg);
      typed_value=v.findViewById(R.id.typed_value);

      switch (info){
          case "nom":
              instruction_msg.append("Nom");
              break;
          case "prenom":
              instruction_msg.append("Prenom");
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
             if(!value.equals("")){
                 switch (info){
                     case "nom":
                         Pattern p= Pattern.compile(Utils.regExFullName);
                         Matcher m=p.matcher(value);
                         if(m.matches()){
                             modifierProfile();
                         }else{
                             Log.d("nominco","nom inccorect");
                             Toast.makeText(getActivity(),"nom incorrect",Toast.LENGTH_LONG).show();


                         }
                         break;
                     case "prenom":
                         Pattern pat_prenom= Pattern.compile(Utils.regExFullName);
                         Matcher mPrenom=pat_prenom.matcher(value);
                         if(mPrenom.matches()){
                             modifierProfile();
                         }else{
                             Log.d("prenominco","prenom inccorect");
                             Toast.makeText(getActivity(),"prenom incorrect",Toast.LENGTH_LONG).show();


                         }
                         break;
                     case "email":
                         Pattern pat_email= Pattern.compile(Utils.regEx);
                         Matcher mEmail=pat_email.matcher(value);
                         if(mEmail.matches()){
                             modifierProfile();
                         }else{
                             Log.d("emailinco","email inccorect");
                             Toast.makeText(getActivity(),"email incorrect",Toast.LENGTH_LONG).show();


                         }
                         break;
                     case "address":
                         modifierProfile();
                         break;
                 }
             }else{
                 Toast.makeText(getActivity(),"champ vide",Toast.LENGTH_LONG);
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
                    case "nom":
                        params.put("iden","nom");
                        params.put("value",value);


                        break;
                    case "prenom":
                        params.put("iden","prenom");
                        params.put("value",value);

                        break;
                    case "email":
                        params.put("iden","email");
                        params.put("value",value);

                        break;
                    case "address":
                        params.put("iden","address");
                        params.put("value",value);

                        break;
                }



                return params;
            }
        };
        reqeu.add(postRequest);

    }
}
