//package com.codewarriors4.tiffin.utils;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.IntentSender;
//import android.location.Location;
//import android.support.annotation.NonNull;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResult;
//
//
//import java.util.ArrayList;
//
//public class LocationHelper implements PermissionUtils.PermissionResultCallback {
//    private Context context;
//    private Activity current_activity;
//
//    private boolean isPermissionGranted;
//
//    private FusedLocationProviderClient mLastLocation;
//
//    // Google client to interact with Google API
//
//    private GoogleApiClient mGoogleApiClient;
//
//    // list of permissions
//
//    private ArrayList<String> permissions=new ArrayList<>();
//    private PermissionUtils permissionUtils;
//
//    private final static int PLAY_SERVICES_REQUEST = 1000;
//    private final static int REQUEST_CHECK_SETTINGS = 2000;
//
//    public LocationHelper(Context context) {
//
//        this.context=context;
//        this.current_activity= (Activity) context;
//
//        permissionUtils=new PermissionUtils(context,this);
//
//        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//
//    }
//
//    public void checkpermission()
//    {
//        permissionUtils.check_permission(permissions,"Need GPS permission for getting your location",1);
//    }
//
//    private boolean isPermissionGranted() {
//        return isPermissionGranted;
//    }
//
//    public boolean checkPlayServices() {
//
//        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
//
//        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
//
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (googleApiAvailability.isUserResolvableError(resultCode)) {
//                googleApiAvailability.getErrorDialog(current_activity,resultCode,
//                        PLAY_SERVICES_REQUEST).show();
//            } else {
//                showToast("This device is not supported.");
//            }
//            return false;
//        }
//        return true;
//    }
//
//    public void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(context)
//                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) current_activity)
//                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) current_activity)
//                .addApi(LocationServices.API).build();
//
//        mGoogleApiClient.connect();
//
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(mLocationRequest);
//
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
//
//                final Status status = locationSettingsResult.getStatus();
//
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied. The client can initialize location requests here
//                        mLastLocation=getLocation();
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            status.startResolutionForResult(current_activity, REQUEST_CHECK_SETTINGS);
//
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        break;
//                }
//            }
//        });
//
//
//    }
//
//    public FusedLocationProviderClient getLocation() {
//
//        if (isPermissionGranted()) {
//
//            try
//            {
//                mLastLocation = LocationServices.getFusedLocationProviderClient(current_activity);
//                return mLastLocation;
//            }
//            catch (SecurityException e)
//            {
//                e.printStackTrace();
//
//            }
//
//        }
//
//        return null;
//
//    }
//
//    @Override
//    public void PermissionGranted(int request_code) {
//
//    }
//
//    @Override
//    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
//
//    }
//
//    @Override
//    public void PermissionDenied(int request_code) {
//
//    }
//
//    @Override
//    public void NeverAskAgain(int request_code) {
//
//    }
//}
