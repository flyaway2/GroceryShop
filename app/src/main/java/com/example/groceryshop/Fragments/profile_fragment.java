package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class profile_fragment extends Fragment implements DialogInterface.OnDismissListener, View.OnClickListener {

    private ImageButton nom,prenom,email,address,phone,password;
    private TextView nom_text,prenom_text,email_text,address_text,phone_text,password_text,username_text;
    private String getUsername,getPassword,getNom,getPrenom,getEmail,getPhone,getAddress;
    private ArrayList<String> profileInfo;
    private SaveSharedPreference SSP;
    private String URL_DATA= DBUrl.URL_DATA.concat("Client.php?");
    private Fragment frg;
    public static boolean progress_exec;

    private ArrayList<String> profInfo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.compte,container,false);
         progress_exec=true;
        SSP=new SaveSharedPreference(getActivity());
        getUsername=SSP.getUsername();
        getPassword=SSP.getPassword();
        getProfileInfo();
        nom_text=view.findViewById(R.id.Nom_text);
        prenom_text=view.findViewById(R.id.Prenom_text);
        username_text=view.findViewById(R.id.Username_text);
        password_text=view.findViewById(R.id.Password_text);
        email_text=view.findViewById(R.id.Email_text);
        address_text=view.findViewById(R.id.Address_text);
        phone_text=view.findViewById(R.id.Phone_text);

        profileInfo=new ArrayList<>();
        nom=view.findViewById(R.id.Nom);
        prenom=view.findViewById(R.id.Prenom);
        email=view.findViewById(R.id.Email);
        address=view.findViewById(R.id.Address);
        phone=view.findViewById(R.id.Phone);
        password=view.findViewById(R.id.Password);



        email.setOnClickListener(this);
        address.setOnClickListener(this);
        nom.setOnClickListener(this);
        prenom.setOnClickListener(this);

        password.setOnClickListener(this);
        phone.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Bundle args=new Bundle();
        modifier_dialogfragment modDial;
        modifier_passphone_dialogFragment passPhone_modDial;


        switch (v.getId()){
            case R.id.Nom:
                modDial =new modifier_dialogfragment();
                modDial.setOnDismissListener(this);
                args.putString("info","nom");

                modDial.setArguments(args);
                modDial.show(getFragmentManager(),"modifier Profile");
                break;
            case R.id.Prenom:


                args.putString("info","prenom");
                modDial =new modifier_dialogfragment();
                modDial.setOnDismissListener(this);
                modDial.setArguments(args);
                modDial.show(getFragmentManager(),"modifier Profile");
                break;
            case R.id.Email:

                args.putString("info","email");
                modDial =new modifier_dialogfragment();
                modDial.setOnDismissListener(this);

                modDial.setArguments(args);
                modDial.show(getFragmentManager(),"modifier Profile");
                break;
            case R.id.Address:
                Log.d("address","inflate");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.map_fragment);
                break;
            case R.id.Phone:
                args.putString("info","phone");
                passPhone_modDial=new modifier_passphone_dialogFragment();

                passPhone_modDial.setOnDismissListener(this);
                passPhone_modDial.setArguments(args);
                passPhone_modDial.show(getFragmentManager(),"modifier Profile");
                break;
            case R.id.Password:
                args.putString("info","password");

                passPhone_modDial=new modifier_passphone_dialogFragment();
                passPhone_modDial.setOnDismissListener(this);
                passPhone_modDial.setArguments(args);
                passPhone_modDial.show(getFragmentManager(),"modifier Profile");
                break;
        }


    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commitAllowingStateLoss();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void getProfileInfo(){
        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("loading...");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
        URL_DATA=URL_DATA.concat("username="+getUsername);
        URL_DATA=URL_DATA.concat("&password="+getPassword);

        Log.d("url",""+URL_DATA);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String nom, prenom, email, phone, address;

                try {
                    JSONArray json = new JSONArray(response);

                    JSONObject client = json.getJSONObject(0);

                    getNom = client.getString("Nom");

                    getPrenom = client.getString("Prenom");
                    getEmail = client.getString("email");
                    getAddress = client.getString("address");
                    getPhone = client.getString("phone");
                    nom_text.setText(getNom);
                    prenom_text.setText(getPrenom);
                    email_text.setText(getEmail);
                    address_text.setText(getAddress);
                    phone_text.setText(getPhone);
                    password_text.setText(getPassword);
                    username_text.setText(getUsername);

                    Log.d("Nom"," "+getNom+" "+getPrenom+" "+getEmail+" "+getAddress+" "+getPhone);
                    profileInfo.add(getNom);profileInfo.add(getPrenom);profileInfo.add(getEmail);
                    profileInfo.add(getAddress);profileInfo.add(getPhone);
                    Log.d("profileinfo1"," size:"+profileInfo.size());
                    progressdialog.dismiss();
                    progress_exec=false;
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
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }else if(error instanceof NoConnectionError)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("error","Noconx");
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }
            }
        });
        reqeu.add(req);

    }


}
