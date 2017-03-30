# TimeoutSensor
Timeout Sensor for Android Applications

Make sure to add to your gradle:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    
    
    	dependencies {
	        compile 'com.github.buradd:TimeoutSensor:1.0'
	}





To use, you must be using AppCompat - and replace each Activity extention with TimeoutCompatActivity:
    
    public class MainActivity extends TimeoutCompatActivity {
    
    }


Then, set the timeout duration (in minutes) and start. (This must only be called once, preferably from your MainActivity's onCreate method:

    TimeoutSensor.setTimeoutDuration(5);
    TimeoutSensor.start();
   
   
That's it!
