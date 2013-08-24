
package eu.nerdz.api;

import java.io.Externalizable;

/**
 * This class wraps UserInfo for an implementation. One class of these is usable by an implementation to reinitialize an Application.
 */
public interface UserInfo extends Externalizable {

    public abstract int getNerdzID();

    public abstract String getUsername();
}
