package com.debajyotibasak.services.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by debajyotibasak on 04/03/18.
 */

public class MyIntentService extends IntentService {

    private static final String TAG = MyIntentService.class.getSimpleName();

    public MyIntentService() {
        super("MyWorkerThread");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate, Thread Name " + Thread.currentThread().getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent, Thread Name " + Thread.currentThread().getName());
        int sleepTime = intent.getIntExtra("Sleep Time", 1);

        ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");


        int counter = 1;

        while(counter <= sleepTime){
            Log.d(TAG, "Counter : " + counter);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            counter++;
        }

        Bundle bundle = new Bundle();
        bundle.putString("resultIntentService", "Counter Stopped at " + counter + " seconds.");
        resultReceiver.send(18, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy, Thread Name " + Thread.currentThread().getName());
    }
}
