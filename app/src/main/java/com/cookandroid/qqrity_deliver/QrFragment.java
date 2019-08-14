package com.cookandroid.qqrity_deliver;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONObject;

import java.io.IOException;

@SuppressLint("ValidFragment")
public class QrFragment extends Fragment {
    private final int REQUEST_ACT = 101;
    SurfaceView surfaceView;
    TextView textView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    Boolean qrReadChecker = false;
    Context context;

    @SuppressLint("ValidFragment")
    public QrFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //qr 인식
        surfaceView = (SurfaceView) view.findViewById(R.id.barcodeScanner);
        textView = (TextView) view.findViewById(R.id.textview);
        barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(context, barcodeDetector).setRequestedPreviewSize(640, 480).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
                final Intent intent = new Intent(getContext(), CustomerInformationActivity.class);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult call", "ok");
        qrReadChecker = false;
    }
}