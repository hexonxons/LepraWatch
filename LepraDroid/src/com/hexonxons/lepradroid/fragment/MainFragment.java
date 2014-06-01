package com.hexonxons.lepradroid.fragment;

import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexonxons.lepradroid.structures.Post;
import com.hexonxons.lepradroid.R;

public class MainFragment extends SherlockFragment
{
    public static final String TAG  = "MainFragment";
    
    private Post[] mPosts = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
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
                view = (ViewGroup) getSherlockActivity().getLayoutInflater().inflate(R.layout.post_element, parent, false);
                
                ViewHolder holder = new ViewHolder();
                holder.author = (TextView) view.findViewById(R.id.post_element_author);
                holder.commentsCount = (TextView) view.findViewById(R.id.post_element_comments_count);
                holder.commentsIcon = (ImageView) view.findViewById(R.id.post_element_comments_icon);
                holder.rating = (TextView) view.findViewById(R.id.post_element_rating);
                holder.text = (TextView) view.findViewById(R.id.post_element_text);
                
                view.setTag(holder);
            }
            
            Resources res = getResources();
            
            Post post = mPosts[position];
            
            ViewHolder holder = (ViewHolder) view.getTag();
            
            // Build author text.
            SpannableStringBuilder authorBuilder = new SpannableStringBuilder();
            // Gender text. (Написал/Написала)
            authorBuilder.append((post.userGender == 0 ? res.getText(R.string.write_man) : res.getText(R.string.write_woman)) + " ");
            // Rank.
            authorBuilder.append(post.userRank + " ");
            int authorStart = authorBuilder.length();
            // Nickname text.
            authorBuilder.append(post.userName);
            int authorEnd = authorBuilder.length();
            // Nickname color.
            authorBuilder.setSpan(new ForegroundColorSpan(res.getColor(R.color.lepra_blue)), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Bold style.
            authorBuilder.setSpan(new StyleSpan(Typeface.BOLD), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            authorBuilder.append(", ");
            // Date.
            authorBuilder.append(post.date);
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
                view.setBackgroundResource(R.drawable.card_background_gold);
            }
            else
            {
                holder.rating.setBackgroundColor(res.getColor(R.color.lepra_gray));
                view.setBackgroundResource(R.drawable.card_background);
            }
            holder.rating.setText("" + post.rating);
            
            // TODO: build that too.
            holder.text.setText(post.text);
            
            return view;
        }
        
        private class ViewHolder
        {
            TextView author         = null;
            TextView text           = null;
            ImageView commentsIcon  = null;
            TextView commentsCount  = null;
            TextView rating         = null;
        }
    }
}
