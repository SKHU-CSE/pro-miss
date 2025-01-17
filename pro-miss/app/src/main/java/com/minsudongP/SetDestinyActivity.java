package com.minsudongP;

import android.content.Intent;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.minsudongP.Singletone.UrlConnection;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SetDestinyActivity extends BaseActivity implements OnMapReadyCallback {


    ArrayList<PromissItem> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    AllRecyclerAdapter adapter;
    EditText editText;
    NaverMap mMap;
    InputMethodManager imm;
    LinearLayout linearLayout;
    static public int result_code = 2;
    String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_destiny);


        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mapFragment.getMapAsync(this);
        recyclerView = findViewById(R.id.set_destiny_recle);
        adapter = new AllRecyclerAdapter(arrayList, SetDestinyActivity.this);

        recyclerView.setAdapter(adapter);
        linearLayout = findViewById(R.id.set_destiny_search_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editText = findViewById(R.id.set_destiny_edit);


        findViewById(R.id.set_destiny_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLocationAddress();
            }
        });

        adapter.SetClickListner(new AllRecyclerAdapter.PromissClick() {
            @Override
            public void OnClick(View view, int position) {

                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                Double longitude = Double.parseDouble(arrayList.get(position).getPositionX());
                Double latitude = Double.parseDouble(arrayList.get(position).getPositionY());
                address = arrayList.get(position).getJibun();

                arrayList.clear();
                editText.setText("");
                adapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.GONE);
                LatLng latLng = new LatLng(latitude, longitude);
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng)
                        .animate(CameraAnimation.Easing);
                mMap.moveCamera(cameraUpdate);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new Thread() {
                    @Override
                    public void run() {
                        if (!editText.getText().toString().equals(""))
                            GetSearchPlace();
                    }
                }.run();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                linearLayout.setVisibility(View.GONE);
                return true;
            }
        });

    }


    public void GetSearchPlace() {
        arrayList.clear();
        String address = editText.getText().toString();
        UrlConnection connection = UrlConnection.shardUrl;
        LatLng position = mMap.getCameraPosition().target;

        connection.MapSearch(address, position.longitude + "," + position.latitude, searchCallback);

    }

    public void GetLocationAddress() {
        UrlConnection connection = UrlConnection.shardUrl;
        LatLng position = mMap.getCameraPosition().target;
        connection.GetAddress(position, addressCallback);
    }

    Callback searchCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            SetDestinyActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SetDestinyActivity.this, "네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();

//            Log.d("url", body);
            try {
                JSONObject jsonObject = new JSONObject(body);

                if (jsonObject.getString("status").equals("OK")) {
                    JSONArray data = jsonObject.getJSONArray("places");

                    for (int i = 0; i < data.length(); i++) {
                        jsonObject = data.getJSONObject(i);
                        arrayList.add(new PromissItem(PromissType.SearchList, jsonObject.getString("name"), jsonObject.getString("jibun_address"), jsonObject.getString("x"), jsonObject.getString("y")));
                    }
                    SetDestinyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            if (linearLayout.getVisibility() == View.GONE)
                                linearLayout.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    SetDestinyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SetDestinyActivity.this, "현재 장소를 불러올 수 없습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Callback addressCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            SetDestinyActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SetDestinyActivity.this, "네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();

            Log.d("url", body);

            try {
                JSONObject jsonObject = new JSONObject(body);
                JSONObject status = jsonObject.getJSONObject("status");

                if (status.getString("name").equals("ok")) {
                    JSONArray data = jsonObject.getJSONArray("results");
                    Log.d("result!", data.toString());

                    String addr = "";
                    for (int i = 0; i < data.length(); i++) {
                        jsonObject = data.getJSONObject(i);
                        addr = "";

                        //도로명
                        if (jsonObject.getString("name").equals("roadaddr")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("region");
                            // 지역
                            addr = jsonObject1.getJSONObject("area1").getString("name") + " ";   // 시,도
                            addr += jsonObject1.getJSONObject("area2").getString("name") + " ";  // 시,군,구
                            if (!jsonObject1.getJSONObject("area3").getString("name").contains("동"))  // 읍,면,동
                                addr += jsonObject1.getJSONObject("area3").getString("name") + " ";
                            // 상세
                            JSONObject jsonObject2 = jsonObject.getJSONObject("land");
                            addr += jsonObject2.getString("name") + " ";    // 도로명
                            if (!jsonObject2.getString("number1").equals(""))    // 상세주소
                                addr += jsonObject2.getString("number1") + " ";
                            if (!jsonObject2.getJSONObject("addition0").getString("value").equals(""))   // 건물명
                                addr += "(" + jsonObject2.getJSONObject("addition0").getString("value") + ")";
                            break;
                        }
                        //지번
                        else if (jsonObject.getString("name").equals("addr")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("region");
                            // 지역
                            addr = jsonObject1.getJSONObject("area1").getString("name") + " ";   // 시,도
                            addr += jsonObject1.getJSONObject("area2").getString("name") + " ";  // 시,군,구
                            if (!jsonObject1.getJSONObject("area3").getString("name").equals(""))  // 읍,면,동
                                addr += jsonObject1.getJSONObject("area3").getString("name") + " ";
                            if (!jsonObject1.getJSONObject("area4").getString("name").equals(""))  // 리
                                addr += jsonObject1.getJSONObject("area4").getString("name") + " ";
                            // 상세
                            JSONObject jsonObject2 = jsonObject.getJSONObject("land");
                            if(!jsonObject2.getString("number1").equals(""))
                                addr+=jsonObject2.getString("number1");
                            if(!jsonObject2.getString("number2").equals(""))
                                addr+="-"+jsonObject2.getString("number2");
                            break;
                        }
                    }
                    Log.d("주소", addr);
                    address=addr;

                    Intent intent = new Intent(SetDestinyActivity.this, appointment.class);
                    intent.putExtra("latitude", mMap.getCameraPosition().target.latitude);
                    intent.putExtra("longitude", mMap.getCameraPosition().target.longitude);
                    intent.putExtra("address", address);
                    setResult(result_code, intent);
                    finish();
                } else {
                    SetDestinyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SetDestinyActivity.this, "현재 장소를 불러올 수 없습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mMap = naverMap;

        LatLng coord = new LatLng(37.5662952, 126.97794509999994);

        mMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
        mMap.setCameraPosition(new CameraPosition(coord, 17.0)); // 카메라 위치 셋팅
//        mMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(int i, boolean b) {
//                GetLocationAddress();
//            }
//        });
//        val uiSettings = mMap!!.uiSettings
//        uiSettings.isZoomControlEnabled = false // 줌 버튼 지우기
//        mMap!!.setContentPadding(0, 0, 0, 1410) // 로고 상단으로 올리기
    }
}
