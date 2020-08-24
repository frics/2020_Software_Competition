package kr.ac.ssu.myrecipe.design;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.jar.Attributes;

import kr.ac.ssu.myrecipe.R;

@SuppressLint("AppCompatCustomView")
public class TextBorderEffect extends TextView {

    private boolean stroke = false;
    private float strokeWidth = 0.0f;
    private int strokeColor;

    public TextBorderEffect(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public TextBorderEffect(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TextBorderEffect(Context context) {
        super(context);
    }

    private void initView(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextBorderEffect);
        stroke = a.getBoolean(R.styleable.CustomTextBorderEffect_textStroke, false);
        strokeWidth = a.getFloat(R.styleable.CustomTextBorderEffect_textStrokeWidth, 0.0f);
        strokeColor= a.getColor(R.styleable.CustomTextBorderEffect_textStrokeColor, 0xffffffff);
    }

    @Override
    protected void onDraw(Canvas canvas){
        if(stroke){
            ColorStateList states = getTextColors();
            getPaint().setStyle(Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            super.onDraw(canvas);
            getPaint().setStyle(Style.FILL);
            setTextColor(states);
        }
        super.onDraw(canvas);
    }
}
