package blog.practice.capstone_ii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import blog.practice.capstone_ii.DataVO.UserVO;
import blog.practice.capstone_ii.CustomView.CustomActionBar;
import blog.practice.capstone_ii.CustomView.CustomDialog;
import blog.practice.capstone_ii.Email.SendMail;
import blog.practice.capstone_ii.Fragment.FindPasswordFragment;
import blog.practice.capstone_ii.Fragment.FindTokenFragment;
import blog.practice.capstone_ii.Interface.CustomDialogListener;
import blog.practice.capstone_ii.OkHttpConnetion.HttpConnection;
import blog.practice.capstone_ii.Util.StringUtility;
import blog.practice.capstone_ii.Util.OnSingleClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindInfoActivity extends AppCompatActivity{

    private FragmentManager fragmentManager;
    private FindPasswordFragment findPasswordFragment;
    private FindTokenFragment findTokenFragment;
    private FragmentTransaction transaction;

    private boolean btnState = true;
    private Button btn_f_pw, btn_f_token, btn_f_submit;
    private TextView txtString;
    private SmsManager mSmsManager;

    private String smsText = "";

    private HttpConnection httpConnection = HttpConnection.getInstance();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_info);
        setActionBar();

        context = this;

        btn_f_pw = findViewById(R.id.btn_f_pw);
        btn_f_token = findViewById(R.id.btn_f_token);
        btn_f_submit = findViewById(R.id.btn_f_submit);
        txtString = findViewById(R.id.txtString);

        view = findViewById(R.id.view_dark2);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(100);
        view.setBackgroundColor(paint.getColor());
        progressBar2 = findViewById(R.id.progressBar2);

        fragmentManager = getSupportFragmentManager();

        findPasswordFragment = new FindPasswordFragment();
        findTokenFragment = new FindTokenFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.f_frameLayout, findTokenFragment).commitAllowingStateLoss();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

        btn_f_submit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(btnState){ // 인증번호 전송
                    FindTokenFragment findTokenFragment = new FindTokenFragment();
                    ftPhone = findTokenFragment.getFTPhone();
                    ftName = findTokenFragment.getFTName();
                    UserVO userVO = new UserVO();
                    userVO.setPhone(ftPhone);
                    userVO.setName(ftName);
                    smsText = StringUtility.create_int(smsText);
                    mSmsManager = SmsManager.getDefault();
                    Log.d("확인", "난수 생성");
                    sendDataToken("findToken.do",userVO);
                    showProgressView();
                }
                else{ // 비밀번호 전송
                    FindPasswordFragment findPasswordFragment = new FindPasswordFragment();
                    fpToken = findPasswordFragment.getFPToken();
                    fpName = findPasswordFragment.getFPName();
                    Log.d("확인", "fpToken: " + fpToken + ", fpName: " + fpName);
                    UserVO userVO = new UserVO();
                    userVO.setToken(fpToken);
                    userVO.setName(fpName);
                    sendDataPW("findPW.do",userVO);
                    showProgressView();
                }
            }
        });
    }

    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }
    String fpToken,fpName,ftPhone, ftName;
    public void fOnClick(View v){
        switch (v.getId()){
            case R.id.btn_f_token:
                btnState = true;
                btn_f_pw.setEnabled(true);
                btn_f_pw.setTextColor(getColorStateList(R.color.black));
                btn_f_pw.setBackgroundResource(R.drawable.btn_normal);
                btn_f_token.setTextColor(getColorStateList(R.color.white));
                btn_f_token.setBackgroundResource(R.drawable.btn_press);
                btn_f_token.setEnabled(false);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.f_frameLayout, findTokenFragment).commitAllowingStateLoss();
                btn_f_submit.setText("인증번호 전송");

                break;
            case R.id.btn_f_pw:
                btnState = false;
                btn_f_token.setEnabled(true);
                btn_f_token.setTextColor(getColorStateList(R.color.black));
                btn_f_token.setBackgroundResource(R.drawable.btn_normal);
                btn_f_pw.setTextColor(getColorStateList(R.color.white));
                btn_f_pw.setBackgroundResource(R.drawable.btn_press);
                btn_f_pw.setEnabled(false);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.f_frameLayout, findPasswordFragment).commitAllowingStateLoss();
                btn_f_submit.setText("비밀번호 전송");
                break;
            case R.id.img_back_button2:
                onBackPressed();
                break;
        }
    }

    private ProgressBar progressBar2;
    private View view;
    private Paint paint;

    private void showProgressView(){;
        view.bringToFront();
        view.setVisibility(View.VISIBLE);
        progressBar2.bringToFront();
        progressBar2.setVisibility(View.VISIBLE);
        btn_f_submit.setEnabled(false);
    }

    private void goneProgressView(){;
        view.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
        btn_f_submit.setEnabled(true);
    }


    private void sendDataPW(final String url, final UserVO userVO){
        Log.d("확인", url+" 실행");
        new Thread(){
            @Override
            public void run() {
                httpConnection.requestWebServer(url,userVO,findPW_callback);
            }
        }.start();
    }

    private void sendDataToken(final String url, final UserVO userVO){
        Log.d("확인", url+" 실행");
        new Thread(){
            @Override
            public void run() {
                httpConnection.requestWebServer(url,userVO,findToken_callback);
            }
        }.start();
    }

    private String pw = "false";
    private String token = "false";
    private Callback findPW_callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("확인", "오류 발생, " + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                JSONObject object = new JSONObject(response.body().string());
                pw = object.getString("pw");
                Log.d("확인", "pw: " + pw);
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pw.equals("false")) {
                                Log.d("확인", "DB에 이름 또는 이메일 존재하지 않음");
                                Toast.makeText(getApplicationContext(), "이름 또는 이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                                goneProgressView();
                                return;
                            } else{
                                String subject = "Capstone 임시 비밀번호가 발급되었습니다.";
                                String content = "임시 비밀번호가 발급되었습니다. 로그인후 변경해주세요\n"+"임시 비밀번호: "+pw;
                                SendMail sendMail = new SendMail(subject,content);
                                // mainThread에서 sendmail까지 처리하려니까 오류가 남.... 그래서 서브 Thread 하나 더 생성(작동은 되는데, 수정이 필요해보임)
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendMail.sendSecurityCode(getApplicationContext(),fpToken);
                                    }
                                }).start();
                                Log.d("확인", "DB에 이메일 존재");
                                Toast.makeText(getApplicationContext(), "비밀번호가 재발급되었습니다.", Toast.LENGTH_SHORT).show();
                                redirectLoginActivity();
                            }
                        }
                    });

                }
            } catch (IOException e) {
                Log.d("Check", "IOException: " + e.getMessage());
            } catch (JSONException e) {
                Log.d("Check", "JSONException: " + e.getMessage());
            } catch (Exception e) {
                Log.d("Check", "Exception: " + e.getMessage());
            }
        }
    };

    private Callback findToken_callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("확인", "오류 발생, " + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                JSONObject object = new JSONObject(response.body().string());
                token = object.getString("token");
                Log.d("확인", "token: " + token);
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (token.equals("false")) {
                                Log.d("확인", "DB에 이름 또는 전화번호가 존재하지 않음");
                                Toast.makeText(getApplicationContext(), "이름 또는 전화번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                goneProgressView();
                                return;

                            } else{
                                // thread를 추가로 생성해야할 수 도 있음
                                goneProgressView();
                                mSmsManager.sendTextMessage(ftPhone, null, "본인확인 인증번호["+smsText+"]를 화면에 입력해주세요.", null, null);
                                showDialog(smsText);
                            }
                        }
                    });

                }
            } catch (IOException e) {
                Log.d("Check", "IOException: " + e.getMessage());
            } catch (JSONException e) {
                Log.d("Check", "JSONException: " + e.getMessage());
            } catch (Exception e) {
                Log.d("Check", "Exception: " + e.getMessage());
            }
        }
    };

    private void showDialog(String smsText){
        CustomDialog customDialog = new CustomDialog(this, smsText);
        customDialog.setDialogListener(new CustomDialogListener() {
            @Override
            public void onPositiveClicked(boolean flag) {

                if(flag){
                    txtString.setText("이메일 주소는 "+token+" 입니다.");
                }
                else{
                    txtString.setText("인증시간이 만료되었습니다. 다시 인증해주세요.");
                    token = "";
                }
            }
            @Override
            public void onNegativeClicked() {

            }
        });
        customDialog.show();
    }

    private void redirectLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
