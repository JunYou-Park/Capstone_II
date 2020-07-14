package blog.practice.capstone_ii;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import blog.practice.capstone_ii.Help.IntroMain;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityManager am;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    // 현재 프래그먼트 위치
    private String nowFragment = "nav_home";
    // 인트로
    private int intro = 0;

    private Button btn_help;
    private ImageView img_help;
    private int helpCnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_diet)
                .setDrawerLayout(drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        btn_help = findViewById(R.id.btn_help);
        img_help = findViewById(R.id.img_help);

        btn_help.setOnClickListener(v->{
            if(helpCnt==1){
                img_help.setImageResource(R.drawable.home_help2);
                btn_help.setBackgroundResource(R.drawable.ic_btn_help2);
                helpCnt=2;
            }
            else{
                img_help.setVisibility(View.GONE);
                btn_help.setVisibility(View.GONE);
            }
        });

        sp = getSharedPreferences("UserData", MODE_PRIVATE);
        editor = sp.edit();
        loadShared();
        showLog(intro+"");
        if(intro==0){
            Intent intent = new Intent(this, IntroMain.class);
            startActivityForResult(intent, 100);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                signOut();
                break;
            case R.id.action_help:
                // 현재 프래그먼트를 추출 (도움말 기능을 각 프래그먼트 마다 다르게 설정하기 위해)
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                String tmp =  navController.getCurrentDestination().toString();
                nowFragment = tmp.substring( (tmp.indexOf("/")+1), (tmp.indexOf(")")));
                showLog(tmp);
                showLog(nowFragment);
                if(nowFragment.equals("nav_home")){
                    img_help.setImageResource(R.drawable.home_help);
                    btn_help.setBackgroundResource(R.drawable.ic_btn_help);
                    helpCnt=1;
                    img_help.setVisibility(View.VISIBLE);
                    btn_help.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(this, "도움말이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*쉐어드에 입력값 저장*/
    private void saveShared(String id, String pw) {
        editor.putString("token", id);
        editor.putString("pw", pw);
        editor.apply();
    }

    private void loadShared(){
        // 0이면 intro가 아직 실행되지 않은 상태
        intro = sp.getInt("intro", 0);
    }

    private void signOut() {
        saveShared("", "");
        redirectLoginActivity();
    }

    private void redirectLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private long lastTimeBackPressed=0;
    private Toast toast;
    @Override
    public void onBackPressed() {
        if (lastTimeBackPressed == 0) {
            showExitGuide();
            lastTimeBackPressed = System.currentTimeMillis();
        }
        // 두번 눌러서 종료
        else {
            int seconds = (int) (System.currentTimeMillis() - lastTimeBackPressed);
            if (seconds > 2000) {
                showExitGuide();
                lastTimeBackPressed = 0;
            }
            else {
                am.restartPackage(getPackageName());
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        }
    }
    private void showExitGuide(){
        toast = Toast.makeText(MainActivity.this,"뒤로 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showLog(String str){
        Log.d("확인", TAG + ", " + str);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            // 인트로 실행 상태 저장
            editor.putInt("intro", 1);
            editor.apply();
        }
    }
}
