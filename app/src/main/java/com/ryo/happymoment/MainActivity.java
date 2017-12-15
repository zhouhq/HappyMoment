package com.ryo.happymoment;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ryo.view.AmazingTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends Activity implements View.OnClickListener {
    Button send;
    Button sendSticky;
    Button reg;
    Button unreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);

        sendSticky = (Button) findViewById(R.id.sendsticky);
        sendSticky.setOnClickListener(this);

        reg = (Button) findViewById(R.id.reg);
        reg.setOnClickListener(this);

        unreg = (Button) findViewById(R.id.unreg);
        unreg.setOnClickListener(this);

        
    }

    @Override
    public void onClick(View view) {
        if (view == send) {
            EventBus.getDefault().post(new MyEventMessage("这是测试文字", 1));
            return;
        }

        if (view == sendSticky) {
            EventBus.getDefault().postSticky(new MyEventMessage("这是粘性事件", 1));
            return;
        }
        if (view == reg) {
            EventBus.getDefault().register(this);
            return;
        }
        if (view == unreg) {
            EventBus.getDefault().unregister(this);
            return;
        }
    }

    public static class MyEventMessage {
        String msg;
        int v;

        public MyEventMessage(String msg, int v) {
            this.msg = msg;
            this.v = v;
        }
    }

    @Subscribe
    public void onmessage(MyEventMessage message) {
        Toast.makeText(this, message.msg + message.v, Toast.LENGTH_LONG).show();
        Log.e("zhouhq", "onmessage1");
    }

    @Subscribe(sticky = true)
    public void onmessage2(MyEventMessage message) {
        Toast.makeText(this, message.msg + message.v + "222", Toast.LENGTH_LONG).show();
        Log.e("zhouhq", "onmessage2");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
