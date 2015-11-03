package com.example.bhavya.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
//import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback, BaseFragment.OnFragmentInteractionListener{

    Camera mCamera;
    SurfaceView mPreview;
    final String TAG="CAMERA_ACTIVITY";

    public static final int SELECT_PHOTO_ACTION = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPreview = (SurfaceView) findViewById(R.id.preview);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCamera = Camera.open();

        Log.d(TAG,"onCreate()");
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();

        Log.d(TAG,"onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.release();
        Log.d(TAG, "Destroy");
    }


    @Override
    public void onResume(){
        super.onResume();
        mCamera.startPreview();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onStart(){
        super.onStart();

        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop(){
        super.onStop();

        Log.d(TAG, "onStop()");
    }




    public void onCancelClick(View v) {
        finish();
    }

    public void onSnapClick(View v) {
        mCamera.takePicture(this, null, null, this);

    }


    public void slideShow(View v) {
        //mCamera.takePicture(this, null, null, this);
        //Intent intent = new Intent(this, TopListActivity.class);

        FragmentManager fm= this.getFragmentManager();
        BaseFragment targetFragment = null;
        targetFragment = HorizontalPhotoGalleryFragment.newInstance(1);

        fm.beginTransaction()
                .replace(R.id.container, targetFragment)
                .commit();

    }


    @Override
    public void onShutter() {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //Here, we chose internal storage

        double[] MyLoc = new double[2];
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            try{l = lm.getLastKnownLocation(providers.get(i));}catch(Exception e){}
            if (l != null) break;
        }
        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }

        //LatLng abc=new LatLng(gps[0],gps[1]);
        try {

            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(gps[0], gps[1], 1);
            String s=addresses.get(0).getLocality();
            Toast toast = Toast.makeText(this, addresses.get(0).getLocality(), Toast.LENGTH_SHORT);
            toast.show();

            File direct = new File(Environment.getExternalStorageDirectory() + addresses.get(0).getLocality());

            if (!direct.exists()) {
                File wallpaperDirectory = new File("/sdcard/"+addresses.get(0).getLocality());
                wallpaperDirectory.mkdirs();
            }

            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            String fileName=currentDateTimeString+System.currentTimeMillis();
            File file = new File(new File("/sdcard/"+addresses.get(0).getLocality()), fileName+".PNG");
            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        params.setPreviewSize(selected.width,selected.height);
        mCamera.setParameters(params);

        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("PREVIEW","surfaceDestroyed");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }
    @Override
    public void onFragmentInteraction(String id) {
    }
    @Override
    public void onFragmentInteraction(int actionId) {
    }
}