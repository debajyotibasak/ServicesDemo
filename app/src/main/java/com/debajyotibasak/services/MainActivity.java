package com.debajyotibasak.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTxvStartedServiceResult, mTxvIntentServiceResult;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxvStartedServiceResult = findViewById(R.id.txv_started_service_result);
        mTxvIntentServiceResult = findViewById(R.id.txv_intent_service_result);
    }

    public void startStartedService(View view) {
        Intent intent = new Intent(MainActivity.this, MyStartedService.class);
        intent.putExtra("Sleep Time", 10);
        startService(intent);
    }

    public void stopStartedService(View view) {
        stopService(new Intent(MainActivity.this, MyStartedService.class));
    }

    public void startIntentService(View view) {
        ResultReceiver myResultReceiver = new MyResultReceiver(null);

        Intent intent = new Intent(MainActivity.this, MyIntentService.class);
        intent.putExtra("Sleep Time", 10);
        intent.putExtra("receiver", myResultReceiver);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.service.to.activity");
        registerReceiver(myStartedServiceReceiver, intentFilter);
    }

    //To receive data back from MyStartedService.class
    private BroadcastReceiver myStartedServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("startServiceResult");
            mTxvStartedServiceResult.setText(result);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(myStartedServiceReceiver);
    }

    //To receive data back from MyIntentService.class
    private class MyResultReceiver extends ResultReceiver {

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == 18 && resultData != null) {

                Log.d("MyResultReceiver", Thread.currentThread().getName());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MyHandler", Thread.currentThread().getName());
                        mTxvIntentServiceResult.setText(resultData.getString("resultIntentService"));
                    }
                });

            }
        }
    }
}
