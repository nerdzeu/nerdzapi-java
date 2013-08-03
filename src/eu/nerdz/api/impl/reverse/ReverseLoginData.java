package eu.nerdz.api.impl.reverse;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Date;

import eu.nerdz.api.ContentException;

/**
 * Represents reverse login data.
 */
public class ReverseLoginData {

    private String userName;
    private Cookie nerdzU;
    private Cookie nerdzId;

    /**
     * Creates an instance, an fills it with preprocessed loginData.
     * @param userName a username
     * @param cookieStore an HttpClient CookieStore, initialized with a NERDZ login.
     * @throws ContentException
     */
    ReverseLoginData(String userName, CookieStore cookieStore) throws ContentException {
        this.userName = userName;
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

    /**
     * Creates an instance using external data.
     * @param userName a username
     * @param nerdzId an id as a String
     * @param nerdzU a nerdz_u token
     */
    public ReverseLoginData(String userName, String nerdzId, String nerdzU) {

        this.userName = userName;
        BasicClientCookie nerdzUCookie = new BasicClientCookie("nerdz_u",nerdzU);
        nerdzUCookie.setExpiryDate(new Date(new Date().getTime() + 1000*365*24*3600*1000L));
        nerdzUCookie.setPath("/");
        nerdzUCookie.setDomain('.' + AbstractReverseApplication.SUBDOMAIN_FULL);
        this.nerdzU = nerdzUCookie;
        BasicClientCookie nerdzIdCookie = new BasicClientCookie("nerdz_id",nerdzId);
        nerdzIdCookie.setExpiryDate(new Date(new Date().getTime() + 1000*365*24*3600*1000L));
        nerdzIdCookie.setPath("/");
        nerdzIdCookie.setDomain('.' + AbstractReverseApplication.SUBDOMAIN_FULL);
        this.nerdzId = nerdzIdCookie;

    }

    public String getUserName() {
        return userName;
    }

    public Cookie getNerdzU() {
        return nerdzU;
    }

    public Cookie getNerdzId() {
        return nerdzId;
    }


}
