package blog.practice.capstone_ii.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import blog.practice.capstone_ii.Interface.CustomDialogListener;
import blog.practice.capstone_ii.R;

public class CustomDialog extends Dialog implements View.OnClickListener {

    String str = "";
    int ctnTime = 181;
    Context context;
    CustomDialogListener customDialogListener;


    EditText etString;
    TextView txtTime,txtError;
    Button btnOK;

    public CustomDialog(Context context, String str) {
        super(context);
        this.str = str;
        this.context = context;
        Log.d("확인" ,"str: " + str);
    }


    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_token);

        etString = findViewById(R.id.etString);
        txtTime = findViewById(R.id.txtTime);
        txtError = findViewById(R.id.txtError);
        btnOK = findViewById(R.id.btnOK);

        btnOK.setOnClickListener(this);

        TimeThread timeThread = new TimeThread();
        Thread t = new Thread(timeThread);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void onClick(View v) {
        String txtString = etString.getText().toString();
        if(txtString.equals(str)){
            customDialogListener.onPositiveClicked(true);
            dismiss();
        }
        else{
            txtError.setText("인증번호를 확인해주세요!");
            return ;
        }
    }

    class TimeThread implements Runnable{
        private boolean flag = true;
        @Override
        public void run() {
            while (flag){
                handler.sendEmptyMessage(0);
                ctnTime--;
                if(ctnTime==-1){
                    flag = false;
                    customDialogListener.onPositiveClicked(false);
                    dismiss();
                }
                try{
                    Thread.sleep(1000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==0){
                txtTime.setText(ctnTime+"");
            }
            super.handleMessage(msg);
        }
    };
}
