package com.example.groceryshop.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.groceryshop.Activities.LoginActivity;
import com.example.groceryshop.Activities.SignUpActivity;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.Locale;

import static android.app.AlertDialog.THEME_HOLO_DARK;
import static android.app.AlertDialog.THEME_HOLO_LIGHT;
import static androidx.core.app.ActivityCompat.recreate;

public class parametre extends Fragment {
    private SaveSharedPreference SSP;
    private CardView ChangeLang,Profil,LogOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.parametre,container,false);
        ChangeLang=v.findViewById(R.id.ChangeLang);
        LogOut=v.findViewById(R.id.LogOut);
        Profil=v.findViewById(R.id.profil);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("locale ",Locale.getDefault().toLanguageTag()+" ");
        }
        SSP=new SaveSharedPreference(getContext());
        Log.d("local ",Locale.getDefault().toString());
        buttonEffect(ChangeLang);
        buttonEffect(LogOut);
        buttonEffect(Profil);

        clicking();


        return v;
    }

    private void clicking() {
        ChangeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LangChoice langchoice=new LangChoice(getActivity());

                langchoice.show(getFragmentManager(),"change language");
            }
        });
        Profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.profile_fragment);

            }
        });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SSP.logoutUser();
                Intent intent1=new Intent(getContext(), LoginActivity.class);
                startActivity(intent1);

            }
        });
    }
    public  void buttonEffect(View button) {

        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.parseColor("#CAD3C8"), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
    private void ShowChangeLangDialog()
    {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity(), THEME_HOLO_LIGHT);
        if(SSP.getLang().equals("ar"))
        {
            final String[] listItems={"الفرنسية","العربية"};

            mBuilder.setTitle("تغيير اللغـة");
            mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int i) {
                    Log.d("lang choosed"," "+i);
                    if(i==0)
                    {
                        setAppLocale("en_us");
                    }else if(i==1)
                    {
                        setAppLocale("ar");
                    }
                    dialog.dismiss();

                }

            });
        }else
        {

        final String[] listItems={"Français","Arabe"};

        mBuilder.setTitle("Choisir la langue");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Log.d("lang choosed"," "+i);
                if(i==0)
                {
                   setAppLocale("en_us");
                }else if(i==1)
                {
                    setAppLocale("ar");
                }
                dialog.dismiss();

            }

        });
        }
        AlertDialog alterdial=mBuilder.create();
        alterdial.show();

    }
    private void setLocale(String lang)
    {

        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }else
        {
            config.locale=locale;
        }
        String Username=SSP.getUsername();
        String Password=SSP.getPassword();
        getActivity().getBaseContext().getResources().updateConfiguration(config,getActivity().getBaseContext().getResources().getDisplayMetrics());
        getActivity().recreate();
        Log.d("logging status"," "+SSP.getLoggedSattus(getContext()));
        SSP.setLang(lang);
        SSP.createLoginSession(Username,Password);
        Log.d("logging status"," "+SSP.getLoggedSattus(getContext()));
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
        getActivity().recreate();
        Log.d("logging status"," "+SSP.getLoggedSattus(getContext()));
        SSP.setLang(localeCode);
        SSP.createLoginSession(Username,Password);
        Log.d("logging status"," "+SSP.getLoggedSattus(getContext()));
    }
}
