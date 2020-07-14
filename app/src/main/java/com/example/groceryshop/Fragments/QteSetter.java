package com.example.groceryshop.Fragments;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.SaveSharedPreference;

import java.util.ArrayList;
import java.util.Locale;

public class QteSetter extends DialogFragment {

    private EditText typed_value;
    private Button annuler,valider;

    private TextView Qte,Total,Prix;
    private produitCommand Produit;
    private DatabaseHelpler db;
    private SaveSharedPreference SSP;
    public QteSetter(TextView Qte, TextView Total, TextView Prix,produitCommand Produit)
    {


        this.Qte=Qte;
        this.Total=Total;
        this.Prix=Prix;
        this.Produit=Produit;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.modifier_popup,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        SSP=new SaveSharedPreference(getContext());
        getDialog().setCanceledOnTouchOutside(false);
        db = new DatabaseHelpler(getContext());

        annuler=v.findViewById(R.id.annuler);
        valider=v.findViewById(R.id.valider);

        typed_value=v.findViewById(R.id.typed_value);
        typed_value.setInputType(InputType.TYPE_CLASS_PHONE);

        clicking();

        return v;
    }

    public void clicking()
    {
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typed_value.getText().toString().equals("") || typed_value.getText().toString().equals("0"))
                {
                    Toast.makeText(getActivity(),getString(R.string.ChampVide),Toast.LENGTH_SHORT);

                }else
                {
                    float prix=Produit.getPrix();
                    float sum=0;

                    if(Produit.getPromotion()>0)
                    {
                        prix=Produit.getPromotion();
                    }
                    if(Produit.getPrixRemise()>0 && Produit.getQteRemise()>0)
                    {
                        if(Integer.parseInt(typed_value.getText().toString())>=Produit.getQteRemise())
                        {
                            prix=Produit.getPrixRemise();
                        }

                    }
                    sum=prix*Integer.parseInt(typed_value.getText().toString());

                    Total.setText(String.format(Locale.US,"%.2f",sum));
                    Prix.setText(String.format(Locale.US,"%.2f",prix));
                    Qte.setText(typed_value.getText().toString());
                    db.updateQte(Produit.getCodeProd(),Integer.parseInt(typed_value.getText().toString()),SSP.getUsername());
                    getDialog().dismiss();









                }
            }
        });
    }
}
