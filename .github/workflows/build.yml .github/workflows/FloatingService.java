package com.example.floatingclipboardkey;

import android.app.*;import android.content.*;import android.graphics.Color;import android.graphics.PixelFormat;import android.os.*;import android.view.*;import android.widget.*;

public class FloatingService extends Service {
    WindowManager wm; View button; final int id=1001;
    @Override public void onCreate(){ super.onCreate();
        if(Build.VERSION.SDK_INT>=26){ NotificationChannel c=new NotificationChannel("float","Floating Key",NotificationManager.IMPORTANCE_LOW); getSystemService(NotificationManager.class).createNotificationChannel(c); }
        Intent i=new Intent(this,MainActivity.class); PendingIntent pi=PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        Notification n=new Notification.Builder(this, "float").setContentTitle("Floating Clipboard Key").setContentText("Floating COPY button is active").setSmallIcon(android.R.drawable.ic_menu_edit).setContentIntent(pi).build();
        startForeground(id,n); showButton();
    }
    void showButton(){
        wm=(WindowManager)getSystemService(WINDOW_SERVICE); Button b=new Button(this); b.setText("COPY"); b.setTextColor(Color.WHITE); b.setTextSize(12); b.setBackgroundColor(Color.rgb(30,80,150));
        b.setOnClickListener(v->{ String t=getSharedPreferences("data",0).getString("text",""); if(!t.isEmpty()){ ClipboardManager cm=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE); cm.setPrimaryClip(ClipData.newPlainText("Saved text",t)); Toast.makeText(this,"Copied",Toast.LENGTH_SHORT).show(); }});
        button=b; int type=Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE;
        WindowManager.LayoutParams p=new WindowManager.LayoutParams(150,150,type,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT); p.gravity=Gravity.RIGHT|Gravity.CENTER_VERTICAL; p.x=12; p.y=0; wm.addView(b,p);
    }
    @Override public int onStartCommand(Intent i,int f,int s){ return START_STICKY; }
    @Override public void onDestroy(){ if(wm!=null&&button!=null) wm.removeView(button); super.onDestroy(); }
    @Override public android.os.IBinder onBind(Intent i){return null;}
}
