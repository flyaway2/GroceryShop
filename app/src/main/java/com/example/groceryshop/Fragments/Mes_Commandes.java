package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.Adapters.MesComAdapter;
import com.example.groceryshop.Beans.Command;
import com.example.groceryshop.Beans.produitList;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mes_Commandes extends Fragment {
    private RecyclerView RecView;
    private ArrayList<Command> ComList;
    private LinearLayoutManager LL;
    private String URL_DATA;
    private ProgressDialog progressdialog;
    private MesComAdapter pc;

    private SaveSharedPreference SSP;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.mes_commandes_recycler,container,false);
        SSP=new SaveSharedPreference(getContext());
        RecView=v.findViewById(R.id.ComList);
        LL=new LinearLayoutManager(getActivity());
        URL_DATA= DBUrl.URL_DATA_Command_;
        ComList=new ArrayList<>();
        getCommand();


        return v;
    }
    private void getCommand(){
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

                    for(int i=0;i<json.length();i++)
                    {
                        JSONObject client = json.getJSONObject(i);
                        Command com=new Command(client.getInt("NumCom"),client.getInt("NumProd"),
                                client.getString("Date"),client.getString("Disp")
                        ,client.getInt("Delivrer"));

                        ComList.add(com);
                    }


                    pc=new MesComAdapter(ComList,getActivity());
                    RecView.setAdapter(pc);
                    RecView.setLayoutManager(LL);
                    RecView.setHasFixedSize(true);
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



                return params;
            }
        };
        reqeu.add(req);

    }
}
