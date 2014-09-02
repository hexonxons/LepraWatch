package com.hexonxons.leprawatch.structures;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message
{
    @JsonProperty("text")
    public String text      = null;
    
    @JsonProperty("link")
    public String link      = null;
    
    @JsonProperty("image")
    public String image     = null;
    
    @JsonProperty("color")
    public int color        = Integer.MAX_VALUE;
    
    @JsonProperty("typeface")
    public String typeface  = null;
}
