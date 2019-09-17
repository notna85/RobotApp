package com.example.boldi.bluetoothcarcontroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.OutputStream;

public class AutoMode extends Fragment
{
    MainActivity MA = new MainActivity();
    OutputStream outputStream = MA.outputStream;
    Button go_btn, stop_btn;
    String command;

    public AutoMode()
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
        View autoView = inflater.inflate(R.layout.fragment_auto_mode, container, false);

        go_btn = autoView.findViewById(R.id.go_btn);
        stop_btn = autoView.findViewById(R.id.stop_btn);

        //Button that goes
        go_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
        stop_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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

        return autoView;
    }
}