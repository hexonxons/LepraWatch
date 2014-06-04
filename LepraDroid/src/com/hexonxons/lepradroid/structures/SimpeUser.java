package com.hexonxons.lepradroid.structures;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpeUser
{
    @JsonProperty("userName")
    public String userName  = null;
    
    @JsonProperty("userRank")
    public String userRank  = null;
    
    @JsonProperty("userGender")
    public int userGender   = 0;
}
