package com.example.noah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;
import com.naver.maps.map.widget.LocationButtonView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource mlocationSource;
    private NaverMap mnaverMap;
    private InfoWindow minfoWindow;
    private DatabaseReference trashRef;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("e33kfxvzrt"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        mlocationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        minfoWindow = new InfoWindow();
        marker = new Marker();
        // 서버에서 받아오는 값으로 교체해야함 (현재값 : 한성대학교)
        // marker.setPosition(new LatLng(37.3455, 127.038));
        marker.setPosition(new LatLng(37.5670135, 126.9783740)); // 시청
        marker.setMap(naverMap);

        // marker.setMap(null); 마커 지우는 코드

        // 마커를 눌렀을 때 MapInfoActivity로 화면 전환
        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                Intent intent = new Intent(MainActivity.this, MapInfoActivity.class);
                startActivity(intent);
                return false;
            }
        });

        // 마커 위치 세밀 조정
        marker.setAnchor(new PointF(1, 1));

        // 3D 마커, 지도
        marker.setFlat(true);

        // 마커 텍스트 조정
        marker.setCaptionText("시청 하수구");

        // 지도가 준비되었을 때 호출되는 콜백
        mnaverMap = naverMap;
        // FusedLocationSource를 지도에 연결
        mlocationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationSource(mlocationSource);
        // 위치 추적 모드를 Face로 설정하여 사용자의 방향을 기준으로 지도가 회전
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);
        // UI 설정 변경 코드를 이곳으로 이동
        naverMap.getUiSettings().setLocationButtonEnabled(true);

        UiSettings uiSettings = naverMap.getUiSettings();

        uiSettings.setCompassEnabled(false);
        uiSettings.setLocationButtonEnabled(true);

        // LocationButtonView locationButtonView = findViewById(R.id.location);
        // location 이미지 추가

        // Firebase Realtime Database의 데이터를 감지하여 마커 아이콘 색상을 업데이트
        trashRef = FirebaseDatabase.getInstance().getReference("arduino").child("busan").child("trash");
        trashRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer trashValue = snapshot.getValue(Integer.class);
                updateMarkerIcon(trashValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //위치 권한 요청 결과를 처리
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mnaverMap.setLocationTrackingMode(LocationTrackingMode.None);
                // if (mlocationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                // 위치 권한이 허용되었을 경우
                // if (!mlocationSource.isActivated()) { // 권한이 거부되었을 경우
                // 위치 추적 모드를 None으로 변경하여 사용자 위치 추적을 중지
                // mnaverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            // return;
        }
    }
    // 마커 아이콘 색상 업데이트 메서드
    private void updateMarkerIcon(int trashValue) {
        if (marker == null) {
            // 마커가 아직 생성되지 않은 경우 처리
            return;
        }

        // 포화도에 따라 마커 색상 설정
        if (trashValue <= 100 && trashValue >= 45) {
            marker.setIconTintColor(Color.GREEN);
        } else if (trashValue < 45 && trashValue >= 30 ) {
            marker.setIconTintColor(Color.YELLOW);
        } else if (trashValue < 30  && trashValue >= 10){
            marker.setIconTintColor(Color.RED);
            Toast.makeText(this, "현재 시청 하수구의 포화량이 "+(100 - trashValue)+"입니다. 쓰레기를 수거하세요.", Toast.LENGTH_LONG).show();
        }
    }
}