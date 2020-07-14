package com.example.groceryshop.Fragments;

import android.app.ActionBar;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.Adapters.categorieAdapter;
import com.example.groceryshop.Beans.categorieList;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.DynamicGridLayoutManager;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.CustomToast;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.groceryshop.R.id.recyclerProd;

public class categorie_fragment extends Fragment {
    private  String URL_DATA;
    private RecyclerView RecView;
    private RecyclerView.Adapter adapter;
    public static List<categorieList> catlists;
    public DynamicGridLayoutManager ll;
    private Context context;
    private SaveSharedPreference SSP;
    View view1;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view1= inflater.inflate(R.layout.fragment_home,container,false);
        SSP=new SaveSharedPreference(getContext());

        URL_DATA=DBUrl.URL_DATA_Categorie;
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
        super.onViewCreated(view, savedInstanceState);}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private  List<categorieList> loadUrlData() {
                catlists=new ArrayList<>();
                final ProgressDialog progressdialog = new ProgressDialog(getActivity());
                progressdialog.setMessage("loading..");
                progressdialog.setCanceledOnTouchOutside(false);
                progressdialog.show();
                RequestQueue reqeu = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressdialog.dismiss();
                        try {
                            Log.i("response", "OnResponse "+response);
                            JSONArray json = new JSONArray(response);

                            for (int i = 0; i < json.length(); i++) {
                                JSONObject cat = json.getJSONObject(i);

                                categorieList catlist = new categorieList(cat.getInt("ID"),cat.getString("Nom"),cat.getString("NomAr"), cat.getString("Img"));
                                catlists.add(catlist);


                            }
                            ArrayList<produitCommand> commlist;
                            if(getArguments()==null){
                                commlist=new ArrayList<produitCommand>();

                            }else{
                                commlist= getArguments().getParcelableArrayList("commandList");
                            }

                            adapter = new categorieAdapter(getActivity(),catlists, getActivity(),commlist);

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
                        progressdialog.dismiss();
                        if(error instanceof NoConnectionError){
                            Bundle bundle=new Bundle();
                            bundle.putString("error","noconx");
                            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);

                        }else if(error instanceof TimeoutError)
                        {
                            Bundle bundle=new Bundle();
                            bundle.putString("error","timeout");
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
                        params.put("query","Select");



                        return params;
                    }
                };

                Log.i("this is my shit", " catlists size: "+catlists.size());
                reqeu.add(stringRequest);
                return catlists;


            }



}