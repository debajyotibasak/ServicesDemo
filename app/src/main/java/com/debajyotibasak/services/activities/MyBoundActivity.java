package com.debajyotibasak.services.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.debajyotibasak.services.services.MyBoundService;
import com.debajyotibasak.services.R;

/**
 * Created by debajyotibasak on 04/03/18.
 */

public class MyBoundActivity extends AppCompatActivity {

    boolean isBound = false;

    private MyBoundService myBoundService = new MyBoundService();

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {

            MyBoundService.MyLocalBinder myLocalBinder = (MyBoundService.MyLocalBinder) iBinder;
            myLocalBinder.getService();
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
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, MyBoundService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    public void onClickEvent(View view) {
        EditText edtOne = findViewById(R.id.et_number_one);
        EditText editTwo = findViewById(R.id.et_number_two);
        TextView txvResult = findViewById(R.id.tv_answer);

        int numOne = Integer.valueOf(edtOne.getText().toString());
        int numTwo = Integer.valueOf(editTwo.getText().toString());

        String resultString = "";

        if (isBound) {
            switch (view.getId()) {
                case R.id.btn_add:
                    resultString = String.valueOf(myBoundService.add(numOne, numTwo));
                    break;

                case R.id.btn_substract:
                    resultString = String.valueOf(myBoundService.substract(numOne, numTwo));
                    break;

                case R.id.btn_multiply:
                    resultString = String.valueOf(myBoundService.multiply(numOne, numTwo));
                    break;

                case R.id.btn_divide:
                    resultString = String.valueOf(myBoundService.divide(numOne, numTwo));
                    break;
            }

            txvResult.setText(resultString);
        }
    }
}
