package com.example.groceryshop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.MenuItem;
import android.widget.Toast;

import com.example.groceryshop.Fragments.produit_fragment;
import com.example.groceryshop.Interface.IOnBackPressed;
import com.example.groceryshop.R;
import com.example.groceryshop.Beans.categorieList;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String URL_DATA= DBUrl.URL_DATA.concat("test.php");
 private DrawerLayout drawer;
    private RecyclerView RecView;
    private RecyclerView.Adapter adapter;
    private List<categorieList> catlists;
    public Toolbar toolbar;
    private NavigationView navview;
    private NavController navController;
    private  ArrayList<produitCommand> CommList;
    SaveSharedPreference SSP;
    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localeCode.toLowerCase()));
        }else
        {
            configuration.locale=new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }
    @Override

    public void onCreate(Bundle savedInstanceState) {

        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        super.onCreate(savedInstanceState);

        SSP=new SaveSharedPreference(getApplicationContext());
        setAppLocale(SSP.getLang());


        setContentView(R.layout.activity_main);




        setupNavigation();

        Log.d("FCMTOKEN", FirebaseInstanceId.getInstance().getToken());

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

            case R.id.nav_parametre:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.parametre,bundle);
                break;
            case R.id.nav_Credit:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.credit);
                break;
            case R.id.nav_MesCommandes:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.mes_Commandes);
                break;
        }

        menuItem.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void onBackPressed(){

         Log.d("onbackpressed"," "+Navigation.findNavController(this,R.id.nav_host_fragment).getCurrentDestination().getId()
           +" "+R.id.recyclerP_fragment);
         if(drawer.isDrawerOpen(GravityCompat.START)){
             drawer.closeDrawer(GravityCompat.START);
        }else{

             if(Navigation.findNavController(this,R.id.nav_host_fragment).getCurrentDestination().getId()==R.id.recyclerP_fragment)
             {

                 Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
                 Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);


                 Fragment fragment =getSupportFragmentManager().findFragmentById(R.id.recyclerP_fragment);
             
                 if (!(((IOnBackPressed) currentFragment).onBackPressed())) {
                     Log.d("this it guys","backing up*********************");
                     super.onBackPressed();
                 }
             }else
             {
                 super.onBackPressed();
             }




         }





    }


}

