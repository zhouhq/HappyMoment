package com.ryo.happymoment;



import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ryo.happymoment.data.RyoDataBaseHelper;

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

        ContentResolver resolver=getContentResolver();
        if (view == send) {

            ContentValues values=new ContentValues();
            values.put("title","这是标题");
            values.put("content","这是内容，终于成功了 不容易呀再来一次");
           resolver.insert(Uri.parse(RyoDataBaseHelper.RecommendProvider.Recommend_Uri),values);
            return;
        }

        if (view == sendSticky) {
           Cursor cursor= resolver.query(Uri.parse(RyoDataBaseHelper.RecommendProvider.Recommend_Uri),new String[]{"title","content"},null,null,null);
           while (cursor.moveToNext())
           {
               String title= cursor.getString(cursor.getColumnIndex("title"));
               String content= cursor.getString(cursor.getColumnIndex("content"));
               Log.e("zhouhq","title="+title+" content = "+content);
           }
            return;
        }
        if (view == reg) {
           int count= resolver.delete(Uri.parse(RyoDataBaseHelper.RecommendProvider.Recommend_Uri),"title=?",new String[]{"这是标题"});
           Log.e("zhouhq","删除记录结果="+count);
            return;
        }
        if (view == unreg) {
            ContentValues values=new ContentValues();
            values.put("content","测试更新");
            resolver.update(Uri.parse(RyoDataBaseHelper.RecommendProvider.Recommend_Uri),values,"title= ?",new String[]{"这是标题3"});
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
