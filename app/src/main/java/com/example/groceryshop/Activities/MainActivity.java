package com.example.groceryshop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.Fragments.profile_fragment;
import com.example.groceryshop.R;
import com.example.groceryshop.Beans.categorieList;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.google.android.gms.common.util.Hex;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String URL_DATA= DBUrl.URL_DATA.concat("test.php");
 private DrawerLayout drawer;
    private RecyclerView RecView;
    private RecyclerView.Adapter adapter;
    private List<categorieList> catlists;
    private Toolbar toolbar;
    private NavigationView navview;
    private NavController navController;
    private  ArrayList<produitCommand> CommList;
    SaveSharedPreference SSP;
    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SSP=new SaveSharedPreference(getApplicationContext());

        setupNavigation();
        Log.d("mainlog"," "+SSP.getLoggedSattus(getApplicationContext()));
        if(SSP.getLoggedSattus(getApplicationContext())==false){

            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
       CommList=new ArrayList<produitCommand>();


    }
    private void setupNavigation() {
        Log.d("Hello","setup navigation");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#ffda79"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawer = findViewById(R.id.drawer);
        navview = findViewById(R.id.nav_view);
        navview.bringToFront();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(R.navigation.nav_graph);
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        NavigationUI.setupWithNavController(navview, navController);
        navview.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawer);
    }

    private void loadUrlData(){
        final ProgressDialog progressdialog=new ProgressDialog(this);
        progressdialog.setMessage("loading.. Im here");
        progressdialog.show();
        RequestQueue reqeu= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET,URL_DATA,new Response.Listener<String>(){
            @Override
           public void onResponse(String response){
                Log.i("I'm here","OnResponse");
               progressdialog.dismiss();
               try{
                   JSONArray json = new JSONArray(response);

                   for(int i=0;i<json.length();i++){
                       JSONObject cat= json.getJSONObject(i);

                       categorieList catlist=new categorieList(cat.getString("Nom"),cat.getString("img"));
                       catlists.add(catlist);
                       Log.i("this is my shit",catlist.getNom());

                   }


               }catch (JSONException e){
                 Log.e("my error","I got error I got hoes",e);
               }
           }
        }
        ,new Response.ErrorListener(){
          @Override
          public void onErrorResponse(VolleyError error){
              Toast.makeText(MainActivity.this, "error"+error.toString(), Toast.LENGTH_SHORT).show();

          }
        });
        reqeu.add(stringRequest);


        RecView.setAdapter(adapter);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("Hello","on navigation item selected");
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("commandList",CommList);
        switch (menuItem.getItemId()){

            case R.id.nav_produit:
                Toast.makeText(this,"I kill me",Toast.LENGTH_LONG);

                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.fragment1,bundle);

                Log.d("Hello","product selected");
                break;
            case R.id.nav_panier:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.fragment2,bundle);
                Log.d("Hello","panier selected");
                break;
            case R.id.nav_logout:
                SSP.logoutUser();
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_Profile:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.profile_fragment,bundle);
                Log.d("Hello","profile selected");
                break;
        }

        menuItem.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void onBackPressed(){

         if(drawer.isDrawerOpen(GravityCompat.START)){
             drawer.closeDrawer(GravityCompat.START);
        }else{
             if(profile_fragment.progress_exec==false){

             }
             super.onBackPressed();
         }

    }


}

 class getData extends AsyncTask<String, String, String> {

    HttpURLConnection urlConnection;
    private Context mContext;
    private String url1;
    getData(Context context,String url){
        mContext = context;
        url1=url;
    }
    @Override
    protected String doInBackground(String... args) {

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(url1);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {

        //Do something with the JSON string


        try {
           JSONArray json = new JSONArray(result);
              JSONObject ar= json.getJSONObject(0);
              Toast.makeText(mContext,"toast " +ar.getString("CodeProd")+" "+ar.getString("Nom")+ar.getString("Marque")
                , Toast.LENGTH_LONG)
                .show();


        } catch (JSONException e) {
            Log.e("Exception","",e);
            e.printStackTrace();

    }


}}
