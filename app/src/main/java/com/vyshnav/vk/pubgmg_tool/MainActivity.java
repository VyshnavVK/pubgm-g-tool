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
import android.os.Bundle;
import android.os.Environment;
import android.text.util.Linkify;
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

public class MainActivity extends AppCompatActivity {
Button apply,open;
Spinner spinner;
SharedPreferences preferences;
SharedPreferences.Editor editor;
public static final String PATH="Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/UserCustom.ini";



    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
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



    public void replace(String qulity,String divicepath){

        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
       // String rarPath = "Quality/Quality540p/UserCustom.ini";
        String rarPath=qulity;

            try {
                in = assetManager.open(rarPath);
                File outFile = new File(Environment.getExternalStorageDirectory(),divicepath);

                if(outFile.exists()){


                out = new FileOutputStream(outFile);
                copyFile(in, out);
                }else{
                    alert("An error occurred while executing the program ERROR_CODE :FNFE","Error !",R.mipmap.ic_failed);
                }

            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + rarPath, e);
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
alert("Process completed !","Success",R.mipmap.ic_complete);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apply=findViewById(R.id.edit);
        open=findViewById(R.id.open);
        spinner=findViewById(R.id.select);
        preferences=getSharedPreferences("settings",MODE_PRIVATE);
        editor=preferences.edit();
if(preferences.getInt("selection",0)!=0){
    spinner.setSelection(preferences.getInt("selection",0));

}else{
    spinner.setSelection(0);
}

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 100);
        }
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(spinner.getSelectedItem().toString().equals("Select Quality")){

    alert("Select a quality first !","Warning !",R.mipmap.ic_failed);
editor.putInt("selection",0);
}else if(spinner.getSelectedItem().toString().equals("480p 40fps mid")){
    replace("Quality/Quality480pMid/UserCustom.ini",PATH);
    editor.putInt("selection",1);
}
else if(spinner.getSelectedItem().toString().equals("480p 40fps")){
    replace("Quality/Quality480p/UserCustom.ini",PATH);
    editor.putInt("selection",2);
}
else if(spinner.getSelectedItem().toString().equals("540p 60fps mid")){
    replace("Quality/Quality540pMid/UserCustom.ini",PATH);
    editor.putInt("selection",3);
}
else if(spinner.getSelectedItem().toString().equals("540p 60fps")){
    replace("Quality/Quality540p/UserCustom.ini",PATH);
    editor.putInt("selection",4);
}
else if(spinner.getSelectedItem().toString().equals("720p 60fps mid")){
    replace("Quality/Quality720pMid/UserCustom.ini",PATH);
    editor.putInt("selection",5);
}
else if(spinner.getSelectedItem().toString().equals("720p 60fps")){
    replace("Quality/Quality720p/UserCustom.ini",PATH);
    editor.putInt("selection",6);
}



editor.apply();

 }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAppInstalled = appInstalledOrNot("com.tencent.ig");
                if(isAppInstalled) {

                    Intent LaunchIntent = getPackageManager()
                            .getLaunchIntentForPackage("com.tencent.ig");
                    startActivity(LaunchIntent);


                } else {


                    alert("Didn't Find PUBG Mobile in your phone","Warning !",R.mipmap.ic_failed);
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
alert("This app is developed by Vyshnav.K and \n " +
        "this app is for educational purpose only! use it at your own risk. \n" +
        "You can contact me and ask questions at \n"+
        "Email : vyshnavvyshu20@gmail.com \n" +
        "thanks! for using my app","About",R.drawable.pubgicon);
        }
        return super.onOptionsItemSelected(item);
    }
}
