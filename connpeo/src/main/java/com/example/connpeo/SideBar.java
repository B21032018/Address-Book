package com.example.connpeo;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class SideBar extends TextView {
    private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint textPaint;
    private Paint bigTextPaint;
    private Paint scaleTextPaint;

    private Canvas canvas;
    private int itemH;
    private int w;
    private int h;
    /**
     * 普通情况下字体大小
     */
    float singleTextH;
    /**
     * 缩放离原始的宽度
     */
    private float scaleWidth;
    /**
     * 滑动的Y
     */
    private float eventY = 0;
    /**
     * 缩放的倍数
     */
    private int scaleSize = 1;
    /**
     * 缩放个数item，即开口大小
     */
    private int scaleItemCount = 6;
    private ISideBarSelectCallBack callBack;

    //使用 new SideBar(context) 创建一个 SideBar 实例时，会调用这个构造方法
    //调用了第二个构造方法 SideBar(Context context, AttributeSet attrs)，
    // 并将 attrs 参数传递为 null
    // 这个构造方法的作用是为了在不需要处理 XML 属性时提供一个简便的初始化方式
    public SideBar(Context context) {
        this(context, null);
    }

    //在 XML 文件中使用 <com.example.SideBar> 标签时，系统会调用这个构造方法来创建 SideBar 实例
    //它调用了第三个构造方法 SideBar(Context context, AttributeSet attrs, int defStyleAttr)，
    // 并将 defStyleAttr 参数传递为 0。这个构造方法的作用是在处理 XML 属性时提供更多的初始化选项
    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //context 是用于访问应用程序资源的上下文，attrs 是一个 AttributeSet 对象，
    // 用于从 XML 中读取属性，defStyleAttr 是一个主题资源 ID
    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    //初始化 SideBar 控件时，根据 XML 中定义的自定义属性设置相关的尺寸和文本画笔样式
    private void init(AttributeSet attrs) {

        //如果自定义属性attrs 不为 null，则使用
        // getContext().obtainStyledAttributes(attrs,R.styleable.SideBar) 方法
        // 获取一个 TypedArray 对象 ta，
        // 通过 R.styleable.SideBar 来获取自定义属性集合 SideBar 中的属性
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SideBar);
            scaleSize = ta.getInteger(R.styleable.SideBar_scaleSize, 1);
            scaleItemCount = ta.getInteger(R.styleable.SideBar_scaleItemCount, 6);
            scaleWidth = ta.getDimensionPixelSize(R.styleable.SideBar_scaleWidth, dp(100));
            ta.recycle();
        }
        textPaint = createPaint(getTextSize());
        bigTextPaint = createPaint(getTextSize() * (scaleSize + 3));
        scaleTextPaint = createPaint(getTextSize() * (scaleSize + 1));
    }

    private Paint createPaint(float textSize) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getCurrentTextColor());
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    //设置回调接口，这个接口定义了在侧边栏中选择项目后的回调方法
    public void setOnStrSelectCallBack(ISideBarSelectCallBack callBack) {
        this.callBack = callBack;
    }

    //将像素单位转换为设备独立像素
    // 通过 density 属性获取当前屏幕的密度比例，并将传入的像素值转换成dp单位后返回
    private int dp(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    //处理触摸事件，并根据触摸的位置执行相应的操作
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (event.getX() > (w - getPaddingRight() - singleTextH - 10)) {
                    eventY = event.getY();
                    invalidate();
                    return true;
                } else {
                    eventY = 0;
                    invalidate();
                    break;
                }
            case MotionEvent.ACTION_CANCEL:
                eventY = 0;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if (event.getX() > (w - getPaddingRight() - singleTextH - 10)) {
                    eventY = 0;
                    invalidate();
                    return true;
                } else
                    break;
        }
        return super.onTouchEvent(event);
    }


    //绘制视图内容，需要重新绘制视图时系统调用此方法
    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        DrawView(eventY);
    }

    //根据触摸位置y来确定当前选中的索引，并绘制视图
    private void DrawView(float y) {
        int currentSelectIndex = -1;
        if (y != 0) {
            for (int i = 0; i < letters.length; i++) {
                float currentItemY = itemH * i;  // 计算当前项的Y坐标
                float nextItemY = itemH * (i + 1);
                if (y >= currentItemY && y < nextItemY) {  // 如果触摸位置在当前项范围内
                    currentSelectIndex = i;  // 更新当前选中索引为当前项索引
                    if (callBack != null) {
                        callBack.onSelectStr(currentSelectIndex, letters[i]);  // 调用回调接口通知选中的字母（或选项）
                    }
                    //画大的字母
                    Paint.FontMetrics fontMetrics = bigTextPaint.getFontMetrics();
                    float bigTextSize = fontMetrics.descent - fontMetrics.ascent;
                    canvas.drawText(letters[i], w - getPaddingRight() - scaleWidth - bigTextSize, singleTextH + itemH * i, bigTextPaint);
                }
            }
        }
        drawLetters(y, currentSelectIndex);
    }

    private void drawLetters(float y, int index) {
        //第一次进来没有缩放情况，默认画原图
        // 确定每个字母在垂直方向的位置绘制每个字母到对应的位置。
        if (index == -1) {
            w = getMeasuredWidth();
            h = getMeasuredHeight();
            itemH = h / letters.length;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            singleTextH = fontMetrics.descent - fontMetrics.ascent;
            //确定每个字母在垂直方向的位置，使用 textPaint 对象绘制每个字母到对应的位置
            for (int i = 0; i < letters.length; i++) {
                canvas.drawText(letters[i], w - getPaddingRight(), singleTextH + itemH * i, textPaint);
            }
            //触摸的时候画缩放图
        } else {
            //遍历所有字母
            for (int i = 0; i < letters.length; i++) {
                //要画的字母的起始Y坐标
                float currentItemToDrawY = singleTextH + itemH * i;
                float centerItemToDrawY;
                if (index < i)
                    centerItemToDrawY = singleTextH + itemH * (index + scaleItemCount);
                else
                    centerItemToDrawY = singleTextH + itemH * (index - scaleItemCount);
                float delta = 1 - Math.abs((y - currentItemToDrawY) / (centerItemToDrawY - currentItemToDrawY));
                float maxRightX = w - getPaddingRight();
                //如果大于0，表明在y坐标上方
                scaleTextPaint.setTextSize(getTextSize() + getTextSize() * delta);
                float drawX = maxRightX - scaleWidth * delta;
                //超出边界直接画在边界上
                if (drawX > maxRightX)
                    canvas.drawText(letters[i], maxRightX, singleTextH + itemH * i, textPaint);
                else
                    canvas.drawText(letters[i], drawX, singleTextH + itemH * i, scaleTextPaint);
            }
        }
    }

    //当用户触摸并选择了某个字母时，可以通过这个接口通知外部代码，
    //传递选择的字母索引 index 和具体的字母 selectStr
    public interface ISideBarSelectCallBack {
        void onSelectStr(int index, String selectStr);
    }

}

