package com.android.sb_camera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.core.content.FileProvider.getUriForFile;

public class MainActivity extends AppCompatActivity
        implements PermissionsChecker.PermissionListener{

    private static int  REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    private String  currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        PermissionsChecker.permissionListener = this;
        PermissionsChecker.checkPermissions(Manifest.permission.CAMERA, 1);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        PermissionsChecker.onRequestPermissionsResult(requestCode,
                permissions, grantResults);
    }

    @Override
    public void permissionResponse(int code) {
        if(code ==1) {
            ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dispatchTakePic();
                }
            });
        };
    }


    private void dispatchTakePic() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

            // Create the File where the photo should go
            File photoFile = null;
            try  {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred  while creating the  File
                Log.d("MACT", ex.toString());
            }
            // Continue only  if the File  was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.android.sb_camera.fileprovider",
                            photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
        }
    }

    //automatically called when camera returns result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            //camera should put bitmapdata in this key
            Bitmap imageBitmap = (Bitmap) extra.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
