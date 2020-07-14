package com.example.groceryshop.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.groceryshop.Activities.LoginActivity;
import com.example.groceryshop.Activities.MainActivity;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.SaveSharedPreference;

import java.util.Locale;

public class LangChoice extends DialogFragment {
    private Activity act;
    private LinearLayout Lang1;
    private LinearLayout Lang2;
    private SaveSharedPreference SSP;

    public LangChoice(Activity act)
    {
        this.act=act;

    }
    public LangChoice()
    {
        Log.d("Lanchaoice"," empty constructor");



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.choose_lang,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Log.d("sspvsact"," "+act);
        SSP=new SaveSharedPreference(act);

        Lang1=v.findViewById(R.id.lang1);
        Lang2=v.findViewById(R.id.lang2);
        buttonEffect(Lang1);
        buttonEffect(Lang2);

        clicking();


        return v;
    }
    public void clicking()
    {
        Lang1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SSP.getLang().equals("en_us"))
                {
                    setAppLocale("en_us");
                }else
                {
                    getDialog().dismiss();
                }


            }
        });
        Lang2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SSP.getLang().equals("ar"))
                {
                    setAppLocale("ar");

                }else
                {
                    getDialog().dismiss();
                }


            }
        });
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

    @Override
    public void onDestroy() {
        Log.d("ondestroylangchange"," ");
        super.onDestroy();
    }

    private void setAppLocale(String localeCode){
        String Username=SSP.getUsername();
        String Password=SSP.getPassword();
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localeCode.toLowerCase()));
        }else
        {
            configuration.locale=new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);



        getDialog().dismiss();
        getActivity().recreate();
        Log.d("passrecreate"," ");

        SSP.setLang(localeCode);
        if (SSP.getLoggedSattus(getContext())==false) {
            Log.d("logging status"," "+SSP.getLoggedSattus(getContext()));
            Intent intent=new Intent(getContext(), LoginActivity.class);
            startActivity(intent);

        }else{

            SSP.createLoginSession(Username,Password);
            Log.d("logging status"," "+SSP.getLoggedSattus(getContext()));

        }


    }
}
