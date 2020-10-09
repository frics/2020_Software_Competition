package kr.ac.ssu.billysrecipe.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class SettingGridView  extends GridView {

    public SettingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingGridView(Context context) {
        super(context);
    }

    public SettingGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}