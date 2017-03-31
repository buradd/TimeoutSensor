package com.buradd.timeoutsample;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.buradd.timeoutsensor.TimeoutSensor;
import com.buradd.timeoutsensor.TimeoutSensor.TimeoutCompatActivity;

public class TimeoutSample extends TimeoutCompatActivity {

    Button btnStartStop;
    Button btnGetDuration;
    Button btnSetDuration;
    EditText etDuration;
    TextView tvDuration;
    TextView tvStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_sample);


        //set durations in minutes
        TimeoutSensor.setTimeoutDuration(1);

        //start the sensor
        TimeoutSensor.start();


        //You can also simply call:
        //TimeoutSensor.start(1); where the argument passed is the duration in minutes.


        btnStartStop = (Button) findViewById(R.id.button3);
        btnGetDuration = (Button) findViewById(R.id.button2);
        btnSetDuration = (Button) findViewById(R.id.button1);
        etDuration = (EditText) findViewById(R.id.editText1);
        tvDuration = (TextView) findViewById(R.id.textView1);
        tvStatus = (TextView) findViewById(R.id.textView2);


        if(TimeoutSensor.timeoutSensorTask != null){
            tvStatus.setText(TimeoutSensor.timeoutSensorTask.isCancelled() ? "Status: CANCELLED" : "Status: RUNNING");
            tvDuration.setText("Current duration: " + String.valueOf(TimeoutSensor.getTimeoutDuration()));
            etDuration.setText(String.valueOf(TimeoutSensor.getTimeoutDuration()));
            btnStartStop.setText(TimeoutSensor.timeoutSensorTask.isCancelled() ? "START" : "STOP");
        }

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TimeoutSensor.timeoutSensorTask != null){
                    if(btnStartStop.getText().equals("START")){
                        TimeoutSensor.start();
                        btnStartStop.setText(TimeoutSensor.timeoutSensorTask.isCancelled() ? "START" : "STOP");
                        tvStatus.setText(TimeoutSensor.timeoutSensorTask.isCancelled() ? "Status: CANCELLED" : "Status: RUNNING");
                    }else{
                        TimeoutSensor.stop();
                        btnStartStop.setText(TimeoutSensor.timeoutSensorTask.isCancelled() ? "START" : "STOP");
                        tvStatus.setText(TimeoutSensor.timeoutSensorTask.isCancelled() ? "Status: CANCELLED" : "Status: RUNNING");
                    }
                }
            }
        });

        btnSetDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etDuration.getText().toString().isEmpty()){
                    TimeoutSensor.setTimeoutDuration(Integer.parseInt(etDuration.getText().toString()));
                    TimeoutSensor.stop();
                    TimeoutSensor.start();
                    tvStatus.setText(TimeoutSensor.timeoutSensorTask.isCancelled() ? "Status: CANCELLED" : "Status: RUNNING");
                    btnStartStop.setText(TimeoutSensor.timeoutSensorTask.isCancelled() ? "START" : "STOP");
                }
            }
        });

        btnGetDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TimeoutSensor.timeoutSensorTask != null) {
                    tvDuration.setText("Current duration: " + String.valueOf(TimeoutSensor.getTimeoutDuration()));
                }
            }
        });

    }
}
