package com.example.jlm.ecomm_experiment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
*/


// NOTE: REMOVE THE TOASTS THAT LOAD WITH THE APP

    public ArrayList<String> jokes;
    public TextView txtview;
    public SharedPreferences prefs;
    public static BufferedWriter out;
    //private boolean conversion = false;
    private int connected = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jokes = new ArrayList<>(); //14 jokes total
        jokes.add("I lost my job at the bank on my very first day. – A woman asked me to check her balance, so I pushed her over.");
        jokes.add("I tried to sue the airport for misplacing my luggage. – I lost my case.");
        jokes.add("Googled “How to start a Wildfire”. Got 48,500 matches.");
        jokes.add("What's a pirate's favorite letter? You'd think it's the \"R\" but actually it's the \"C\"");
        jokes.add("I’ve just been fired from the clock making factory after all those extra hours I put in.");
        jokes.add("Claustrophobic people are more productive thinking out of the box.");
        jokes.add("Did you hear about the restaurant on the moon? Great food, no atmosphere.");
        jokes.add("The shovel was a ground-breaking invention.");
        jokes.add("My boss told me to have a good day.. so I went home.");
        jokes.add("This graveyard looks overcrowded. People must be dying to get in there.");
        jokes.add("Two goldfish are in a tank. One says to the other, \"do you know how to drive this thing?\"");
        jokes.add("I don’t play football because I enjoy the sport. I’m just doing it for kicks.");
        jokes.add("Why did the old man fall in the well? Because he couldn't see that well.");
        jokes.add("What did one hat say to the other? You stay here. I’ll go on ahead.");
        jokes.add("This app");


        prefs = this.getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, ExpInfo.class);
                Main2Activity.this.startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        chkStatus();

        logJokeDay();

        displayJoke();

    }




    public void logJokeDay(){
        Date cDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String joke_log = this.prefs.getString("jokenum", null);
        String date_log = this.prefs.getString("date", null);
        Toast.makeText(Main2Activity.this,date_log, Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor  = this.prefs.edit();
        if (joke_log == null || date_log == null){
            joke_log = "0";
            date_log = dateFormat.format(cDate);
            editor.putString("jokenum", joke_log);
            editor.putString("date",date_log);
            editor.commit();
            Toast.makeText(Main2Activity.this,joke_log+" "+date_log, Toast.LENGTH_LONG).show();
        }
        else{
            int joke_num = Integer.parseInt(joke_log);
            int date_val_from_log = Integer.parseInt(date_log);
            int date_val = Integer.parseInt(dateFormat.format(cDate));

            //20180327
            if (date_val > date_val_from_log){
                joke_num = (joke_num >= 13)? 13 : ++joke_num;
                editor.putString("jokenum", joke_num+"");
                editor.putString("date", date_val+"");
                editor.commit();
                Toast.makeText(Main2Activity.this,"Diff day" + joke_num+" "+date_log, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(Main2Activity.this,"Same day: "+joke_log+" "+date_log, Toast.LENGTH_LONG).show();
            }

        }

    }


    public void displayJoke(){
        this.txtview = (TextView) findViewById(R.id.joke_view2);
        int joke_num = Integer.parseInt(this.prefs.getString("jokenum", null));
        joke_num = (joke_num >= 13)? 13 : joke_num;
        Toast.makeText(Main2Activity.this,"Joke Num: "+joke_num, Toast.LENGTH_LONG).show();
        this.txtview.setText(jokes.get(joke_num).toString());
    }




    private void createFileOnDevice(Boolean append, String msg) throws IOException {
        /*
                 * Function to initially create the log file and it also writes the time of creation to file.
                 */
        File Root = Environment.getExternalStorageDirectory();
        if(Root.canWrite()){
            File  LogFile = new File(Root, "exp_log.txt");
            FileWriter LogWriter = new FileWriter(LogFile, append);
            out = new BufferedWriter(LogWriter);
            //Date date = new Date();
            //out.write("Logged at" + String.valueOf(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "\n"));
            out.write(msg);
            out.close();

        }
    }


    public void writeToFile(String message) {
        try {
            out.write(message + "\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean logFileExists(){
        File Root = Environment.getExternalStorageDirectory();
        File file = new File(Root, "exp_log.txt");
        return file.exists();
    }

    //-----btns
    public void likeDislikeClick(View view) {

        try {
            logConversion(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);

    }

    public void exitClick(View view) throws IOException {

        try {
            logConversion(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);

    }




    public void logConversion(boolean convert) throws IOException {
        String joke_log = this.prefs.getString("jokenum", null);
        String date_log = this.prefs.getString("date", null);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // String date_str = (String.valueOf(d.getHours()+":"+ d.getMinutes()));
        String date_str = sdf.format(d);
        String net_status = chkStatus();
        createFileOnDevice(true, joke_log+","+date_str+","+convert+","+net_status+" \n");
        Toast.makeText(Main2Activity.this,"Response Recorded", Toast.LENGTH_LONG).show();
    }





    //-----Permissions stuff

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Main2Activity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(Main2Activity.this, "These permissions are required for this experiment. Please give permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local storage .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local storage .");
                }
                break;
        }
    }


/*
    public void chkStatus() {
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()) {
            Toast.makeText(this, "Wifi", Toast.LENGTH_LONG).show();
        } else if (mobile.isConnectedOrConnecting ()) {
            Toast.makeText(this, "Mobile 3G ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
        }
    }
*/

    public String chkStatus() {
        String str="";
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()) {
            Toast.makeText(this, "Wifi", Toast.LENGTH_LONG).show();
            return  "wifi";
        } else if (mobile.isConnectedOrConnecting ()) {
            Toast.makeText(this, "Mobile 3G ", Toast.LENGTH_LONG).show();
            return "mobile_data";
        } else {
            Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
            return "no_network";
        }
    }

    //for text commit


}
