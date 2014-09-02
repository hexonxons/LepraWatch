package org.koroed.lepra.content.parser;

import org.json.JSONObject;
import org.koroed.lepra.LepraException;
import org.koroed.lepra.content.LepraUserContext;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 26.05.2014
 * Time: 19:59
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 27.08.2014
 */
public class CurrentUserInfoParser extends LepraContentParser<LepraUserContext>
{
    private static CurrentUserInfoParser sInstance    = null;
    private static Object sLock                         = new Object();
    
    public static CurrentUserInfoParser getInstance()
    {
        synchronized(sLock)
        {
            if (sInstance == null)
            {
                sInstance = new CurrentUserInfoParser();
            }
            
            return sInstance;
        }
    }
    
    private CurrentUserInfoParser() {}
    
    @Override
    protected LepraUserContext parseContent(String content) throws LepraException
    {
        LepraUserContext context = new LepraUserContext();
        
        int indexOfUserData = content.indexOf("globals.user");
        
        if(indexOfUserData < 0)
        {
            return context;
        }
        
        content = content.substring(indexOfUserData);
        content = content.substring(0, content.indexOf("};"));
        content = content.substring(content.indexOf("{")) + "};";
        
        JSONObject obj = new JSONObject(content);
        
        context.csrfToken = obj.getString("csrf_token");
        context.created = obj.getLong("created") * 1000;
        context.invitedById = obj.getInt("invited_by_id");
        
        return context;
    }
}
