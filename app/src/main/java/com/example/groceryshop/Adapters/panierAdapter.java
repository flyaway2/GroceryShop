package com.example.groceryshop.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.Fragments.validerCom_dialogFragment;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.example.groceryshop.custom.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class panierAdapter extends ArrayAdapter<produitCommand> implements View.OnClickListener {
     Context context;
     LayoutInflater inflter;
     ViewGroup footer;

    private DatabaseHelpler db;
    private produitCommand pc;
    private ArrayList<produitCommand> commandList;

    private TextView nom,marque,prix,size,prix_total_prod,total,codeProd;
    Resources ress;
    private ViewGroup vg;
    private ViewGroup listviewContainer;
    private CheckBox check_prod;
    private TextView empty_text,Qte;
    private Button valider;
    private String URL_DATA;
    private SaveSharedPreference SSP;
    public panierAdapter(@NonNull Context context, int resource,ViewGroup v, @NonNull ArrayList<produitCommand> objects,Resources res) {
        super(context, resource, objects);
        Log.d("panieradapter","");
        this.context=context;
        this.commandList=objects;
        inflter=(LayoutInflater.from(context));
        footer=v;
        db=new DatabaseHelpler(context);
        this.ress=res;
    }

    @Override
    public int getCount() {
        return commandList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        int sum=0;
        Spinner qte;
        ImageButton remove;

       if(view==null)
           view=inflter.inflate(R.layout.fragment_gallery,parent,false);

       vg=(ViewGroup)view;

       listviewContainer=parent;
       SSP=new SaveSharedPreference(getContext());

        URL_DATA= DBUrl.URL_DATA.concat("AddCommand.php?");
        pc=commandList.get(position);
        empty_text=view.findViewById(R.id.empty_text);
        Log.d("empty",""+empty_text);
        check_prod=view.findViewById(R.id.check_prod);
        check_prod.setTag("check"+position);
        codeProd=view.findViewById(R.id.codeProd);
        nom=view.findViewById(R.id.Nom);
        marque=view.findViewById(R.id.Marque);
        prix=view.findViewById(R.id.Prix);
        size=view.findViewById(R.id.SizeDesc);
        Qte=view.findViewById(R.id.Qte);
        prix_total_prod=view.findViewById(R.id.Total);
        prix_total_prod.setTag("prod"+position);

       codeProd.setText(pc.getCodeProd());


        nom.setText(pc.getNom());


       marque.setText(pc.getMarque());




       final float prix_total=pc.getPrix()*(pc.getQte());
       Log.d("prixtotal"," "+pc.getPrix()+" "+prix_total+" ");

       prix_total_prod.setText(Float.toString(pc.getPrix()*(pc.getQte())));
       prix.setText(Float.toString(pc.getPrix()));


       size.setText(pc.getSize());



       return view;
    }
public void removeProd(boolean all){
        Log.d("removeprod"," ");
        if(all==true){
            commandList.clear();
            db.deleteAll(SSP.getUsername());
        }else
        {
            for(int i=0;i<commandList.size();i++)
            {
                CheckBox box=listviewContainer.findViewWithTag("check"+i);
                Log.d("boxtag"," "+box.getTag()+" "+commandList.size());
                if(box.isChecked())
                {
                    db.deleteData(commandList.get(i).getCodeProd(),SSP.getUsername());
                    commandList.remove(i);
                }
            }
        }



}




public void displayChecks(){
    for(int i=0;i<commandList.size();i++)
    {
        CheckBox box=listviewContainer.findViewWithTag("check"+i);
        box.setVisibility(View.VISIBLE);
    }
}
public void hideChecks(){
    for(int i=0;i<commandList.size();i++)
    {
        CheckBox box=listviewContainer.findViewWithTag("check"+i);
        box.setVisibility(View.GONE);
    }
}
public void selectAll(){
        Log.d("selectall"," ");
        for(int i=0;i<commandList.size();i++)
        {
            CheckBox box=listviewContainer.findViewWithTag("check"+i);
            box.setChecked(true);
        }

}
public void disselectAll(){
        Log.d("selectall"," ");
        for(int i=0;i<commandList.size();i++)
        {
            CheckBox box=listviewContainer.findViewWithTag("check"+i);
            box.setChecked(false);
        }

    }
    public void empty(){
        empty_text.setText("panier vide");
    }

    public void validerCommand(){
        RequestQueue reqeu = VolleySingleton.getInstance(getContext()).getRequestQueue();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(getContext(),"Command Valider",Toast.LENGTH_LONG);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response","");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username",SSP.getUsername());

                params.put("NumProd",""+commandList.size());
                for (int i=0;i<commandList.size();i++)
                {
                    params.put("CodeProd"+i,commandList.get(i).getCodeProd());
                    params.put("Qte"+i,""+commandList.get(i).getQte());

                }



                return params;
            }
        };
        reqeu.add(postRequest);
    }

    @Override
    public void onClick(View v) {
        validerCom_dialogFragment dialmod=new validerCom_dialogFragment(commandList);
        FragmentManager fm=((AppCompatActivity)context).getSupportFragmentManager();
        dialmod.show(fm,"valider");

    }
}

