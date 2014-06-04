package com.hexonxons.lepradroid.structures;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post
{
    @JsonProperty("user")
    public SimpeUser user   = null;
    
    @JsonProperty("date")
    public String date      = null;
    
    @JsonProperty("text")
    public String text      = null;
    
    @JsonProperty("rating")
    public int rating       = Integer.MIN_VALUE;
    
    @JsonProperty("isGold")
    public boolean isGold   = false;
    
    @JsonProperty("comments")
    public int comments     = Integer.MIN_VALUE;
    
    @JsonProperty("newComments")
    public int newComments  = Integer.MIN_VALUE;
}
