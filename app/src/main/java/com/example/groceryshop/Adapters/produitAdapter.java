package com.example.groceryshop.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop.Beans.produitList;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.R;

import java.util.ArrayList;


public class produitAdapter extends RecyclerView.Adapter<produitAdapter.ViewHolder>  {
  private ArrayList<produitList> pl;
  private ArrayList<produitCommand> commList;

    public produitAdapter(ArrayList<produitList> pl, ArrayList<produitCommand> commList) {
        this.pl = pl;
        this.commList=commList;
        Log.d("prodcat constr"," "+pl.size());
    }

    @NonNull
    @Override
    public produitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.produit,parent,false);


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final produitAdapter.ViewHolder holder, int position) {
      produitList plist=pl.get(position);
      holder.Nom.setText("Nom: "+plist.getNom());
      Log.d("prodCat bindV"," "+plist.getPrix());

      holder.Marq.setText("Marque: "+plist.getMarque());
        holder.Prix.setText("Prix: "+String.valueOf(plist.getPrix()+" DA"));
        holder.codeProd.setText(plist.getCodeProd());

      holder.acheter.setOnClickListener(new Button.OnClickListener(){
          @Override
          public void onClick(View v) {
              holder.acheter.setVisibility(v.INVISIBLE);
              holder.ProdCo.setVisibility(v.VISIBLE);
              holder.currentVal.setVisibility(v.VISIBLE);
              String prix=holder.Prix.getText().toString();

              int endI=prix.indexOf("D")-1;
              int startI=prix.indexOf(":")+2;
              Log.d("prix D:"," "+" D:"+prix.indexOf("D")+" :"+prix.indexOf(":")+" text:"+prix+" substring:"+prix.substring(startI,endI));
              commList.add(new produitCommand(holder.Nom.getText().toString(),holder.Marq.getText().toString(),Integer.parseInt(holder.Prix.getText().toString().substring(startI,endI))
              ,holder.qte.getText().toString(),Integer.parseInt(holder.currentVal.getText().toString()) ,holder.codeProd.getText().toString() ));
          }
      });
      holder.qte.setText("size: "+plist.getQte());
        holder.ProdCo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("seekbar"," "+commList.size());
                for(produitCommand p:commList){
                    Log.d("seekbar El"," "+p.getNom());
                }

                if(pval==0){
                    holder.currentVal.setVisibility(View.INVISIBLE);
                    holder.ProdCo.setVisibility(View.INVISIBLE);
                    holder.acheter.setVisibility(View.VISIBLE);
                    for(produitCommand p:commList){
                        if(p.getCodeProd().equals(holder.codeProd.getText().toString())){
                            commList.remove(p);
                        }
                    }
                }else{


                    holder.currentVal.setText(" "+pval);
                    Log.d("before loop "," "+commList.size());
                    for(int i=0;i<commList.size();i++){
                        Log.d("codeprod1 "," "+commList.get(i).getCodeProd()+" "+holder.codeProd.getText().toString());
                     if(commList.get(i).getCodeProd().equals(holder.codeProd.getText().toString())){
                         commList.get(i).setQte(Integer.parseInt(holder.currentVal.getText().toString().trim()));
                         Log.d("viewHolder curval"," "+commList.get(i).getQte());


                     }
                    }
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return pl.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Nom,Marq,Prix,qte,currentVal,codeProd;
        SeekBar ProdCo;
        Button acheter;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            Nom=itemView.findViewById(R.id.Name);
            Marq=itemView.findViewById(R.id.marque);
            Prix=itemView.findViewById(R.id.prix);
            qte=itemView.findViewById(R.id.qte);
            currentVal=itemView.findViewById(R.id.currentVal);
            ProdCo=itemView.findViewById(R.id.prodCount);
            acheter=itemView.findViewById(R.id.AjoutP);
            codeProd=itemView.findViewById(R.id.codeProd);
            ProdCo.setVisibility(itemView.INVISIBLE);
            currentVal.setVisibility(itemView.INVISIBLE);

        }


    }


}

