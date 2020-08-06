package com.vyshnav.vk.pubgmg_tool;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity" ;
    Button apply,open;
Spinner spinner;
SharedPreferences preferences;
SharedPreferences.Editor editor;



    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

  public void  initializeViews(){

      setContentView(R.layout.activity_main);
      apply=findViewById(R.id.edit);
      open=findViewById(R.id.open);
      spinner=findViewById(R.id.select);
    }
    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        initializeViews();

        if(preferences.getInt(getString(R.string.selection),0)!=0){
            spinner.setSelection(preferences.getInt(getString(R.string.selection),0));
        }else{
            spinner.setSelection(0);
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean appInstalledOrNot() {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(getString(R.string.pubg_package_name), PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void alert(String message,String title,int image){
        LayoutInflater inflater= getLayoutInflater();
        View view= inflater.inflate(R.layout.alert,null,false);
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setIcon(image);
        builder.setView(view);
        final AlertDialog alertDialog= builder.create();
        alertDialog.show();
        Button ok=view.findViewById(R.id.ok);
        TextView text= view.findViewById(R.id.msg);
        text.setText(message);
        Linkify.addLinks(text,Linkify.EMAIL_ADDRESSES);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }



    public void replace(String quality,String devicepath){

        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
       // String rarPath = "Quality/Quality540p/UserCustom.ini";

        try {
                in = assetManager.open(quality);
                File outFile = new File(Environment.getExternalStorageDirectory(),devicepath);

                if(outFile.exists()){


                out = new FileOutputStream(outFile);
                copyFile(in, out);
                }else{
                    alert(getString(R.string.file_error_message),getString(R.string.error),R.mipmap.ic_failed);
                }

            } catch(IOException e) {
                Log.e(TAG, getString(R.string.failed_to_copy_asset_file) + quality, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                    }
                }
            }

    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
alert(getString(R.string.process_compeleted),getString(R.string.success),R.mipmap.ic_complete);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences=getSharedPreferences(getString(R.string.settings),MODE_PRIVATE);
        editor=preferences.edit();
        super.onCreate(savedInstanceState);

        if(Objects.equals(preferences.getString("language", ""), "")){
            editor.putString("language","english").apply();
            setLocale("en");
        }else {
            if(Objects.equals(preferences.getString("language", ""), "english")){
                setLocale("en");
            }else {
                setLocale("ml");
            }
        }
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 100);
        }



         final String PATH =getString(R.string.file_path);




apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*if(spinner.getSelectedItem().toString().equals(getString(R.string.select_quality))){*/
                if(spinner.getSelectedItemPosition()==0){

    alert(getString(R.string.select_a_qality_first),getString(R.string.warning),R.mipmap.ic_failed);
editor.putInt(getString(R.string.selection),0);
}else if(spinner.getSelectedItem().toString().equals(getString(R.string.q_480_40fps_mid))){
    replace(getString(R.string.mid_480p),PATH);
    editor.putInt(getString(R.string.selection),1);
}
else if(spinner.getSelectedItem().toString().equals("480p 40fps")){
    replace(getString(R.string.q_480p),PATH);
    editor.putInt(getString(R.string.selection),2);
}
else if(spinner.getSelectedItem().toString().equals("540p 60fps mid")){
    replace(getString(R.string.mid_540p),PATH);
    editor.putInt(getString(R.string.selection),3);
}
else if(spinner.getSelectedItem().toString().equals("540p 60fps")){
    replace(getString(R.string.q_540p),PATH);
    editor.putInt(getString(R.string.selection),4);
}
else if(spinner.getSelectedItem().toString().equals("720p 60fps mid")){
    replace(getString(R.string.mid_720p),PATH);
    editor.putInt(getString(R.string.selection),5);
}
else if(spinner.getSelectedItem().toString().equals("720p 60fps")){
    replace(getString(R.string.q_720p),PATH);
    editor.putInt(getString(R.string.selection),6);
}



editor.apply();

 }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAppInstalled = appInstalledOrNot();
                if(isAppInstalled) {

                    Intent LaunchIntent = getPackageManager()
                            .getLaunchIntentForPackage(getString(R.string.pubg_package_name));
                    startActivity(LaunchIntent);


                } else {


                    alert(getString(R.string.pubg_not_installed),getString(R.string.warning),R.mipmap.ic_failed);
                }
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.about){
alert(getString(R.string.about),getString(R.string.about_text),R.drawable.pubgicon);
        }else if(item.getItemId()==R.id.english){
            editor.putString("language","english").apply();
            setLocale("en");
        }else if(item.getItemId()==R.id.malayalam){
            editor.putString("language","malayalam").apply();
        setLocale("ml");
        }
        return super.onOptionsItemSelected(item);
    }
}
