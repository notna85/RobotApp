package com.example.boldi.bluetoothcarcontroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.MotionEvent;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ManualMode extends Fragment
{
    BlueTooth BT = new BlueTooth();
    static OutputStream outputStream;
    static InputStream inputStream;
    Button forward_btn, forward_left_btn, forward_right_btn, reverse_btn;
    String command;
    String currentDistance;
    String clothChange;

    // Required empty public constructor
    public ManualMode() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View manualView = inflater.inflate(R.layout.fragment_manual_mode, container, false);

        forward_btn = manualView.findViewById(R.id.forward_btn);
        forward_left_btn = manualView.findViewById(R.id.left_btn);
        forward_right_btn = manualView.findViewById(R.id.right_btn);
        reverse_btn = manualView.findViewById(R.id.reverse_btn);

        outputStream = BT.outputStream;
        inputStream = BT.inputStream;

        RoomDB db = RoomDB.getDB(getContext());
        final RoomDAO roomDAO = db.getRoomDAO();

        //OnTouchListener code for the forward button (button long press)
        forward_btn.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = "F";
                    try
                    {
                        outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    }
                    catch (IOException e) { e.printStackTrace(); }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = "P";
                    try
                    {
                        outputStream.write(command.getBytes());
                        outputStream.write(command.getBytes());
                        sendDataToDB(roomDAO);
                    }
                    catch(Exception e) { e.printStackTrace(); }
                }
                return false;
            }
        });
        //OnTouchListener code for the reverse button (button long press)
        reverse_btn.setOnTouchListener(new View.OnTouchListener()
        {
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
                    catch (IOException e) { e.printStackTrace(); }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "P";
                    try
                    {
                        outputStream.write(command.getBytes());
                        outputStream.write(command.getBytes());
                        sendDataToDB(roomDAO);
                    }
                    catch(IOException e) { e.printStackTrace(); }

                }
                return false;
            }
        });
        //OnTouchListener code for the forward left button (button long press)
        forward_left_btn.setOnTouchListener(new View.OnTouchListener()
        {
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
                    catch (IOException e) { e.printStackTrace(); }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "P";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e) { e.printStackTrace(); }
                }
                return false;
            }
        });
        //OnTouchListener code for the forward right button (button long press)
        forward_right_btn.setOnTouchListener(new View.OnTouchListener()
        {
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
                    catch (IOException e) { e.printStackTrace(); }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "P";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e) { e.printStackTrace(); }
                }
                return false;
            }
        });

        return manualView;
    }
    public void sendDataToDB(final RoomDAO roomDAO)
    {
        try
        {
            Thread.sleep(100); //A slight pause to give the app time to receive the full inputStream
            int byteCount = inputStream.available();
            byte[] rawBytes = new byte[byteCount];
            inputStream.read(rawBytes);
            int distance = Integer.parseInt(new String(rawBytes, "UTF-8"));
            roomDAO.updateDistance(distance, BT.address);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}