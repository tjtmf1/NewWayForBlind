package com.example.newwayforblind;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    LinearLayout linearLayout;
    ImageView arrowUp, arrowDown;

    private static final LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 15000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 15000;
    private static final int START_MARKER = 1001;
    private static final int FINISH_MARKER = 1002;

    private Location currentLocation = null;
    private Location startLocation = null;
    private Location finishLocation = null;
    private GoogleMap googleMap = null;
    private MapView mapView = null;
    private GoogleApiClient googleApiClient = null;
    private Marker startMarker = null;
    private Marker finishMarker = null;

    private ArrayList<Location> locations;
    private boolean startFlag = false;
    private boolean finishFlag = true;
    private double distanceTravelled = 0;

    private final static int MAXENTRIES = 5;
    private String[] LikelyPlaceNames = null;
    private LatLng[] LikelyLatLngs = null;

    private TextView strideTv;

    private StepCheck stepCheck;
    private Handler mHandler;

    public MapFragment() {

    }

    public void setMarker(Location location, String markerTitle, int markerType) {
        if(location != null){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            switch (markerType){
                case START_MARKER:
                    if(startMarker != null) startMarker.remove();
                    MarkerOptions markerOptions1 = new MarkerOptions();
                    markerOptions1.position(latLng);
                    markerOptions1.title(markerTitle);
                    markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    startMarker = this.googleMap.addMarker(markerOptions1);
                    startLocation.setLatitude(location.getLatitude());
                    startLocation.setLongitude(location.getLongitude());

                    return;
                case FINISH_MARKER:
                    if(finishMarker != null) finishMarker.remove();
                    MarkerOptions markerOptions2 = new MarkerOptions();
                    markerOptions2.position(latLng);
                    markerOptions2.title(markerTitle);
                    markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    finishMarker = this.googleMap.addMarker(markerOptions2);
                    finishLocation.setLatitude(location.getLatitude());
                    finishLocation.setLongitude(location.getLongitude());

                    return;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentLocation = new Location("");
        startLocation = new Location("");
        finishLocation = new Location("");
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                strideTv.setText(stepCheck.getStep()+"");
            }
        };
        stepCheck = new StepCheck(getContext(), mHandler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_map, container, false);

        linearLayout = (LinearLayout)layout.findViewById(R.id.linearLayout);
        linearLayout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeTop() {
                if(startFlag == false) {
                    setMarker(currentLocation, "START", START_MARKER);

                    locations = new ArrayList<>();
                    locations.add(new Location(currentLocation));

                    stepCheck.startSensor();
                    startFlag = true;
                    finishFlag = false;

                    arrowUp.setColorFilter(getResources().getColor(R.color.colorRed));
                    arrowDown.setColorFilter(getResources().getColor(R.color.colorGreen));
                }
            }
            public void onSwipeBottom() {
                if(startFlag == true) {
                    setMarker(currentLocation, "FINISH", FINISH_MARKER);

                    Toast.makeText(getActivity(), stepCheck.getStep() + "", Toast.LENGTH_SHORT).show();

                    stepCheck.endSensor();
                    stepCheck.resetStep();
                    startFlag = false;
                    finishFlag = true;

                    drawPolyline();
                    calculateDistanceTravelled();

                    Toast.makeText(getContext(), distanceTravelled + "", Toast.LENGTH_SHORT).show();

                    locations = null;

                    arrowDown.setColorFilter(getResources().getColor(R.color.colorWhite));
                    arrowUp.setColorFilter(getResources().getColor(R.color.colorWhite));
                }
            }
        });

        arrowUp = (ImageView)layout.findViewById(R.id.arrowUp);
        arrowDown = (ImageView)layout.findViewById(R.id.arrowDown);

        mapView = (MapView)layout.findViewById(R.id.map);
        mapView.getMapAsync(this);

        strideTv = (TextView) layout.findViewById(R.id.strideTv);

        locations = new ArrayList<>();

        return layout;
    }

    public void drawPolyline(){
        PolylineOptions polylineOptions = new PolylineOptions();

        for(int i=0; i<locations.size(); i++){
            LatLng latLng = new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude());
            polylineOptions.add(latLng);
        }

        polylineOptions.width(3)
                        .color(Color.BLUE)
                        .geodesic(true);

        this.googleMap.addPolyline(polylineOptions);
    }

    public void calculateDistanceTravelled(){
        LatLng start, end;

        for(int i=0; i<locations.size() - 1; i++){
            start = new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude());
            end = new LatLng(locations.get(i + 1).getLatitude(), locations.get(i + 1).getLongitude());

            distanceTravelled += calculationByDistance(start, end);
        }
    }

    public double calculationByDistance(LatLng startP, LatLng endP){
        int Radius = 6371;// radius of earth in Km
        double lat1 = startP.latitude;
        double lat2 = endP.latitude;
        double lon1 = startP.longitude;
        double lon2 = endP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

        if ( googleApiClient != null && googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if ( googleApiClient != null)
            googleApiClient.connect();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수
        MapsInitializer.initialize(getActivity().getApplicationContext());

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // OnMapReadyCallback implements 해야 mapView.getMapAsync(this); 사용가능. this 가 OnMapReadyCallback

        this.googleMap = googleMap;

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에 지도의 초기위치를 서울로 이동
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //나침반이 나타나도록 설정
        googleMap.getUiSettings().setCompassEnabled(true);
        // 매끄럽게 이동함
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //  API 23 이상이면 런타임 퍼미션 처리 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 사용권한체크
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

            if ( hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
                //사용권한이 없을경우
                //권한 재요청
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                //사용권한이 있는경우
                if ( googleApiClient == null) {
                    buildGoogleApiClient();
                }

                if ( ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    googleMap.setMyLocationEnabled(true);
                }
            }
        } else {

            if ( googleApiClient == null) {
                buildGoogleApiClient();
            }

            googleMap.setMyLocationEnabled(true);
        }


    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage((FragmentActivity)getActivity(), this)
                .build();
        googleApiClient.connect();

        createLocationRequest();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if ( !checkLocationServicesStatus()) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            builder.setTitle("위치 서비스 비활성화");
            builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" +
                    "위치 설정을 수정하십시오.");
            builder.setCancelable(true);
            builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent callGPSSettingIntent =
                            new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                LocationServices.FusedLocationApi
                        .requestLocationUpdates(googleApiClient, locationRequest, this);
            }
        } else {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(googleApiClient, locationRequest, this);

            this.googleMap.getUiSettings().setCompassEnabled(true);
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

    }

    @Override
    public void onConnectionSuspended(int cause) {
        if ( cause ==  CAUSE_NETWORK_LOST )
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED )
            Log.e(TAG,"onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Location location = new Location("");
        location.setLatitude(DEFAULT_LOCATION.latitude);
        location.setLongitude((DEFAULT_LOCATION.longitude));

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        Toast.makeText(getActivity(), "위치정보 가져오지 못함.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());
        Log.i(TAG, "onLocationChanged call.." + currentLocation.getLatitude() + " " + currentLocation.getLongitude());

        if(startFlag == true && finishFlag == false){
            Location now = new Location(location);
            locations.add(now);
            for(int i=0; i<locations.size(); i++){
                Log.i(TAG, i + " : " + locations.get(i).getLatitude() + " " + locations.get(i).getLongitude());
            }
//            Log.i(TAG, 0 + " : " + locations.get(0).getLatitude() + " " + locations.get(0).getLongitude());
        }

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void searchCurrentPlaces() {
        @SuppressWarnings("MissingPermission")
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>(){

            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                int i = 0;
                LikelyPlaceNames = new String[MAXENTRIES];
                LikelyLatLngs = new LatLng[MAXENTRIES];

                for(PlaceLikelihood placeLikelihood : placeLikelihoods) {
                    LikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                    LikelyLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                    i++;
                    if(i > MAXENTRIES - 1 ) {
                        break;
                    }
                }

                placeLikelihoods.release();

                Location location = new Location("");
                location.setLatitude(LikelyLatLngs[0].latitude);
                location.setLongitude(LikelyLatLngs[0].longitude);

            }
        });
    }

    private void createLocationRequest(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
    }
}
