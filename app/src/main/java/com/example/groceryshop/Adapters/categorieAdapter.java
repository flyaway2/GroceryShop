package com.example.groceryshop.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop.R;
import com.example.groceryshop.Beans.categorieList;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.squareup.picasso.Picasso;
import com.example.groceryshop.Beans.produitCommand;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

public class categorieAdapter extends RecyclerView.Adapter<categorieAdapter.ViewHolder> {
public static final String name="name";
public static final String image="image";
public static final String url="url";
private List<categorieList> CatLists;
private ArrayList<produitCommand> commList;
private SaveSharedPreference SSP;
private Context context;
private String Lang;
private Activity act;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.categorie,parent,false);
        Lang=SSP.getLang();


        return new ViewHolder(v);
    }

    public  categorieAdapter(Activity act, List<categorieList> catLists, Context context1, ArrayList<produitCommand> commList){
       CatLists=catLists;
       this.act=act;
       context=context1;
       this.commList=commList;
        SSP=new SaveSharedPreference(context);
        Log.d("categorie adapter","constructeur "+CatLists.size());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        Log.d("categorie adapter","on bind view holder");
        final categorieList CatList=CatLists.get(position);
        if(Lang.equals("ar"))
        {
            holder.Nom.setText(CatList.getNomAr());
        }else
        {
            holder.Nom.setText(CatList.getNom());
        }


        Picasso.get().load(DBUrl.URL_DATA_ImgCat +CatList.getUrl()).into(holder.CatImage);
        Log.d("adapter onbind","image: "+holder.CatImage.getDrawable());
    }

    @Override
    public int getItemCount() {
        Log.d("categorie adapter","get item count "+CatLists.size());
        return CatLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView Nom;
        public ImageView CatImage;


        public ViewHolder(final View itemView){

            super(itemView);
            Log.d("categorie adapter","view holder constructeur");
            Nom=itemView.findViewById(R.id.TextCat);
        CatImage=itemView.findViewById(R.id.catImg);
        PushDownAnim.setPushDownAnimTo(itemView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked","bitch"+getLayoutPosition()+" "+  CatLists.get(getAdapterPosition()).getID() );
                Bundle bundle = new Bundle();

                bundle.putInt("cat", CatLists.get(getAdapterPosition()).getID());
                bundle.putString("Nom", CatLists.get(getAdapterPosition()).getNom());
                bundle.putString("NomAr", CatLists.get(getAdapterPosition()).getNomAr());
                bundle.putParcelableArrayList("commList",commList);
                Navigation.findNavController(act,R.id.nav_host_fragment).navigate(R.id.recyclerP_fragment,bundle);


                Toast.makeText(v.getContext(),"",Toast.LENGTH_LONG);
            }
        });

        }
    }

}
