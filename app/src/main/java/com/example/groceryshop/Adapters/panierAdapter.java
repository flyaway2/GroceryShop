package com.example.groceryshop.Adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.R;

import java.util.ArrayList;

public class panierAdapter extends ArrayAdapter<produitCommand> {
     Context context;
     ArrayList<produitCommand> commlist;
     LayoutInflater inflter;
     ViewGroup footer;
    Spinner sp;
    public panierAdapter(Context context,int id, ArrayList<produitCommand> commlist,ViewGroup v){
        super(context,id,commlist);
          this.context=context;
          this.commlist=commlist;
          inflter=(LayoutInflater.from(context));
          footer=v;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final TextView nom,marque,prix,size,prix_total_prod,total,codeProd;
        int sum=0;
        Spinner qte;
        ImageButton remove;
       if(view==null)
           view=inflter.inflate(R.layout.fragment_gallery,parent,false);
       final produitCommand pc=commlist.get(position);
       codeProd=view.findViewById(R.id.codeProd);
       codeProd.setText(pc.getCodeProd());
       nom=view.findViewById(R.id.Com_Nom);
       String pure_nom=pc.getNom().substring(pc.getNom().indexOf(":")+1,pc.getNom().length());
       nom.setText(pure_nom);
       marque=view.findViewById(R.id.Marq);
       String pure_marq=pc.getMarque().substring(pc.getMarque().indexOf(":")+1,pc.getMarque().length());
       marque.setText(pure_marq);
       prix=view.findViewById(R.id.Com_Prix);
       prix_total_prod=view.findViewById(R.id.totalProd);
       int prix_total=pc.getPrix()*(pc.getQte()-1);
       Log.d("prix_total"," "+prix_total+" ");
       prix_total_prod.setText(Integer.toString(prix_total));
       prix.setText(Integer.toString(pc.getPrix()));

       size=view.findViewById(R.id.Com_Size);
       String pure_size=pc.getSize().substring(pc.getSize().indexOf(":")+1,pc.getSize().length());
       size.setText(pure_size);

        sp=view.findViewById(R.id.Com_Qte);
        ArrayList<Integer> qteList=new ArrayList<>();
        qteList.add(1);qteList.add(2);qteList.add(3);qteList.add(4);qteList.add(5);qteList.add(6);qteList.add(7);qteList.add(8);qteList.add(9);qteList.add(10);
        if(sp!=null){
            ArrayAdapter<Integer> SpItem=new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item,qteList);
            SpItem.setDropDownViewResource(android.R.layout.simple_spinner_item);
            sp.setAdapter(SpItem);
            Log.d("spinner qte"," "+pc.getQte());
            sp.setSelection(pc.getQte()-1);
            total=footer.findViewById(R.id.total);

            for(int i=0;i<commlist.size();i++)
            {
                sum=sum+(commlist.get(i).getPrix()*(commlist.get(i).getQte()-1));

            }
            total.setText(Integer.toString(sum));
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                  int prix_totals=pc.getPrix()*Integer.parseInt(sp.getSelectedItem().toString()) ;
                  prix_total_prod.setText(Integer.toString(prix_totals));
                  for(int i=0;i<commlist.size();i++){
                      if(commlist.get(i).getCodeProd().equals(pc.getCodeProd())){
                          commlist.get(i).setQte(Integer.parseInt(sp.getSelectedItem().toString()));

                      }
                      int sum1=0;
                      for(int j=0;j<commlist.size();j++){
                          sum1=sum1+Integer.parseInt(prix_total_prod.getText().toString());

                      }
                      total.setText(Integer.toString(sum1));

                  }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


       return view;
    }


}
