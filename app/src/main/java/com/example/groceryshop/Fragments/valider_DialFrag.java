package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.SaveSharedPreference;

public class valider_DialFrag extends DialogFragment {
    private Button annuler,valider;
    private TextView instruction_Msg;
    private DatabaseHelpler db;
    private SaveSharedPreference SSP;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.valider_command,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        SSP=new SaveSharedPreference(getContext());
        db=new DatabaseHelpler(getContext());
        instruction_Msg=v.findViewById(R.id.instruction_msg);
        valider=v.findViewById(R.id.valider);
        annuler=v.findViewById(R.id.annuler);
        if(SSP.getLang().equals("ar"))
        {
            instruction_Msg.setText("هل تريد فعلا إفراغ السلة ؟");
        }else
        {
            instruction_Msg.setText("voulez-vous vraiment vider le panier ?");
        }

        getDialog().setCanceledOnTouchOutside(false);
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
                db.deleteAll(SSP.getUsername());
                getDialog().dismiss();
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.fragment2);
            }
        });
    }

}
