package com.example.boldi.bluetoothcarcontroller;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
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
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private final String DEVICE_ADDRESS = "00:18:E4:34:CB:D3"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    protected static OutputStream outputStream;
    private InputStream inputStream;

    Button bluetooth_connect_btn, manual_btn, auto_btn, go_btn, stop_btn, getData_btn;
    TextView robotData;

    String command; //string variable that will store value to be transmitted to the bluetooth module

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declaration of button variables
        bluetooth_connect_btn = findViewById(R.id.bluetooth_connect_btn);
        manual_btn = findViewById(R.id.manual_btn);
        auto_btn = findViewById(R.id.auto_btn);
        go_btn = findViewById(R.id.go_btn);
        stop_btn = findViewById(R.id.stop_btn);
        robotData = findViewById(R.id.robotData);
        getData_btn = findViewById(R.id.getData_btn);

        //Button that starts manual mode
        manual_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                command = "M";
                try
                {
                    outputStream.write(command.getBytes());
                    outputStream.write(command.getBytes());
                    addFragment(new ManualMode(),false,"manual mode");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
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
                try
                {
                    outputStream.write(command.getBytes());
                    outputStream.write(command.getBytes());
                    addFragment(new AutoMode(),false,"auto mode");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        //Button that connects the device to the bluetooth module when pressed
        bluetooth_connect_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(BTinit())
                {
                    BTconnect();
                }
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
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Get data failed", Toast.LENGTH_SHORT).show();
        }
    }
    //Initializes bluetooth module
    public boolean BTinit()
    {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    Toast.makeText(getApplicationContext(),"Found device!", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
        return found;
    }
    public boolean BTconnect()
    {
        boolean connected = true;
        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();
            Toast.makeText(getApplicationContext(),"Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Connection to bluetooth device failed", Toast.LENGTH_LONG).show();
            connected = false;
        }
        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
                inputStream = socket.getInputStream();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return connected;
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
