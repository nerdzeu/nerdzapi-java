package eu.nerdz.api.impl.reverse;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import eu.nerdz.api.ContentException;

/**
 * Represents reverse login data.
 */
public class ReverseLoginData {

    private String userName;
    private String password;
    private Cookie nerdzU;
    private Cookie nerdzId;

    /**
     * Creates an instance, an fills it with preprocessed loginData.
     * @param userName a username
     * @param password a password
     * @param cookieStore an HttpClient CookieStore, initialized with a NERDZ login.
     * @throws ContentException
     */
    ReverseLoginData(String userName, String password, CookieStore cookieStore) throws ContentException {
        this.userName = userName;
        this.password = password;
        for (Cookie cookie : cookieStore.getCookies()) {
            if (cookie.getName().equals("nerdz_u")) {
                BasicClientCookie nerdzU = new BasicClientCookie(cookie.getName(),cookie.getValue());
                nerdzU.setExpiryDate(cookie.getExpiryDate());
                nerdzU.setPath(cookie.getPath());
                nerdzU.setDomain(cookie.getDomain());
                nerdzU.setVersion(cookie.getVersion());
                this.nerdzU = nerdzU;
            } else if (cookie.getName().equals("nerdz_id")) {
                BasicClientCookie nerdzId = new BasicClientCookie(cookie.getName(),cookie.getValue());
                nerdzId.setExpiryDate(cookie.getExpiryDate());
                nerdzId.setPath(cookie.getPath());
                nerdzId.setDomain(cookie.getDomain());
                nerdzId.setVersion(cookie.getVersion());
                this.nerdzId = nerdzId;
            }
        }

        if (this.nerdzId == null || this.nerdzU == null)
            throw new ContentException("malformed cookie store");
    }

    public String getUserName() {
        return userName;
    }

    String getPassword() {
        return this.password;
    }

    public Cookie getNerdzU() {
        return nerdzU;
    }

    public Cookie getNerdzId() {
        return nerdzId;
    }


}
