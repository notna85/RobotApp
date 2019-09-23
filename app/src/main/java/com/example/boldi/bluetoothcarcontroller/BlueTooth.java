package com.example.boldi.bluetoothcarcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BlueTooth extends Fragment
{
    //private final String DEVICE_ADDRESS = "00:18:E4:34:CB:D3"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothSocket socket;
    static OutputStream outputStream;
    static InputStream inputStream;
    private ArrayList<String> deviceList = new ArrayList<>();
    private ArrayList<String> deviceAddress = new ArrayList<>();
    private BluetoothAdapter bluetoothAdapter;

    public BlueTooth()
    {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View BTView = inflater.inflate(R.layout.fragment_blue_tooth, container, false);

        ListView devicelistView = BTView.findViewById(R.id.device_list);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getActivity(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                deviceList.add(iterator.getName() + "\n" + iterator.getAddress());
                deviceAddress.add(iterator.getAddress());

                devicelistView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, deviceList));
            }
        }
        devicelistView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String address = deviceAddress.get(i) + "";
                BTconnect(address);
            }
        });

        return BTView;
    }
    public boolean BTconnect(String address)
    {
        boolean connected = true;
        try
        {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();
            Toast.makeText(getActivity(),"Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(),"Connection to bluetooth device failed", Toast.LENGTH_LONG).show();
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
}