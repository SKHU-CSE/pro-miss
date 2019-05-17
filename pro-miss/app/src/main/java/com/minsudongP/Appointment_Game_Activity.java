package com.minsudongP;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

public class Appointment_Game_Activity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int PROMISS_GAME_READY=0;
    public static final int PROMISS_GAME_DC_CIRCLE=1;//점점 줄어드는 원
    public static final int PROMISS_GAME_STOP_CIRCLE=2;//페이지 시 멈출 때
    NaverMap mMap;
    CircleOverlay circle; //줄어들 원
    int radius=500;
//    LocationOverlay locationOverlay;//줄어들 원
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment__game_);


        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);


        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                locationOverlay.setCircleRadius(100);
            }
        });
    }


    Handler CircleHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case PROMISS_GAME_DC_CIRCLE:
                    if(radius>30)
                    MapReSetting();
                    else sendEmptyMessage(PROMISS_GAME_READY);
                    break;
                case PROMISS_GAME_READY:
                    break;
                case PROMISS_GAME_STOP_CIRCLE:
                    break;
            }
        }
    };


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        //약속 목적지가 중앙
        mMap=naverMap;

        LatLng coord = new LatLng(37.5662952,126.97794509999994);

        Marker marker = new Marker();
        marker.setCaptionText("목적지");

        marker.setPosition(new LatLng(37.5670135, 126.9783740));
        marker.setMap(naverMap);

        circle = new CircleOverlay();
        circle.setCenter(new LatLng(37.5666102, 126.9783881)); // 약속 장소 위치
        circle.setRadius(radius); // 타이머에 따른 크기
        circle.setColor(Color.alpha(0)); //투명
        circle.setOutlineWidth(5);
        circle.setOutlineColor(Color.GREEN);
        circle.setMap(mMap);

        MapReSetting();


        mMap.setCameraPosition(new CameraPosition(coord, 17.0)); // 카메라 위치 셋팅
    }


    public void MapReSetting()
    {

        radius -=10;
        circle.setMap(null);
        circle.setRadius(radius);
        circle.setMap(mMap);
        CircleHandler.sendEmptyMessageDelayed(PROMISS_GAME_DC_CIRCLE,1000);
    }
}