package com.inhatc.finddogapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Map<Marker, Report> markerReportMap = new HashMap<>();
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference reportsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        reportsRef = FirebaseDatabase.getInstance().getReference("reports");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "지도를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
            }
        });

        // ✅ 마커 클릭 시 BottomSheet 연결
        mMap.setOnMarkerClickListener(marker -> {
            Report report = markerReportMap.get(marker);
            if (report != null) {
                ReportPreviewBottomSheet sheet = ReportPreviewBottomSheet.newInstance(report);
                sheet.show(getSupportFragmentManager(), "report_preview");
            }
            return true;
        });

        // Firebase 제보 데이터 불러오기
        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMap.clear();
                markerReportMap.clear();

                for (DataSnapshot reportSnapshot : snapshot.getChildren()) {
                    Report report = reportSnapshot.getValue(Report.class);
                    if (report != null) {
                        // ID 수동 설정
                        report.setId(reportSnapshot.getKey());

                        LatLng location = new LatLng(report.getLatitude(), report.getLongitude());
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title("유기견 제보")
                                .snippet(report.getDescription()));

                        if (marker != null) {
                            markerReportMap.put(marker, report); // ✅ 마커에 Report 연결
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainMapActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(mMap);
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}