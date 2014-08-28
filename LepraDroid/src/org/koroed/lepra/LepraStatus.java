package org.koroed.lepra;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 28.05.2014
 * Time: 17:32
 */
public class LepraStatus {
    private int karma;
    private int rating;
    private int voteWeight;
    private int myUnreadPosts;
    private int myUnreadComms;
    private int inboxUnreadPosts;
    private int inboxUnreadComms;

    public LepraStatus(int karma, int rating, int voteWeight, int myUnreadPosts, int myUnreadComms, int inboxUnreadPosts, int inboxUnreadComms) {
        this.karma = karma;
        this.rating = rating;
        this.voteWeight = voteWeight;
        this.myUnreadPosts = myUnreadPosts;
        this.myUnreadComms = myUnreadComms;
        this.inboxUnreadPosts = inboxUnreadPosts;
        this.inboxUnreadComms = inboxUnreadComms;
    }

    public int getKarma() {
        return karma;
    }

    public int getRating() {
        return rating;
    }

    public int getVoteWeight() {
        return voteWeight;
    }

    public int getMyUnreadPosts() {
        return myUnreadPosts;
    }

    public int getMyUnreadComms() {
        return myUnreadComms;
    }

    public int getInboxUnreadPosts() {
        return inboxUnreadPosts;
    }

    public int getInboxUnreadComms() {
        return inboxUnreadComms;
    }

    @Override
    public String toString() {
        return "LepraStatus{" +
                "karma=" + karma +
                ", rating=" + rating +
                ", voteWeight=" + voteWeight +
                ", myUnreadPosts=" + myUnreadPosts +
                ", myUnreadComms=" + myUnreadComms +
                ", inboxUnreadPosts=" + inboxUnreadPosts +
                ", inboxUnreadComms=" + inboxUnreadComms +
                '}';
    }
}
