package com.cookandroid.qqrity_deliver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Method;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class Register1 extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    NetworkService networkService;
    EditText phoneNum, checkNumEdt, nameEdt, id, passwd, checkpasswd;
    Button receiveNum, checkNumBtn, registerBtn;
    Spinner companySpin;
    SpinnerAdapter spinnerAdapter;
    boolean initSpinner;
    String deviceNum, company;
    Deliveryman deliman;
    Key publicKey;
    RSAPublicKeySpec publicKeySpec;

    // camera access
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    deviceNum = Build.getSerial();
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        deviceNum = Build.getSerial();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register1);


        phoneNum = (EditText)findViewById(R.id.phoneNum_regi);
        checkNumEdt = (EditText)findViewById(R.id.checknumEdt_regi);
        nameEdt = (EditText)findViewById(R.id.name_regi);
        receiveNum = (Button)findViewById(R.id.receiveNum_regi);
        checkNumBtn = (Button)findViewById(R.id.checkNumBtn_regi);
        id = (EditText)findViewById(R.id.id_regi);
        passwd = (EditText)findViewById(R.id.passwd_regi);
        checkpasswd = (EditText)findViewById(R.id.checkpasswd_regi);
        registerBtn = (Button)findViewById(R.id.btnRegister);
        companySpin = (Spinner)findViewById(R.id.spinner_company);

        // camera 권환 확인
        int permssionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);

        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this,"000부분 사용을 위해 카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                Toast.makeText(this,"000부분 사용을 위해 카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();

            }
        }

        // password '*' 처리
        passwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        checkpasswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // serial num
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        }

        // spinner company
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,(String[])getResources().getStringArray(R.array.company_array));
        companySpin.setAdapter(spinnerAdapter);
        companySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(initSpinner==false){
                    initSpinner=true;
                    return;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"택배사를 선택해주세요",Toast.LENGTH_SHORT).show();
            }
        });



        networkService = RetrofitSender.getNetworkService();

        // 회원가입 클릭시 data저장, login 화면으로 돌아가기
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // passwd 값이 같으면 회원가입
                if(passwd.getText().toString().equals(checkpasswd.getText().toString())){
                    // key 생성
                    try {
                        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                        keyPairGenerator.initialize(1024);

                        KeyPair keyPair = keyPairGenerator.genKeyPair();
                        publicKey = keyPair.getPublic(); // 공개키
                        Key privateKey = keyPair.getPrivate(); // 개인키

                        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                        publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
                        RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
                        Log.e("##############키생성","키 생성");

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("##############키생성실패","키생성실패");
                    }

                    company = companySpin.getSelectedItem().toString(); // company

                    Log.e("##############",id.getText().toString()+""+ passwd.getText().toString()+""+ nameEdt.getText().toString()+""+ company
                            +""+deviceNum.toString()+""+ phoneNum.getText().toString()+""+ publicKeySpec.toString());


                    Call<Deliveryman> response = networkService.register_deliveryman(id.getText().toString(), passwd.getText().toString(), nameEdt.getText().toString(),
                            company, deviceNum.toString(), phoneNum.getText().toString(), publicKeySpec.toString());
                    response.enqueue(new Callback<Deliveryman>() {
                        @Override
                        public void onResponse(Call<Deliveryman> call, Response<Deliveryman> response) {
                            Deliveryman deliveryman = response.body();
                            if(response.isSuccessful()){
                                Log.e("##############성공","성공");
                            }
                            else {
                                Log.e("########response isnt","실패");
                            }

                        }

                        @Override
                        public void onFailure(Call<Deliveryman> call, Throwable t) {
                            Log.e("##############실패","실패");
                        }
                    });



                    Toast.makeText(getApplicationContext(),"회원가입 완료",Toast.LENGTH_SHORT).show();
                    Log.e("##############Response","회원가입");
                    finish();
                }
                // passwd 값이 불일치 하면 리턴
                else{
                    Toast.makeText(getApplicationContext(),"두 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
