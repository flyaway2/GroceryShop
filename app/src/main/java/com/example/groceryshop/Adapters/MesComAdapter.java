package com.example.groceryshop.Adapters;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop.Beans.Command;
import com.example.groceryshop.Beans.produitList;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class MesComAdapter  extends RecyclerView.Adapter<MesComAdapter.ViewHolder> {
    private ArrayList<Command> ComList;

    private DatabaseHelpler db;
    private Activity act;
    private SaveSharedPreference SSP;


    public MesComAdapter(ArrayList<Command> ComList, Activity act)
    {
        this.ComList=ComList;
        this.act=act;

    }

    @NonNull
    @Override
    public MesComAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mes_commandes,parent,false);
        SSP=new SaveSharedPreference(act);



        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return ComList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Command com=ComList.get(position);

        if(SSP.getLang().equals("ar"))
        {
            holder.NumCom.setText("طلب رقم "+String.valueOf(com.getNumCom()));
            holder.NumProd.setText(String.valueOf(com.getNumProd()+" سلعة"));

        }else
        {
            holder.NumCom.setText("Commande n°"+String.valueOf(com.getNumCom()));
            holder.NumProd.setText(String.valueOf(com.getNumProd()+" Articles"));
        }


        holder.Disp.setText(com.getDisp());
        if(com.getDelivrer()==0)
        {
            holder.DelivrerIcon.setImageResource(R.drawable.en_cours);
            if(SSP.getLang().equals("ar"))
            {
                holder.Delivrer.setText("جاري التوصيل");
            }else
            {
                holder.Delivrer.setText("en cours");
            }

        }else
        {
            holder.DelivrerIcon.setImageResource(R.drawable.livre);
            if(SSP.getLang().equals("ar"))
            {
                holder.Delivrer.setText("تم التوصيل");
            }else
            {
                holder.Delivrer.setText("livrée");
            }
        }
        String datepure=com.getDate().substring(0,com.getDate().indexOf(" "));
        Log.d("datepure",datepure);
        holder.date.setText(datepure);



    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView NumCom,NumProd,Delivrer,date,Disp;
        CardView Command;
        ImageView DelivrerIcon;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            NumCom=itemView.findViewById(R.id.NumCom);
            NumProd=itemView.findViewById(R.id.NumProd);
            Delivrer=itemView.findViewById(R.id.Delivrer);
            date=itemView.findViewById(R.id.Date);
            Disp=itemView.findViewById(R.id.Disp);
            Command=itemView.findViewById(R.id.Command);
            DelivrerIcon=itemView.findViewById(R.id.DelivrerIcon);

            PushDownAnim.setPushDownAnimTo(itemView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("date","MesCom");
                bundle.putString("NumCom",String.valueOf(ComList.get(getAdapterPosition()).getNumCom()));
                bundle.putString("datecom",String.valueOf(ComList.get(getAdapterPosition()).getDate()));
                Navigation.findNavController(act,R.id.nav_host_fragment).navigate(R.id.receipt,bundle);



        }});
        }
}
}
