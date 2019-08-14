package com.cookandroid.qqrity_deliver;

import android.app.Dialog;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TextMessageDialog {
    private Context context;
    private String cphone;
    String messageText;
    RadioGroup radioGroup;
    Boolean state = false;
    Button sendBtn;
    Button cancelBtn;
    public TextMessageDialog (Context context) {
        this.context = context;
    }

    public void callSendMessage (String phone) {
        cphone = phone;
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_text_message);
        dlg.show();

        sendBtn = dlg.findViewById(R.id.sendMessage);
        cancelBtn = dlg.findViewById(R.id.dialogCancel);
        radioGroup = dlg.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                EditText inputText = dlg.findViewById(R.id.inputText);
                if(checkedId == R.id.radio1){
                    Log.e("radio click", "radio 1");
                    state = false;
                    inputText.setEnabled(false);
                    RadioButton r1 = dlg.findViewById(R.id.radio1);
                    messageText = r1.getText().toString();
                }else if(checkedId == R.id.radio2){
                    Log.e("radio click", "radio 2");
                    state = false;
                    inputText.setEnabled(false);
                    RadioButton r2 = dlg.findViewById(R.id.radio1);
                    messageText = r2.getText().toString();
                }else if(checkedId == R.id.radio3){
                    Log.e("radio click", "radio 3");
                    state = false;
                    inputText.setEnabled(false);
                    RadioButton r3 = dlg.findViewById(R.id.radio1);
                    messageText = r3.getText().toString();
                }else if(checkedId == R.id.radio4){
                    Log.e("radio click", "radio 4");
                    state = true;
                    inputText.setEnabled(true);
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("messageText", cphone+"///"+messageText);
                try{
                    if (state){
                        EditText message = dlg.findViewById(R.id.inputText);
                        messageText = message.getText().toString();
                    }

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(cphone, null, messageText, null, null);

                    dlg.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
}