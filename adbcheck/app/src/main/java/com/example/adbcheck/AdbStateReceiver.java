package com.example.adbcheck;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AdbStateReceiver extends BroadcastReceiver {
    private final TextView adbStatusTextView;
    private final Button btnCheck;
    public TextView checkProp1;
    public TextView checkProp2;
    public TextView checkProp3;
    public TextView checkProp4;
    public TextView checkProp5;

    public AdbStateReceiver(TextView textView, Button button, TextView checkProp1,
                            TextView checkProp2, TextView checkProp3, TextView checkProp4, TextView checkProp5) {
        this.adbStatusTextView = textView;
        this.btnCheck = button;
        this.checkProp1 = checkProp1;
        this.checkProp2 = checkProp2;
        this.checkProp3 = checkProp3;
        this.checkProp4 = checkProp4;
        this.checkProp5 = checkProp5;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdbEnabled = intent.getBooleanExtra("adb", false);
                boolean isUsbEnabled = intent.getBooleanExtra("mtp", false);
                boolean isUsbEnabled2 = intent.getBooleanExtra("connected", false);
                String check1 = getProp("getprop init.svc.adbd");
                String check2 = getProp("getprop persist.sys.usb.config");
                String check3 = getProp("getprop init.svc.usbd");
                String check4 = getProp("getprop sys.usb.config");
                String check5 = getProp("getprop sys.usb.state");

                checkProp1.setText("init.svc.adbd: " + check1);
                checkProp2.setText("persist.sys.usb.config: " + check2);
                checkProp3.setText("init.svc.usbd: " + check3);
                checkProp4.setText("sys.usb.config: " + check4);
                checkProp5.setText("sys.usb.state: " + check5
                                + " isAdbEnabled : " + (isAdbEnabled ? "true" : "false")
                                + " isUsbEnabled: " + (isUsbEnabled ? "true" : "false")
                                + " isUsbEnabled2 :" + (isUsbEnabled2 ? "true" : "false"));
                if (isAdbEnabled || isUsbEnabled || isUsbEnabled2) {
                    adbStatusTextView.setText("ADB Debugging is ENABLED");
                } else {
                    adbStatusTextView.setText("ADB Debugging is DISABLED");
                }
            }
        });
    }

    private boolean checkProcess(String prop){
        try{
            return prop.contains("adb");
        }catch (Exception e){
            return  false;
        }
    }

    private String getProp(String command){
        try {
            Process process = new ProcessBuilder("sh", "-c",command).start();
//            Process process = new ProcessBuilder("sh", "-c","getprop | grep adb").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            reader.close();
            process.waitFor();

            return output.toString();
        }catch (Exception exception){
            return "";
        }
    }
}
