package com.android.sb_camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsChecker {
    //public class PermissionsChecker extends AppCompatActivity {
    public interface PermissionListener {
        public Context getApplicationContext();
        public void permissionResponse(int code);
    }

    public static PermissionListener permissionListener =  null;

    public static void checkPermissions(String permission, int code){
        Log.d("MainActivity", "checking perm");
        //String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission( permissionListener.getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) permissionListener, permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }  else  {
                // No explanation needed; request the permission
                //ActivityCompat.requestPermissions( this, new String[]{permission}, 2);
                ActivityCompat.requestPermissions((Activity)permissionListener, new String[]{permission},code);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app -defined int constant. The callback method gets the
                // result of the request.
            }
        } else  {
            // Permission has already been granted, so enblae the location
            Log.d( "MainActivity", "already");
            permissionListener.permissionResponse(code);
        }
    }

    public static void onRequestPermissionsResult(int requestCode,
                                                  String[] permissions, int[] grantResults) {
        //switch (requestCode) {
        //case 0: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay! Do the
            // contacts -related task you need to do.
            permissionListener.permissionResponse(requestCode);
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Context context = permissionListener.getApplicationContext();
            CharSequence text = "Delete this app. It is useless without permissions";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        //return;
        //}

        // other 'case' lines to check for other
        // permissions this app might request.
        //}

    }
}
