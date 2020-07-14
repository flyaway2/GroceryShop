package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.example.groceryshop.Activities.MainActivity;
import com.example.groceryshop.Adapters.produitAdapter;
import com.example.groceryshop.Beans.produitList;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.Interface.IOnBackPressed;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class produit_fragment extends Fragment implements IOnBackPressed {
    private  String URL_DATA;
    RecyclerView RecView;
    NavController navG;
    ArrayList<produitList> pl;
    produitAdapter pc;
    DividerItemDecoration ll;
    GridLayoutManager gridLL;
    private String Categorie;
    private TextView NumberItem;
    private MenuItem panier;
    private SaveSharedPreference SSP;
    private String Lang;

    private ActionBar toolbar;

    private RelativeLayout rellay;
    private LinearLayout linlay;
    private SearchView search;


    View view;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.produit_recycler,container,false);
        view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        SSP=new SaveSharedPreference(getContext());
        Lang=SSP.getLang();
        RecView=view.findViewById(R.id.prodR);
        gridLL=new GridLayoutManager(getActivity(),2);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.line_divider);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.line_divider);

        ll=new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL);

       // ll=new GridDividerItemDecoration(horizontalDivider, verticalDivider, 2) ;
       // RecView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        RecView.addItemDecoration(ll);

        MainActivity activity = (MainActivity) getActivity();
        Log.d("getparent:"," "+activity.toolbar.findViewById(R.id.panier));
        rellay=activity.toolbar.findViewById(R.id.panier);
        search=activity.toolbar.findViewById(R.id.search);
        linlay=activity.toolbar.findViewById(R.id.prodContainer);
        linlay.setVisibility(View.VISIBLE);

        buttonEffect(rellay);

        NumberItem=rellay.findViewById(R.id.badge_textView);
        NumberItem.setVisibility(View.INVISIBLE);

        rellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("rellay"," ");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.fragment2);
            }
        });

        search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                return true;
            }

        }
        );





        URL_DATA= DBUrl.URL_DATA_Produit ;

        Categorie=String.valueOf(getArguments().getInt("cat")) ;


        pl=new ArrayList<>();


        loadUrlData();


        this.view=view;
        setHasOptionsMenu(true);

        return view;
    }
    public  void buttonEffect(View button) {

        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_BUTTON_PRESS: {
                        v.getBackground().setColorFilter(Color.parseColor("#CAD3C8"), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_BUTTON_RELEASE: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        if(Lang.equals("ar"))
        {
            String NomAr=String.valueOf(getArguments().getString("NomAr")) ;
            actionBar.setTitle(NomAr);
        }else
        {
            String Nom=String.valueOf(getArguments().getString("Nom")) ;
            actionBar.setTitle(Nom);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void loadUrlData() {

        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("loading..");
        progressdialog.show();
        progressdialog.setCanceledOnTouchOutside(false);
        RequestQueue reqeu = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressdialog.dismiss();
                try {
                    Log.i("I'm here", "OnResponse "+response);
                    JSONArray json = new JSONArray(response);

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject cat = json.getJSONObject(i);
                        produitList catlist=new produitList(cat.getString("Nom"),cat.getString("Marque"),cat.getInt("Prix")
                        ,cat.getString("Taille"),cat.getString("CodeProd"),cat.getString("NomAr")
                        ,cat.getString("Categorie"),cat.getString("MarqueAr"),cat.getString("TailleAr")
                        ,cat.getString("Img"), BigDecimal.valueOf(cat.getDouble("Promotion")).floatValue(),
                                BigDecimal.valueOf(cat.getDouble("RemisePrix")).floatValue()
                        ,cat.getInt("QteRemise"));

                        pl.add(catlist);

                    }
                    //temporary

                    // temporary ends
                    ArrayList<produitCommand> commList= getArguments().getParcelableArrayList("commList");


                    pc = new produitAdapter(getActivity(),pl,NumberItem);
                    RecView.setAdapter(pc);
                    RecView.setLayoutManager(gridLL);
                    RecView.setHasFixedSize(true);
                    if(pc.getItemCount()==0)
                    {
                        Log.d("getitem","0*******************");
                        Bundle bundle=new Bundle();
                        bundle.putString("Nom",getArguments().getString("Nom"));
                        bundle.putString("NomAr",getArguments().getString("NomAr"));
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.product_to_no_Product,bundle);
                    }

                    Log.d("pfrag oncreatev",""+pc.toString());

                } catch (JSONException e) {
                    Log.e("my error", "I got error I got hoes", e);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error volley", error.toString());
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

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername",DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","SelectCategorie");
                params.put("Categorie",Categorie);



                return params;
            }
        };

        Log.i("this is my shit", " catlists size: "+pl.size());
        reqeu.add(stringRequest);



    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {


        super.onCreateOptionsMenu(menu,inflater);


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("cycles:","on pause****************");
    }

    @Override
    public void onStop() {
        super.onStop();

        search.clearFocus();
        linlay.setVisibility(View.GONE);
        Log.d("cycles:","on stop**************** "+search.isIconified());
        if(!search.isIconified())
        {
            search.setIconified(true);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("cycles:","on destroy****************");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("cycles:","on Resume****************");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Log.d("onoptionsitemselected"," "+id+" "+R.id.home);
        if(id==16908332)
        {
            Log.d("onoptionsitemselected","home");
            if (!search.isIconified()) {
                //action not popBackStack
                search.setIconified(true);

            } else {
                getFragmentManager().popBackStack();

            }
            return true;
        }else
        {
            return super.onOptionsItemSelected(item);
        }


    }



    @Override
    public boolean onBackPressed() {
        Log.d("onbackpressed"," fragment "+search.isIconified());
        if (!search.isIconified()) {
            //action not popBackStack
            search.setIconified(true);
            return true;
        } else {
            return false;
        }
    }

}
