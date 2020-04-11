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
    Spinner sp;
    private DatabaseHelpler db;
    private produitCommand pc;
    private ArrayList<produitCommand> commandList;

    private TextView nom,marque,prix,size,prix_total_prod,total,codeProd;
    Resources ress;
    private ViewGroup vg;
    private ViewGroup listviewContainer;
    private CheckBox check_prod;
    private TextView empty_text;
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
        nom=view.findViewById(R.id.Com_Nom);
        marque=view.findViewById(R.id.Marq);
        prix=view.findViewById(R.id.Com_Prix);
        size=view.findViewById(R.id.Com_Size);
        sp=view.findViewById(R.id.Com_Qte);
        prix_total_prod=view.findViewById(R.id.totalProd);
        prix_total_prod.setTag("prod"+position);

       codeProd.setText(pc.getCodeProd());


        nom.setText(pc.getNom());


       marque.setText(pc.getMarque());




       final int prix_total=pc.getPrix()*(pc.getQte());
       Log.d("prixtotal"," "+pc.getPrix()+" "+prix_total+" ");

       prix_total_prod.setText(Integer.toString(pc.getPrix()*(pc.getQte())));
       prix.setText(Integer.toString(pc.getPrix()));


       size.setText(pc.getSize());

        ArrayList<Integer> qteList=new ArrayList<>();
        qteList.add(1);qteList.add(2);qteList.add(3);qteList.add(4);qteList.add(5);
        qteList.add(6);qteList.add(7);qteList.add(8);qteList.add(9);qteList.add(10);
        qteList.add(11);qteList.add(12);qteList.add(13);qteList.add(14);qteList.add(15);
        qteList.add(16);qteList.add(17);qteList.add(18);qteList.add(19);qteList.add(20);
        if(sp!=null){
            ArrayAdapter<Integer> SpItem=new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item,qteList);
            SpItem.setDropDownViewResource(android.R.layout.simple_spinner_item);
            sp.setAdapter(SpItem);
            Log.d("spinner qte"," "+pc.getQte());
            sp.setSelection(pc.getQte()-1);
            sp.setTag(position);
            total=footer.findViewById(R.id.total);
            valider=footer.findViewById(R.id.valider);

            sum=sum+(pc.getPrix()*(pc.getQte()-1));


            total.setText(Integer.toString(sum));

            valider.setOnClickListener(this);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  Cursor res2=db.getAllData();
                    TextView prodtotal=listviewContainer.findViewWithTag("prod"+parent.getTag());
                    Log.d("spinnerclick",""+parent.getTag()+" "+position);

                  pc=commandList.get(Integer.parseInt(parent.getTag().toString()));

                  int prix_totals=pc.getPrix()*(position+1) ;
                    Log.d("tag3lamantag",""+listviewContainer.findViewWithTag("prod0"));
                  if(prodtotal!=null){
                      prodtotal.setText(Integer.toString(prix_totals));
                      Log.d("tag3lamantag",""+prodtotal+" "+prix_total+" "+position+" "+pc.getPrix());
                  }


                    int sum1=0;
                      while (res2.moveToNext()){
                          if(res2.getString(0).equals(pc.getCodeProd()))
                          {
                              db.updateQte(res2.getString(0),position+1);
                              sum1= res2.getInt(4)*(position+1)+sum1;
                          }else{
                              sum1= res2.getInt(4)*res2.getInt(6)+sum1;
                          }
                          Log.d("totalgen",""+sum1+" "+res2.getInt(4)+" "+res2.getInt(6));




                      }
                      total.setText(Integer.toString(sum1));

                  }





                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


       return view;
    }
public void removeProd(boolean all){
        Log.d("removeprod"," ");
        if(all==true){
            commandList.clear();
            db.deleteAll();
        }else
        {
            for(int i=0;i<commandList.size();i++)
            {
                CheckBox box=listviewContainer.findViewWithTag("check"+i);
                Log.d("boxtag"," "+box.getTag()+" "+commandList.size());
                if(box.isChecked())
                {
                    db.deleteData(commandList.get(i).getCodeProd());
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

