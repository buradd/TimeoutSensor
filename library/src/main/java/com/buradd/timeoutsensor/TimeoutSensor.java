package com.buradd.timeoutsensor;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public final class TimeoutSensor {

    private static Activity mCurrentActivity = null;
    public static long timeoutDuration = 1;
    public static TimeoutSensorTask timeoutSensorTask;

    public static void start(int timeDuration){
        stop();
        timeoutDuration = timeDuration;        
        timeoutSensorTask = new TimeoutSensorTask();
        timeoutSensorTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, timeoutDuration*60*1000);
    }

    public static void start(){
        stop();
        timeoutSensorTask = new TimeoutSensorTask();
        timeoutSensorTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, timeoutDuration*60*1000);
    }

    public static void setTimeoutDuration(int timeDuration){
        timeoutDuration = timeDuration;
    }

    public static long getTimeoutDuration(){
        return timeoutDuration;
    }

    public static void stop(){
        if(timeoutSensorTask != null){
            timeoutSensorTask.cancel(true);
        }
    }

    public static void setCurrentActivity(Activity activity){
        mCurrentActivity = activity;
    }

    public static Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    
    public static class TimeoutCompatActivity extends AppCompatActivity {
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setCurrentActivity(this);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
        }

        @Override
        protected void onPause() {
            super.onPause();
        }

        @Override
        protected void onResume() {
            super.onResume();
            setCurrentActivity(this);
        }

        @Override
        public void onUserInteraction() {
            super.onUserInteraction();
            TimeoutSensorTask.touch();
        }
    }

    public static class TimeoutSensorTask extends AsyncTask<Long, Void, Void> {

        private static final String TAG = TimeoutSensorTask.class.getName();
        private static long lastUsed;
        PowerManager pm;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pm = (PowerManager) mCurrentActivity.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        }

        @Override
        protected Void doInBackground(Long... params) {
            long idle = 0;
            this.touch();
            while(!isCancelled()){
                idle = System.currentTimeMillis() - lastUsed;
                try{
                    Thread.sleep(1000); //check every 1 seconds
                }catch (InterruptedException e){
                    Log.d(TAG, "TimeoutSensor interrupted!");
                }
                if(idle > params[0]){
                    idle = 0;
                    this.cancel(true);
                    if(pm.isScreenOn()) {
                        SessionExpiringDialog sessionExpiringDialog = new SessionExpiringDialog();
                        sessionExpiringDialog.setCancelable(false);
                        sessionExpiringDialog.show(((AppCompatActivity) mCurrentActivity).getSupportFragmentManager(), "tag");
                    }else{
                        if(mCurrentActivity != null) {
                            mCurrentActivity.finishAffinity();
                        }
                        System.exit(0);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public static synchronized void touch(){
            lastUsed = System.currentTimeMillis();
        }

    }

    public static class SessionExpiringDialog extends DialogFragment {
        CountDownTimer countDownTimer;
        public SessionExpiringDialog() {
            super();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.session_dialog, null);
            final TextView sessionText = (TextView) view.findViewById(R.id.session_text);
            countDownTimer = new CountDownTimer(31000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long diffSeconds = millisUntilFinished / 1000 % 60;
                    sessionText.setText("Session will expire in: " + diffSeconds + " seconds.");
                }

                @Override
                public void onFinish() {
                    if(mCurrentActivity != null) {
                        mCurrentActivity.finishAffinity();
                    }
                    System.exit(0);
                }
            };
            countDownTimer.start();
            builder.setView(view);
            builder.setMessage("Your session is about to expire.").setTitle("ATTENTION:")
                    .setPositiveButton("STAY SIGNED IN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            countDownTimer.cancel();
                            timeoutSensorTask = new TimeoutSensorTask();
                            timeoutSensorTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, timeoutDuration*60*1000);
                            dismiss();
                        }
                    });
            return builder.create();
        }
    }
}
