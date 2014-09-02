package org.koroed.lepra.content.parser;


/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 26.05.2014
 * Time: 12:51
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 28.08.2014
 */
public class LepraEmptyContentParser extends LepraContentParser<Object>
{
    private static LepraEmptyContentParser sInstance    = null;
    private static Object sLock                         = new Object();
    
    public static LepraEmptyContentParser getInstance()
    {
        synchronized(sLock)
        {
            if (sInstance == null)
            {
                sInstance = new LepraEmptyContentParser();
            }
            
            return sInstance;
        }
    }
    
    private LepraEmptyContentParser(){}
    
    @Override
    protected synchronized Object parseContent(String content)
    {
        return null;
    }
}
