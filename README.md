# TimeoutSensor
Timeout Sensor for Android Applications

To use, you must be using AppCompat - and replace each Activity extention with TimeoutCompatActivity:
    
    public class MainActivity extends TimeoutCompatActivity {
    
    }


Then, set the timeout duration (in minutes) and start. (This must only be called once, preferably from your MainActivity's onCreate method:

    TimeoutSensor.setTimeoutDuration(5);
    TimeoutSensor.start();
   
   
That's it!
