package com.example.groceryshop.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.groceryshop.R;

public class error_fragment extends Fragment {

   private TextView error_msg;
   private ImageView error_img;
   private String error;
   private Button reessayez;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.nonetwork,container,false);
        Log.d("errorfragment","");
        error_msg=v.findViewById(R.id.error_msq);
        error_img=v.findViewById(R.id.error_img);
        error=getArguments().getString("error");
        reessayez=v.findViewById(R.id.reessayez);

        reessayez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getFragmentManager().popBackStackImmediate();
            }
        });

        return v;
    }


}
