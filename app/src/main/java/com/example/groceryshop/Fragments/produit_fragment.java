package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.Adapters.categorieAdapter;
import com.example.groceryshop.Beans.categorieList;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.DynamicGridLayoutManager;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.groceryshop.R.id.recyclerProd;

public class produit_fragment extends Fragment {
    private static final String URL_DATA= DBUrl.URL_DATA.concat("index.php");
    private RecyclerView RecView;
    private RecyclerView.Adapter adapter;
    public static List<categorieList> catlists;
    public DynamicGridLayoutManager ll;
    private Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view1= inflater.inflate(R.layout.fragment_home,container,false);
        Log.d("produit fragment",view1.toString());
        RecView= view1.findViewById(recyclerProd);

        ll = new DynamicGridLayoutManager(getActivity(),2);

        GridLayoutManager ll2=new GridLayoutManager(getActivity(),2);
        Log.i("auto measure: ",""+ll.isAutoMeasureEnabled());


        loadUrlData();


        return view1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d("produit fragment","onCreate");



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        }

            private  List<categorieList> loadUrlData() {
                catlists=new ArrayList<>();
                final ProgressDialog progressdialog = new ProgressDialog(getActivity());
                progressdialog.setMessage("loading.. Im here");
                progressdialog.show();
                RequestQueue reqeu = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("I'm here", "OnResponse");
                        progressdialog.dismiss();
                        try {
                            JSONArray json = new JSONArray(response);

                            for (int i = 0; i < json.length(); i++) {
                                JSONObject cat = json.getJSONObject(i);

                                categorieList catlist = new categorieList(cat.getString("Nom"), cat.getString("img"));
                                catlists.add(catlist);


                            }
                            //temporary
                            if(catlists.size()==0 && catlists.isEmpty()){
                                categorieList catlist = new categorieList("beurre", "index");
                                categorieList catlist1 = new categorieList("gateaux", "cupcake");
                                catlists.add(catlist);
                                catlists.add(catlist1);
                            }
                            // temporary ends
                            ArrayList<produitCommand> commlist;
                            if(getArguments()==null){
                                commlist=new ArrayList<produitCommand>();

                            }else{
                                commlist= getArguments().getParcelableArrayList("commandList");
                            }

                            adapter = new categorieAdapter(catlists, getActivity(),commlist);

                            RecView.setAdapter(adapter);
                            RecView.setLayoutManager(ll);
                            RecView.setHasFixedSize(true);


                        } catch (JSONException e) {
                            Log.e("my error", "I got error I got hoes", e);
                        }
                    }
                }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("errorprod"," "+error.toString());
                        Toast.makeText(context, "error" + error.toString(), Toast.LENGTH_LONG).show();

                    }

                });

                Log.i("this is my shit", " catlists size: "+catlists.size());
                reqeu.add(stringRequest);
                return catlists;


            }



}