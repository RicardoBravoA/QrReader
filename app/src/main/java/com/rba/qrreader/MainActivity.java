package com.rba.qrreader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, OnQRCodeReadListener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;
    private ViewGroup mainLayout;
    private QRCodeReaderView qrView;
    private CheckBox chkFlash;
    private PointsOverlayView pointView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainLayout = (ViewGroup) findViewById(R.id.main_layout);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            requestCameraPermission();
        }

    }

    private void init() {

        qrView = (QRCodeReaderView) findViewById(R.id.qrView);
        chkFlash = (CheckBox) findViewById(R.id.chkFlash);
        pointView = (PointsOverlayView) findViewById(R.id.pointView);

        qrView.setAutofocusInterval(2000L);
        qrView.setOnQRCodeReadListener(this);
        qrView.setBackCamera();
        chkFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                qrView.setTorchEnabled(isChecked);
            }
        });
        qrView.setQRDecodingEnabled(true);
        qrView.startCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (qrView != null) {
            qrView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (qrView != null) {
            qrView.stopCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.i("X-CAMERA ", "X-CAMERA ");
                        Toast.makeText(this, getString(R.string.permission_true), Toast.LENGTH_SHORT).show();
                        init();
                    }
                }
            }
        }

    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        pointView.setPoints(points);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(mainLayout, getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] { Manifest.permission.CAMERA }, MY_PERMISSION_REQUEST_CAMERA);
                }
            }).show();
        } else {
            tryGetPermission();
            Snackbar.make(mainLayout, getString(R.string.permission_required),
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA },
                    MY_PERMISSION_REQUEST_CAMERA);
        }

    }

    public void tryGetPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED )) {
            requestCameraPermission();
        }
    }


}
