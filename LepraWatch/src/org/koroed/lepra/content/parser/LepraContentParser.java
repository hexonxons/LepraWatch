package org.koroed.lepra.content.parser;

import org.apache.commons.io.IOUtils;
import org.koroed.lepra.LepraException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 26.05.2014
 * Time: 12:41
 */
public abstract class LepraContentParser<T>
{
    public T parseContent(InputStream is) throws LepraException
    {
        try
        {
            return parseContent(IOUtils.toString(is, "UTF-8"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    protected abstract T parseContent(String content) throws LepraException;
}
