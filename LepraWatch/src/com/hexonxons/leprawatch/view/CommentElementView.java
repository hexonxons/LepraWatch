package com.hexonxons.leprawatch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hexonxons.leprawatch.R;

public class CommentElementView extends RelativeLayout
{
    // Post text.
    public LinearLayout messageWrapper  = null;
    // Author name.
    public TextView author              = null;
    // Comment rating.
    public TextView rating              = null;
    // Comment offser.
    public CommentOffsetView offset     = null;
    
    public CommentElementView(Context context)
    {
        super(context);
    }
    
    public CommentElementView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public CommentElementView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        
        messageWrapper = (LinearLayout) findViewById(R.id.comment_message);
        author = (TextView) findViewById(R.id.comment_author);
        rating = (TextView) findViewById(R.id.comment_rating);
        offset = (CommentOffsetView) findViewById(R.id.comment_offset);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        // Comments header bottom margin.
        if(getChildCount() == 1)
        {
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) (getMeasuredHeight() + getResources().getDimension(R.dimen.comments_header_bottom_margin)), MeasureSpec.EXACTLY));
        }
    }
}
