package com.debajyotibasak.services.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.debajyotibasak.services.R;
import com.debajyotibasak.services.services.MyMessengerService;

/**
 * Created by debajyotibasak on 04/03/18.
 */

public class MyMessengerActivity extends AppCompatActivity {

    boolean isBound = false;

    private TextView txvResult;

    private Messenger mService = null;

    private class IncomingResponseHandler extends Handler {
        @Override
        public void handleMessage(Message msgFromService) {
            super.handleMessage(msgFromService);

            switch (msgFromService.what) {
                case 87:

                    Bundle bundle = msgFromService.getData();
                    int result = bundle.getInt("result", 0);
                    txvResult.setText("Result : " + result);

                    break;

                default:
                    super.handleMessage(msgFromService);

            }
        }
    }

    private Messenger incomingMessage = new Messenger(new IncomingResponseHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        txvResult = findViewById(R.id.tv_answer);
    }

    public void performAddOperation(View view) {

        EditText edtOne = findViewById(R.id.et_number_one);
        EditText editTwo = findViewById(R.id.et_number_two);

        int numOne = Integer.valueOf(edtOne.getText().toString());
        int numTwo = Integer.valueOf(editTwo.getText().toString());

        Message msgToService = Message.obtain(null, 43);

        Bundle bundle = new Bundle();
        bundle.putInt("numOne", numOne);
        bundle.putInt("numTwo", numTwo);

        msgToService.setData(bundle);
        msgToService.replyTo = incomingMessage;

        try {
            mService.send(msgToService);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bindService(View view) {
        Intent intent = new Intent(this, MyMessengerService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    public void unBindService(View view) {
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }
}
