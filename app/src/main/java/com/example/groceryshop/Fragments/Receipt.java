package com.example.groceryshop.Fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop.Beans.produitCommand;
import com.example.groceryshop.BuildConfig;
import com.example.groceryshop.Database.DatabaseHelpler;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.provider.Settings.System.DATE_FORMAT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Receipt extends Fragment {
    private LinearLayout Items;
    private DatabaseHelpler db;
    private ArrayList<produitCommand> list_produit;
    private SaveSharedPreference SSP;
    private ProgressDialog progressdialog;
    private TextView Frais;
    private String URL_DATA,URL_DATA_Admin,URL_DATA_Command;
    private TextView Total,TotalGen;
    private TextView Address,Tel;
    private Button pdf;
    private View view;
    private static final String DATE_FORMAT_8 = "dd MMMM yyyy HH:mm:ss";
    private String mDateTime;
    private TextView date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.receipt,container,false);
        view=v;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_8);
        URL_DATA_Command=DBUrl.URL_DATA_Command_;
        date=v.findViewById(R.id.Date);

        if(getArguments()!=null)
        {
            if(getArguments().getString("date")!=null)
            {
                if(!getArguments().getString("date").equals("MesCom"))
                {
                    date.setText("Date:"+getArguments().getString("date"));
                }

            }

        }


        pdf=v.findViewById(R.id.pdf);
        Items=v.findViewById(R.id.Items);
        Frais=v.findViewById(R.id.Frais);
        Address=v.findViewById(R.id.Address);
        Tel=v.findViewById(R.id.Tel);
        URL_DATA=DBUrl.URL_DATA_Livraison;
        URL_DATA_Admin=DBUrl.URL_DATA_Admin;
        Total=v.findViewById(R.id.Total);
        TotalGen=v.findViewById(R.id.TotalGen);
        list_produit=new ArrayList<>();
        db=new DatabaseHelpler(getContext());
        SSP=new SaveSharedPreference(getActivity());
        clicking();

        getProfileInfo();



        return v;

    }
    public void clicking()
    {
        pdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //Assuming your rootView is called mRootView like so
                View mRootView;
                mRootView=view.findViewById(R.id.receipt);

                //First Check if the external storage is writable
                Log.d("pdfcreate"," ");
                String state = Environment.getExternalStorageState();
                if (!Environment.MEDIA_MOUNTED.equals(state)) {
                    Toast.makeText(getActivity(),"access permission denied", Toast.LENGTH_LONG).show();
                }
                //Create a directory for your PDF
                Log.d("directory"," "+getContext().getFilesDir()+" "+getContext().getExternalFilesDir(null)
                +getContext().getExternalCacheDir());

                File pdfDir = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Log.d("Version"," <=KitKat");
                    pdfDir = new File(getContext().getExternalFilesDir(null), "Tawenza Bons");


                }
                if (!pdfDir.exists()){
                    Log.d("dossier"," create tawenza");
                    pdfDir.mkdir();
                }

                //Then take the screen shot
                Bitmap screen;
                View v1 = mRootView;
                v1.setDrawingCacheEnabled(true);
                screen = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);

