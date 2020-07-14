package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.vinay.stepview.HorizontalStepView;
import com.vinay.stepview.models.Step;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class expedition_address extends Fragment {
    private RadioGroup AddressExp;
    private Button valider;
    private RadioButton OldAddress;
    private RadioButton NewAddress;
    private TextView AjouterAddress;
    private CardView AjouterCard;
    private TextView AddressNom,Disp,ModPay,Lat,Longi;
    private SaveSharedPreference SSP;
    private ProgressDialog progressdialog;
    private String URL_DATA,URL_DATA_Client;
    private DatabaseHelpler db;
    private String OldLat;
    private String OldLong;
    private ArrayList<produitCommand> list_produit;
    private HorizontalStepView stepview5;
    private float sum;
    private float Credit;
    String id,date;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.expedition_address,container,false);

        SSP=new SaveSharedPreference(getActivity());
        URL_DATA=DBUrl.URL_DATA_Command_;

        URL_DATA_Client=DBUrl.URL_DATA_Client;
        db=new DatabaseHelpler(getContext());
        list_produit=new ArrayList<>();
        AddressExp=v.findViewById(R.id.AddressExp);
        valider=v.findViewById(R.id.valider);
        OldAddress=v.findViewById(R.id.OldAddress);
        NewAddress=v.findViewById(R.id.NewAddress);
        AjouterCard=v.findViewById(R.id.AddressCard);
        Disp=v.findViewById(R.id.Disp);
        ModPay=v.findViewById(R.id.ModPay);
        Lat=v.findViewById(R.id.Lat);
        Longi=v.findViewById(R.id.Long);
        getProducts();

        List<Step> stepList = new ArrayList<>();
        stepList.add(new Step(getString(R.string.mode_pay), Step.State.COMPLETED));
        stepList.add(new Step(getString(R.string.horaire),Step.State.COMPLETED));
        stepList.add(new Step(getString(R.string.AdressExpTitle),Step.State.CURRENT));
        stepview5 = v.findViewById(R.id.step_view2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            stepview5.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            //stepview5.setTextDirection(View.TEXT_DIRECTION_LTR);
        }else
        {
            ViewCompat.setLayoutDirection (v, ViewCompat.LAYOUT_DIRECTION_LTR);
        }

        stepview5.setSteps(stepList);
        stepview5.setLineLength(90);
        stepview5.setCompletedStepTextColor(Color.DKGRAY) // Default: Color.WHITE
                .setNotCompletedStepTextColor(Color.DKGRAY) // Default: Color.WHITE
                .setCurrentStepTextColor(Color.BLACK); // Default: Color.WHITE
        stepview5.setTextSize(8);

        if(getArguments()!=null)
        {
            if(getArguments().getString("Disp")!=null && getArguments().getString("ModPay")!=null)
            {


                Disp.setText(getArguments().getString("Disp"));
                ModPay.setText(getArguments().getString("ModPay"));

            }
        }


        AjouterAddress=v.findViewById(R.id.AjouterAddress);
        AddressNom=v.findViewById(R.id.AddressNom);
        clicking();
        if(getArguments()!=null)
        {
            if(getArguments().getString("Name")!=null)
            {
                NewAddress.setChecked(true);
                AddressNom.setText(getArguments().getString("Name"));
                Log.d("latlong"," "+getArguments().getString("latitude"));
                Lat.setText(getArguments().getString("latitude"));
                Longi.setText(getArguments().getString("longitude"));

                if(!valider.isEnabled())
                {
                    valider.setEnabled(true);
                }

            }
        }


        return v;
    }
    public void getProducts()
    {
        Cursor res=db.getAllData(SSP.getUsername());

        while (res.moveToNext()){
            produitCommand pc=new produitCommand(res.getString(1),res.getString(3),res.getInt(4)
                    ,res.getString(5),res.getInt(6),res.getString(0)
                    ,res.getInt(11),res.getFloat(12),res.getFloat(10)
                    ,res.getString(13),res.getString(8),res.getString(9)
                    ,res.getString(8));
            list_produit.add(pc);

        }
        res.close();
        sum=0;

        for(int i=0;i<list_produit.size();i++)
        {
            float prix =list_produit.get(i).getPrix()*list_produit.get(i).getQte();

            if(list_produit.get(i).getPromotion()>0)
            {
                prix=list_produit.get(i).getPromotion()*list_produit.get(i).getQte();
            }
            if(list_produit.get(i).getPrixRemise()>0 && list_produit.get(i).getQteRemise()>0)
            {
                prix=list_produit.get(i).getPrixRemise()*list_produit.get(i).getQte();
            }
            sum=sum+prix;

        }


    }

    public void clicking()
    {
        AjouterAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bunde=new Bundle();
                bunde.putString("Disp",Disp.getText().toString());
                bunde.putString("ModPay",ModPay.getText().toString());
                bunde.putString("Address","expedition");


                NavController navcont=Navigation.findNavController(getActivity(),R.id.nav_host_fragment);


                navcont.navigate(R.id.map_fragment,bunde);


            }
        });
        AddressExp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.OldAddress)
                {
                    if(!valider.isEnabled())
                    {
                        valider.setEnabled(true);
                    }
                    if(AjouterCard.getVisibility()==View.VISIBLE)
                    {
                        AjouterCard.setVisibility(View.INVISIBLE);
                    }



                }else if(checkedId==R.id.NewAddress)
                {

                    if(valider.isEnabled() && AddressNom.getText().toString().equals(""))
                    {
                        valider.setEnabled(false);
                    }
                   AjouterCard.setVisibility(View.VISIBLE);
                }
            }
        });
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           getAddress();
            }
        });
    }
    private void getDate(){

        Log.d("getDate",""+URL_DATA);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response getDate"," "+response);
                try {

                    JSONArray json = new JSONArray(response);

                    JSONObject client = json.getJSONObject(0);

                    date=client.getString("Date");


                    Bundle bundle=new Bundle();
                    bundle.putString("Disp",getArguments().getString("Disp"));
                    bundle.putString("ModPay",getArguments().getString("ModPay"));
                    bundle.putString("date",date);
                    if(OldAddress.isChecked())
                    {
                        bundle.putString("Exp","OldAddress");
                    }else
                    {
                        bundle.putString("Exp","NewAddress");
                        bundle.putString("Lat",Lat.getText().toString());
                        bundle.putString("Long",Longi.getText().toString());

                    }


                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_expedition_address_to_receipt,bundle);




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
                Log.d("oldaddress date"," "+OldAddress.isChecked()+" "+id);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","SelectNumCom");
                params.put("NumCom",id);




                return params;
            }
        };
        reqeu.add(req);

    }
    private void setCredit(final float paidSum){

        Log.d("setCredit",""+URL_DATA_Client);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA_Client, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.d("response getprofileinfo"," "+response);




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
                params.put("query","UpdateCreditSub");
                params.put("Username",SSP.getUsername());
                params.put("Credit",String.valueOf(paidSum));



                return params;
            }
        };
        reqeu.add(req);

    }
    private void getCredit(){

        Log.d("getCredit",""+URL_DATA_Client);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA_Client, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.d("response getprofileinfo"," "+response);
                    JSONArray json = new JSONArray(response);

                    JSONObject client = json.getJSONObject(0);
                    float total= BigDecimal.valueOf(client.getDouble("Credit")).floatValue();
                    Credit=total;
                    AjouterCommandes();



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
                params.put("query","SelectUsername");
                params.put("Username",SSP.getUsername());
                params.put("Password",SSP.getPassword());



                return params;
            }
        };
        reqeu.add(req);

    }
    private void AjouterCommandes(){
        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("loading...");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
        Log.d("AjouterCommande",""+URL_DATA);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response AjouterCommand"," "+response);
                try {


                    id=response;
                    getDate();
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
                params.put("query","Insert");
                params.put("Username",SSP.getUsername());
                params.put("NumProd",String.valueOf(db.getAllData(SSP.getUsername()).getCount()));
                params.put("Delivrer","0");
                params.put("Disp",Disp.getText().toString());
                params.put("ModPay",ModPay.getText().toString());
                params.put("Total",String.valueOf(sum));
                if(ModPay.getText().toString().equals("OnDelivery"))
                {
                    params.put("PaidSum","0");

                }else if(ModPay.getText().toString().equals("creditOnDelivery"))
                {
                    params.put("PaidSum",String.valueOf(Credit));
                    setCredit(Credit);


                }else if(ModPay.getText().toString().equals("credit"))
                {

                    params.put("PaidSum",String.valueOf(sum));
                    setCredit(sum);

                }
                Log.d("oldaddress"," "+OldAddress.isChecked()+" "+OldLat+" "+OldLong);
                if(OldAddress.isChecked())
                {
                    Log.d("old address"," "+ Lat.getText().toString()+" "+ Longi.getText().toString());
                    params.put("Latitude",OldLat);
                    params.put("Longitude",OldLong);
                }else
                {
                    Log.d("new address"," "+ Lat.getText().toString()+" "+ Longi.getText().toString());
                    params.put("Latitude",Lat.getText().toString());
                    params.put("Longitude",Longi.getText().toString());
                }
                Log.d("who's the empty bitch"," "+Disp.getText().toString() +ModPay.getText().toString()+" ");

                Log.d("listproduititems"," "+list_produit.size());

                for(int i=0;i<list_produit.size();i++)
                {
                    int j=i+1;
                    params.put("CodeProd"+j,list_produit.get(i).getCodeProd());
                    params.put("Qte"+j,String.valueOf(list_produit.get(i).getQte()));
                }



                return params;
            }
        };
        reqeu.add(req);

    }

    private void getAddress(){

        Log.d("getAddress",""+URL_DATA_Client);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA_Client, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response getAddress"," "+response);
                try {
                    JSONArray json = new JSONArray(response);


                    JSONObject client = json.getJSONObject(0);

                    OldLat=client.getString("latitude");
                    OldLong=client.getString("longitude");
                    getCredit();


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
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername",DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","SelectUsername");
                Log.d("username password"," "+SSP.getUsername()+" "+SSP.getPassword());
                params.put("Username",SSP.getUsername());
                params.put("Password",SSP.getPassword());



                return params;
            }
        };
        reqeu.add(req);

    }
}
