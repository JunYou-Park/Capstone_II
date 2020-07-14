package blog.practice.capstone_ii;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import blog.practice.capstone_ii.DataVO.UserVO;
import blog.practice.capstone_ii.OkHttpConnetion.HttpConnection;
import blog.practice.capstone_ii.Util.OnSingleClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";

    // 객체들
    private TextInputLayout input_email_layout, input_pw_layout;
    private TextInputEditText et_email, et_pw;
    private Button btn_normal_login;
    private TextView txt_register,txt_findInfo;
    private Activity activity;
    private ProgressBar progressBar;
    private View view;
    private Paint paint;
    private String type = "";
    private String id = "";
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    //************************************init()*************************************
    private void init(){
        sp = getSharedPreferences("UserData", MODE_PRIVATE);
        editor = sp.edit();
        view = findViewById(R.id.view_dark);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(100);
        view.setBackgroundColor(paint.getColor());
        progressBar = findViewById(R.id.progressBar);
        input_email_layout = findViewById(R.id.input_email_layout);
        input_pw_layout = findViewById(R.id.input_pw_layout);
        et_email = findViewById(R.id.et_email);
        et_pw = findViewById(R.id.et_pw);
        btn_normal_login = findViewById(R.id.btn_normal_login);
        txt_register = findViewById(R.id.txt_register);
        txt_findInfo = findViewById(R.id.txt_findInfo);
        input_pw_layout.setPasswordVisibilityToggleEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        activity = this;
        // 자동 로그인을 위해 shared 가져옴
        loadShared();
        // 자동 로그인을 위해 사용
        if(!email.equals("")&&!pw.equals("")) autoLogin();

        btn_normal_login.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                email = et_email.getText().toString();
                pw = et_pw.getText().toString();
                if(!email.equals("")&&!pw.equals("")){
                    showProgressView();
                    UserVO userVO = new UserVO(email,pw);
                    sendData(userVO);
                }
                else{
                    et_email.setError("이메일 또는 비밀번호를 입력하세요");
                    et_email.requestFocus();
                    return;
                }
            }
        });
        txt_register.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent register_intent = new Intent(activity, RegisterActivity.class);
                startActivity(register_intent);
            }
        });
        txt_findInfo.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent finding_intent = new Intent(activity, FindInfoActivity.class);
                startActivity(finding_intent);
            }
        });
    }

    //************************************일반 로그인*************************************
    private String email, pw;

    private HttpConnection httpConnection = HttpConnection.getInstance();
    // TODO 중복이 많음(코드 간소화 필요)
    private void sendData(final UserVO userVO){
        new Thread(){
            @Override
            public void run() {
                httpConnection.requestWebServer("login.do",userVO,normal_callback);
            }
        }.start();
    }
    String flag = "";
    String vf_token="";
    private Callback normal_callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("확인", TAG+ " " + "오류 발생, " + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                JSONObject object = new JSONObject(response.body().string());
                Log.d("확인", TAG+ " " + "object: " + object.toString());
                vf_token = object.getString("vf_token");
                flag = object.getString("flag");
                Log.d("확인", TAG+ " " + "vf_token: " + vf_token + ",  flag: " + flag);

                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(flag.equals("true")){
                                if (vf_token.equals("true")) {
                                    Log.d("확인", TAG+ " " + "로그인 성공");
                                    Toast.makeText(getApplicationContext(), "일반 로그인 성공", Toast.LENGTH_SHORT).show();
                                    redirectMainActivity(email,pw);
                                } else if (vf_token.equals("false")) {
                                    goneProgressView();
                                    showMessage("이메일 인증이 필요합니다.");
                                    return;
                                }
                            }
                            else{
                                Log.d("확인", TAG+ " " + "로그인 실패");
                                goneProgressView();
                                et_email.requestFocus();
                                et_email.setError("이메일 또는 비밀번호를 확인해주세요");
                                return;
                            }
                        }
                    });

                }
            } catch (IOException e) {
                Log.d("확인", TAG+ " " + "IOException: " + e.getMessage());
            } catch (JSONException e) {
                Log.d("확인", TAG+ " " + "JSONException: " + e.getMessage());
            } catch (Exception e) {
                Log.d("확인", TAG+ " " + "Exception: " + e.getMessage());
            }

        }
    };

    // 로그인 성공 시
    private void redirectMainActivity(String id, String pw) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        saveShared(id,pw);
        startActivity(intent);
        finish();
    }

    /*쉐어드값 불러오기*/
    private void loadShared() {
        email = sp.getString("token", "");
        pw = sp.getString("pw", "");
        Log.d("확인","email: "+email+", pw:"+pw);
    }

    /*쉐어드에 입력값 저장*/
    private void saveShared(String id, String pw) {
        editor.putString("token", id);
        editor.putString("pw", pw);
        editor.apply();
    }

    public void showMessage(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    private void showProgressView(){;
        view.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        btn_normal_login.setEnabled(false);
        et_email.setEnabled(false);
        et_pw.setEnabled(false);
    }

    private void goneProgressView(){;
        view.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        btn_normal_login.setEnabled(true);
        et_email.setEnabled(true);
        et_pw.setEnabled(true);
    }

    //************************************자동 로그인*************************************
    private void autoLogin(){
        showProgressView();
        redirectMainActivity(email,pw);
    }
}
