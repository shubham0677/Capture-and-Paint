package com.sharma.shubham.captureandpaint;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.captureButton)
    Button mCaptureButton;

    @BindView(R.id.drawingView)
    DrawingView mDrawingView;

    private String mTempPhotoPath;

    static Bitmap sCapturedImageBitmap;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String FILE_PROVIDER_AUTHORITY = "com.sharma.shubham.fileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }
    

    /***
     * On click method for capture image button
     */
    public void captureImage(View v) {
        // Check for storage permission
        if(Utility.checkPermission(this)) {
            // Launch camera if permission exists
            launchCamera();
        } else {
            // Ask for storage permission
            Utility.askForPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera to capture picture
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Grant permissions to the intent
                Utility.grantUriPermissionsToIntent(this, takePictureIntent, photoURI);

              // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If the image capture intent was called and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            processAndSetImage();
        } else {
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    private void processAndSetImage() {
        sCapturedImageBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);

        mCaptureButton.setVisibility(View.GONE);
        mDrawingView.setVisibility(View.VISIBLE);
    }
}