package com.example.individualassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Contacts.People;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int PERMISSIONS_SEND_SMS =0 ;
    private static final int GET_LOCATION=1;
    private static final int PICK_CONTACT = 1;
    //private ImageButton btn;
    private ImageButton btn;
    private TextView notice;
    LocationManager locationManager;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    String longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GET_LOCATION);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        btn = (ImageButton)findViewById(R.id.buttonsos);
        notice = (TextView)findViewById(R.id.notice);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"sadad", Toast.LENGTH_LONG);
                System.out.println("getRealTimeLocation");

                getLocation();
                //getnumber();
                //sendSMS1();

            }
        });




    }

    private void sendSMS1() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        PERMISSIONS_SEND_SMS);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_SEND_SMS: {
                String message = "I’m IM/2017/060. Please Help Me. I’m in http://maps.google.com/?q=";
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("0716332197", null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    private void getnumber() {
        Intent intent = new Intent(Intent.ACTION_PICK, Contacts.People.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getInt(c.getColumnIndexOrThrow(People.NUMBER)));
                        //
                        Toast.makeText(getApplicationContext() , name , Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    public void getLocation() {
    try {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }
    catch(SecurityException e) {
        e.printStackTrace();
    }
}

    @Override
    public void onLocationChanged(Location location) {
       setLongitude(String.valueOf(location.getLongitude()));
        setLatitude(String.valueOf(location.getLatitude()));
        sendSMS1();
        Toast.makeText(MainActivity.this,("your location is : " + location.getLatitude() + location.getLongitude()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        Toast.makeText(MainActivity.this, "Enable GPS", Toast.LENGTH_SHORT).show();
    }
}
