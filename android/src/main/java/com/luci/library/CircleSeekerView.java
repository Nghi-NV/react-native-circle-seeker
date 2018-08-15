package com.luci.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;


public class CircleSeekerView extends View {
    private static final String TAG = "CircleTimerView";

    // Status
    // Default dimension in dp/pt
    private static final float DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 10;
    private static final float DEFAULT_CIRCLE_BUTTON_RADIUS = 15;
    private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 1;

    // Default color
    private static final int DEFAULT_CIRCLE_COLOR = 0xff66ffff;
    private static final int DEFAULT_CIRCLE_BUTTON_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_LINE_COLOR = 0xFFFECE02;
    private static final int DEFAULT_HIGHLIGHT_LINE_COLOR = 0xFF68C5D7;
    private static final int DEFAULT_NUMBER_COLOR = 0xFF181318;
    private static final int DEFAULT_TIMER_NUMBER_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_TIMER_COLON_COLOR = 0xFFFA7777;
    private static final int DEFAULT_TIMER_TEXT_COLOR = 0x99F0F9FF;

    // Paint
//    private Paint mCirclePaint;
    private Paint mLinePaint;
    private Paint mCircleButtonPaint1,mCircleButtonPaint2;
    private Paint mNumberPaint;

    // Dimension
    private float mGapBetweenCircleAndLine;
    private float mCircleButtonRadius;
    private float mCircleStrokeWidth;

    // Color
    private int mLineColor;
    private int mNumberColor;


    private  boolean mEnable = true;

    // Parameters
    private float mCx;
    private float mCy;
    private float mRadius;
    private float mCurrentRadian;
    private float mCurrentRadian1;
    private float mPreRadian;
    private boolean mInCircleButton;
    private boolean mInCircleButton1;


    private boolean mEnableChooseStart = true, mEnableChooseEnd = false;

    private OnTimeChangedListener mListener;

    public CircleSeekerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public CircleSeekerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSeekerView(Context context) {
        this(context, null);
    }

