package com.example.boldi.bluetoothcarcontroller;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.os.Handler;

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
    private OutputStream outputStream;
    private InputStream inputStream;

    Button forward_btn, forward_left_btn, forward_right_btn, reverse_btn, bluetooth_connect_btn,manual_btn, auto_btn, go_btn, stop_btn, main_btn, getData_btn;
    TextView robotData;

    String command; //string variable that will store value to be transmitted to the bluetooth module

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declaration of button variables
        forward_btn = findViewById(R.id.forward_btn);
        forward_left_btn = findViewById(R.id.left_btn);
        forward_right_btn = findViewById(R.id.right_btn);
        reverse_btn = findViewById(R.id.reverse_btn);
        bluetooth_connect_btn = findViewById(R.id.bluetooth_connect_btn);
        manual_btn = findViewById(R.id.manual_btn);
        auto_btn = findViewById(R.id.auto_btn);
        go_btn = findViewById(R.id.go_btn);
        stop_btn = findViewById(R.id.stop_btn);
        main_btn = findViewById(R.id.main_btn);
        robotData = findViewById(R.id.robotData);
        getData_btn = findViewById(R.id.getData_btn);

        //OnTouchListener code for the forward button (button long press)
        forward_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = "F";
                    try
                    {
                        outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = "P";
                    try
                    {
                        outputStream.write(command.getBytes());
                        displayData();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        //OnTouchListener code for the reverse button (button long press)
        reverse_btn.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    command = "B";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "P";
                    try
                    {
                        outputStream.write(command.getBytes());
                        displayData();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        //OnTouchListener code for the forward left button (button long press)
        forward_left_btn.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    command = "L";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "P";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        //OnTouchListener code for the forward right button (button long press)
        forward_right_btn.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    command = "R";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "P";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        //Button that returns to main menu
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command = "Q";
                try
                {
                    outputStream.write(command.getBytes());
                    Toast.makeText(getApplicationContext(), "Going to main menu", Toast.LENGTH_SHORT).show();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        //Button that starts manual mode
        manual_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command = "M";
                try
                {
                    outputStream.write(command.getBytes());
                    Toast.makeText(getApplicationContext(), "Manual mode activated", Toast.LENGTH_SHORT).show();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Manual mode failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Button that starts auto mode
        auto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command = "A";
                try
                {
                    outputStream.write(command.getBytes());
                    Toast.makeText(getApplicationContext(), "Auto mode activated", Toast.LENGTH_SHORT).show();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Auto mode failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Button that goes
        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command = "1";
                try
                {
                    outputStream.write(command.getBytes());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        //Button that stops
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command = "0";
                try
                {
                    outputStream.write(command.getBytes());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        //Button that connects the device to the bluetooth module when pressed
        bluetooth_connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BTinit())
                {
                    BTconnect();
                }
            }
        });
        //Button that gets robot data
        getData_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            handler.post(new Runnable() {
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
    @Override
    protected void onStart()
    {
        super.onStart();
    }
}
