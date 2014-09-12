package com.hexonxons.leprawatch.view;

import com.hexonxons.leprawatch.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CommentOffsetView extends View
{
    private float mCircleSize   = 0;
    private float mCircleMargin = 0;
    
    private int mLevel = 0;
    
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    
    public CommentOffsetView(Context context)
    {
        super(context);
        
        mPaint.setColor(getResources().getColor(R.color.light_blue_500));
        
        mCircleSize = getResources().getDimension(R.dimen.comments_offset_circle_size);
        mCircleMargin = getResources().getDimension(R.dimen.comments_offset_circle_margin);
    }
    
    public CommentOffsetView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        mPaint.setColor(getResources().getColor(R.color.light_blue_500));
        
        mCircleSize = getResources().getDimension(R.dimen.comments_offset_circle_size);
        mCircleMargin = getResources().getDimension(R.dimen.comments_offset_circle_margin);
    }
    
    public CommentOffsetView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        
        mPaint.setColor(getResources().getColor(R.color.light_blue_500));
        
        mCircleSize = getResources().getDimension(R.dimen.comments_offset_circle_size);
        mCircleMargin = getResources().getDimension(R.dimen.comments_offset_circle_margin);
    }
    
    public void setCommentOffset(int level)
    {
        mLevel = level;
        requestLayout();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = (int) (mCircleSize * (mLevel + 1) + mCircleMargin * (mLevel + 2));
        int height = (int) (mCircleSize + 2 * mCircleMargin);
        
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        int cx = (int) (mCircleMargin + mCircleSize / 2);
        int cy = getMeasuredHeight() / 2;
        
        for(int i = -1; i < mLevel; ++i)
        {
            canvas.drawCircle(cx, cy, mCircleSize / 2, mPaint);
            cx += (mCircleSize + mCircleMargin);
        }
        
        super.onDraw(canvas);
    }
}
