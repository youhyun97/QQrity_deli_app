package com.cookandroid.qqrity_deliver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =1002 ;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1003;
    private QrFragment qrFragment = new QrFragment(this);
    private testFragment test = new testFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
        //첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, qrFragment).commitAllowingStateLoss();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.qr_recognition:
                    transaction.replace(R.id.frame_layout, qrFragment).commitAllowingStateLoss();
                    break;
                case R.id.delivery_list:
                    transaction.replace(R.id.frame_layout,test).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    };

//    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults){
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CAMERA: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(getApplicationContext(),"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();
//
//                } else {
//                    Toast.makeText(getApplicationContext(),"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//
//        }
//    }
}