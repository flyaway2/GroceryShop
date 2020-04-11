package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import com.example.groceryshop.Adapters.produitAdapter;
import com.example.groceryshop.Beans.produitList;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class produit_fragment extends Fragment {
    private static  String URL_DATA;
    RecyclerView RecView;
    NavController navG;
    ArrayList<produitList> pl;
    produitAdapter pc;
    LinearLayoutManager ll;


    View view;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.produit_recycler,container,false);
        RecView=view.findViewById(R.id.prodR);
        ll=new LinearLayoutManager(getActivity());
        URL_DATA= DBUrl.URL_DATA.concat("prod.php?cat=") ;

        URL_DATA= URL_DATA.concat(getArguments().getString("cat"));
        pl=new ArrayList<>();


        pl=loadUrlData();


        this.view=view;
        Log.d("Pfrag oncreateview", Objects.requireNonNull(getArguments().getString("cat")));

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    private ArrayList<produitList> loadUrlData() {

        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("loading..");
        progressdialog.show();
        progressdialog.setCanceledOnTouchOutside(false);
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
                        produitList catlist=new produitList(cat.getString("Nom"),cat.getString("Marque"),cat.getInt("Prix")
                        ,cat.getString("Taille"),cat.getString("CodeProd"));

                        pl.add(catlist);

                    }
                    //temporary

                    // temporary ends
                    ArrayList<produitCommand> commList= getArguments().getParcelableArrayList("commList");
                    pc = new produitAdapter(pl);
                    RecView.setAdapter(pc);
                    RecView.setLayoutManager(ll);
                    RecView.setHasFixedSize(true);

                    Log.d("pfrag oncreatev",""+pc.toString());

                } catch (JSONException e) {
                    Log.e("my error", "I got error I got hoes", e);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error" + error.toString(), Toast.LENGTH_LONG).show();
                progressdialog.dismiss();
                if(error instanceof TimeoutError){
                    Bundle bundle=new Bundle();
                    bundle.putString("error","timeout");
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }else if(error instanceof NoConnectionError){
                    Bundle bundle=new Bundle();
                    bundle.putString("error","noconx");
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }

            }

        });

        Log.i("this is my shit", " catlists size: "+pl.size());
        reqeu.add(stringRequest);
        return pl;


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater inflater1=inflater;
       inflater1.inflate(R.menu.search_bar,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);

        SearchView s=(SearchView) searchItem.getActionView();
        s.setImeOptions(EditorInfo.IME_ACTION_DONE);
        s.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Filter f=pc.getFilter();
                Log.d("searchbar",""+f.toString()+"");
                f.filter(newText);
                pc.setFilter(newText);
                return false;
            }
        });
    }

}
