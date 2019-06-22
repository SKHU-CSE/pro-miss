package com.minsudongP.Fragment;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Lifecycle;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.minsudongP.DialogSelectTimer;
import com.minsudongP.R;

import com.minsudongP.SetDestinyActivity;
import com.minsudongP.appointment;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.minsudongP.SetDestinyActivity.result_code;


public class AppointmentFragemnt extends Fragment implements OnMapReadyCallback {

    long mNow;
    Date mDate;
    TextView tvDate;
    TextView tvTime;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    static final int request_code = 1;
    public String latitude;
    public String longitude;
    NaverMap naverMap;
    TextView tvTimer;
    TextView tvAddress;
    String date_time = "00:00";
    EditText text;
    EditText notice;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_1, null);

        // 지도 터치 방지를 위해 생성됨
        (view.findViewById(R.id.upperMapView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetDestinyActivity.class);
                startActivityForResult(intent,request_code);
            }
        });

        // 지도 객체 받아오기
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getChildFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        (view.findViewById(R.id.map)).setClickable(false);
        (view.findViewById(R.id.map)).setFocusable(false);
        (view.findViewById(R.id.map)).setEnabled(false);

        text = view.findViewById(R.id.appointment_name);
        notice=view.findViewById(R.id.appointment_memo);
        final TextView upperDate = view.findViewById(R.id.upper_date);
        tvDate = view.findViewById(R.id.appointment_date);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        tvDate.setText(String.format("%02d-%02d-%02d", year, month + 1, date));
                        tvDate.setTextColor(Color.BLACK);
                        upperDate.setVisibility(View.VISIBLE);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));


                dialog.getDatePicker().setMinDate(cal.getTime().getTime());    //현재 날짜 이전으로 클릭 안되게 옵션 설정
                dialog.show();
            }
        });


        // 시간 설정
        final TextView upperTime = view.findViewById(R.id.upper_time);
        tvTime = view.findViewById(R.id.appointment_time);
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {

                        int currentHour = cal.get(Calendar.HOUR_OF_DAY);

                        int currentMin = cal.get(Calendar.MINUTE);
                        if (currentHour < hour || currentHour == hour && currentMin < min) {
                            date_time = hour + ":" + min;
                            String ampm = "AM";

                            if (hour >= 12) {
                                ampm = "PM";
                                if (hour != 12) hour -= 12;
                            }

                            tvTime.setText(String.format("%s %02d : %02d", ampm, hour, min));
                            tvTime.setTextColor(Color.BLACK);
                            upperTime.setVisibility(View.VISIBLE);
                        } else {
                            AlertDialog.Builder alert_time = new AlertDialog.Builder(getContext());

                            alert_time.setTitle("시간설정 오류");
                            alert_time.setMessage("현재시간 보다 이전시간으로 설정하셨습니다. 다시 설정하세요");

                            alert_time.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            AlertDialog alert = alert_time.create();
                            alert.show();
                        }
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);//마지막 boolean 값은 시간을 24시간으로 보일지 아닐지

                dialog.show();
            }
        });

        // 장소 설정
        tvAddress = view.findViewById(R.id.appointment_address);
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetDestinyActivity.class);
                startActivityForResult(intent,request_code);
            }
        });

        // 타이머 설정
        tvTimer = view.findViewById(R.id.appointment_timer);
        final TextView upperTimer = view.findViewById(R.id.upper_timer);
        final TextView plusTime = view.findViewById(R.id.appointment_plustime);
        View.OnClickListener timerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelectTimer dialogSelectTimer = new DialogSelectTimer(getActivity());
                dialogSelectTimer.callFunction(tvTimer,upperTimer,plusTime);
            }
        };

        tvTimer.setOnClickListener(timerListener);
        plusTime.setOnClickListener(timerListener);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        naverMap.setMaxZoom(14.0);
        naverMap.setMinZoom(14.0);
//        this.naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
//                Intent intent = new Intent(getActivity(), SetDestinyActivity.class);
//                startActivityForResult(intent,request_code);
//            }
//        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_code) {
            if (resultCode == result_code) {
                LatLng latLng = new LatLng(data.getDoubleExtra("latitude", 37.5662952), data.getDoubleExtra("longitude", 126.97794509999994));
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng)
                        .animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);
                naverMap.setCameraPosition(new CameraPosition(latLng, 17));
                String address = data.getStringExtra("address");
                if (!address.equals("")) {
                    tvAddress.setText(address);
                    tvAddress.setTextColor(Color.BLACK);
                    ((TextView)getView().findViewById(R.id.upper_address)).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }


    public int getTimer() {
        String s = tvTimer.getText().toString();

        if (tvTimer.getText().equals("약속장소로 언제 출발하시나요?")){
            return -1;
        } else {
            s = s.replaceAll(" ", "");
            s = s.replaceAll("분", "");
            s = s.trim();
            if (s.contains("시간")) {
                String str[] = s.split("시간");


                int time = 0;
                time += Integer.parseInt(str[0]) * 60;
                if (str.length > 1)
                    time += Integer.parseInt(str[1]);

                return time;
            } else
                return Integer.parseInt(s.trim());
        }
    }


    public void SendDatatoActivity() {
        ((appointment) getActivity()).setAppointment_role_1(text.getText().toString(), "" + naverMap.getCameraPosition().target.latitude, "" + naverMap.getCameraPosition().target.longitude
                , "" + getTimer(), tvDate.getText().toString(), date_time,notice.getText().toString());
    }

}
