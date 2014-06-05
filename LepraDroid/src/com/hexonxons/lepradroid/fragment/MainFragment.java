package com.hexonxons.lepradroid.fragment;

import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexonxons.lepradroid.MainActivity;
import com.hexonxons.lepradroid.R;
import com.hexonxons.lepradroid.structures.Post;

public class MainFragment extends Fragment
{
    public static final String TAG      = "MainFragment";
    
    private Post[] mPosts               = null;
    
    private ViewHolder mActiveHolder    = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        
        ((MainActivity)getActivity()).setDrawerEnabled(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mPosts = mapper.readValue(getResources().getAssets().open("posts.json"), Post[].class);
        }
        catch(JsonParseException e)
        {
            e.printStackTrace();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        ListView view = (ListView) inflater.inflate(R.layout.main_post_list, container, false);
        view.setAdapter(new MainAdapter());
        view.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(mActiveHolder != null)
                {
                    mActiveHolder.infoWrapper.setVisibility(View.VISIBLE);
                    mActiveHolder.actionWrapper.setVisibility(View.GONE);
                    
                    mActiveHolder = null;
                }
                
                return false;
            }
        });
        
        return view;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_menu, menu);
    }
    
    private class MainAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mPosts.length;
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
            ViewGroup view = (ViewGroup) convertView;
            
            if(view == null)
            {
                view = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.post_element, parent, false);
                
                ViewHolder holder = new ViewHolder();
                
                holder.text = (TextView) view.findViewById(R.id.post_element_text);
                
                holder.infoWrapper = (ViewGroup) view.findViewById(R.id.post_info_wrapper);
                
                holder.author = (TextView) view.findViewById(R.id.post_info_author);
                holder.commentsCount = (TextView) view.findViewById(R.id.post_info_comments_count);
                holder.commentsIcon = (ImageView) view.findViewById(R.id.post_info_comments_icon);
                holder.rating = (TextView) view.findViewById(R.id.post_info_rating);
                
                holder.actionWrapper = (ViewGroup) view.findViewById(R.id.post_action_wrapper);
                
                holder.like = (ImageView) view.findViewById(R.id.post_action_like);
                holder.dislike = (ImageView) view.findViewById(R.id.post_action_dislike);
                holder.toMy = (ImageView) view.findViewById(R.id.post_action_to_my);
                holder.toFavorites = (ImageView) view.findViewById(R.id.post_action_to_favorites);
                holder.aboutAuthor = (ImageView) view.findViewById(R.id.post_action_about_author);
                holder.share = (ImageView) view.findViewById(R.id.post_action_share);
                holder.hide = (ImageView) view.findViewById(R.id.post_action_hide);
                
                view.setTag(holder);
            }
            
            Resources res = getResources();
            
            final Post post = mPosts[position];
            
            final ViewHolder holder = (ViewHolder) view.getTag();
            
            // Build author text.
            SpannableStringBuilder authorBuilder = new SpannableStringBuilder();
            // Gender text. (Написал/Написала)
            authorBuilder.append((post.user.userGender == 0 ? res.getText(R.string.write_man) : res.getText(R.string.write_woman)) + " ");
            // Rank.
            if(post.user.userRank.length() != 0)
            {
                authorBuilder.append(post.user.userRank + " ");
            }
            int authorStart = authorBuilder.length();
            // Nickname text.
            authorBuilder.append(post.user.userName);
            int authorEnd = authorBuilder.length();
            // Nickname color.
            authorBuilder.setSpan(new ForegroundColorSpan(res.getColor(post.user.userGender == 0 ? R.color.lepra_blue : R.color.lepra_magneta)), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Bold style.
            authorBuilder.setSpan(new StyleSpan(Typeface.BOLD), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Date.
            authorBuilder.append(", " + post.date);
            holder.author.setText(authorBuilder);
            
            // Build comments text.
            SpannableStringBuilder commentsBuilder = new SpannableStringBuilder();
            // Amount of comments.
            commentsBuilder.append("" + post.comments);
            // "Comments" text.
            if(post.comments % 10 == 1 && post.comments % 100 != 11)
                commentsBuilder.append(" " + res.getText(R.string.comments_one));
            else if(
                    (post.comments % 10 == 2 || post.comments % 10 == 3 || post.comments % 10 == 4) && 
                    (post.comments % 100 != 12 || post.comments % 100 != 13 || post.comments % 100 != 14))
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_text_few));
            }
            else
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_text_other));
            }
            
            if(post.newComments > 0)
            {
                holder.commentsIcon.setImageResource(R.drawable.ic_comments_new);
                
                // Build new comments text.
                commentsBuilder.append(" (");
                int commentsStart = commentsBuilder.length();
                commentsBuilder.append("+ " + post.newComments);
                
                if(post.newComments % 10 == 1 && post.newComments % 100 != 11)
                    commentsBuilder.append(" " + res.getText(R.string.comments_one));
                else
                    commentsBuilder.append(" " + res.getText(R.string.comments_other));
                
                int commentsEnd = commentsBuilder.length();
                // New comments color.
                commentsBuilder.setSpan(new ForegroundColorSpan(res.getColor(R.color.lepra_green)), commentsStart, commentsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // Bold style.
                commentsBuilder.setSpan(new StyleSpan(Typeface.BOLD), commentsStart, commentsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                commentsBuilder.append(")");
            }
            else
            {
                holder.commentsIcon.setImageResource(R.drawable.ic_comments);
            }
            holder.commentsCount.setText(commentsBuilder);
            
            if(post.isGold)
            {
                holder.rating.setBackgroundColor(res.getColor(R.color.lepra_gold));
                holder.infoWrapper.setBackgroundResource(R.drawable.post_gold);
                holder.actionWrapper.setBackgroundResource(R.drawable.post_gold);
            }
            else
            {
                holder.rating.setBackgroundColor(res.getColor(R.color.lepra_gray));
                holder.infoWrapper.setBackgroundResource(R.drawable.post_default);
                holder.actionWrapper.setBackgroundResource(R.drawable.post_default);
            }
            holder.rating.setText("" + post.rating);
            
            holder.infoWrapper.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(mActiveHolder != null)
                    {
                        mActiveHolder.infoWrapper.setVisibility(View.VISIBLE);
                        mActiveHolder.actionWrapper.setVisibility(View.GONE);
                    }
                    
                    mActiveHolder = holder;
                    
                    holder.infoWrapper.setVisibility(View.GONE);
                    holder.actionWrapper.setVisibility(View.VISIBLE);
                }
            });
            
            // TODO: build that too.
            holder.text.setText(post.text);
            
            return view;
        }
    }
    
    private class ViewHolder
    {
        TextView text           = null;
        
        // Info group
        ViewGroup infoWrapper   = null;
        
        TextView author         = null;
        ImageView commentsIcon  = null;
        TextView commentsCount  = null;
        TextView rating         = null;
        
        // Action group
        ViewGroup actionWrapper = null;
        
        ImageView like          = null;
        ImageView dislike       = null;
        ImageView toMy          = null;
        ImageView toFavorites   = null;
        ImageView aboutAuthor   = null;
        ImageView share         = null;
        ImageView hide          = null;
    }
}
