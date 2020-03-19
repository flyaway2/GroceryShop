package com.example.groceryshop.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.groceryshop.Activities.LoginActivity;
import com.example.groceryshop.Adapters.panierAdapter;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.SaveSharedPreference;

import java.util.ArrayList;

public class panier_fragment extends Fragment {
    ArrayList<produitCommand> commlist;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    GridLayout GridPanier;
    ListView panier_gridview;
    SaveSharedPreference SSP;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.panier_gridview,container,false);
        SSP=new SaveSharedPreference(getActivity());
        Log.d("logpanier"," ");
        if(SSP.getLoggedSattus(getActivity())==false){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        GridPanier =v.findViewById(R.id.fraggally);
        panier_gridview=v.findViewById(R.id.grid_panier);

        if(getArguments()!=null){
            commlist= getArguments().getParcelableArrayList("commandList");


/*
            ((TextView)nom.findViewById(R.id.Com_Nom)).setText("Nom");
            View marq=linf.inflate(R.layout.fragment_gallery,null);


            ((TextView)marq.findViewById(R.id.Marq)).setText("Marque");

            View Size=linf.inflate(R.layout.fragment_gallery,null);


            ((TextView)Size.findViewById(R.id.Com_Size)).setText("Size");

            View prix=linf.inflate(R.layout.fragment_gallery,null);


            ((TextView)prix.findViewById(R.id.Com_Prix)).setText("Prix");

            View currency=linf.inflate(R.layout.fragment_gallery,null);
            ((TextView)currency.findViewById(R.id.currency)).setText("DA");


            View qte=linf.inflate(R.layout.fragment_gallery,null);

            ((TextView)qte.findViewById(R.id.Com_Qte)).setText("Qte");

            GridPanier.addView(nom);GridPanier.addView(marq);GridPanier.addView(Size);GridPanier.addView(prix);
            GridPanier.addView(currency);GridPanier.addView(qte);
*/
            if(!commlist.isEmpty()){

                Log.d("panier F:"," "+commlist.size());
                Log.d("panier nom:"," "+commlist.get(0).getNom());

                LayoutInflater inflater1=getLayoutInflater();
                LayoutInflater inflater2=getLayoutInflater();
                ViewGroup header=(ViewGroup)inflater1.inflate(R.layout.panier_header,panier_gridview,false);
                ViewGroup fouter=(ViewGroup)inflater2.inflate(R.layout.panier_footer,panier_gridview,false);
                panierAdapter panier = new panierAdapter(getActivity(),0, commlist,fouter);
                panier_gridview.addHeaderView(header);
                panier_gridview.addFooterView(fouter);
                 panier_gridview.setAdapter(panier);
                /*
                for(int i=0;i<commlist.size();i++){
                    LayoutInflater linf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    linf = LayoutInflater.from(getActivity());

                    View vi=linf.inflate(R.layout.fragment_gallery,null);
                    View view=getView();

                    String nomProd=commlist.get(i).getNom();


                    ((TextView)vi.findViewById(R.id.Com_Nom)).setText(nomProd.substring(nomProd.indexOf(":")+1,nomProd.length()));

                    String marqProd=commlist.get(i).getMarque();
                    LayoutInflater linfMarq = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    linfMarq = LayoutInflater.from(getActivity());

                    View marq=linf.inflate(R.layout.fragment_gallery,GridPanier,false);
                    ((TextView)marq.findViewById(R.id.Marq)).setText(marqProd.substring(marqProd.indexOf(":")+1,marqProd.length()));

                    View Size=linf.inflate(R.layout.fragment_gallery,null);


                    ((TextView)Size.findViewById(R.id.Com_Size)).setText(commlist.get(i).getSize());

                    View prix=linf.inflate(R.layout.fragment_gallery,null);


                    ((TextView)prix.findViewById(R.id.Com_Prix)).setText(Integer.toString(commlist.get(i).getPrix()) );



                    View qte=linf.inflate(R.layout.fragment_gallery,null);
                    Log.d("panier nom:"," "+commlist.get(i).getNom());
                    GridPanier.addView(marq);
                    GridPanier.addView(vi);





                }

                 */
            }
        }







        return v;
    }

}
