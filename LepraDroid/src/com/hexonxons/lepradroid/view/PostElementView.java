package com.hexonxons.lepradroid.view;

import com.hexonxons.lepradroid.R;
import com.hexonxons.lepradroid.system.Build;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
    
    // Post action group.
    public ViewGroup actionWrapper      = null;
    // Post action +.
    public ImageView like               = null;
    // Post action -.
    public ImageView dislike            = null;
    // Post action to my.
    public ImageView toMy               = null;
    // Post action to favourites.
    public ImageView toFavorites        = null;
    // Post action about author.
    public ImageView aboutAuthor        = null;
    // Post action share.
    public ImageView share              = null;
    // Post action hide.
    public ImageView hide               = null;
    
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
        author = (TextView) findViewById(R.id.post_info_author);
        
        infoWrapper = (ViewGroup) findViewById(R.id.post_info_wrapper);
        commentsCount = (TextView) findViewById(R.id.post_info_comments_count);
        commentsIcon = (ImageView) findViewById(R.id.post_info_comments_icon);
        rating = (TextView) findViewById(R.id.post_info_rating);
        
        actionWrapper = (ViewGroup) findViewById(R.id.post_action_wrapper);
        like = (ImageView) findViewById(R.id.post_action_like);
        dislike = (ImageView) findViewById(R.id.post_action_dislike);
        toMy = (ImageView) findViewById(R.id.post_action_to_my);
        toFavorites = (ImageView) findViewById(R.id.post_action_to_favorites);
        aboutAuthor = (ImageView) findViewById(R.id.post_action_about_author);
        share = (ImageView) findViewById(R.id.post_action_share);
        hide = (ImageView) findViewById(R.id.post_action_hide);
        
        if(Build.DEBUG)
            Log.d("com.hexonxons.lepradroid.view.PostElementView", "Finish inflate.");
    }
}
