package com.example.groceryshop.Fragments;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class modifier_dialogfragment extends DialogFragment {
    Button annuler,valider;
    EditText typed_value;
    String value,info,URL_DATA;
    SaveSharedPreference SSP;
    Boolean annuler_clicked;
    private Spinner Genre;
    private ArrayList<String> GenreChoice;
    private ArrayAdapter<String> GenreAdapter;
    private DialogInterface.OnDismissListener onDismissListener;
    private ProgressDialog progressdialog;
    private Activity act;
    private TextView instruction_msg;

    private Fragment mFragment;
    public modifier_dialogfragment(Activity act)
    {
        this.act=act;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View v=inflater.inflate(R.layout.modifier_popup,container,false);
      getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        instruction_msg=v.findViewById(R.id.instruction_msg);
        Genre=v.findViewById(R.id.Genre);
      SSP=new SaveSharedPreference(act);
      URL_DATA= DBUrl.URL_DATA_Client;
      info=getArguments().getString("info");
      GenreChoice=new ArrayList<>();
      mFragment=this;



      annuler_clicked=false;



      annuler=v.findViewById(R.id.annuler);
      valider=v.findViewById(R.id.valider);
      typed_value=v.findViewById(R.id.typed_value);

      switch (info){
          case "nom":

              if(SSP.getLang().equals("ar")){
                  typed_value.setHint("اللقب");
              }else
              {
                  typed_value.setHint("Nom");
              }

              break;
          case "prenom":
              if(SSP.getLang().equals("ar")){
                  typed_value.setHint("اسم");
              }else
              {
                  typed_value.setHint("Prenom");
              }
              break;
          case "email":
              if(SSP.getLang().equals("ar")){
                  typed_value.setHint("بريد الإلكتروني");
              }else
              {
                  typed_value.setHint("email");
              }
              break;
          case "Genre":
              instruction_msg.setVisibility(View.VISIBLE);

              if(SSP.getLang().equals("ar"))
              {
                  GenreChoice.add("ذكر");
                  GenreChoice.add("أنثى");
                  instruction_msg.setText("جنـس");

              }else
              {
                  GenreChoice.add("Mâle");
                  GenreChoice.add("Femelle");
                  instruction_msg.setText("Genre");
              }
              GenreAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,GenreChoice);

              Genre.setVisibility(View.VISIBLE);

              GenreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
              Genre.setAdapter(GenreAdapter);
              typed_value.setVisibility(View.GONE);
              break;
      }
      annuler.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              getDialog().dismiss();
          }
      });
      valider.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

             value=typed_value.getText().toString();
             if(!value.equals("") || Genre.getVisibility()==View.VISIBLE){
                 switch (info){
                     case "nom":
                         Pattern p= Pattern.compile(Utils.regExFullName);
                         Matcher m=p.matcher(value);
                         if(m.matches()){
                             modifierProfile();
                         }else{
                             Log.d("nominco","nom inccorect");
                             Toast.makeText(act, R.string.NomIncorrect,Toast.LENGTH_LONG).show();


                         }
                         break;
                     case "prenom":
                         Pattern pat_prenom= Pattern.compile(Utils.regExFullName);
                         Matcher mPrenom=pat_prenom.matcher(value);
                         if(mPrenom.matches()){
                             modifierProfile();
                         }else{
                             Log.d("prenominco","prenom inccorect");
                             Toast.makeText(act,R.string.PrenomIncorrect,Toast.LENGTH_LONG).show();


                         }
                         break;
                     case "email":
                         Pattern pat_email= Pattern.compile(Utils.regEx);
                         Matcher mEmail=pat_email.matcher(value);
                         if(mEmail.matches()){
                             modifierProfile();
                         }else{
                             Log.d("emailinco","email inccorect");
                             Toast.makeText(act, R.string.email_incorrect,Toast.LENGTH_LONG).show();


                         }
                         break;
                     case "address":
                         modifierProfile();
                         break;
                     case "Genre":

                         Log.d("clicked"," genre");
                         setDateNais();
                         break;
                 }
             }else{
                 Toast.makeText(act,R.string.ChampVide,Toast.LENGTH_LONG).show();
             }
          }
      });

      return v;
    }


    private void setDateNais(){
        getDialog().dismiss();
        progressdialog = new ProgressDialog(act);
        progressdialog.setMessage("loading...");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
        Log.d("url",""+URL_DATA);

        RequestQueue reqeu= Volley.newRequestQueue(act);

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("response"," "+response);
                    progressdialog.dismiss();
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.modifierProfile_to_profile_fragment);


                } catch (Exception e) {
                    Log.d("jsonException"," "+e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorvolley"," "+error.toString());
                if(error instanceof TimeoutError)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("error","timeout");
                    Navigation.findNavController(act,R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }else if(error instanceof NoConnectionError)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("error","Noconx");
                    Navigation.findNavController(act,R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("Username",SSP.getUsername());
                params.put("query","UpdateGenre");
                Log.d("getLang"," "+SSP.getLang());
                if(SSP.getLang().equals("ar"))
                {
                    if(Genre.getSelectedItem().toString().equals("ذكر"))
                    {
                        params.put("Genre","1");
                    }else
                    {
                        params.put("Genre","2");
                    }
                }else
                {
                    if(Genre.getSelectedItem().toString().equals("Mâle"))
                    {
                        params.put("Genre","1");
                    }else
                    {
                        params.put("Genre","2");
                    }
                }




                return params;
            }
        };
        reqeu.add(req);

    }
    public void modifierProfile(){
        getDialog().dismiss();
        RequestQueue reqeu = VolleySingleton.getInstance(act).getRequestQueue();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        Navigation.findNavController(act,R.id.nav_host_fragment).navigate(R.id.profile_fragment);


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
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
              params.put("Username",SSP.getUsername());
                switch (info){
                    case "nom":
                        params.put("query","UpdateNom");
                        params.put("value",value);



                        break;
                    case "prenom":
                        params.put("query","UpdatePrenom");
                        params.put("value",value);

                        break;
                    case "email":
                        params.put("query","UpdateEmail");
                        params.put("value",value);

                        break;
                    case "address":
                        params.put("query","UpdateAddress");
                        params.put("value",value);

                        break;
                }



                return params;
            }
        };
        reqeu.add(postRequest);

    }
}
