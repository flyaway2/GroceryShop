package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;

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
import com.vinay.stepview.HorizontalStepView;
import com.vinay.stepview.models.Step;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Disp extends Fragment {
    private HorizontalStepView stepview5;
    private ImageButton Start;
    private ProgressDialog progressdialog;
    private String URL_DATA;
    private TextView LivraisonTemps;
    private String Debut;
    private String Fin;
    private String[] fileList;
    private TextView interval,ModPay;
    private Button valider;
    private String ChosenTime;
    private Button Horaire;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.disp,container,false);
        URL_DATA=DBUrl.URL_DATA_Livraison;
        interval=v.findViewById(R.id.interval);
        ModPay=v.findViewById(R.id.ModPay);

        if(getArguments()!=null)
        {
            if(getArguments().getString("ModPay")!=null)
            {
                ModPay.setText(getArguments().getString("ModPay"));

            }
        }

        valider=v.findViewById(R.id.valider);
        stepview5 = v.findViewById(R.id.step_view2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            stepview5.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            //stepview5.setTextDirection(View.TEXT_DIRECTION_LTR);
        }else
        {
            ViewCompat.setLayoutDirection (v, ViewCompat.LAYOUT_DIRECTION_LTR);
        }

        Start=v.findViewById(R.id.start);
        LivraisonTemps=v.findViewById(R.id.LivraisonTemps);
        Horaire=v.findViewById(R.id.horaire);

        List<Step> stepList = new ArrayList<>();
        stepList.add(new Step(getString(R.string.mode_pay), Step.State.COMPLETED));
        stepList.add(new Step(getString(R.string.horaire),Step.State.CURRENT));
        stepList.add(new Step(getString(R.string.AdressExpTitle)));
        stepview5.setSteps(stepList);
        stepview5.setLineLength(90);
        stepview5.setCompletedStepTextColor(Color.DKGRAY) // Default: Color.WHITE
                .setNotCompletedStepTextColor(Color.DKGRAY) // Default: Color.WHITE
                .setCurrentStepTextColor(Color.BLACK); // Default: Color.WHITE
        stepview5.setTextSize(8);
        getProfileInfo();
        clicking();




        return v;
    }
    public void clicking()
    {
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("Disp",interval.getText().toString());
                bundle.putString("ModPay",ModPay.getText().toString());
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.expedition_address,bundle);
            }
        });
        Horaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minvalue=Integer.valueOf(Debut.substring(0,Debut.indexOf(":")));
                int maxvalue=Integer.valueOf(Fin.substring(0,Fin.indexOf(":")));
                String minute=Debut.substring(Debut.indexOf(":"),Debut.lastIndexOf(":"));
                ChosenTime="";
              fileList=new String[maxvalue-minvalue];
               int inc=minvalue;int i=0;
               while(inc<maxvalue)
               {

                   String interval=inc+""+minute+" - ";
                   inc++;
                   interval=interval+inc+""+minute;

                   fileList[i]=interval;
                   i++;

               }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.Choisir_liv));
                builder.setSingleChoiceItems(fileList, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("filelist"," "+which);
                        ChosenTime=fileList[which];




                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("filelist"," "+whichButton);
                        if(!ChosenTime.equals(""))
                        {
                            interval.setText(ChosenTime);
                            if(!valider.isEnabled())
                            {
                                valider.setEnabled(true);
                            }
                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                builder.create();
                builder.show();

            }
        });

    }
    private void getProfileInfo(){
        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("loading...");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
        Log.d("url",""+URL_DATA);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.d("response getprofileinfo"," "+response);
                    JSONArray json = new JSONArray(response);

                    JSONObject client = json.getJSONObject(0);
                    Debut=client.getString("Debut");
                    Fin=client.getString("Fin");

                    String interval=Debut+" - "+Fin;
                    LivraisonTemps.setText(interval);
                    progressdialog.dismiss();


                } catch (Exception e) {
                    Log.d("jsonException"," "+e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorvolley"," "+error.toString());
                progressdialog.dismiss();
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
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","select");



                return params;
            }
        };
        reqeu.add(req);

    }
}
