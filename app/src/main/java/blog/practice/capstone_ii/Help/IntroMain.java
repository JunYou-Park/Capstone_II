package blog.practice.capstone_ii.Help;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

import blog.practice.capstone_ii.MainActivity;

public class IntroMain extends AppIntro {

    private Fragment intro1 = new Intro1();
    private Fragment intro2 = new Intro2();
    private Fragment intro3 = new Intro3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        doFullScreen();
        addSlide(intro1);
        addSlide(intro2);
        addSlide(intro3);

        setZoomAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment,
                               @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void doFullScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE|
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }
}
