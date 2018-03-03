package com.debajyotibasak.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by debajyotibasak on 03/03/18.
 */

public class MyStartedService extends Service {

    private static final String TAG = MyStartedService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate, Thread Name " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand, Thread Name " + Thread.currentThread().getName());

        int sleepTime = intent.getIntExtra("Sleep Time", 1);

        new MyAsynTask().execute(sleepTime);

        return START_STICKY;
        /*
        * You can use START_STICKY/START_REDELIVER_INTENT/START_NOT_STICKY instead of return super.
        * return super.onStartCommand(intent, flags, startId);
        * */
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind, Thread Name " + Thread.currentThread().getName());
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy, Thread Name " + Thread.currentThread().getName());
        super.onDestroy();
    }

    class MyAsynTask extends AsyncTask<Integer, String, String> {

        private final String TAG = MyAsynTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute, Thread Name " + Thread.currentThread().getName());
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(MyStartedService.this, values[0], Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Counter value => " + values[0] + " onProgressUpdate, Thread Name " + Thread.currentThread().getName());
        }

        @Override
        protected String doInBackground(Integer... params) {
            Log.d(TAG, "doInBackground, Thread Name " + Thread.currentThread().getName());

            int sleepTime = params[0];
            int counter = 1;

            while(counter <= sleepTime){
                publishProgress("Counter : " + counter);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                counter++;
            }

            return "Counter stopped at " + counter + " seconds";
        }

        @Override
        protected void onPostExecute(String value) {
            Log.d(TAG, " onPostExecute, Thread Name " + Thread.currentThread().getName());
            super.onPostExecute(value);
            stopSelf();

            Intent intent = new Intent("action.service.to.activity");
            intent.putExtra("startServiceResult", value);
            sendBroadcast(intent);
        }
    }
}
