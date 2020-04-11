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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.groceryshop.Activities.LoginActivity;
import com.example.groceryshop.Adapters.panierAdapter;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.SaveSharedPreference;

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
    ListView panier_gridview;
    SaveSharedPreference SSP;
    private panierAdapter panier;
    private  CheckBox checkB;
    private Button annuler;
    private MenuItem menItem,menItem1;
    private Boolean vis;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.panier_gridview,container,false);


        SSP=new SaveSharedPreference(getActivity());
        db=new DatabaseHelpler(getActivity());
        list_produit=new ArrayList<>();
         ress=getResources();
        if(SSP.getLoggedSattus(getActivity())==false){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        GridPanier =v.findViewById(R.id.fraggally);

        panier_gridview=v.findViewById(R.id.grid_panier);
        setHasOptionsMenu(true);

        listProd();
        return v;
    }
    public void listProd(){
            Cursor res=db.getAllData();

            while (res.moveToNext()){
                produitCommand pc=new produitCommand(res.getString(1),res.getString(3),res.getInt(4)
                ,res.getString(5),res.getInt(6),res.getString(0));
                list_produit.add(pc);

            }
            res.close();
        Log.d("logpanier"," "+list_produit.size());
            LayoutInflater inflater1=getLayoutInflater();
            LayoutInflater inflater2=getLayoutInflater();
            ViewGroup fouter=(ViewGroup) inflater2.inflate(R.layout.panier_footer,panier_gridview,false);


            panier = new panierAdapter(getActivity(),0,fouter,list_produit,ress);
            panier_gridview.addFooterView(fouter);
            panier_gridview.setAdapter(panier);
            if(panier.getCount()==0){
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.empty_fragment);
            }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater inflater1=inflater;
        inflater1.inflate(R.menu.remove_prod,menu);
        menItem=menu.findItem(R.id.select_all);
        menItem1=menu.findItem(R.id.annuler);
        annuler=(Button)menItem1.getActionView();

       checkB=(CheckBox)menItem.getActionView();

        menItem.setVisible(false);
        menItem1.setVisible(false);
        vis=false;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("optionitemselected"," ");
        int id=item.getItemId();
        if(id==R.id.remove_prod)
        {
            Log.d("optionitemselected"," if"+checkB.getVisibility());
            if(vis==false)
            {
                menItem.setVisible(true);
                menItem1.setVisible(true);
                panier.displayChecks();
                vis=true;
            }else
            {
                if(menItem.isChecked())
                {
                    panier.removeProd(true);
                    menItem.setChecked(false);
                    menItem.setVisible(false);
                    menItem1.setVisible(false);
                }else
                {
                    panier.removeProd(false);

                }

             panier.notifyDataSetChanged();
             if(panier.getCount()==0)
             {
                 Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.empty_fragment);
             }
            }


         Log.d("optionitemselected"," if"+checkB.getVisibility());

        }else if(id==R.id.select_all)
        {
            if(menItem.isChecked())
            {
                menItem.setChecked(false);
                panier.disselectAll();

            }else{
                menItem.setChecked(true);
                panier.selectAll();
            }


        }else if(id==R.id.annuler)
        {
            panier.hideChecks();
            menItem.setVisible(false);
            menItem1.setVisible(false);
            vis=false;
        }
        return super.onOptionsItemSelected(item);
    }
}
