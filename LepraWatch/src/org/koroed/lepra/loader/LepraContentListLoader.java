package org.koroed.lepra.loader;

import org.apache.commons.lang3.ObjectUtils;
import org.koroed.lepra.LepraHttpClient;
import org.koroed.lepra.content.LepraContext;
import org.koroed.lepra.content.parser.LepraContentParser;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 28.05.2014
 * Time: 10:36
 */
public class LepraContentListLoader
{
    private LepraContentParser<Integer> parser;
    private int defOffset = 42;
    private int offset = 0;
    private String sorting;
    private URI uri;
    private LepraContext ctx;
    private LepraHttpClient httpClient;
    
    public LepraContentListLoader(URI uri, LepraContentParser<Integer> parser, String sorting, LepraContext ctx, LepraHttpClient httpClient)
    {
        this.parser = parser;
        this.sorting = sorting;
        this.uri = uri;
        this.ctx = ctx;
        this.httpClient = httpClient;
    }
    
    public void load()
    {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("csrf_token", ctx.userContext.csrfToken);
        parameters.put("offset", Integer.toString(offset));
        parameters.put("sorting", sorting);
        setNewOffset(httpClient.loadContent(uri, parameters, parser));
    }
    
    public void setNewOffset(Integer offset)
    {
        this.offset = ObjectUtils.defaultIfNull(offset, defOffset);
    }
    
    public void setDefOffset(int defOffset)
    {
        this.defOffset = defOffset;
    }
}
