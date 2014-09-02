package org.koroed.lepra.content.parser;

import org.json.JSONObject;
import org.koroed.lepra.LepraStatus;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 28.05.2014
 * Time: 17:36
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 28.08.2014
 */
public class LepraStatusParser extends LepraContentParser<LepraStatus>
{
    private static LepraStatusParser sInstance  = null;
    private static Object sLock                 = new Object();
    
    public static LepraStatusParser getInstance()
    {
        synchronized(sLock)
        {
            if (sInstance == null)
            {
                sInstance = new LepraStatusParser();
            }
            
            return sInstance;
        }
    }
    
    private LepraStatusParser(){}
    
    @Override
    protected synchronized LepraStatus parseContent(String content)
    {
        if(!content.startsWith("{"))
        {
            return null;
        }
        
        JSONObject obj = new JSONObject(content);
        int karma = obj.getInt("karma");
        int rating = obj.getInt("rating");
        int voteWeight = obj.getInt("voteweight");
        int myUnreadPosts = obj.getInt("myunreadposts");
        int myUnreadComms = obj.getInt("myunreadcomms");
        int inboxUnreadPosts = obj.getInt("inboxunreadposts");
        int inboxUnreadComms = obj.getInt("inboxunreadcomms");
        return new LepraStatus(karma, rating, voteWeight, myUnreadPosts, myUnreadComms, inboxUnreadPosts, inboxUnreadComms);
    }
}
