package com.hexonxons.lepradroid.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.koroed.lepra.content.LepraPost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hexonxons.lepradroid.R;
import com.hexonxons.lepradroid.system.Constants;
import com.hexonxons.lepradroid.view.PostElementView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class PostsFragment extends Fragment
{
    public static final String TAG      = "MainFragment";
    
    // Posts list.
    private ArrayList<LepraPost> mPosts         = null;
    // Active post view.
    private PostElementView mActivePost         = null;
    // Today begin time.
    private long mTodayBegin                    = Long.MIN_VALUE;
    // Yesterday begin time.
    private long mYesterdayBegin                = Long.MIN_VALUE;
    // DateFormatter
    private SimpleDateFormat mSimpleDateFormat  = null;
    
    private final BroadcastReceiver mBroadcastReceiver  = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            switch(intent.getAction())
            {
                case Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_FAIL:
                {
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_SUCCESS:
                {
                    mPosts = intent.getParcelableArrayListExtra(Constants.BUNDLE.KEY_POSTS);
                    
                    Calendar calendar = new GregorianCalendar();
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    
                    mTodayBegin = calendar.getTime().getTime();
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    mYesterdayBegin = calendar.getTime().getTime();
                    
                    // Remove loading view.
                    ViewGroup wrapper = (ViewGroup) getView().findViewById(R.id.posts_wrapper);
                    wrapper.removeAllViews();
                    
                    // Inflate posts layout.
                    getActivity().getLayoutInflater().inflate(R.layout.posts_list, wrapper, true);
                    
                    ListView listView = (ListView) wrapper.findViewById(R.id.posts_list);
                    listView.setAdapter(new PostsAdapter());
                    listView.setOnTouchListener(new OnTouchListener()
                    {
                        @Override
                        public boolean onTouch(View v, MotionEvent event)
                        {
                            if(mActivePost != null)
                            {
                                mActivePost.infoWrapper.setVisibility(View.VISIBLE);
                                mActivePost.actionWrapper.setVisibility(View.GONE);
                                
                                mActivePost = null;
                            }
                            
                            return false;
                        }
                    });
                    
                    listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
                    
                    break;
                }
            }
        }
    };
    
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        mSimpleDateFormat = new SimpleDateFormat("HH:mm");
        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("Russia/Moscow"));
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            mPosts = savedInstanceState.getParcelableArrayList(Constants.BUNDLE.KEY_POSTS);
        }
        
        ViewGroup wrapper = (ViewGroup) inflater.inflate(R.layout.posts_wrapper, container, false);
        
        if(mPosts == null)
        {
            // Inflate loading layout.
            inflater.inflate(R.layout.loading, wrapper, true);
            
            // TODO: Fix that.
            new Handler().post(new Runnable()
            {
                
                @Override
                public void run()
                {
                    // Send request intent.
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Constants.INTENT_FILTER.ACTION_GET_POSTS));
                }
            });
            
        }
        else
        {
            // Inflate books list.
            inflater.inflate(R.layout.posts_list, wrapper, true);
            ListView listView = (ListView) wrapper.findViewById(R.id.posts_list);
            listView.setAdapter(new PostsAdapter());
            listView.setOnTouchListener(new OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if(mActivePost != null)
                    {
                        mActivePost.infoWrapper.setVisibility(View.VISIBLE);
                        mActivePost.actionWrapper.setVisibility(View.GONE);
                        
                        mActivePost = null;
                    }
                    
                    return false;
                }
            });
            
            listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        }
        
        return wrapper;
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_FAIL);
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_SUCCESS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, filter);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_menu, menu);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        if(mPosts != null)
        {
            outState.putParcelableArrayList(Constants.BUNDLE.KEY_POSTS, mPosts);
        }
    }
    
    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        
        super.onPause();
    }
    
    private class PostsAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mPosts.size();
        }
        
        @Override
        public Object getItem(int position)
        {
            return position;
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            PostElementView view = (PostElementView) convertView;
            
            if(view == null)
            {
                view = (PostElementView) getActivity().getLayoutInflater().inflate(R.layout.post_element, parent, false);
            }
            
            view.messageWrapper.removeAllViews();
            
            Resources res = getResources();
            
            LepraPost post = mPosts.get(position);
            
            // Build author text.
            SpannableStringBuilder authorBuilder = new SpannableStringBuilder();
            // Gender text. (Написал/Написала)
            authorBuilder.append((post.userGender.compareTo("male") == 0 ? res.getText(R.string.write_man) : res.getText(R.string.write_woman)) + " ");
            // Rank.
            if(post.userTitle.length() != 0)
            {
                authorBuilder.append(post.userTitle + " ");
            }
            int authorStart = authorBuilder.length();
            // Nickname text.
            authorBuilder.append(post.userLogin);
            int authorEnd = authorBuilder.length();
            // Nickname color.
            authorBuilder.setSpan(new ForegroundColorSpan(res.getColor(post.userGender.compareTo("male") == 0 ? R.color.light_blue_500 : R.color.pink_500)), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Bold style.
            authorBuilder.setSpan(new StyleSpan(Typeface.BOLD), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Date.
            if(post.date >= mTodayBegin)
            {
                authorBuilder.append(", " + res.getText(R.string.post_date_today) + " " + DateFormat.format("kk:mm", new Date(post.date)));
            }
            else if(post.date >= mYesterdayBegin)
            {
                authorBuilder.append(", " + res.getText(R.string.post_date_yesterday) + " " + DateFormat.format("kk:mm", new Date(post.date)));
            }
            else
            {
                authorBuilder.append(", " + DateFormat.format("yyyy-MM-dd hh:mm", new Date(post.date)));
            }
            view.author.setText(authorBuilder);
            
            // Build comments text.
            SpannableStringBuilder commentsBuilder = new SpannableStringBuilder();
            // Amount of comments.
            commentsBuilder.append("" + post.totalCommentCnt);
            // "Comments" text.
            /*if(post.totalCommentCnt % 10 == 1 && post.totalCommentCnt % 100 != 11)
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_one));
            }
            else if(
                    (post.totalCommentCnt % 10 == 2 || post.totalCommentCnt % 10 == 3 || post.totalCommentCnt % 10 == 4) && 
                    (post.totalCommentCnt % 100 != 12 || post.totalCommentCnt % 100 != 13 || post.totalCommentCnt % 100 != 14))
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_text_few));
            }
            else
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_text_other));
            }*/
            
            //if(post.newCommentCnt > 0)
            if(post.newCommentCnt != null && post.newCommentCnt.length() != 0)
            {
                view.commentsIcon.setImageResource(R.drawable.ic_comments_new);
                
                // Build new comments text.
                commentsBuilder.append(" (");
                int commentsStart = commentsBuilder.length();
                commentsBuilder.append("+ " + post.newCommentCnt);
                
                /*if(post.newCommentCnt % 10 == 1 && post.newCommentCnt % 100 != 11)
                    commentsBuilder.append(" " + res.getText(R.string.comments_one));
                else
                    commentsBuilder.append(" " + res.getText(R.string.comments_other));
                */
                int commentsEnd = commentsBuilder.length();
                // New comments color.
                commentsBuilder.setSpan(new ForegroundColorSpan(res.getColor(R.color.green_500)), commentsStart, commentsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // Bold style.
                commentsBuilder.setSpan(new StyleSpan(Typeface.BOLD), commentsStart, commentsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                commentsBuilder.append(")");
            }
            else
            {
                view.commentsIcon.setImageResource(R.drawable.ic_comments);
            }
            view.commentsCount.setText(commentsBuilder);
            
            if(post.isGold)
            {
                view.rating.setBackgroundColor(res.getColor(R.color.amber_500));
                view.infoWrapper.setBackgroundResource(R.drawable.post_gold);
                view.actionWrapper.setBackgroundResource(R.drawable.post_gold);
            }
            else
            {
                view.rating.setBackgroundColor(res.getColor(R.color.lepra_gray));
                view.infoWrapper.setBackgroundResource(R.drawable.post_default);
                view.actionWrapper.setBackgroundResource(R.drawable.post_default);
            }
            view.rating.setText("" + post.rating);
            
            view.infoWrapper.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(mActivePost != null)
                    {
                        mActivePost.infoWrapper.setVisibility(View.VISIBLE);
                        mActivePost.actionWrapper.setVisibility(View.GONE);
                    }
                    
                    mActivePost = (PostElementView) v.getParent();
                    
                    mActivePost.infoWrapper.setVisibility(View.GONE);
                    mActivePost.actionWrapper.setVisibility(View.VISIBLE);
                }
            });
            
            /*SpannableStringBuilder messageBuilder = new SpannableStringBuilder();
            
            // Build post message.
            for(final Message message : post.message)
            {
                if(message.image == null)
                {
                    int messageStart = messageBuilder.length();
                    messageBuilder.append(message.text);
                    int messageEnd = messageBuilder.length();
                    
                    if(message.link != null)
                    {
                        messageBuilder.setSpan(new URLSpan(message.link), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    
                    if(message.color != Integer.MAX_VALUE)
                    {
                        messageBuilder.setSpan(new ForegroundColorSpan(message.color), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    
                    if(message.typeface != null)
                    {
                        if(message.typeface.compareTo("italic") == 0)
                        {
                            messageBuilder.setSpan(new StyleSpan(Typeface.ITALIC), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        else if(message.typeface.compareTo("bold") == 0)
                        {
                            messageBuilder.setSpan(new StyleSpan(Typeface.BOLD), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                else
                {
                    // Add text.
                    if(messageBuilder.length() != 0)
                    {
                        TextView text = new TextView(getActivity());
                        text.setText(messageBuilder);
                        view.messageWrapper.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        
                        messageBuilder = new SpannableStringBuilder();
                    }
                    
                    // Add image
                    final ImageView image = new ImageView(getActivity());
                    image.setScaleType(ScaleType.CENTER_INSIDE);
                    view.messageWrapper.addView(image, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    view.messageWrapper.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ImageLoader.getInstance().displayImage("assets://" + message.image, image);
                        }
                    });
                }
            }
            
            // Add text.
            if(messageBuilder.length() != 0)
            {
                TextView text = new TextView(getActivity());
                text.setText(messageBuilder);
                view.messageWrapper.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }*/
            
            TextView text = new TextView(getActivity());
            text.setText(post.content);
            view.messageWrapper.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            
            return view;
        }
    }
}
