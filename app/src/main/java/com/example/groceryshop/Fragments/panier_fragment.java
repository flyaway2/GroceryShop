package com.example.groceryshop.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop.Activities.LoginActivity;
import com.example.groceryshop.Adapters.panierAdapter;
import com.example.groceryshop.Adapters.panierRecAdapter;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class panier_fragment extends Fragment {
    private DatabaseHelpler db;
    private ArrayList<produitCommand> list_produit;
    Resources ress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    GridLayout GridPanier;
    RecyclerView RecView;
    SaveSharedPreference SSP;
    private panierRecAdapter panier;
    private Boolean vis;
    private LinearLayoutManager ll;
    private TextView Total;
    private BottomAppBar botbar;
    private Button valider;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.panier_gridview,container,false);

        botbar=v.findViewById(R.id.bottom_bar);

        Total=v.findViewById(R.id.TotalGen);
        valider=v.findViewById(R.id.valider);


        SSP=new SaveSharedPreference(getActivity());
        Log.d("language "," "+SSP.getLang());
        db=new DatabaseHelpler(getActivity());
        ll=new LinearLayoutManager(getActivity());
        list_produit=new ArrayList<>();
         ress=getResources();
        if(SSP.getLoggedSattus(getActivity())==false){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getAllData(SSP.getUsername()).getCount()>0)
                {
                    Bundle bundle=new Bundle();
                    bundle.putFloat("total",Float.parseFloat(Total.getText().toString()));
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.modalitePay,bundle);
                }

            }
        });

        GridPanier =v.findViewById(R.id.fraggally);

        RecView=v.findViewById(R.id.grid_panier);
        setHasOptionsMenu(true);

        listProd();
        return v;
    }
    public void listProd(){
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
        Log.d("logpanier"," "+list_produit.size());
            LayoutInflater inflater1=getLayoutInflater();
            LayoutInflater inflater2=getLayoutInflater();

            panier = new panierRecAdapter(getActivity().getSupportFragmentManager(),getActivity(),list_produit,Total,botbar);
            RecView.setAdapter(panier);
            RecView.setLayoutManager(ll);
            if(panier.getItemCount()==0){
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_panier_to_empty_fragment);
            }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater inflater1=inflater;
        inflater1.inflate(R.menu.remove_prod,menu);



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("optionitemselected"," ");
        int id=item.getItemId();
        if(id==R.id.remove_prod)
        {
            valider_DialFrag valider=new valider_DialFrag();
            Bundle bundle=new Bundle();
            bundle.putString("valider","viderpanier");
            valider.setArguments(bundle);
            FragmentManager fm=(getActivity()).getSupportFragmentManager();
            valider.show(fm,"vider panier");

        }
        return super.onOptionsItemSelected(item);
    }
}
