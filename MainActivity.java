package com.example.andro.imr;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {
    ArrayList <String> n=new ArrayList();
    List<ScanResult> a;
    WifiManager c;
    String h="";
    Process process;
    NotificationManager k;
    static int i=0;
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
       h=a.get(position).BSSID;
        k.notify(i,new NotificationCompat.Builder(this).setDefaults(Notification.DEFAULT_ALL).setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(a.get(position).SSID).setContentText(h).build());i++;
                d.setEnabled(true);
        Toast.makeText(this,h, Toast.LENGTH_LONG).show();
        super.onListItemClick(l, v, position, id);
    }
Button s,d;
ListView q;
Switch v;
void scan(){
    try {
    n.clear();
    c.startScan();
    a= c.getScanResults();
    for(ScanResult scanResult:a ){n.add(scanResult.SSID);}
    setListAdapter(new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,n));}
    catch (Exception e){
        Toast.makeText(this, "Please wait ...", Toast.LENGTH_SHORT).show();
    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c =(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        q=findViewById(android.R.id.list);
        k=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      s=findViewById(R.id.s);
      d=findViewById(R.id.d);
      v=findViewById(R.id.switch1);
      d.setEnabled(false);
        if(c.isWifiEnabled()){v.setChecked(true); scan();}
        else {v.setChecked(false);}
      v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
           c.setWifiEnabled(v.isChecked());
                n.clear();
                setListAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_expandable_list_item_1, n));

          }
      });
      s.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              scan();
          }
      });
d.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        try {
            process=Runtime.getRuntime().exec("su");
            process.getOutputStream().write("rm efs/wifi/.mac.info\n".getBytes());
            process.getOutputStream().write(("echo "+h.toUpperCase()+" >> efs/wifi/.mac.info\n").getBytes());
            new AlertDialog.Builder(MainActivity.this).setMessage("You need restart phone").setTitle("Restart")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                process.getOutputStream().write("reboot\n".getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("No",null).create().show();

        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
});}
}
