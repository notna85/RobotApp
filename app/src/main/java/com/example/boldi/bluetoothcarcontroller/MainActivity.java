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
    protected static InputStream inputStream;
    Handler handler = new Handler();
    String distance, cloth;

    Button devicelist_btn, manual_btn, auto_btn, getData_btn, changeCloth_btn;
    TextView DistanceData, ClothData;

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
        getData_btn = findViewById(R.id.getData_btn);
        changeCloth_btn = findViewById(R.id.changeCloth_btn);
        DistanceData = findViewById(R.id.distanceData);
        ClothData = findViewById(R.id.ClothData);


        RoomDB db = RoomDB.getDB(this);
        final RoomDAO roomDAO = db.getRoomDAO();

        roomDAO.deleteAll();

        outputStream = BT.outputStream;
        inputStream = BT.inputStream;

        //Button that starts manual mode
        manual_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                outputStream = BT.outputStream;
                inputStream = BT.inputStream;
                command = "M";
                if(outputStream != null)
                {
                    try
                    {
                        outputStream.write(command.getBytes());
                        outputStream.write(command.getBytes());
                        addFragment(new ManualMode(), false, "manual mode");
                    }
                    catch (IOException e) { e.printStackTrace(); }
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
                command = "A";
                outputStream = BT.outputStream;
                inputStream = BT.inputStream;
                if(outputStream != null)
                {
                    try
                    {
                        outputStream.write(command.getBytes());
                        outputStream.write(command.getBytes());
                        addFragment(new AutoMode(), false, "auto mode");
                    }
                    catch (IOException e) { e.printStackTrace(); }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please connect to a device", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Button that shows a list of all the paired devices
        devicelist_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addFragment(new BlueTooth(), false, "device list");
            }
        });
        //Button that gets and displays robot data
        getData_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                displayData(roomDAO);
            }
        });

        changeCloth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "C";
                try
                {
                    int distance = roomDAO.getDistance(BT.address);
                    int cloth = roomDAO.getCloth(BT.address);
                    if(cloth < distance / 10)
                    {
                        roomDAO.updateCloth(1, BT.address);
                    }
                    outputStream.write(command.getBytes());
                }
                catch (IOException e) { e.printStackTrace(); }
            }
        });
    }
    public void displayData(final RoomDAO roomDAO) //Method that queries the database and pulls the requested data
    {
        try
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    distance = Integer.toString(roomDAO.getDistance(BT.address));
                    cloth = Integer.toString(roomDAO.getCloth(BT.address));
                    handler.post(new Runnable() {
                        public void run() {
                            DistanceData.setText(distance);
                            ClothData.setText(cloth);
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }
        catch (Exception e)
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
