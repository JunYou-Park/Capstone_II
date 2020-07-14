package blog.practice.capstone_ii.Util;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {

    // 중복 클릭 방지 시간 설정 ( 해당 시간 이후에 다시 클릭 가능 )
    private static final long MIN_CLICK_INTERVAL = 600;
    private long mLastClickTime = 0;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapseTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        if(elapseTime > MIN_CLICK_INTERVAL){
            onSingleClick(v);
        }
    }
}
