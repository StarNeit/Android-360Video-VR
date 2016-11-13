package com.degree.hitaishin.UI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;

public class ProgressArcView extends View {

    public ProgressArcView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private static double M_PI_2 = Math.PI/2;
	private int mBackgroundWidth;
	private int mPrimaryWidth;
	private int mTargetColor;
	private int mTargetLength;
	private int mPrimaryColor;
	private int mBackgroundColor;
	private Paint mArcPaintBackground;
	private Context mContext;
	private Paint mArcPaintPrimary;
	private Paint mTargetMarkPaint;
	private float mPadding;
	private int mProgress;
	private RectF mDrawingRect;
	private int mSize;
	private int mTarget;
   
    private void init(Context ctx) {
        mContext = ctx;
        Resources res = ctx.getResources();
        float density = res.getDisplayMetrics().density;

        mBackgroundColor = res.getColor(android.R.color.darker_gray);
        mBackgroundWidth = (int)(8 * density); // default to 8dp
        mPrimaryColor = res.getColor(android.R.color.holo_orange_dark);
        mPrimaryWidth = (int)(8 * density);  // default to 8dp
        mTargetColor = res.getColor(android.R.color.holo_orange_light);
        mTargetLength = (int)(mPrimaryWidth * 1.50); // 20% longer than arc line width

        mArcPaintBackground = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setAntiAlias(true);
            }
        };
        mArcPaintBackground.setColor(mBackgroundColor);
        mArcPaintBackground.setStrokeWidth(mBackgroundWidth);

        mArcPaintPrimary = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setAntiAlias(true);
            }
        };
        mArcPaintPrimary.setColor(mPrimaryColor);
        mArcPaintPrimary.setStrokeWidth(mPrimaryWidth);

        mTargetMarkPaint = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setAntiAlias(true);
            }
        };
        mTargetMarkPaint.setColor(mTargetColor);
        // make target tick mark 1/3 width of progress(primary) arc width
        mTargetMarkPaint.setStrokeWidth(mPrimaryWidth / 3);

        // get widest drawn element to properly pad the rect we draw inside
        float maxW = (mTargetLength >= mBackgroundWidth) ? mTargetLength : mBackgroundWidth;
        // arc is drawn with it's stroke center at the rect size provided, so we have to pad    
        // it by half to bring it inside our bounding rect
        mPadding = maxW / 2;
        mProgress = 0;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // full circle (start at 270, the "top")
        canvas.drawArc(mDrawingRect, 270, 360, false, mArcPaintBackground);

        // draw starting at top of circle in the negative (counter-clockwise) direction
        canvas.drawArc(mDrawingRect, 270 ,-(360*(mProgress/100f)), false, mArcPaintPrimary);

        // draw target mark along, but perpendicular to the arc's line
        float radius = mDrawingRect.width() <= mDrawingRect.height() 
                     ? mDrawingRect.width()/2 : mDrawingRect.height()/2;
        // Shift cos/sin by -90 deg (M_PI_2) to put start at 0 (top) and is in radians
        float circleX = mDrawingRect.centerX() + radius * 
                        (float)Math.cos(Math.PI * 2 * - mTarget/100f - M_PI_2);
        float circleY = mDrawingRect.centerY() + radius * 
                        (float)Math.sin(Math.PI * 2 * - mTarget/100f - M_PI_2);

        float slope = circleX - mDrawingRect.centerX() == 0 ? 999999 
                    : (circleY - mDrawingRect.centerY())/(circleX - mDrawingRect.centerX());

        float projectedX = (float)((mTargetLength/2.0)/Math.sqrt(1 + Math.pow(slope, 2.0)));
        float projectedY = (float)(((mTargetLength/2.0)*slope)
                            /Math.sqrt(1 + Math.pow(slope, 2.0)));

        canvas.drawLine(circleX - projectedX,
                circleY - projectedY,
                circleX + projectedX,
                circleY + projectedY,
                mTargetMarkPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (0 == height) height = width; // 0 vertical space, make it square
        if (0 == width) width = height; // 0 horizontal space, make it square

        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // set the dimensions
        int size = 0;
        if (widthWithoutPadding > heigthWithoutPadding) {
            size = heigthWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                             size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // bound our drawable arc to stay fully within our canvas
        mDrawingRect = new RectF(mPadding + getPaddingLeft(),
                                 mPadding + getPaddingTop(),
                                 w - mSize - mPadding - getPaddingRight(),
                                 h - mSize - mPadding - getPaddingBottom());
    }
}