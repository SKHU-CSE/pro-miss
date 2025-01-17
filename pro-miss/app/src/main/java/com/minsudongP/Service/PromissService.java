package com.minsudongP.Service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.minsudongP.AlertActivity;
import com.minsudongP.App;
import com.minsudongP.Appointment_Game_Activity;
import com.minsudongP.MainActivity;
import com.minsudongP.R;
import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Notification.VISIBILITY_PUBLIC;
import static com.minsudongP.App.CHAANEL_ID;

public class PromissService extends Service implements LocationListener {

    private final String id=UserInfor.shared.getId_num();
    private final String name=UserInfor.shared.getName();
    Location location;

    protected LocationManager locationManager;

    //push 알람

    NotificationManagerCompat manager;
    NotificationCompat.Builder Init;
    NotificationCompat.Builder New_Alert; // 새로운 알림이 도착했습니다.
    NotificationCompat.Builder Appoitment_End;  // 약속 제목 약속이 종료되었습니다! PendingIntent는 없어도 됨
    NotificationCompat.Builder Fine;//벌금이 누적되었습니다.
    @Override
    public void onCreate() {
        super.onCreate();
    }


    //서비스가 죽었다가 다시 실행이 될 때, 호출되는 함수
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

        }else {

            locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    60000,
                    1,
                    this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    60000,
                    1,
                    this);
        }

        //알람 등록
        {
            manager=NotificationManagerCompat.from(this);
            Intent FineIntent=new Intent(this, Appointment_Game_Activity.class);
            Intent AlertIntent= new Intent(this, AlertActivity.class);
            PendingIntent FinePendingIntent=PendingIntent.getActivity(this
                    ,0,FineIntent,0); //알람을 눌렀을 때 해당 엑티비티로
            PendingIntent AlertPendingIntent=PendingIntent.getActivity(this
                    , 0,AlertIntent,0);


            Fine=new NotificationCompat.Builder(this,CHAANEL_ID)
                    .setContentTitle("약속")
                    .setAutoCancel(true)// 사용자가 알람을 탭했을 때, 알람이 사라짐
                    .setContentText("벌금이 누적되었습니다.")
                    .setVisibility(VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.logo)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            Init=new NotificationCompat.Builder(this,CHAANEL_ID)
                    .setContentTitle("Pro-Miss")
                    .setAutoCancel(true)// 사용자가 알람을 탭했을 때, 알람이 사라짐
                    .setContentText("반갑습니다. "+name+"님")
                    .setVisibility(VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(FinePendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            New_Alert=new NotificationCompat.Builder(this,CHAANEL_ID)
                    .setContentTitle("프로미스")
                    .setVisibility(VISIBILITY_PUBLIC)
                    .setAutoCancel(true)// 사용자가 알람을 탭했을 때, 알람이 사라짐
                    .setContentText("사용자에게 새로운 알림이 도착했습니다.")
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(AlertPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            Appoitment_End=new NotificationCompat.Builder(this,CHAANEL_ID)
                    .setContentTitle("프로미스")
                    .setVisibility(VISIBILITY_PUBLIC)
                    .setAutoCancel(true)// 사용자가 알람을 탭했을 때, 알람이 사라짐
                    .setContentText("약속이 종료되었습니다.")
                    .setSmallIcon(R.drawable.ic_add_alarm_black_24dp)
                    .setContentIntent(FinePendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);


            //////////////////////////////////////////////////////////////////////////////////////////
            //Notification 알람///////////////
            PusherOptions options = new PusherOptions();
            options.setCluster("ap3");
            Pusher pusher = new Pusher("60518d2597abbeaa238c", options);

            Channel channel = pusher.subscribe("ProMiss");

            channel.bind("event_user"+id, new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, final String data) {
                    //type: 1. 약속초대
                    try {
                        JSONObject jsonObject=new JSONObject(data);

                        switch (jsonObject.getString("type"))
                        {
                            case "약속 초대":
                            case "gps 경고":
                                manager.notify(20, New_Alert.setContentText(jsonObject.getString("message")).build());
                                break;
                            case "약속 종료":
                                manager.notify(20, Appoitment_End.setContentText(jsonObject.getString("message")).build());
                                locationManager.removeUpdates(PromissService.this);//더 이상 필요하지 않을 때, 자원 누락을 방지하기 위해
                                break;
                            case "약속 시작":
                                if ( Build.VERSION.SDK_INT >= 23 &&
                                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                                }else {
                                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                            60000,
                                            1,
                                            PromissService.this);
                                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                            60000,
                                            1,
                                            PromissService.this);
                                }
                                manager.notify(20, Appoitment_End.setContentText(jsonObject.getString("message")).build());
                                break;
                            case "벌금 부과":
                                manager.notify(20, Fine.setContentText(jsonObject.getString("message")).build());
                                break;

                                default:
                                    break;
                        }

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            pusher.connect();
        }

        startForeground(10,Init.build());

      //  manager.notify(30, New_Alert);
        return START_STICKY;  //서비스가 종료되어도 다시 시작
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            locationManager.removeUpdates(this);//더 이상 필요하지 않을 때, 자원 누락을 방지하기 위해

        }catch (NullPointerException e)
        {
                //locationmanager가 null일 때
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


//    private void sendGPSMessage(double latitude,double longitude){
//        Log.d("GPSService", "Broadcasting message");
//        Intent intent = new Intent("GPS-event-name");
//        intent.putExtra("send","gps");
//        intent.putExtra("latitude",latitude);
//        intent.putExtra("longitude", longitude);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//    }
//
//    private void sendErrorMessage(String message){
//        Log.d("GPSService", "Broadcasting message");
//        Intent intent = new Intent("GPS-event-name");
//        intent.putExtra("send","error");
//        intent.putExtra("message",message);
//
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//    }


    //사용자의 gps가 변할 때, 서버와 통신하는 Callback 함수
    @Override
    public void onLocationChanged(final Location location) {
        Log.d("gps","LocationChanged");
        this.location=location;
//        sendGPSMessage(location.getLatitude(),location.getLongitude());

        new Thread()
        {
            @Override
            public void run() {
                UrlConnection connection=UrlConnection.shardUrl;
                HashMap<String,String> hash=new HashMap<>();

                hash.put("id",id);
                hash.put("latitude",""+location.getLatitude());
                hash.put("longitude",""+location.getLongitude());
              connection.PostRequest("api/gps/upload",callback,hash);
            }
        }.run();
       // Log.d("gps",location.getLatitude()+","+location.getLongitude());
    }


    //서버통신 후 JSON 파싱
    private Callback callback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s=response.body().string();

            JSONObject object= null;
            try {
                object = new JSONObject(s);
                if(object.getInt("result")==2000)
                {

                }else{
//                    sendErrorMessage(object.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    // LocationListener
    ////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("gps","statusChange");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("gps","providerEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("gps","providerDisabled");
    }


}
