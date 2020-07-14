package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyachi.stepview.bean.StepBean;
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
import java.util.Locale;
import java.util.Map;

public class ModalitePay  extends Fragment {
    private HorizontalStepView stepview5;
    private ProgressDialog progressdialog;
    private String URL_DATA;
    private TextView Montant,Credit,On_Deliver_Text;
    private CheckBox check_Credit,On_Delivery;
    private SaveSharedPreference SSP;
    private Button valider;
    private Float total;
    private float Wallet;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.mode_pay,container,false);
        Montant=v.findViewById(R.id.Montant);
        check_Credit=v.findViewById(R.id.Check_Credit);
        Credit=v.findViewById(R.id.Credit);
        On_Delivery=v.findViewById(R.id.On_Delivery);
        On_Deliver_Text=v.findViewById(R.id.On_Delivery_Text);
        total=getArguments().getFloat("total");



        SSP=new SaveSharedPreference(getActivity());
        valider=v.findViewById(R.id.valider);

        stepview5 = v.findViewById(R.id.step_view2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            stepview5.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            //stepview5.setTextDirection(View.TEXT_DIRECTION_LTR);
        }else
        {
            ViewCompat.setLayoutDirection (v, ViewCompat.LAYOUT_DIRECTION_LTR);
        }

        URL_DATA=DBUrl.URL_DATA_Client;
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Paiement", 0);
        StepBean stepBean1 = new StepBean("Disponibilité",-1);
        StepBean stepBean2 = new StepBean("Expédition",-1);
        stepsBeanList.add(stepBean0);stepsBeanList.add(stepBean1);stepsBeanList.add(stepBean2);
        List<Step> stepList = new ArrayList<>();
        stepList.add(new Step(getString(R.string.mode_pay), Step.State.CURRENT));
        stepList.add(new Step(getString(R.string.horaire)));
        stepList.add(new Step(getString(R.string.AdressExpTitle)));




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d("stepview "," "+stepview5.getClipBounds());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d("stepview "," "+" "+stepview5.getClipToPadding());
            }
        }


        stepview5.setSteps(stepList);
        stepview5.setLineLength(90);
        stepview5.setCompletedStepTextColor(Color.DKGRAY) // Default: Color.WHITE
                .setNotCompletedStepTextColor(Color.DKGRAY) // Default: Color.WHITE
                .setCurrentStepTextColor(Color.BLACK); // Default: Color.WHITE
        stepview5.setTextSize(8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.d("direction", " "+stepview5.getLayoutDirection()+" align="+stepview5.getTextAlignment());
            stepview5.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);


            Log.d("direction", " "+stepview5.getLayoutDirection()+" "+stepview5.getTextAlignment());

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.d("stepview info"," "+stepview5.getTextAlignment()+" "+stepview5.getTextDirection());
        }


        getProfileInfo();
        clicking();

        return v;
    }
    public void clicking()
    {
        On_Delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    valider.setEnabled(true);
                    if(check_Credit.isEnabled() && !check_Credit.isChecked())
                    {
                        check_Credit.setEnabled(false);
                        Credit.setEnabled(false);
                    }
                }else
                {
                    valider.setEnabled(false);
                    if(!check_Credit.isEnabled() && Float.parseFloat(Montant.getText().toString())>0)
                    {
                        check_Credit.setEnabled(true);
                        Credit.setEnabled(true);
                    }
                }
            }
        });

        check_Credit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(Float.parseFloat(Montant.getText().toString())>=total)
                    {
                        On_Delivery.setEnabled(false);
                        On_Deliver_Text.setEnabled(false);
                        valider.setEnabled(true);

                    }else
                    {


                    }
                    float remain=Wallet;
                    remain=remain-total;
                    Montant.setText(String.format(Locale.US,"%.2f",remain));
                }else
                {
                    Montant.setText(String.format(Locale.US,"%.2f",Wallet));
                    if(!On_Delivery.isEnabled())
                    {
                        On_Delivery.setEnabled(true);
                        On_Deliver_Text.setEnabled(true);
                        valider.setEnabled(false);
                    }else if(!On_Delivery.isChecked())
                    {
                        valider.setEnabled(false);
                    }

                }
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                if(check_Credit.isChecked())
                {
                    if(On_Delivery.isChecked())
                    {
                        bundle.putString("ModPay","creditOnDelivery");
                    }else
                    {
                        bundle.putString("ModPay","credit");
                    }
                }else
                {
                    bundle.putString("ModPay","OnDelivery");
                }
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.disp,bundle);
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
                    float total= BigDecimal.valueOf(client.getDouble("Credit")).floatValue();

                    Montant.setText(String.format("%.2f",total));
                    Wallet=total;
                    if(total==0)
                    {
                        Credit.setEnabled(false);
                        check_Credit.setEnabled(false);

                    }
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
                params.put("query","SelectUsername");
                params.put("Username",SSP.getUsername());
                params.put("Password",SSP.getPassword());



                return params;
            }
        };
        reqeu.add(req);

    }
}
