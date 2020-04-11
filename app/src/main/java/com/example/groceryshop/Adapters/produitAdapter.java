package com.example.groceryshop.Adapters;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop.Beans.produitList;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;

import java.util.ArrayList;


public class produitAdapter extends RecyclerView.Adapter<produitAdapter.ViewHolder>  implements Filterable {
  private ArrayList<produitList> pl;
    private ArrayList<produitList> pl_full;

    private DatabaseHelpler db;
    private Cursor res;
    private View vi;

    public produitAdapter(ArrayList<produitList> pl) {
        this.pl = pl;

        pl_full=new ArrayList<>(pl);
        Log.d("plfull"," "+pl_full.size());
    }

    @NonNull
    @Override
    public produitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.produit,parent,false);
        vi=view;

        db = new DatabaseHelpler(parent.getContext());
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull final produitAdapter.ViewHolder holder, int position) {
      produitList plist=pl.get(position);
      holder.Nom.setText(plist.getNom());


      holder.Marq.setText(plist.getMarque());
        holder.Prix.setText(plist.getPrix()+" DA");
        holder.codeProd.setText(plist.getCodeProd());
        holder.qte.setText(plist.getQte());

        if(holder.qte.getText().toString().toLowerCase().contains("l")){
            holder.qte_img.setImageResource(R.drawable.volume);
        }else if(holder.qte.getText().toString().toLowerCase().contains("g")){
            holder.qte_img.setImageResource(R.drawable.weight);
        }else{
            holder.qte_img.setImageResource(R.drawable.pcs);
        }
      if(db.getAllData().getCount()!=0){
         Cursor res= db.getAllData();

          while(res.moveToNext()){

              if(res.getString(0).equals(holder.codeProd.getText().toString())){
                  holder.acheter.setEnabled(false);
                holder.QteContainer.setVisibility(View.VISIBLE);
                  holder.currentVal.setText(String.valueOf(res.getInt(6)));
                  holder.ProdCo.setProgress(res.getInt(6));
              }

          }
          res.close();


      }
      holder.minus.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              int prog_value=holder.ProdCo.getProgress()-1;
              Log.d("progvalue",""+prog_value);
              holder.ProdCo.setProgress(prog_value);
              if(holder.ProdCo.getProgress()==0){
                  holder.QteContainer.setVisibility(View.INVISIBLE);
                  holder.acheter.setEnabled(true);
                  res = db.getAllData();
                  while(res.moveToNext()){

                      if(res.getString(0).contains(holder.codeProd.getText().toString())){
                          db.deleteData(res.getString(0));
                      }

                  }
                  res.close();
              }else
              {
                  holder.currentVal.setText(""+prog_value);

                  boolean b= db.updateQte(holder.codeProd.getText().toString(),prog_value);
              }

          }
      });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prog_value=holder.ProdCo.getProgress()+1;
                Log.d("progvalue",""+prog_value);
                holder.ProdCo.setProgress(prog_value);


                    holder.currentVal.setText(""+prog_value);

                    boolean b= db.updateQte(holder.codeProd.getText().toString(),prog_value);

            }
        });
      holder.acheter.setOnClickListener(new Button.OnClickListener(){
          @Override
          public void onClick(View v) {
              holder.acheter.setEnabled(false);
              holder.QteContainer.setVisibility(View.VISIBLE);
              String codeprod=holder.codeProd.getText().toString();
              String Nom=holder.Nom.getText().toString();
              String marq=holder.Marq.getText().toString();
              String stripped_price = holder.Prix.getText().toString().replaceAll("[^0-9]", "");
              int price=Integer.parseInt(stripped_price);
              String taille=holder.qte.getText().toString();
              int qte=Integer.parseInt(holder.currentVal.getText().toString());
              db.insertData(codeprod,Nom,marq,price,taille,qte);
          }
      });

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

                res = db.getAllData();
                if(pval==0){
                    holder.QteContainer.setVisibility(View.INVISIBLE);
                    holder.acheter.setEnabled(true);
                    while(res.moveToNext()){

                        if(res.getString(0).contains(holder.codeProd.getText().toString())){
                          db.deleteData(res.getString(0));
                        }

                    }


                }else{


                    holder.currentVal.setText(""+pval);

                   boolean b= db.updateQte(holder.codeProd.getText().toString(),pval);


                }
                res.close();


            }
        });

    }




    @Override
    public int getItemCount() {
        return pl.size();
    }

    public void setFilter(String query){
        Log.d("insidesetfilter",""+query);
        getFilter().filter(query);

    }
    @Override
    public Filter getFilter() {
        Log.d("insidefilter"," ");
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                ArrayList<produitList> filteredList=new ArrayList<>();
                if(constraint==null || constraint.length()==0)
                {
                    Log.d("filterresult"," empty="+pl_full.size());
                    filteredList.addAll(pl_full);
                }else
                {

                    String filterPattern=constraint.toString().toLowerCase().trim();
                    for(produitList prodL:pl_full){
                        Log.d("prodlist"," constraint="+prodL.getNom().toLowerCase());
                        if(prodL.getNom().toLowerCase().contains(filterPattern)){
                            filteredList.add(prodL);

                        }
                    }
                    Log.d("filterresult"," constraint="+filteredList.size());
                }
                FilterResults results=new FilterResults();
                results.values=filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {

                pl.clear();
                pl.addAll((ArrayList)results.values);
                Log.d("publishresult"," "+pl.size());
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Nom,Marq,Prix,qte,currentVal,codeProd;
        SeekBar ProdCo;
        LinearLayout QteContainer;
        Button acheter;
        ImageButton plus,minus;
        ImageView qte_img;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            Nom=itemView.findViewById(R.id.Name);
            Marq=itemView.findViewById(R.id.marque);
            Prix=itemView.findViewById(R.id.prix);
            qte=itemView.findViewById(R.id.qte);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
            qte_img=itemView.findViewById(R.id.qte_img);

            QteContainer=itemView.findViewById(R.id.QteContainer);
            currentVal=itemView.findViewById(R.id.currentVal);
            ProdCo=itemView.findViewById(R.id.prodCount);
            acheter=itemView.findViewById(R.id.AjoutP);
            codeProd=itemView.findViewById(R.id.codeProd);
            QteContainer.setVisibility(View.INVISIBLE);


        }


    }


}

