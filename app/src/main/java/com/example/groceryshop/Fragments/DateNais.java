package com.example.groceryshop.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class DateNais extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private TextView editText;
    private Context context;
    private ProgressDialog progressdialog;
    private String URL_DATA;
    private int ID;
    private String state;
    private SaveSharedPreference SSP;
    private  String formattedDate;
    private Activity act;

    public DateNais(Activity act)
    {
        this.act=act;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        URL_DATA=DBUrl.URL_DATA_Client;
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SSP=new SaveSharedPreference(getContext());

            /*
                Initialize a new DatePickerDialog

                DatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callBack,
                    int year, int monthOfYear, int dayOfMonth)
             */
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),this,year,month,day);
        return  dpd;
    }


    public void onDateSet(DatePicker view, int year, int month, int day){
        // Do something with the chosen date

        // Create a Date variable/object with user chosen date
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, 0, 0, 0);
        Date chosenDate = cal.getTime();

        int Month = month + 1;
        String formattedMonth = "" + Month;
        String formattedDayOfMonth = "" + day;

        if(Month < 10){

            formattedMonth = "0" + Month;
        }
        if(day < 10){

            formattedDayOfMonth = "0" + day;
        }
        formattedDate= year +"-" + formattedMonth + "-"+formattedDayOfMonth ;
        // Format the date using style and locale
       setDateNais();

        // Display the chosen date to app interface

    }
    private void setDateNais(){
        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("loading...");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
        Log.d("url",""+URL_DATA);
        getDialog().dismiss();

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.d("response"," "+response);
                    progressdialog.dismiss();

                    Navigation.findNavController(act,R.id.nav_host_fragment).navigate(R.id.profile_fragment);

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
                params.put("query","UpdateDateNais");
                params.put("DateNais",formattedDate);
                params.put("Username",SSP.getUsername());
                return params;
            }
        };
        reqeu.add(req);

    }
}
