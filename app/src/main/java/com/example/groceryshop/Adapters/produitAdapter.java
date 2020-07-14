package com.example.groceryshop.Adapters;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.icu.text.Transliterator;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop.Beans.produitList;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


public class produitAdapter extends RecyclerView.Adapter<produitAdapter.ViewHolder>  implements Filterable {
  private ArrayList<produitList> pl;
    private ArrayList<produitList> pl_full;

    private DatabaseHelpler db;
    private Cursor res;
    private View vi;
    private TextView NumberItem;
    private Activity act;
    private SaveSharedPreference SSP;
    private String Lang;

    public produitAdapter(Activity act, ArrayList<produitList> pl, TextView NumberItem) {
        this.pl = pl;
        this.NumberItem=NumberItem;
        this.act=act;
        SSP=new SaveSharedPreference(act);
        Lang=SSP.getLang();

        pl_full=new ArrayList<>(pl);
        Log.d("plfull"," "+pl_full.size());
    }

    @NonNull
    @Override
    public produitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.produits,parent,false);
        vi=view;

        db = new DatabaseHelpler(parent.getContext());

        if(db.getAllData(SSP.getUsername()).getCount()>0)
        {
            Log.d("NumberItem before"," "+NumberItem.getVisibility());
            if(NumberItem.getVisibility()==View.INVISIBLE)
            {

                NumberItem.setVisibility(View.VISIBLE);
                Log.d("NumberItem loop"," "+NumberItem.getVisibility());
            }
            Log.d("NumberItem after"," "+NumberItem.getVisibility());

            NumberItem.setText(String.valueOf(db.getAllData(SSP.getUsername()).getCount()));
            db.close();
        }
        return new ViewHolder(view);
    }

    public void passTextview(TextView NumberItem)
    {
        this.NumberItem=NumberItem;

    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
      final produitList plist=pl.get(position);
        if(Lang.equals("ar"))
        {
            holder.Name.setText(plist.getNomAr()+" "+plist.getMarqueAr());
            holder.TailleDesc.setText(plist.getTailleAr());
        }else {
            holder.Name.setText(plist.getNom()+" "+plist.getMarque());
            holder.TailleDesc.setText(plist.getTaille());
        }


        holder.Taille.setText(plist.getTaille());
        holder.NomAr.setText(plist.getNomAr());

        holder.Nom.setText(plist.getNom());
        holder.Marque.setText(plist.getMarque());
        holder.MarqueAr.setText(plist.getMarqueAr());
        holder.TailleAr.setText(plist.getTailleAr());
        holder.Taille.setText(plist.getTaille());
        holder.Promotion.setText(String.valueOf(plist.getPromotion()));
        holder.PrixRemise.setText(String.format(Locale.US,"%.2f",plist.getRemisePrix()));
        holder.QteRemise.setText(String.valueOf(plist.getQteRemise()));
        holder.Qte.setText("1");

        holder.Prix.setText(String.format(Locale.US,"%.2f",plist.getPrix()));
        holder.codeProd.setText(plist.getCodeProd());
        if(plist.getPromotion()>0)
        {
            holder.Promotion.setText(String.format(Locale.US,"%.2f",plist.getPromotion()));
            holder.Promotion.setVisibility(View.VISIBLE);
            holder.Prix.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if(!plist.getImg().equals(""))
        {
            Picasso.get().load(DBUrl.URL_DATA_ImgProd +plist.getImg()).into(holder.Img);
            holder.ImgNom.setText(plist.getImg());
        }


      if(db.getAllData(SSP.getUsername()).getCount()!=0){


         Cursor res= db.getAllData(SSP.getUsername());

          while(res.moveToNext()){

              if(res.getString(0).equals(holder.codeProd.getText().toString())){





                  holder.acheter.setVisibility(View.GONE);
                holder.QteContainer.setVisibility(View.VISIBLE);
                Log.d("Qteressource"," "+res.getInt(6));
                  holder.Qte.setText(String.valueOf(res.getInt(6)));
                  float price;
                  if(holder.Promotion.getVisibility()==View.VISIBLE)
                  {
                      price=Float.parseFloat(holder.Promotion.getText().toString());
                  }else
                  {

                      price=Float.parseFloat(holder.Prix.getText().toString());

                  }
                  if(Integer.parseInt(holder.QteRemise.getText().toString())>0 && Float.parseFloat(holder.PrixRemise.getText().toString())>0)
                  {
                      Log.d("Quantite remise "," "+holder.Qte.getText().toString()+" " +
                              holder.QteRemise.getText().toString());
                      if(Integer.parseInt(holder.Qte.getText().toString())>=Integer.parseInt(holder.QteRemise.getText().toString()))
                      {
                          holder.Prix.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                          holder.Promotion.setVisibility(View.VISIBLE);
                          holder.Promotion.setText(holder.PrixRemise.getText().toString());
                      }
                  }
                  holder.PrixTotal.setVisibility(View.VISIBLE);
                  holder.PrixTotal.setText(String.format(Locale.US,"%.2f",Integer.parseInt(holder.Qte.getText().toString())*price));
              }

          }
          res.close();
          db.close();


      }

      holder.minus.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              int prog_value=Integer.valueOf(holder.Qte.getText().toString())-1;
              Log.d("progvalue",""+prog_value);
              holder.Qte.setText(String.valueOf(prog_value));
              float price;
              if(holder.Promotion.getVisibility()==View.VISIBLE)
              {
                  price=Float.parseFloat(holder.Promotion.getText().toString());
              }else
              {

                  price=Float.parseFloat(holder.Prix.getText().toString());

              }
              if(Integer.parseInt(holder.QteRemise.getText().toString())>0 && Float.parseFloat(holder.PrixRemise.getText().toString())>0)
              {
                  Log.d("Quantite remise minus"," "+holder.Qte.getText().toString()+" " +
                          holder.QteRemise.getText().toString());
                  if(Integer.parseInt(holder.Qte.getText().toString())<Integer.parseInt(holder.QteRemise.getText().toString()))
                  {
                      holder.Prix.setPaintFlags(0);
                      holder.Promotion.setVisibility(View.GONE);
                  }
              }
              holder.PrixTotal.setText(String.format(Locale.US,"%.2f",prog_value*price));

              if(Integer.parseInt(holder.Qte.getText().toString())==0){
                  int NumItem=Integer.valueOf(NumberItem.getText().toString())-1;
                  NumberItem.setText(String.valueOf(NumItem));
                  if(Integer.parseInt(NumberItem.getText().toString())==0)
                  {
                      NumberItem.setVisibility(View.INVISIBLE);
                  }

                  prog_value=prog_value+1;
                  holder.Qte.setText(String.valueOf(prog_value));
                  holder.QteContainer.setVisibility(View.GONE);
                  holder.PrixTotal.setVisibility(View.INVISIBLE);
                  holder.acheter.setVisibility(View.VISIBLE);
                  res = db.getAllData(SSP.getUsername());
                  while(res.moveToNext()){

                      if(res.getString(0).contains(holder.codeProd.getText().toString())){
                          db.deleteData(res.getString(0),SSP.getUsername());
                      }

                  }
                  res.close();
                  db.close();
              }else
              {
                  if(Integer.parseInt(holder.Qte.getText().toString())==1)
                  {
                      holder.PrixTotal.setVisibility(View.INVISIBLE);
                  }
                  holder.Qte.setText(""+prog_value);

                  boolean b= db.updateQte(holder.codeProd.getText().toString(),prog_value,SSP.getUsername());
              }

          }
      });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prog_value=Integer.parseInt(holder.Qte.getText().toString())+1;
                Log.d("progvalue",""+prog_value);
                holder.Qte.setText(String.valueOf(prog_value));

                float price;
                if(Integer.parseInt(holder.QteRemise.getText().toString())>0 && Float.parseFloat(holder.PrixRemise.getText().toString())>0)
                {
                    Log.d("Quantite remise "," "+holder.Qte.getText().toString()+" " +
                            holder.QteRemise.getText().toString());
                    if(Integer.parseInt(holder.Qte.getText().toString())==Integer.parseInt(holder.QteRemise.getText().toString()))
                    {
                        holder.Prix.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.Promotion.setVisibility(View.VISIBLE);
                        holder.Promotion.setText(holder.PrixRemise.getText().toString());
                    }
                }

                if(holder.Promotion.getVisibility()==View.VISIBLE)
                {
                    price=Float.parseFloat(holder.Promotion.getText().toString());
                }else
                {

                    price=Float.parseFloat(holder.Prix.getText().toString());

                }

                holder.PrixTotal.setText(String.format(Locale.US,"%.2f",prog_value*price));
                if(holder.PrixTotal.getVisibility()==View.INVISIBLE)
                {
                    holder.PrixTotal.setVisibility(View.VISIBLE);
                }

                 boolean b= db.updateQte(holder.codeProd.getText().toString(),prog_value,SSP.getUsername());
                db.close();

            }
        });
      holder.acheter.setOnClickListener(new Button.OnClickListener(){
          @Override
          public void onClick(View v) {
              int NumItem=Integer.valueOf(NumberItem.getText().toString())+1;
              NumberItem.setText(String.valueOf(NumItem));
              if(NumberItem.getVisibility()==View.INVISIBLE)
              {
                  NumberItem.setVisibility(View.VISIBLE);
              }
              Log.d("acheter button"," ");
              holder.QteContainer.setVisibility(View.VISIBLE);
              holder.acheter.setVisibility(View.GONE);
              String codeprod=holder.codeProd.getText().toString();
              String Nom=holder.Nom.getText().toString();
              String marq=holder.Marque.getText().toString();
              String marqAr=holder.MarqueAr.getText().toString();
              String NomAr=holder.NomAr.getText().toString();
              String TailleAr=holder.TailleAr.getText().toString();
              String taille=holder.Taille.getText().toString();




              float Promotion=Float.valueOf(holder.Promotion.getText().toString());
              int QteRemise=Integer.valueOf(holder.QteRemise.getText().toString());
              float PrixRemise=Float.valueOf(holder.PrixRemise.getText().toString());
              float price=Float.parseFloat(holder.Prix.getText().toString());

              int qte=Integer.parseInt(holder.Qte.getText().toString());
              db.insertData(codeprod,Nom,marq,price,taille,qte,TailleAr,NomAr,marqAr,Promotion,QteRemise,PrixRemise,holder.ImgNom.getText().toString(),SSP.getUsername());
              db.close();
          }
      });
      buttonEffect(holder.acheter);

      buttonEffect(holder.plus);
      buttonEffect(holder.minus);


    }






    @Override
    public int getItemCount() {

        return pl.size();
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
                        if(SSP.getLang().equals("ar"))
                        {
                            if(prodL.getNomAr().toLowerCase().contains(filterPattern) || prodL.getMarqueAr().toLowerCase().contains(filterPattern)){
                                filteredList.add(prodL);

                            }
                        }else
                        {
                            if(prodL.getNom().toLowerCase().contains(filterPattern) || prodL.getMarque().toLowerCase().contains(filterPattern)){
                                filteredList.add(prodL);

                            }
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
        TextView Name,Prix,codeProd,ImgNom;
        TextView Promotion,Qte,Nom,NomAr,Marque,MarqueAr,Taille,TailleAr,TailleDesc;
        TextView PrixRemise,QteRemise,PrixTotal;
        LinearLayout acheter;
        LinearLayout QteContainer;
        ImageButton plus,minus;
        ImageView Img;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ImgNom=itemView.findViewById(R.id.ImgNom);
            PrixRemise=itemView.findViewById(R.id.PrixRemise);
            QteRemise=itemView.findViewById(R.id.QteRemise);
            Img=itemView.findViewById(R.id.Img);
            Nom=itemView.findViewById(R.id.Nom);
            Name=itemView.findViewById(R.id.Desc);
            NomAr=itemView.findViewById(R.id.NomAr);
            Marque=itemView.findViewById(R.id.Marque);
            MarqueAr=itemView.findViewById(R.id.MarqueAr);
            Taille=itemView.findViewById(R.id.Taille);
            TailleDesc=itemView.findViewById(R.id.TailleDesc);
            TailleAr=itemView.findViewById(R.id.TailleAr);
            PrixTotal=itemView.findViewById(R.id.PrixTotal);
            Prix=itemView.findViewById(R.id.Prix);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
            Promotion=itemView.findViewById(R.id.Promotion);
            Qte=itemView.findViewById(R.id.Qte);

            QteContainer=itemView.findViewById(R.id.QteContainer);
            acheter=itemView.findViewById(R.id.ajouterPanier);
            codeProd=itemView.findViewById(R.id.codeProd);


        }


    }


}

