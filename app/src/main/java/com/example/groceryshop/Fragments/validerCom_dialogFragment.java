package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.example.groceryshop.custom.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class validerCom_dialogFragment extends DialogFragment {
    private Button annuler,valider;
    private String URL_DATA;
    private SaveSharedPreference SSP;
    private DatabaseHelpler db;
    private ArrayList<produitCommand> commandList;

    public validerCom_dialogFragment(ArrayList<produitCommand> ar){
        commandList=ar;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.valider_command,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        URL_DATA= DBUrl.URL_DATA.concat("AddCommand.php?");
        SSP=new SaveSharedPreference(getContext());
        db=new DatabaseHelpler(getContext());

        valider=v.findViewById(R.id.valider);
        annuler=v.findViewById(R.id.annuler);
        getDialog().setCanceledOnTouchOutside(false);
        clicking();
        return v;
    }
    public void clicking(){
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 validerCommand();
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getDialog().dismiss();
            }
        });
    }
    public void validerCommand(){
        getDialog().dismiss();
        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("loading...");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
        RequestQueue reqeu = VolleySingleton.getInstance(getContext()).getRequestQueue();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        db.deleteAll();
                         progressdialog.dismiss();
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.empty_fragment);
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
                    int j=i+1;
                    params.put("CodeProd"+j,commandList.get(i).getCodeProd());
                    params.put("Qte"+j,""+commandList.get(i).getQte());

                }



                return params;
            }
        };
        reqeu.add(postRequest);
    }
}
