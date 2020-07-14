package com.example.groceryshop.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.Fragments.QteSetter;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class panierRecAdapter extends RecyclerView.Adapter<panierRecAdapter.ViewHolder> {

    private ArrayList<produitCommand> commandList;
    private Activity act;
    private DatabaseHelpler db;
    private TextView TotalGen;
    private BottomAppBar bottomBar;
    private SaveSharedPreference SSP;
    private String Lang;
    private FragmentManager fm;

    public panierRecAdapter(FragmentManager fm,Activity act, ArrayList<produitCommand> commandList, TextView TotalGen, BottomAppBar bottomBar)
    {
        this.fm=fm;
        this.commandList=commandList;
        this.act=act;
        this.TotalGen=TotalGen;
        this.bottomBar=bottomBar;
        SSP=new SaveSharedPreference(act);
        Lang=SSP.getLang();


    }

    @NonNull
    @Override
    public panierRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_gallery,parent,false);
        db = new DatabaseHelpler(parent.getContext());
        float sum=0;
        float prix=0;

        for(int i=0;i<commandList.size();i++)
        {
            prix=commandList.get(i).getPrix();

            if(commandList.get(i).getPromotion()>0)
            {
                prix=commandList.get(i).getPromotion();

            }
            if(commandList.get(i).getQteRemise()>0 && commandList.get(i).getPrixRemise()>0)
            {
                if(commandList.get(i).getQteRemise()<=commandList.get(i).getQte())
                {
                    prix=commandList.get(i).getPrixRemise();
                }


            }
            Log.d("totalgen"," "+commandList.get(i).getPrix()+" "+commandList.get(i).getQteRemise()+
                    " "+commandList.get(i).getPrixRemise()+" "+commandList.get(i).getQte()+" "+commandList.get(i).getPromotion()+" "
            +prix);
            sum=sum+(commandList.get(i).getQte()*prix);

        }
        TotalGen.setText(String.format(Locale.US,"%.2f",sum));

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        produitCommand prodCom=commandList.get(position);

        //setting product info
        holder.CodeProd.setText(prodCom.getCodeProd());
        holder.Nom.setText(prodCom.getNom());
        holder.NomAr.setText(prodCom.getNomAr());
        holder.Marque.setText(prodCom.getMarque());
        holder.MarqueAr.setText(prodCom.getMarqueAr());
        holder.size.setText(prodCom.getSize());
        holder.sizeAr.setText(prodCom.getSizeAr());

        if(Lang.equals("ar"))
        {

            holder.NomDesc.setText(prodCom.getNomAr());
            holder.MarqueDesc.setText(prodCom.getMarqueAr());
            holder.SizeDesc.setText(prodCom.getSizeAr());
        }else
        {
            holder.NomDesc.setText(prodCom.getNom());
            holder.MarqueDesc.setText(prodCom.getMarque());
            holder.SizeDesc.setText(prodCom.getSize());
        }

        holder.Promotion.setText(String.format(Locale.US,"%.2f",prodCom.getPromotion()));
        holder.PrixRemise.setText(String.format(Locale.US,"%.2f",prodCom.getPrixRemise()));

        holder.PrixUnit.setText(String.format(Locale.US,"%.2f",prodCom.getPrix()));

        holder.QteRemise.setText(String.valueOf(prodCom.getQteRemise()));

        holder.Qte.setText(String.valueOf(prodCom.getQte()));

        Log.d("prixUnit"," "+prodCom.getPrix()+" "+prodCom.getQteRemise()+" "
        +prodCom.getQte()+" "+prodCom.getPrixRemise());
        if(prodCom.getPromotion()>0)
        {
            holder.Prix.setText(String.format(Locale.US,"%.2f",prodCom.getPromotion()));
        }else{
            holder.Prix.setText(String.format(Locale.US,"%.2f",prodCom.getPrix()));
        }
        if(prodCom.getQteRemise()>0 && prodCom.getPrixRemise()>0)
        {
            if(prodCom.getQte()>=prodCom.getQteRemise())
            {
                holder.Prix.setText(String.format(Locale.US,"%.2f",prodCom.getPrixRemise()));
            }
        }
        Log.d("miracle"," "+holder.Prix.getText().toString());

        float prixTotal=prodCom.getQte()*Float.parseFloat(holder.Prix.getText().toString());

        holder.Total.setText(String.format("%.2f",prixTotal));




        Log.d("imgprodpanier"," "+prodCom.getImg());
        if(!prodCom.getImg().equals(""))
        {
            Picasso.get().load(DBUrl.URL_DATA_ImgProd +prodCom.getImg()).into(holder.Img);
        }

        // click listeners
        holder.Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float sum=Float.parseFloat(TotalGen.getText().toString());
                sum=sum-(Float.parseFloat(holder.Total.getText().toString()));
                int Qte=Integer.parseInt(holder.Qte.getText().toString())+1;
                db.updateQte(holder.CodeProd.getText().toString(),Qte,SSP.getUsername());
                holder.Qte.setText(String.valueOf(Qte));
                float prix=0;
                Log.d("promotionRemise"," "+holder.QteRemise.getText().toString()+" "+holder.PrixRemise.getText().toString()+" "+holder.Promotion.getText().toString()
                +" "+holder.PrixUnit.getText().toString()+" "+holder.Prix.getText().toString());
                if(Integer.parseInt(holder.QteRemise.getText().toString())>0 && Float.parseFloat(holder.PrixRemise.getText().toString())>0)
                {
                   int qteRemise= Integer.parseInt(holder.QteRemise.getText().toString());
                   float PrixRemise=Float.parseFloat(holder.PrixRemise.getText().toString());
                    if(qteRemise<=Qte)
                    {
                        prix=PrixRemise*Qte;
                        holder.Prix.setText(holder.PrixRemise.getText().toString());
                    }else
                    {
                        prix=Float.parseFloat(holder.PrixUnit.getText().toString())*Qte;
                        Log.d("floatproblem"," "+prix+" "+Float.parseFloat(holder.PrixUnit.getText().toString()));
                    }
                }else
                {
                    if(Float.parseFloat(holder.Promotion.getText().toString())>0)
                    {
                        prix=Float.parseFloat(holder.Promotion.getText().toString())*Qte;

                    }else
                    {
                        prix=Float.parseFloat(holder.PrixUnit.getText().toString())*Qte;
                        Log.d("floatproblem"," "+prix+" "+Float.parseFloat(holder.PrixUnit.getText().toString()));
                    }

                }
                holder.Total.setText(String.format(Locale.US,"%.2f",prix));
                sum=sum+Float.parseFloat(holder.Total.getText().toString());
                TotalGen.setText(String.format(Locale.US,"%.2f",sum));


            }
        });

        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float sum=Float.parseFloat(TotalGen.getText().toString());
                sum=sum-(Float.parseFloat(holder.Total.getText().toString()));
                if(Integer.parseInt(holder.Qte.getText().toString())>1)
                {
                    int Qte=Integer.parseInt(holder.Qte.getText().toString())-1;
                    db.updateQte(holder.CodeProd.getText().toString(),Qte,SSP.getUsername());
                    holder.Qte.setText(String.valueOf(Qte));
                    float prix=0;
                    if(Integer.parseInt(holder.QteRemise.getText().toString())>0 && Float.parseFloat(holder.PrixRemise.getText().toString())>0)
                    {
                        int qteRemise= Integer.parseInt(holder.QteRemise.getText().toString());
                        float PrixRemise=Float.parseFloat(holder.PrixRemise.getText().toString());
                        Log.d("qteremise"," "+qteRemise+" "+Qte);
                        if(qteRemise<=Qte)
                        {
                            prix=PrixRemise*Qte;
                        }else
                        {
                            holder.Prix.setText(holder.PrixUnit.getText().toString());
                            prix=Float.parseFloat(holder.PrixUnit.getText().toString())*Qte;
                            Log.d("floatproblem"," "+prix+" "+Float.parseFloat(holder.PrixUnit.getText().toString()));
                        }
                    }else
                    {
                        if(Float.parseFloat(holder.Promotion.getText().toString())>0)
                        {
                            prix=Float.parseFloat(holder.Promotion.getText().toString())*Qte;

                        }else
                        {
                            prix=Float.parseFloat(holder.PrixUnit.getText().toString())*Qte;
                        }

                    }
                    holder.Total.setText(String.format(Locale.US,"%.2f",prix));
                    sum=sum+Float.parseFloat(holder.Total.getText().toString());
                    TotalGen.setText(String.format(Locale.US,"%.2f",sum));



                }

            }
        });






    }

    @Override
    public int getItemCount() {

        return commandList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView Img;
        private TextView Nom,Marque,size,Prix,Total,Qte,PrixUnit,CodeProd,NomAr,MarqueAr,sizeAr;
        private TextView Promotion,QteRemise,PrixRemise,NomDesc,MarqueDesc,SizeDesc;
        private LinearLayout Remove_Prod;
        private ImageButton Add,sub;
        public ViewHolder(@NonNull View view)
        {
            super(view);
            CodeProd=view.findViewById(R.id.codeProd);
            PrixUnit=view.findViewById(R.id.PrixUnit);
            Img=view.findViewById(R.id.Img);
            Nom=view.findViewById(R.id.Nom);
            NomAr=view.findViewById(R.id.NomAr);
            Marque=view.findViewById(R.id.Marque);
            MarqueAr=view.findViewById(R.id.MarqueAr);

            size=view.findViewById(R.id.Taille);
            sizeAr=view.findViewById(R.id.TailleAr);

            NomDesc=view.findViewById(R.id.NomDesc);
            MarqueDesc=view.findViewById(R.id.MarqueDesc);
            SizeDesc=view.findViewById(R.id.SizeDesc);

            Prix=view.findViewById(R.id.Prix);
            Total=view.findViewById(R.id.Total);
            Qte=view.findViewById(R.id.Qte);
            Remove_Prod=view.findViewById(R.id.remove_prod);
            Add=view.findViewById(R.id.plus);
            sub=view.findViewById(R.id.minus);

            Promotion=view.findViewById(R.id.Promotion);
            QteRemise=view.findViewById(R.id.QteRemise);
            PrixRemise=view.findViewById(R.id.PrixRemise);

            Qte.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    QteSetter qteSetter=new QteSetter(Qte,Total,Prix,commandList.get(getAdapterPosition()));

                    qteSetter.show(fm,"modifier Qte");
                    return true;
                }
            });

            Remove_Prod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(getAdapterPosition(),CodeProd.getText().toString());
                    float remain=Float.parseFloat(Total.getText().toString());
                    remain=Float.parseFloat(TotalGen.getText().toString())-remain;
                    TotalGen.setText(String.format(Locale.US,"%.2f",remain));
                }
            });


        }

    }
    public void removeAt(int position,String CodeProd) {
        HideBottomViewOnScrollBehavior behavior = (HideBottomViewOnScrollBehavior) bottomBar.getBehavior();// use this to hide it
        bottomBar.refreshDrawableState();
        Log.d("bottombar"," "+bottomBar.getVisibility()+" "+bottomBar.getHideOnScroll());
        Log.d("bottombar"," "+bottomBar.getVisibility()+" "+bottomBar.getHideOnScroll());

        commandList.remove(position);
        notifyItemRemoved(position);
        Log.d("removeAt"," "+CodeProd);


        db.deleteData(CodeProd,SSP.getUsername());
        notifyItemRangeChanged(position, commandList.size());
    }
}
