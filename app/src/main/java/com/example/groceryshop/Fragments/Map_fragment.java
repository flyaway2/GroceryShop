package com.example.groceryshop.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.groceryshop.Activities.MainActivity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.AlertDialog.THEME_HOLO_LIGHT;
import static com.example.groceryshop.custom.Constants.MAPVIEW_BUNDLE_KEY;

public class Map_fragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "UserListFragment";
    private MapView mMapView;
    private Marker mMarker;
    private Button confirmer_adr;
    private String URL_DATA,value;
    private SaveSharedPreference SSP;
    private LatLng g;
    private final LatLng beni_isguen = new LatLng(32.463321,3.685161);
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private Location currentLocation;
    GoogleMap googleMap;
    private boolean firstTimeFlag = true;
    private String add;
    private LatLngBounds agharm;
    private Fragment mFragment;





    public static Map_fragment newInstance() {
        return new Map_fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        Log.d("mapfragment","");
        mMapView = view.findViewById(R.id.location_map);
        confirmer_adr=view.findViewById(R.id.choisir_adr);
        URL_DATA= DBUrl.URL_DATA_Client;
        SSP=new SaveSharedPreference(getActivity());

        mFragment=this;

        confirmer_adr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments().getString("Address")!=null){
                    g= mMarker.getPosition();
                    if(getArguments().getString("Address").equals("modifier")){



                  Log.d("mapfragment"," ");

                      Log.d("mapfragment"," modifierprofile");
                      modifierProfile();

                }else if(getArguments().getString("Address").equals("expedition"))
                        {
                            Log.d("mapfragment"," expedition");
                            Expedition();
                        }

                    }

                }
            });

            initGoogleMap(savedInstanceState);

        return view;
    }

    private void initGoogleMap(Bundle savedInstanceState){
        Log.d("init google map"," ");
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("on save instance state"," ");
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
        Log.d("on resume"," ");
        super.onResume();
        mMapView.onResume();
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            startCurrentLocationUpdates();
        }
    }
    private boolean isGooglePlayServicesAvailable() {
        Log.d("googleplayserviceavail"," ");
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(getActivity(), "Please Install google play services to use this application", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onStart() {
        Log.d("on start"," ");
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        Log.d("on stop"," ");
        super.onStop();
        mMapView.onStop();
    }
    private void startCurrentLocationUpdates() {
        Log.d("startcurlocationupdate"," ");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                Log.d("no permissions "," location update");
                return;
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d("location callback"," ");
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation);
                firstTimeFlag = false;
            }
            showMarker(currentLocation);
        }
    };

    private void animateCamera(@NonNull Location location) {
        Log.d("animate camera"," ");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));
    }
    private void showMarker(@NonNull Location currentLocation) {
        Log.d("show marker"," ");
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (mMarker == null )
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
        Log.d("camerca posbearing"," ");
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }
    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("on map ready"," ");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("no permission"," ");

            return;
        }
        this.googleMap=map;
        Log.d("onmapready"," ");
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
        LocationManager lm = (LocationManager)getContext().getSystemService(getContext().LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        String status="";
        Boolean AvailConx=false;
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
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

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}
        if(!gps_enabled) {
            AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity(), THEME_HOLO_LIGHT);


            if(SSP.getLang().equals("ar"))
            {
                final String[] listItems={"العودة","إعادة المحاولة"};
                mBuilder.setTitle("الرجاء تفعيل خاصية تحديد الموقع");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            getFragmentManager().beginTransaction().detach(mFragment).attach(mFragment).commit();

                        }else if(which==1)
                        {
                            getFragmentManager().popBackStack();

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
                            getFragmentManager().beginTransaction().detach(mFragment).attach(mFragment).commit();

                        }else if(which==1)
                        {
                            getFragmentManager().popBackStack();

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
            AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity(), THEME_HOLO_LIGHT);
            if(SSP.getLang().equals("ar"))
            {
                final String[] listItems={"العودة","إعادة المحاولة"};
                mBuilder.setTitle("الرجاء تشغيل الأنترنت");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            getFragmentManager().beginTransaction().detach(mFragment).attach(mFragment).commit();
                        }else if(which==1)
                        {
                            getFragmentManager().popBackStack();
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
                            // Reload current fragment


                            getFragmentManager().beginTransaction().detach(mFragment).attach(mFragment).commit();


                        }else if(which==1)
                        {
                            getFragmentManager().popBackStack();

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
        Log.d("on pause"," ");
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("on destroy"," ");
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        Log.d("on low memory"," ");
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    public void Expedition(){
        Log.d("expedition"," ");
        String Name="";
        try{
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(g.latitude,g.longitude, 1);
        Address obj = addresses.get(0);

        String add = "";
        add = add + obj.getLocality();
        add = add +","+ obj.getSubAdminArea();
            add = add +","+ obj.getSubLocality();

        Name=add;

    } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

        Bundle bundle=new Bundle();
        bundle.putString("latitude",String.valueOf(g.latitude));
        bundle.putString("longitude",String.valueOf(g.longitude));
        bundle.putString("Disp",getArguments().getString("Disp"));
        bundle.putString("ModPay",getArguments().getString("ModPay"));
        bundle.putString("Name",Name);


        NavController nv= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);

        nv.navigate(R.id.action_map_fragment_to_expedition_address,bundle);




    }
    public void modifierProfile(){
        Log.d("modifier profile"," ");
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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

        RequestQueue reqeu = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.profile_fragment);


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
                params.put("Username",SSP.getUsername());
                params.put("DBUsername", DBUrl.DBUsername);
                params.put("DBPassword",DBUrl.DBPassword);
                params.put("query","UpdateAddress");
                params.put("value",add);
                params.put("latitude",String.valueOf(g.latitude));
                params.put("longitude",String.valueOf(g.longitude));


                return params;
            }
        };
        reqeu.add(postRequest);

    }
}



