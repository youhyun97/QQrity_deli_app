package com.cookandroid.qqrity_deliver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerInformationActivity extends AppCompatActivity {
    String custPhone;
    String senderName;
    String senderAddress;
    String senderPhone;
    final Context context = this;
    Button statusChangeButton;
    Button sendTextMessage;
    Button callToCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_information);
        Button showSenderBtn = findViewById(R.id.showSenderBtn);
        statusChangeButton = findViewById(R.id.statusChangeButton);
        sendTextMessage = findViewById(R.id.ic_text_message);
        callToCustomer = findViewById(R.id.ic_call);
        Intent intent = getIntent();

//        custPhone = intent.getStringExtra("toPhone").replaceAll("-","");
        custPhone = "01042143453";
        senderName = intent.getStringExtra("fromName");
        senderAddress = intent.getStringExtra("fromAddress");
        senderPhone = intent.getStringExtra("fromPhone");
        Log.e("sender Name", senderName);
        TextView custName = (TextView)findViewById(R.id.sname);
        custName.setText(intent.getStringExtra("toName") + " 고객님");

        showSenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sender info show button", "onclick");
                SenderInfoDialog dialog = new SenderInfoDialog(CustomerInformationActivity.this);
                dialog.callFunction(senderName,senderAddress,senderPhone);
            }
        });

        statusChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder stateChangeDialog = new AlertDialog.Builder(context);
                stateChangeDialog.setTitle("배송 완료 확인");
                stateChangeDialog.setMessage("배송을 완료 하시겠습니까? \n완료 후에는 상태를 변경할 수 없습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                TextView deliveryState = findViewById(R.id.deliveryState);
                                deliveryState.setText("배송 완료");
                                statusChangeButton.setEnabled(false);
                                Toast.makeText(getApplicationContext(), "배송이 완료되었습니다.", Toast.LENGTH_LONG);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = stateChangeDialog.create();
                alertDialog.show();
            }
        });

        sendTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextMessageDialog tdlg = new TextMessageDialog(CustomerInformationActivity.this);
                tdlg.callSendMessage(custPhone);
            }
        });
        callToCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:"+custPhone)));
            }
        });
    }

    @Override
    public void onBackPressed(){
        Log.d("onBackPressed", "ok");
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}