package com.example.groceryshop.custom;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.Activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuerySender {
    private Map<String,String> Variables;
    private String URL_DATA;
    private Context context;

    public QuerySender(String URL_DATA, Map<String,String> Var, Context context)
    {
        Variables=Var;
        this.URL_DATA=URL_DATA;
        this.context=context;
    }



    public void SendQuery()
    {
            RequestQueue reqeu = VolleySingleton.getInstance(context).getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
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
                            Log.d("passclient",""+pass);
                            email=cat.getString("email");
                            address=cat.getString("address");
                            phone=cat.getString("phone");
                            ClientExist=true;


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


                }

            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("DBUsername",DBUrl.DBUsername);
                    params.put("DBPassword",DBUrl.DBPassword);



                    return params;
                }
            };
            reqeu.add(stringRequest);



    }
    }

