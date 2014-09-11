package org.koroed.lepra.content.parser;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.koroed.lepra.content.LepraComment;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 26.05.2014
 * Time: 19:30
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 08.09.2014
 */
public class LepraCommentsListParser extends LepraContentParser<ArrayList<LepraComment>>
{
    public static final String TAG    = "LepraPostCommentsListParser";
    
    private static String COMMENT_END = "</div>\\n\\n\\t\\t\\t</div>\\n\\n\\t\\t</div>\\n\\n";
    
    private static String COMMENT_CROP_START = "<div class=\"b-post_comments\" id=\"js-commentsHolder\">";
    private static String COMMENT_CROP_END = "<a name=\"comment_textarea\">";
    
    private static LepraCommentsListParser sInstance    = null;
    private static Object sLock                     = new Object();
    
    public static LepraCommentsListParser getInstance()
    {
        synchronized (sLock)
        {
            if (sInstance == null)
            {
                sInstance = new LepraCommentsListParser();
            }
            
            return sInstance;
        }
    }
    
    private LepraCommentsListParser(){}
    
    @Override
    protected synchronized ArrayList<LepraComment> parseContent(String content)
    {
        ArrayList<LepraComment> comments = new ArrayList<>();
        
        String commentsContent = content.substring(content.indexOf(COMMENT_CROP_START) + COMMENT_CROP_START.length(), content.indexOf(COMMENT_CROP_END));
        
        String[] rawComments = commentsContent.split(COMMENT_END);
        
        for(int i = 0; i < rawComments.length - 1; ++i)
        {
            comments.add(parseComment(rawComments[i]));
        }
        
        return comments;
    }
    
    private LepraComment parseComment(String rawComment)
    {
        Document doc = Jsoup.parse(rawComment);
        
        int id = Integer.valueOf(doc.select(".comment").attr("id"));
        int parentId = Integer.MIN_VALUE;
        
        String parentCommentId = doc.select(".comment").attr("data-parent_comment_id");
        if(!StringUtils.isEmpty(parentCommentId))
        {
            parentId = Integer.valueOf(parentCommentId);
        }
        
        String content = doc.select(".c_body").html();
        
        String userLogin = doc.select(".c_user").first().html();
        String userGender = doc.select(".ddi").first().childNode(2).toString().substring(1, 9).compareTo("Написала") == 0 ? "female" : "male";
        String userTitle = doc.select(".ddi").first().childNode(2).toString().substring(userGender.compareTo("male") == 0 ? 9 : 10);
        
        long date =  Long.valueOf(doc.select(".js-date").first().attr("data-epoch_date")) * 1000;
        int rating = Integer.parseInt(doc.select(".vote_result").first().text());
        
        LepraComment comment = new LepraComment();
        comment.id = id;
        comment.parentId = parentId;
        
        comment.content = content;
        
        comment.userLogin = userLogin;
        comment.userTitle = userTitle;
        comment.userGender = userGender;
        
        comment.date = date;
        comment.rating = rating;
        
        return comment;
    }
}