    private void initialize() {
        Log.d(TAG, "initialize");
        // Set default dimension or read xml attributes
        mGapBetweenCircleAndLine = DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE;
        mCircleButtonRadius = DEFAULT_CIRCLE_BUTTON_RADIUS;
        mCircleStrokeWidth = DEFAULT_CIRCLE_STROKE_WIDTH;

        // Set default color or read xml attributes
        mLineColor = DEFAULT_LINE_COLOR;
        mNumberColor = Color.RED; //DEFAULT_NUMBER_COLOR;

        // Init all paints
        mCircleButtonPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleButtonPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // CircleButtonPaint
        mCircleButtonPaint1.setColor(Color.CYAN);
        mCircleButtonPaint1.setStrokeWidth(mCircleButtonRadius * 2);
        mCircleButtonPaint1.setStyle(Paint.Style.STROKE);


        // CircleButtonPaint
        mCircleButtonPaint2.setColor(Color.TRANSPARENT);
        mCircleButtonPaint2.setStrokeWidth(mCircleButtonRadius * 2);
        mCircleButtonPaint2.setStyle(Paint.Style.STROKE);

        // LinePaint
        mLinePaint.setStrokeWidth(mCircleButtonRadius * 2);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setShader(new SweepGradient(getWidth()/ 2, getHeight() / 2, new int[]{Color.BLUE,Color.CYAN,Color.YELLOW,Color.GREEN}, null));


        // NumberPaint
        mNumberPaint.setColor(mNumberColor);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);
        mNumberPaint.setStyle(Paint.Style.STROKE);
        mNumberPaint.setStrokeWidth(mCircleButtonRadius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();

        canvas.drawCircle(mCx, mCy, mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine, mNumberPaint);
        canvas.save();
        canvas.rotate(-90, mCx, mCy);
        RectF rect = new RectF(mCx - (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
        ), mCy - (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
        ), mCx + (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
        ), mCy + (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
        ));

        if (mCurrentRadian1 > mCurrentRadian) {
            canvas.drawArc(rect, (float) Math.toDegrees(mCurrentRadian1), (float) Math.toDegrees(2 * (float) Math.PI) - (float) Math.toDegrees(mCurrentRadian1) + (float) Math.toDegrees(mCurrentRadian), false, mLinePaint);
        } else {
            canvas.drawArc(rect, (float) Math.toDegrees(mCurrentRadian1), (float) Math.toDegrees(mCurrentRadian) - (float) Math.toDegrees(mCurrentRadian1), false, mLinePaint);
        }
        canvas.restore();


        // TimerNumber// Vẽ hình tròn end
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian1), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, 0.01f, mCircleButtonPaint2);
        canvas.restore();

        //Ve hinh tron start
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine
                , 0.01f, mCircleButtonPaint1);
        canvas.restore();
        // TimerNumber
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mEnable){
            return false;
        }
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {

                // If the point in the circle button
                if (mEnableChooseStart && mInCircleButton(event.getX(), event.getY()) && isEnabled()) {
                    mInCircleButton = true;
                    mPreRadian = getRadian(event.getX(), event.getY());
                    Log.d(TAG, "In circle button");
                } else if (mEnableChooseEnd && mInCircleButton1(event.getX(), event.getY()) && isEnabled()) {
                    mInCircleButton1 = true;
                    mPreRadian = getRadian(event.getX(), event.getY());
                }
//                WritableNativeArray params = new WritableNativeArray();
//                params.pushDouble((int) Math.toDegrees(mCurrentRadian1));
//                params.pushDouble((int) Math.toDegrees(mCurrentRadian));
                ThemedReactContext reactContext = (ThemedReactContext) getContext();
                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onDownCircleSeeker", (int) Math.toDegrees(mCurrentRadian));
            }
            break;
            case MotionEvent.ACTION_MOVE: {

                if (mInCircleButton && isEnabled()) {
                    float temp = getRadian(event.getX(), event.getY());
                    if (mPreRadian > Math.toRadians(270) && temp < Math.toRadians(90)) {
                        mPreRadian -= 2 * Math.PI;
                    } else if (mPreRadian < Math.toRadians(90) && temp > Math.toRadians(270)) {
                        mPreRadian = (float) (temp + (temp - 2 * Math.PI) - mPreRadian);
                    }
                    mCurrentRadian += (temp - mPreRadian);
                    mPreRadian = temp;
                    if (mCurrentRadian > 2 * Math.PI) {
                        mCurrentRadian -= (float) (2 * Math.PI);
                    }
                    if (mCurrentRadian < 0) {
                        mCurrentRadian += (float) (2 * Math.PI);
                    }
                    invalidate();
                } else if (mInCircleButton1 && isEnabled()) {
                    float temp = getRadian(event.getX(), event.getY());
                    if (mPreRadian > Math.toRadians(270) && temp < Math.toRadians(90)) {
                        mPreRadian -= 2 * Math.PI;
                    } else if (mPreRadian < Math.toRadians(90) && temp > Math.toRadians(270)) {
                        mPreRadian = (float) (temp + (temp - 2 * Math.PI) - mPreRadian);
                    }
                    mCurrentRadian1 += (temp - mPreRadian);
                    mPreRadian = temp;
                    if (mCurrentRadian1 > 2 * Math.PI) {
                        mCurrentRadian1 -= (float) (2 * Math.PI);
                    }
                    if (mCurrentRadian1 < 0) {
                        mCurrentRadian1 += (float) (2 * Math.PI);
                    }
                    invalidate();
                }

//                WritableNativeArray params = new WritableNativeArray();
//                params.pushDouble((int) Math.toDegrees(mCurrentRadian1));
//                params.pushDouble((int) Math.toDegrees(mCurrentRadian));
                ThemedReactContext reactContext = (ThemedReactContext) getContext();
                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onChangeCircleSeeker", (int) Math.toDegrees(mCurrentRadian));


            }
            break;
            case MotionEvent.ACTION_UP: {

                if (mInCircleButton && isEnabled()) {
                    mInCircleButton = false;
                } else if (mInCircleButton1 && isEnabled()) {
                    mInCircleButton1 = false;
                }

//                WritableNativeArray params = new WritableNativeArray();
//                params.pushDouble((int) Math.toDegrees(mCurrentRadian1));
//                params.pushDouble((int) Math.toDegrees(mCurrentRadian));
                ThemedReactContext reactContext = (ThemedReactContext) getContext();
                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onUpCircleSeeker", (int) Math.toDegrees(mCurrentRadian));

            }
            break;
        }
        return true;
    }

    public void setAllowChooseStart(boolean chooseStart){
        mEnableChooseStart = chooseStart;
    }

    public void setAllowChooseEnd(boolean chooseEnd){
        mEnableChooseEnd = chooseEnd;
    }

    public void setColorCircleBackground(int c){
        mNumberPaint.setColor(c);
    }

    public void setColorCircle(int c){
        mLinePaint.setColor(c);
    }

    public void setColorPointStart(int c){
        mCircleButtonPaint1.setColor(c);
    }

    public void setColorPointEnd(int c){
        mCircleButtonPaint2.setColor(c);
    }

    public void setWithLineBackground(int with){
        mCircleButtonRadius = with;
        mNumberPaint.setStrokeWidth(mCircleButtonRadius * 2);
    }

    public void setWithLine(int with){
        mCircleButtonPaint1.setStrokeWidth(with * 2 );
        mCircleButtonPaint2.setStrokeWidth(with * 2 );
        mLinePaint.setStrokeWidth(with * 2 );
    }

    public void setGradientColorCircle(int [] colors) {
        mLinePaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, colors, null));
    }

    public void setCurrentRadianStart(float rad){
        mCurrentRadian1 = rad;
    }

    public void setCurrentRadianEnd(float rad){
        mCurrentRadian = rad;
    }

    public void setEnable(boolean enable){
        mEnable = enable;
    }

    // Whether the down event inside circle button
    private boolean mInCircleButton1(float x, float y) {
        float r = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine;
        float x2 = (float) (mCx + r * Math.sin(mCurrentRadian1));
        float y2 = (float) (mCy - r * Math.cos(mCurrentRadian1));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < mCircleButtonRadius) {
            return true;
        }
        return false;
    }

    // Whether the down event inside circle button
    private boolean mInCircleButton(float x, float y) {
        float r = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine;
        float x2 = (float) (mCx + r * Math.sin(mCurrentRadian));
        float y2 = (float) (mCy - r * Math.cos(mCurrentRadian));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < mCircleButtonRadius) {
            return true;
        }
        return false;
    }

    // Use tri to cal radian
    private float getRadian(float x, float y) {
        float alpha = (float) Math.atan((x - mCx) / (mCy - y));
        // Quadrant
        if (x > mCx && y > mCy) {
            // 2
            alpha += Math.PI;
        } else if (x < mCx && y > mCy) {
            // 3
            alpha += Math.PI;
        } else if (x < mCx && y < mCy) {
            // 4
            alpha = (float) (2 * Math.PI + alpha);
        }
        return alpha;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Ensure width = height
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        this.mCx = width / 2;
        this.mCy = height / 2;
        // Radius
        if (mGapBetweenCircleAndLine + mCircleStrokeWidth >= mCircleButtonRadius) {
            this.mRadius = width / 2 - mCircleStrokeWidth / 2;
        } else {
            this.mRadius = width / 2 - (mCircleButtonRadius - mGapBetweenCircleAndLine -
                    mCircleStrokeWidth / 2);
        }
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setOnTimeChangedListener(OnTimeChangedListener listener) {
        if (null != listener) {
            this.mListener = listener;
        }
    }

    public interface OnTimeChangedListener {
        void start(String starting);

        void end(String ending);
    }
}
