package org.koroed.lepra.content;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 14.05.14
 * Time: 14:25
 */
public class LepraProfile {

    private LepraUser lepraUser;
    private String userFullName;
    private String userResidence;
    private String userText;
    private String userPic;

    public LepraProfile(LepraUser lepraUser, String userFullName, String userResidence, String userText, String userPic) {
        this.lepraUser = lepraUser;
        this.userFullName = userFullName;
        this.userResidence = userResidence;
        this.userText = userText;
        this.userPic = userPic;
    }

    public LepraUser getLepraUser() {
        return lepraUser;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getUserResidence() {
        return userResidence;
    }

    public String getUserText() {
        return userText;
    }

    public String getUserPic() {
        return userPic;
    }

    @Override
    public String toString() {
        return "LepraProfile{" +
                "lepraUser=" + lepraUser +
                ", userFullName='" + userFullName + '\'' +
                ", userResidence='" + userResidence + '\'' +
                //", userText='" + userText + '\'' +
                ", userPic='" + userPic + '\'' +
                '}';
    }
}
