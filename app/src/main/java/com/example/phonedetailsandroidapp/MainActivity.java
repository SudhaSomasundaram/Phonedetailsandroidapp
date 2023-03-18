package com.example.phonedetailsandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;



import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class MainActivity extends AppCompatActivity {
    public Button buttonclick,buttondb;
    TextView tvphdet,ChargingState;
    public String strimeinumber,timeStamp,dateString,batstatus;
    // private static final int REQUEST_CODE = 101;
    private BroadcastReceiver MyReceiver = null;
   DatabaseReference ref;
   MemberData member;

   public  String deviceid;
    public String allstr;
    public int level;

    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation,buttoncapt;
    private TextView AddressText;
    private Button LocationButton;
    private LocationRequest locationRequest;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        tvphdet = findViewById(R.id.phdet);

        tvphdet.setTextColor(Color.BLUE);
        imageView = (ImageView) this.findViewById(R.id.imageView1);
        ref = FirebaseDatabase.getInstance().getReference().child("Member");
        member = new MemberData();

        buttoncapt=findViewById(R.id.btncapture);

        buttoncapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });



        buttonclick=findViewById(R.id.btnclick);
        deviceid= Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        buttonclick.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                allstr="Phone Details" +   System.getProperty("line.separator") +System.getProperty("line.separator") + "Device Id is " + deviceid;
                getBatteryPercentage();
                if(isPhonePluggedIn(getApplicationContext()).compareToIgnoreCase("yes")== 0)
                {
                    // ChargingState.setText("Battery is Charging");
                    batstatus="Battery is Charging";
                    allstr=allstr+"\n" + "Battery is Charging";

                }
                else
                {
                    // ChargingState.setText("Battery is not Charging");
                    batstatus="Battery is not Charging";
                    allstr=allstr+"\n" + "Battery is not Charging";

                }
              //  firebaseins();


            }

        });



        buttondb=findViewById(R.id.btndb);
        buttondb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (allstr=="")
                {
                    Toast.makeText(MainActivity.this, "Sorry ! No Data", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    String M_devid = deviceid;
                    int M_batlev = level;
                    String M_timestamp =timeStamp.trim();
                    String M_currdate = dateString.trim();

                    String batterystatus=batstatus.trim();

                    member.setfbphdevid(M_devid);
                    member.setfbphchrgestatus(batterystatus);
                    member.setfbphcharglevel(M_batlev);
                    member.setfbtimestamp(M_timestamp);
                    member.setfbcurrdate( M_currdate);
                    ref.push().setValue(member);
                    Toast.makeText(MainActivity.this, "Data Inserted successfully to Firebase", Toast.LENGTH_SHORT).show();
                }
          }
        });



    }



    private void getBatteryPercentage() {


        BroadcastReceiver batteryLevel = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                }

                allstr=allstr+"\n" + "Battery Charge Level " +level + " %";
                timeStamp=String.valueOf(System.currentTimeMillis());
                allstr=allstr+"\n" + "TimeStamp is " +timeStamp;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                dateString = formatter.format(new Date(Long.parseLong(timeStamp)));
                allstr=allstr+"\n" + "Timeformat is " +dateString;
                tvphdet.setText(allstr);



            }
        };


        IntentFilter batteryLevelFilter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevel, batteryLevelFilter);

    }

    public static String isPhonePluggedIn(Context context) {
        boolean charging = false;
        String result = "No";
        final Intent batteryIntent = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status == BatteryManager.BATTERY_STATUS_CHARGING;

        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (batteryCharge)
            charging = true;
        if (usbCharge)
            charging = true;
        if (acCharge)
            charging = true;

        if (charging) {
            result = "Yes";

        }
        return result;

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    public void firebaseins()

    {
        Toast.makeText(MainActivity.this, "Inside function", Toast.LENGTH_SHORT).show();
        if (allstr=="")
        {
            Toast.makeText(MainActivity.this, "Sorry ! No Data", Toast.LENGTH_SHORT).show();
        }
        else
        {

            String M_devid = deviceid;
            int M_batlev = level;
            String M_timestamp =timeStamp.trim();
            String M_currdate = dateString.trim();

            String batterystatus=batstatus.trim();

            member.setfbphdevid(M_devid);
            member.setfbphchrgestatus(batterystatus);
            member.setfbphcharglevel(M_batlev);
            member.setfbtimestamp(M_timestamp);
            member.setfbcurrdate( M_currdate);
            ref.push().setValue(member);
            Toast.makeText(MainActivity.this, "Data Inserted successfully to Firebase", Toast.LENGTH_SHORT).show();
        }
    }


}