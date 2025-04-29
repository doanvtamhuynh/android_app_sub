package com.example.adbcheck;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView adbStatusTextView;
    private AdbStateReceiver adbStateReceiver;
    private boolean isReceiverRegistered = false;
    private Button btnCheck;
    private TextView checkProp1;
    private TextView checkProp2;
    private TextView checkProp3;
    private TextView checkProp4;
    private TextView checkProp5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adbStatusTextView = findViewById(R.id.adbStatusTextView);
        btnCheck = findViewById(R.id.btnCheck);
        checkProp1 = findViewById(R.id.checkProp1);
        checkProp2 = findViewById(R.id.checkProp2);
        checkProp3 = findViewById(R.id.checkProp3);
        checkProp4 = findViewById(R.id.checkProp4);
        checkProp5 = findViewById(R.id.checkProp5);
        adbStateReceiver = new AdbStateReceiver(adbStatusTextView, btnCheck, checkProp1,
                                                checkProp2, checkProp3, checkProp4, checkProp5);

        IntentFilter filter = new IntentFilter("android.hardware.usb.action.USB_STATE");
        registerReceiver(adbStateReceiver, filter);
        isReceiverRegistered = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isReceiverRegistered) {
            unregisterReceiver(adbStateReceiver);
            isReceiverRegistered = false;
        }
    }
}
