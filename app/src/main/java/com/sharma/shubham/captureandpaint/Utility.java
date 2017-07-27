package com.sharma.shubham.captureandpaint;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Shubham on 2017-07-26.
 */

public class Utility {

    // Declare constant for storage permission request
    public static final int REQUEST_STORAGE_PERMISSION = 1;


    public static boolean checkPermission(Context context) {

        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    public static void askForPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_STORAGE_PERMISSION);
    }
}
