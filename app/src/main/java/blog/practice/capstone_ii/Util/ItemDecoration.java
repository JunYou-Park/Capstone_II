package blog.practice.capstone_ii.Util;

import android.app.Activity;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private int outerMargin;

    public ItemDecoration(Activity activity){
        spanCount = 2;
        spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                12, activity.getResources().getDisplayMetrics());
        outerMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50, activity.getResources().getDisplayMetrics());

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int maxCount = parent.getAdapter().getItemCount();
        int postion = parent.getChildAdapterPosition(view);
        int column = postion%spanCount;
        int row = postion / spanCount;
        int lastRow = (maxCount-1) / spanCount;

        outRect.left = column * spacing / spanCount;
        outRect.right = spacing - (column+1) * spacing / spanCount;
        outRect.top = spacing * 2;

        if(row==lastRow){
            outRect.bottom = outerMargin;
        }

        super.getItemOffsets(outRect, view, parent, state);

    }
}
