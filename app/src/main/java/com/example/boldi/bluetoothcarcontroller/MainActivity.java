package com.example.boldi.bluetoothcarcontroller;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.os.Handler;
import android.os.Bundle;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    BlueTooth BT = new BlueTooth();
    protected static OutputStream outputStream;
    private InputStream inputStream;

    Button devicelist_btn, manual_btn, auto_btn, getData_btn;
    TextView robotData;

    String command; //string variable that will store value to be transmitted to the bluetooth module

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declaration of button variables
        devicelist_btn = findViewById(R.id.devicelist_btn);
        manual_btn = findViewById(R.id.manual_btn);
        auto_btn = findViewById(R.id.auto_btn);
        robotData = findViewById(R.id.robotData);
        getData_btn = findViewById(R.id.getData_btn);

        //Button that starts manual mode
        manual_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                outputStream = BT.outputStream;
                command = "M";
                if(outputStream != null)
                {
                    try
                    {
                        outputStream.write(command.getBytes());
                        outputStream.write(command.getBytes());
                        addFragment(new ManualMode(), false, "manual mode");
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please connect to a device", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Button that starts auto mode
        auto_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                outputStream = BT.outputStream;
                command = "A";
                if(outputStream != null)
                {
                    try
                    {
                        outputStream.write(command.getBytes());
                        outputStream.write(command.getBytes());
                        addFragment(new AutoMode(), false, "auto mode");
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please connect to a device", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Button that shows all the paired devices
        devicelist_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addFragment(new BlueTooth(), false, "device list");
            }
        });
        //Button that gets robot data
        getData_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                displayData();
            }
        });
    }
    public void displayData()
    {
        Handler handler = new Handler();
        inputStream = BT.inputStream;
        try
        {
            int byteCount = inputStream.available();
            byte[] rawBytes = new byte[byteCount];
            inputStream.read(rawBytes);
            final String getData = new String(rawBytes, "UTF-8");
            handler.post(new Runnable()
            {
                public void run()
                {
                    robotData.setText(getData);
                }
            });
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Get data failed", Toast.LENGTH_SHORT).show();
        }
    }
    public void addFragment(Fragment fragment, boolean addToBackStack, String tag)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack)
        {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.fragment_container, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }
}
