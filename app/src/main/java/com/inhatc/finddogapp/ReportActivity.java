package com.inhatc.finddogapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.location.Priority;



import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int REQUEST_CODE_PICK_IMAGE = 2001;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private TextView textLocation;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private Uri selectedImageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);

        databaseRef = FirebaseDatabase.getInstance().getReference("reports");
        storageRef = FirebaseStorage.getInstance().getReference("report_images");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        textLocation = findViewById(R.id.textLocation);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        ImageView imagePreview = findViewById(R.id.imagePreview);
        Button btnSubmitReport = findViewById(R.id.btnSubmitReport);
        EditText editDescription = findViewById(R.id.editDescription);

        checkLocationPermissionAndFetch();

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });


        btnSubmitReport.setOnClickListener(v -> {
            String description = editDescription.getText().toString().trim();

            if (currentLatitude == 0 || currentLongitude == 0) {
                Toast.makeText(this, "위치를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (description.isEmpty()) {
                Toast.makeText(this, "설명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageUri != null) {
                uploadImageAndSaveReport(description);
            } else {
                saveReport(description, null);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void checkLocationPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                        textLocation.setText("위도: " + currentLatitude + "\n경도: " + currentLongitude);
                    } else {
                        textLocation.setText("위치 정보를 가져올 수 없습니다.");
                    }
                })
                .addOnFailureListener(e -> {
                    textLocation.setText("위치 획득 실패: " + e.getMessage());
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
            } else {
                textLocation.setText("위치 권한이 필요합니다.");
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ImageView imagePreview = findViewById(R.id.imagePreview);
            imagePreview.setImageURI(selectedImageUri);
        }
    }
    private void uploadImageAndSaveReport(String description) {
        String imageName = "report_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            saveReport(description, imageUrl);
                        }))
                .addOnFailureListener(e -> {
                    Log.e("UploadError", "이미지 업로드 실패", e);
                    Toast.makeText(this, "이미지 업로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void saveReport(String description, String imageUrl) {
        Report report = new Report(currentLatitude, currentLongitude, description, imageUrl);
        databaseRef.push().setValue(report)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "신고가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "신고 등록 실패", Toast.LENGTH_SHORT).show();
                });
    }
}