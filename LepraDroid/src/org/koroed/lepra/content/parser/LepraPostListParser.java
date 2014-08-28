package org.koroed.lepra.content.parser;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.koroed.lepra.content.LepraPost;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 26.05.2014
 * Time: 19:30
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 27.08.2014
 */
public class LepraPostListParser extends LepraContentParser<ArrayList<LepraPost>>
{
    public static final String TAG    = "LepraPostListParser";
    
    private static String POST_SEPARATOR = "\\n\\n\\t\\t\\n\\t\\t\\t\\t";
    
    private static LepraPostListParser sInstance    = null;
    private static Object sLock                     = new Object();
    
    public static LepraPostListParser getInstance()
    {
        synchronized (sLock)
        {
            if (sInstance == null)
            {
                sInstance = new LepraPostListParser();
            }
            
            return sInstance;
        }
    }
    
    private LepraPostListParser(){}
    
    @Override
    protected synchronized ArrayList<LepraPost> parseContent(String content)
    {
        ArrayList<LepraPost> posts = new ArrayList<>();
        
        if(!content.contains("{"))
        {
            System.out.println(content);
            return posts;
        }
        
        content = content.substring(content.indexOf("{"));
        
        Integer newOffset = null;
        String template = null;
        
        try
        {
            JSONObject obj = new JSONObject(content);
            template = obj.getString("template");
            newOffset = obj.getInt("offset");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        if(StringUtils.isBlank(template))
        {
            return null;
        }
        
//        if(template.indexOf(POST_SEPARATOR) < 0)
//        {
//            return Collections.emptyList();
//        }
        
        String[] rawPostArray =  template.split(POST_SEPARATOR, -1);
        
        for(String rawPost : rawPostArray)
        {
            if(StringUtils.isNotBlank(rawPost))
            {
                posts.add(postParser(rawPost));
            }
        }
        
        return posts;
    }
    
    private LepraPost postParser(String rawPost)
    {
        Document doc = Jsoup.parse(rawPost);
        
        String postId = doc.select(".post").first().attr("id");
        String postLink = null;
        boolean isGold = !doc.select(".golden").isEmpty();
        
        String userLogin = doc.select(".c_user").first().text();
        String userTitle = doc.select(".ddi").first().html();
        if(StringUtils.isNotBlank(userTitle))
        {
            userTitle = userTitle.trim().substring(8, userTitle.indexOf("<a")).replaceAll("\\s", " ").trim();
        }
        String userGender = doc.select(".ddi").first().textNodes().get(0).text().trim().compareTo("Написал") == 0 ? "male" : "female";
        
        long postDate =  Long.valueOf(doc.select(".js-date").first().attr("data-epoch_date"));
        String totalCommentsCnt = null;
        String newCommentsCnt = null;
        
        Element commentsCounts = doc.select(".b-post_comments_links").first();
        if(commentsCounts != null)
        {
           Elements cnts = commentsCounts.getElementsByTag("a");
           if(!cnts.isEmpty())
           {
               totalCommentsCnt = cnts.first().text();
               postLink = cnts.first().attr("href");
               if(cnts.size() > 1)
               {
                   newCommentsCnt = cnts.get(1).text();
               }
           }
        }
        
        int postRating = Integer.parseInt(doc.select(".vote_result").first().text());
        
        String postContent = doc.select(".dti").first().html();
        
        int postIdInt = Integer.valueOf(postId.substring(1, postId.length()));
        
        LepraPost post = new LepraPost();
        post.postId = postIdInt;
        post.postLink = postLink;
        post.isGold = isGold;
        
        post.userLogin = userLogin;
        post.userTitle = userTitle;
        post.userGender = userGender;
        
        post.date = postDate * 1000;
        post.totalCommentCnt = totalCommentsCnt;
        post.newCommentCnt = newCommentsCnt;
        post.rating = postRating;
        
        post.content = postContent;
        
        return post;
    }
}
