package org.koroed.lepra;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 26.05.2014
 * Time: 12:18
 */
public class LepraURI {
    private static String PROTOCOL = "https://";
    private static String LEPRA_HOSTNAME = "leprosorium.ru";
    public static URI LEPRA = newURI(PROTOCOL + "leprosorium.ru");
    public static URI LOGIN = newURI(PROTOCOL + LEPRA_HOSTNAME + "/ajax/auth/login/");
    public static URI LOGOUT = newURI(PROTOCOL + LEPRA_HOSTNAME + "/ajax/auth/logout/");
    public static URI LEPROPANEL = newURI(PROTOCOL + LEPRA_HOSTNAME + "/api/lepropanel");

    public static URI getProfileURI(String login) {
        return newURI(PROTOCOL + LEPRA_HOSTNAME + "/users/" + login);
    }

    public static URI getPostListURI(String leprosorium) {
        String hostname = leprosorium != null ? leprosorium + "." + LEPRA_HOSTNAME : LEPRA_HOSTNAME;
        return newURI(PROTOCOL + hostname + "/ajax/index/moar/");
    }

    private static URI newURI(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
