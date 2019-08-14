package com.cookandroid.qqrity_deliver;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SenderInfoDialog {

    private Context context;
    private TextView senderName, senderAddress, senderPhone;
    public SenderInfoDialog(Context context){
        this.context = context;
    }

    public void callFunction(String name, String address, String phone){
        Log.e("call Function", "OK");
        Log.e("call Function", name+"");
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.sender_info_dialog);
        dlg.show();
        final Button confirmBtn = (Button)dlg.findViewById(R.id.confirmBtn);
        senderName = (TextView)dlg.findViewById(R.id.sender_name);
        senderAddress = (TextView)dlg.findViewById(R.id.senderAddress);
        senderPhone = (TextView)dlg.findViewById(R.id.senderPhone);
        senderName.setText(name);
        senderAddress.setText(address);
        senderPhone.setText(phone);

        confirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
}