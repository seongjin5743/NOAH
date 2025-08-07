package com.example.noah;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.util.Log;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.map.overlay.Marker;

public class MapInfoActivity extends AppCompatActivity {

    private int width;
    private int height;

    private Button openBtn;
    private Button closeBtn;
    private DatabaseReference trashRef;
    private TextView trashTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_info);

        openBtn = findViewById(R.id.openbtn);
        closeBtn = findViewById(R.id.closebtn);

        trashTextView = findViewById(R.id.Trash);

        trashRef = FirebaseDatabase.getInstance().getReference("arduino").child("busan").child("trash");

        // Trash 데이터베이스 변경 사항을 감지하여 TextView 업데이트
        trashRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 데이터 변경 시 호출됨
                // 정수 값을 가져와서 문자열로 변환하여 TextView에 설정
                Integer trashValue = snapshot.getValue(Integer.class);
                String trashString = String.valueOf(trashValue);
                trashTextView.setText("포화도 : " + trashString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터베이스 읽기가 취소된 경우 호출됨
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firebase 데이터베이스에 arduino/busan/state 경로에 있는 데이터의 값을 true로 설정
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("arduino").child("busan").child("door");
                databaseRef.setValue(1);
                finish();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("arduino").child("busan").child("door");
                databaseRef.setValue(0);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLayout();
    }

    private void initLayout() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 버전
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            width = windowMetrics.getBounds().width();
            height = windowMetrics.getBounds().height();
        } else { // API Level 30 이전 버전
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getRealMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;
        }
        getWindow().setLayout((int) Math.round(width * 0.9), (int)Math.round(height * 0.22));
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}


