package com.hexonxons.lepradroid.structures;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post
{
    @JsonProperty("userName")
    public String userName  = null;
    
    @JsonProperty("userRank")
    public String userRank  = null;
    
    @JsonProperty("userGender")
    public int userGender   = 0;
    
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
