package com.cookandroid.qqrity_deliver;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONObject;
import java.io.IOException;
import java.lang.Object;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =1002 ;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1003;
    private final int REQUEST_ACT = 101;
    SurfaceView surfaceView;
    TextView textView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    Boolean qrReadChecker = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //(카메라)권한 보유 여부를 확인하려면 ContextCompat.checkSelfPermission() 메서드를 호출
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){}
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }

        //(sms) 권한 보유 여부 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        //(CALL) 권한 보유 여부 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            //권한을 허용하지 않는 경우
        } else {
            //권한을 허용한 경우
        }

        //qr 인식
        surfaceView = (SurfaceView) findViewById(R.id.barcodeScanner);
        textView = (TextView)findViewById(R.id.textview);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrBarcode = detections.getDetectedItems();
                final Intent intent = new Intent(getApplicationContext(), CustomerInformationActivity.class);
                if(qrReadChecker == false){
                    if(qrBarcode.size() != 0){
                        qrReadChecker = true;
                        try{
                            JSONObject jobj = new JSONObject(qrBarcode.valueAt(0).displayValue);
                            JSONObject to = jobj.getJSONObject("to");
                            JSONObject from = jobj.getJSONObject("from");
                            intent.putExtra("toName", to.getString("cname"));
                            intent.putExtra("toPhone", to.getString("cphone"));
                            intent.putExtra("fromName", from.getString("sname"));
                            intent.putExtra("fromAddress", from.getString("saddress"));
                            intent.putExtra("fromPhone", from.getString("sphone"));
                            startActivityForResult(intent, REQUEST_ACT);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult call", "ok");
        qrReadChecker = false;
    }
}