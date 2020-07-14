package blog.practice.capstone_ii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import blog.practice.capstone_ii.DataVO.UserVO;
import blog.practice.capstone_ii.CustomView.CustomActionBar;
import blog.practice.capstone_ii.Email.SendMail;
import blog.practice.capstone_ii.Fragment.EmailFragment;
import blog.practice.capstone_ii.Fragment.NameFragment;
import blog.practice.capstone_ii.Fragment.PWFragment;
import blog.practice.capstone_ii.Fragment.PhoneFragment;
import blog.practice.capstone_ii.OkHttpConnetion.HttpConnection;
import blog.practice.capstone_ii.Util.OnSingleClickListener;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements Dialog.OnCancelListener{

    private EmailFragment emailFragment;
    private NameFragment nameFragment;
    private PWFragment pwFragment;
    private PhoneFragment phoneFragment;

    private FragmentPagerAdapter adapterViewPager;
    private ViewPager vpPager;
    private Button btn_okay;
    private LinearLayout line3;
    private int currentIndex;
    private static String [] list = new String[4];

    private ProgressBar progressBar;
    private View view;
    private Paint paint;

    final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setActionBar();
        init();

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("Check","onPageScrolled : "+position);
                // 페이지가 드래그되는 순간 페이지의 위치를 확인
            }
            @Override
            public void onPageSelected(int position) {
                //Log.d("Check","onPageSelected : "+position);
                // 페이지가 이동이 완료되면 순간 페이지의 위치를 확인
                if(position<3){
                    btn_okay.setVisibility(View.GONE);
                    line3.setVisibility(View.VISIBLE);
                }
                else if(position==3){
                    btn_okay.setVisibility(View.VISIBLE);
                    line3.setVisibility(View.GONE);
                }
                Log.d("확인", "현재 페이지: " + position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
//                state에는 3가지 값이 존재한다.
//                0 : SCROLL_STATE_IDLE 종료 시점
//                1 : SCROLL_STATE_DRAGGING 드래그 시점
//                2 : SCROLL_STATE_SETTLING 고정(버튼으로 이동)
                //Log.d("Check","onPageScrollStateChanged : "+state);
            }
        });

        btn_okay.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                emailFragment = new EmailFragment();
                list [0] = emailFragment.getText();
                Log.d("확인",TAG+ " " +  list[0]);
                if(checking_input(0,list[0])){
                    vpPager.setCurrentItem(0);
                    return;
                }
                pwFragment = new PWFragment();
                list[1] = pwFragment.getText();
                Log.d("확인",TAG+ " " +  list[1]);
                if(checking_input(1,list[1])){
                    vpPager.setCurrentItem(1);
                    return;
                }

                nameFragment = new NameFragment();
                list [2] = nameFragment.getText();
                Log.d("확인", TAG+ " " + list[2]);
                if(checking_input(2,list[2])){
                    vpPager.setCurrentItem(2);
                    return;
                }

                phoneFragment = new PhoneFragment();
                list[3] = phoneFragment.getText();
                Log.d("확인",TAG+ " " +  list[3]);
                if(checking_input(3,list[3])){
                    vpPager.setCurrentItem(3);
                    return;
                }
                showProgressView();
                UserVO userVO = new UserVO("normal",list[0],list[1],list[2],list[3]);
                sendData(userVO);
            }
        });
    }

    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

    private void init(){
        vpPager = findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);

        view = findViewById(R.id.view_dark);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(100);
        view.setBackgroundColor(paint.getColor());
        progressBar = findViewById(R.id.progressBar);

        line3 = findViewById(R.id.line3);
        btn_okay = findViewById(R.id.btn_okay);

        // 메일 전송을 위한 빌드
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
    }

    private int getNextPossibleItemIndex (int change) {
        currentIndex = vpPager.getCurrentItem();
        //Log.d("Check","currentIndex: " + currentIndex);
        int total = vpPager.getAdapter().getCount();

        if (currentIndex + change < 0) {
            return 0;
        }

        return Math.abs((currentIndex + change) % total) ;
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return EmailFragment.newInstance();
                case 1:
                    return PWFragment.newInstance();
                case 2:
                    return NameFragment.newInstance();
                case 3:
                    return PhoneFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page" + position;
        }
    }

    public void rgActivityButtonClick(View v){
        switch (v.getId()){
            case R.id.img_back_button:{
                onBackPressed();
                break;
            }
            case R.id.btn_next:{
                vpPager.setCurrentItem(getNextPossibleItemIndex(1));
                break;
            }
            case R.id.btn_prev:{
                vpPager.setCurrentItem(getNextPossibleItemIndex(-1));
                break;
            }
            case R.id.btn_emailAuth:{
                authDialog.cancel();
            }
            default:
                break;
        }
    }

    private boolean checking_input(int sequence, String inputString){
        switch(sequence){
            case 0:
                if(!Patterns.EMAIL_ADDRESS.matcher(inputString).matches()){
                    Toast.makeText(this,"잘못된 이메일 형식입니다." , Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;
            case 1:
                if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[`~!@$%#\\^&*\\-_\\+=\"])[A-Za-z\\d!\"$%#\\^&*!@\\`\\~_=\\+\\-\"]{8,16}$",inputString)){
                    Toast.makeText(this,"잘못된 비밀번호 형식입니다." , Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;
            case 2:
                if(inputString.equals("")){
                    Toast.makeText(this,"잘못된 이름 형식입니다." , Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;
            case 3:
                if(inputString.matches("/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/g")){
                    Toast.makeText(this,"휴대전화 번호가 아닙니다." , Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;
        }
        return false;
    }


    private HttpConnection httpConnection = HttpConnection.getInstance();
    // TODO 중복이 많음(코드 간소화 필요)
    private void sendData(final UserVO userVO){
        new Thread(){
            @Override
            public void run() {
                httpConnection.requestWebServer("register.do",userVO,callback);
            }
        }.start();
    }

    private String flag = "";
    private Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("확인", TAG+ " 오류 발생, " + e.getMessage());

        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                JSONObject object = new JSONObject(response.body().string());
                flag = object.getString("flag");
                Log.d("확인", TAG+ " 서버에서 받은 값: " + flag);

                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag.equals("true")) {
                                Log.d("확인", TAG+ " 회원가입 성공");
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                String subject = "Capstone 이메일 인증 절차입니다.";
                                String content = "이메일 계정을 확인하기 위해 아래의 링크를 클릭하여 인증 절차를 진행해 주세요"+"\nhttp://jypjun1234.cafe24.com/mobile/emailTest.do?email="+list[0];
                                SendMail sendMail = new SendMail(subject,content);
                                sendMail.sendSecurityCode(getApplicationContext(),list[0]);
                                showDialog();
                            } else if (flag.equals("false")) {
                                Log.d("확인",TAG+ " 회원가입 실패");
                                Toast.makeText(getApplicationContext(), "이메일이 중복됩니다.", Toast.LENGTH_SHORT).show();
                                goneProgressView();
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

    private LayoutInflater dialog; //LayoutInflater
    private View dialogLayout; //layout을 담을 View
    private Dialog authDialog; //dialog 객체
    private void showDialog(){
        dialog = LayoutInflater.from(this);
        dialogLayout = dialog.inflate(R.layout.auth_dialog, null); // LayoutInflater를 통해 XML에 정의된 Resource들을 View의 형태로 반환 시켜 줌
        authDialog = new Dialog(this); //Dialog 객체 생성
        authDialog.setContentView(dialogLayout); //Dialog에 inflate한 View를 탑재 하여줌
        authDialog.setCanceledOnTouchOutside(false); //Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
        authDialog.setOnCancelListener(this); //다이얼로그를 닫을 때 일어날 일을 정의하기 위해 onCancelListener 설정
        authDialog.show(); //Dialog를 나타내어 준다.
    }

    private void redirectLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressView(){;
        view.bringToFront();
        view.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        progressBar.setVisibility(View.VISIBLE);
        btn_okay.setEnabled(false);
    }

    private void goneProgressView(){;
        view.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        btn_okay.setEnabled(true);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        redirectLoginActivity();
    } //다이얼로그 닫을 때 카운트 다운 타이머의 cancel()메소드 호출
}
