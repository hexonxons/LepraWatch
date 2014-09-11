package com.hexonxons.leprawatch.view;

import com.hexonxons.leprawatch.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PostElementView extends RelativeLayout
{
    // Post text.
    public LinearLayout messageWrapper  = null;
    // Author name.
    public TextView author              = null;
    
    // Post info group.
    public ViewGroup infoWrapper        = null;
    // Comments icon.
    public ImageView commentsIcon       = null;
    // Comments count.
    public TextView commentsCount       = null;
    // Post rating.
    public TextView rating              = null;
    
    public PostElementView(Context context)
    {
        super(context);
    }
    
    public PostElementView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public PostElementView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        
        messageWrapper = (LinearLayout) findViewById(R.id.post_message);
        author = (TextView) findViewById(R.id.post_author);
        
        infoWrapper = (ViewGroup) findViewById(R.id.post_info_wrapper);
        commentsCount = (TextView) findViewById(R.id.post_info_comments_count);
        commentsIcon = (ImageView) findViewById(R.id.post_info_comments_icon);
        rating = (TextView) findViewById(R.id.post_info_rating);
    }
}
