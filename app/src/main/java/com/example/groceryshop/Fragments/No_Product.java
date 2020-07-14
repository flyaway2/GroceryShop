package com.example.groceryshop.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.groceryshop.R;
import com.example.groceryshop.custom.SaveSharedPreference;

public class No_Product extends Fragment {
    private SaveSharedPreference SSP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.no_product,container,false);
        SSP=new SaveSharedPreference(getContext());


        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        if(SSP.getLang().equals("ar"))
        {
            String NomAr=String.valueOf(getArguments().getString("NomAr")) ;
            actionBar.setTitle(NomAr);
        }else
        {
            String Nom=String.valueOf(getArguments().getString("Nom")) ;
            actionBar.setTitle(Nom);
        }
    }

   
}
