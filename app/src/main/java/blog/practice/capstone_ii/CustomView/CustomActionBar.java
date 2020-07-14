package blog.practice.capstone_ii.CustomView;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import blog.practice.capstone_ii.R;

public class CustomActionBar {

    private Activity activity;
    private ActionBar actionBar;

    public CustomActionBar(Activity activity, ActionBar actionBar){
        this.activity = activity;
        this.actionBar = actionBar;
    }


    public void setActionBar(){
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);			//액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);		//액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);			//홈 아이콘을 숨김처리합니다.
        View mCustomView = null;
        Log.d("확인", activity.toString());
        if(activity.toString().contains("RegisterActivity")){
            mCustomView = LayoutInflater.from(activity).inflate(R.layout.actionbar_register, null);
        }
        else if(activity.toString().contains("FindInfoActivity")){
            mCustomView = LayoutInflater.from(activity).inflate(R.layout.actionbar_findinfo, null);
        }
        else if(activity.toString().contains("UserinfoActivity")){
            mCustomView = LayoutInflater.from(activity).inflate(R.layout.actionbar_userinfo, null);
        }

        actionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar)mCustomView.getParent();
        parent.setContentInsetsAbsolute(0,0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

    }
}
