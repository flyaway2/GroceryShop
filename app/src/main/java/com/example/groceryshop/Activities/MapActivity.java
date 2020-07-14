package com.example.groceryshop.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.groceryshop.Fragments.Map_fragment;
import com.example.groceryshop.R;
import com.example.groceryshop.custom.DBUrl;
import com.example.groceryshop.custom.SaveSharedPreference;
import com.example.groceryshop.custom.VolleySingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.AlertDialog.THEME_HOLO_LIGHT;
import static com.example.groceryshop.custom.Constants.MAPVIEW_BUNDLE_KEY;

public class MapActivity extends AppCompatActivity  implements OnMapReadyCallback{
    private static final String TAG = "UserListFragment";
    private MapView mMapView;
    private Marker mMarker;
    private Button confirmer_adr;
    private String URL_DATA,value;
    private LatLng g;
    private final LatLng beni_isguen = new LatLng(32.463321,3.685161);
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private Location currentLocation;
    GoogleMap googleMap;
    private boolean firstTimeFlag = true;
    private String add;

    private SaveSharedPreference SSP;
    private  LatLngBounds agharm;




    public static MapActivity newInstance() {
        return new MapActivity();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        SSP=new SaveSharedPreference(this);
        mMapView = findViewById(R.id.location_map);
        Log.d("mmapview"," "+mMapView);
        confirmer_adr=findViewById(R.id.choisir_adr);
        URL_DATA= DBUrl.URL_DATA.concat("modifierProfile.php?");

        confirmer_adr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    g= mMarker.getPosition();
                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses = null;
                add = "";
                try {
                    addresses = geocoder.getFromLocation(g.latitude,g.longitude, 1);
                    Address obj = addresses.get(0);


                    add = add + obj.getLocality();
                    add = add +","+ obj.getSubAdminArea();
                    add = add +","+ obj.getSubLocality();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                    Intent prev_intent=getIntent();
                    Intent intent=new Intent(getApplicationContext(),VerifyPhoneActivity.class);
                    intent.putExtra("nom", prev_intent.getStringExtra("nom"));
                    intent.putExtra("prenom", prev_intent.getStringExtra("prenom"));
                    intent.putExtra("username", prev_intent.getStringExtra("username"));
                    intent.putExtra("password", prev_intent.getStringExtra("password"));
                    intent.putExtra("phone", prev_intent.getStringExtra("phone"));
                    intent.putExtra("email", prev_intent.getStringExtra("email"));
                    intent.putExtra("latitude", String.valueOf(g.latitude));
                    intent.putExtra("longitude", String.valueOf(g.longitude));

                    intent.putExtra("Address",add);
                    startActivity(intent);
            }
        });

        initUserListRecyclerView();
        initGoogleMap(savedInstanceState);


    }



    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    private void initUserListRecyclerView() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            startCurrentLocationUpdates();
        }
    }
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(this, "Please Install google play services to use this application", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation);
                firstTimeFlag = false;
            }
            LocationManager lm = (LocationManager)getBaseContext().getSystemService(getBaseContext().LOCATION_SERVICE);


                showMarker(currentLocation);



        }
    };

    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));
    }
    private void showMarker(@NonNull Location currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (mMarker == null)
        {
            mMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng));

        }else if(mMarker.getPosition().latitude==beni_isguen.latitude
                && mMarker.getPosition().longitude==beni_isguen.longitude)
        {
            mMarker.setPosition(latLng);
        }


    }
    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }
    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.googleMap=map;
        Log.d("onmapready","");
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("onmapready","onclick");
                if(agharm.contains(latLng))
                {
                    mMarker.setPosition(latLng);
                }



            }
        });
        LocationManager lm = (LocationManager)getBaseContext().getSystemService(getBaseContext().LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        String status="";
        Boolean AvailConx=false;
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";
                AvailConx=true;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
                AvailConx=true;

            }
        } else {
            status = "No internet is available";

        }
        Log.d("connextion"," "+status+" "+AvailConx);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        if(!gps_enabled) {
            AlertDialog.Builder mBuilder=new AlertDialog.Builder(this, THEME_HOLO_LIGHT);


            if(SSP.getLang().equals("ar"))
            {
                final String[] listItems={"العودة","إعادة المحاولة"};
                mBuilder.setTitle("الرجاء تفعيل خاصية تحديد الموقع");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            finish();
                            startActivity(getIntent());

                        }else if(which==1)
                        {
                            finish();

                        }

                    }

                });

            }else{
                final String[] listItems={"réessayer","retour"};
                mBuilder.setTitle("s'il vous plaît activer la localisation");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            finish();
                            startActivity(getIntent());

                        }else if(which==1)
                        {
                            finish();

                        }

                    }
                });
            }
            AlertDialog alterdial=mBuilder.create();
            alterdial.setCanceledOnTouchOutside(false);
            alterdial.setCancelable(false);
            alterdial.show();

        }else
        if(!AvailConx){
            AlertDialog.Builder mBuilder=new AlertDialog.Builder(this, THEME_HOLO_LIGHT);
            if(SSP.getLang().equals("ar"))
            {
                final String[] listItems={"العودة","إعادة المحاولة"};
                mBuilder.setTitle("الرجاء تشغيل الأنترنت");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            finish();
                            startActivity(getIntent());

                        }else if(which==1)
                        {
                            finish();

                        }


                    }

                });

            }else{
                final String[] listItems={"réessayer","retour"};
                mBuilder.setTitle("s'il vous plaît activer l'internet");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            finish();
                            startActivity(getIntent());

                        }else if(which==1)
                        {
                            finish();

                        }


                    }
                });
            }
            AlertDialog alterdial=mBuilder.create();
            alterdial.setCanceledOnTouchOutside(false);
            alterdial.setCancelable(false);
            alterdial.show();
        }
        map.setMyLocationEnabled(true);



       agharm = new LatLngBounds(new LatLng(32.456401, 3.681278),
                new LatLng(32.481167,3.703852));
        map.setLatLngBoundsForCameraTarget(agharm);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(beni_isguen, 15));


        if(mMarker==null){
            mMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(beni_isguen));

        }




    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    public void modifierProfile(){
        RequestQueue reqeu = VolleySingleton.getInstance(this).getRequestQueue();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);



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


                params.put("iden","address");
                params.put("value",value);
                params.put("latitude",String.valueOf(g.latitude));
                params.put("longitude",String.valueOf(g.longitude));


                return params;
            }
        };
        reqeu.add(postRequest);

    }
}