//Now create the name of your PDF file that you will generate
                File pdfFile = new File(pdfDir, date.getText().toString()+".pdf");
                try {
                    Log.d("pdfcreate"," save pic");
                    PdfDocument document = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        document = new PdfDocument();

                        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(screen.getWidth(), screen.getHeight(),1).create();
                        PdfDocument.Page page = document.startPage(pageInfo);

                        Canvas canvas = null;
                        canvas = page.getCanvas();
                        Paint paint = new Paint();
                        paint.setColor(Color.parseColor("#ffffff"));
                        canvas.drawPaint(paint);
                        canvas.drawBitmap(screen, 0, 0, null);
                        document.finishPage(page);
                        document.writeTo(new FileOutputStream(pdfFile));
                        Toast.makeText(getContext(),"PDF Crée avec succes",Toast.LENGTH_LONG).show();
                    }


                }
                catch (Exception e){
                    e.printStackTrace();
                }
                String s="";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                  s= Environment.getExternalStorageState(new File(Environment.DIRECTORY_DOCUMENTS));
                }


                s=pdfDir.getAbsolutePath().replace(" ","\\");
                Log.d("open folder"," "+s+" "+Uri.fromFile(pdfFile)+" ");
                openFolder(pdfFile);

            }
        });
    }
    public void openFolder(File file)
    {
        // location = "/sdcard/my_folder";

        // or use */*
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            data = FileProvider.getUriForFile(getContext(),BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            data = Uri.fromFile(file);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(data, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d("activitynofoundexce"," "+e.toString());
            // Instruct the user to install a PDF reader here, or something
        }



    }

    public void listProd() {
        Log.d("database panier"," "+db.getAllData(SSP.getUsername()).getCount());

        Cursor res = db.getAllData(SSP.getUsername());

        while (res.moveToNext()) {
            Log.d("ListProd"," inside while");
            produitCommand pc = new produitCommand(res.getString(1), res.getString(3), res.getInt(4)
                    , res.getString(5), res.getInt(6), res.getString(0)
                    , res.getInt(11), res.getFloat(12), res.getFloat(10)
                    , res.getString(13), res.getString(8), res.getString(9)
                    , res.getString(8));
            list_produit.add(pc);

        }
        Log.d("ListProd"," "+list_produit.size());
        res.close();


        db.deleteAll(SSP.getUsername());
        fill();

    }
    public void fill()
    {
        float sum=0;
        for(int i=0;i<list_produit.size();i++)

        {
            LinearLayout.LayoutParams LinearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, WRAP_CONTENT);

            LinearLayout.LayoutParams LinearParams2 = new LinearLayout.LayoutParams(100, WRAP_CONTENT);
            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            LinearLayout produit=new LinearLayout(getContext());

            produit.setLayoutParams(LinearParams);
            TextView Desc=new TextView(getContext());
            Desc.setLayoutParams(textviewParams);

            if(SSP.getLang().equals("ar"))
            {
                Desc.setText(list_produit.get(i).getNomAr()+" "+list_produit.get(i).getMarqueAr()+" "+list_produit.get(i).getSizeAr()+" "+list_produit.get(i).getQte()+"x ");
            }else
            {
                Desc.setText(list_produit.get(i).getNom()+" "+list_produit.get(i).getMarque()+" "+list_produit.get(i).getSize()+" "+list_produit.get(i).getQte()+"x ");
            }



            TextView Prix=new TextView(getContext());
            Prix.setLayoutParams(LinearParams);
            Prix.setGravity(Gravity.END);

            String UnitPrix="";




            float prix=list_produit.get(i).getPrix()*list_produit.get(i).getQte();
            Prix.setText(String.format(Locale.US,"%.2f",prix));
            UnitPrix=String.format(Locale.US,"%.2f",list_produit.get(i).getPrix());
            if(list_produit.get(i).getPromotion()>0)
            {
                prix=list_produit.get(i).getPromotion()*list_produit.get(i).getQte();
                Prix.setText(String.format(Locale.US,"%.2f",prix));

                UnitPrix=String.format(Locale.US,"%.2f",list_produit.get(i).getPromotion());
            }else if(list_produit.get(i).getQteRemise()>0 && list_produit.get(i).getPrixRemise()>0)
            {
                if(list_produit.get(i).getQteRemise()>=list_produit.get(i).getQte())
                {
                    prix=list_produit.get(i).getPrixRemise()*list_produit.get(i).getQte();
                    Prix.setText(String.format(Locale.US,"%.2f",prix));

                    UnitPrix=String.format(Locale.US,"%.2f",list_produit.get(i).getPrixRemise());
                }
            }

            Desc.append(UnitPrix);

            produit.addView(Desc);

            produit.addView(Prix);

            Items.addView(produit);
            Log.d("ajouteritem"," "+Desc.getText().toString()+" "+Prix.getText().toString());
            sum=sum+prix;



        }
        Total.setText(String.format(Locale.US,"%.2f",sum));
        sum=sum+Float.parseFloat(Frais.getText().toString());
        TotalGen.setText(String.format(Locale.US,"%.2f",sum));
        Log.d("logpanier"," "+list_produit.size());


    }
    private void getStoreInfo(){
        Log.d("url",""+URL_DATA_Admin);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA_Admin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.d("response getprofileinfo"," "+response);
                    JSONArray json = new JSONArray(response);

                    JSONObject client = json.getJSONObject(0);
                    if(SSP.getLang().equals("ar"))
                    {
                        Address.setText("العنوان: ");
                        Address.append(client.getString("AddressAr"));
                        Tel.setText("الهاتف: ");
                        Tel.append(client.getString("Phone"));
                    }else
                    {
                        Address.setText("Adresse: ");
                        Address.append(client.getString("Address"));
                        Tel.setText("Tel: ");
                        Tel.append(client.getString("Phone"));

                    }


                    if(getArguments()!=null)
                    {
                        Log.d("mescom"," "+getArguments().getString("date")+" "+
                                getArguments().getString("NumCom"));
                        if(getArguments().getString("date").equals("MesCom"))
                        {
                            getComProd();
                        }else
                        {
                            listProd();
                            progressdialog.dismiss();
                        }
                    }else
                    {
                        listProd();
                        progressdialog.dismiss();
                    }




                } catch (Exception e) {
                    Log.d("jsonException"," "+e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorvolley"," "+error.toString());
                progressdialog.dismiss();
                if(error instanceof TimeoutError)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("error","timeout");
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }else if(error instanceof NoConnectionError)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("error","Noconx");
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","select");



                return params;
            }
        };
        reqeu.add(req);

    }
    private void getComProd(){
        Log.d("url",""+URL_DATA_Command);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA_Command, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.d("response getprofileinfo"," "+response);
                    JSONArray json = new JSONArray(response);

                    for(int i=0;i<json.length();i++)
                    {
                        JSONObject command = json.getJSONObject(i);

                        produitCommand prodcom=new produitCommand(command.getString("Nom"),command.getString("Marque"),
                                BigDecimal.valueOf(command.getDouble("Prix")).floatValue(),command.getString("Taille")
                        ,command.getInt("Qte"),command.getString("CodeProd"),command.getInt("QteRemise")
                        ,BigDecimal.valueOf(command.getDouble("RemisePrix")).floatValue(),BigDecimal.valueOf(command.getDouble("Promotion")).floatValue()
                        ,command.getString("Img"),command.getString("NomAr"),command.getString("MarqueAr"),
                                command.getString("TailleAr"));

                        date.setText(getArguments().getString("datecom"));
                        list_produit.add(prodcom);


                    }
                    progressdialog.dismiss();


                    fill();
                } catch (Exception e) {
                    Log.d("jsonException"," "+e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorvolley"," "+error.toString());
                progressdialog.dismiss();
                if(error instanceof TimeoutError)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("error","timeout");
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }else if(error instanceof NoConnectionError)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("error","Noconx");
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","selectComProd");
                params.put("NumCom",getArguments().getString("NumCom"));




                return params;
            }
        };
        reqeu.add(req);

    }
    private void getProfileInfo(){
        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("loading...");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
        Log.d("url",""+URL_DATA);

        RequestQueue reqeu= Volley.newRequestQueue(getActivity());

        StringRequest req;

        req = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.d("response getprofileinfo"," "+response);
                    JSONArray json = new JSONArray(response);

                    JSONObject client = json.getJSONObject(0);
                    Frais.setText(String.format(Locale.US,"%.2f",Float.parseFloat(client.getString("Frais"))));
                    getStoreInfo();


                } catch (Exception e) {
                    Log.d("jsonException"," "+e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorvolley"," "+error.toString());
                progressdialog.dismiss();
                if(error instanceof TimeoutError)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("error","timeout");
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }else if(error instanceof NoConnectionError)
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("error","Noconx");
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.error_fragment,bundle);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","select");



                return params;
            }
        };
        reqeu.add(req);

    }
}
