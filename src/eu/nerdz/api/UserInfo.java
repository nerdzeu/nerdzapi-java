
package eu.nerdz.api;

import java.io.Externalizable;

/**
 * This class wraps UserInfo for an implementation. One class of these is usable by an implementation to reinitialize an Application.
 */
public interface UserInfo extends Externalizable {

    /**
     * Gets the NerdzID associated with this account.
     * @return a NerdzID.
     */
    public abstract int getNerdzID();

    /**
     * Gets the Nerdz username associated with this account.
     * @return a Nerdz username
     */
    public abstract String getUsername();

}
