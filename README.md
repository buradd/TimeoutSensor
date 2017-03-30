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
	        compile 'com.github.buradd:TimeoutSensor:1.2'
	}





To use, you must be using AppCompat - and replace each Activity extention with TimeoutCompatActivity:
    
    public class MainActivity extends TimeoutCompatActivity {
    
    }


Then, start the sensor and provide the duration (in minutes.) (This must only be called once, preferably from your MainActivity's onCreate method):

    //This will start the sensor to check for 15 minutes of inactivity.
    TimeoutSensor.start(15);
   
   
That's it!
