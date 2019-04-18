package com.example.imagefromgalaryorcamera;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    Uri image_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imageView) {
            //Below method is called to select Image from galary or camera.
            selectImage();
        }

    }

    private void selectImage(){
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        /*--------AlertDialog is to show options to user from which they want to select image--------*/
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")){
                    //If user selects to take pic from camera then floowing code will be performed

                    /*---------Here Below if condition is used for taking permissions from users if android version is above marshmello--------*/
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

                        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                            /*------- On basis of permission onRequestPermissionsResult Method will handle below result------*/
                            String [] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission,1000);
                        }
                        else {
                            /*-------- If permission is already granted then it calls openCamera method --------*/
                            openCamera();
                        }
                    }
                    else {
                        /*-------- If version is below Marshmello then it calls openCamera method --------*/
                        openCamera();
                    }
                }
                else if (options[item].equals("Choose from Gallery")){
                    //If user selects to take pic from galary then floowing code will be performed

                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                    /*-----------onRequestPermissionsResult Method will handle below code------*/
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    /*---------- If user select option then no action will be performed and Alert dialog will be dismissed---------*/
                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }


    private void openCamera() {

        /*---------- This method is for taking pictures from camera and saving it to galary as well-------*/

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //Camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(takePictureIntent, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //This method handles the result of user's permission if version is above marshmello.
        switch (requestCode){
            case 1000:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else {
                    //permisiion from pop up was denied.
                    Toast.makeText(MainActivity.this,"Permission Denied...",Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
       if (resultCode == Activity.RESULT_OK) {
           switch (requestCode) {
               case 1:
                   imageView.setImageURI(image_uri);
                   break;
               case 2:
                   //data.getData returns the content URI for the selected Image
                   image_uri = data.getData();
                   imageView.setImageURI(image_uri);
                   break;
           }
       }
    }
}
