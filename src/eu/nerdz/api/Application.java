/*
 This file is part of NerdzApi-java.

    NerdzApi-java is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NerdzApi-java is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NerdzApi-java.  If not, see <http://www.gnu.org/licenses/>.

    (C) 2013 Marco Cilloni <marco.cilloni@yahoo.com>
*/

package eu.nerdz.api;

import java.io.IOException;
import java.io.Serializable;

/**
 * Interface representing a basic NerdzApi application.
 * The Application contains login information, and takes care internally of validating access to the NERDZ website.
 *
 * @author Marco Cilloni
 * @version 0.1
 */
public interface Application extends Serializable {

    /**
     * Gets the username associated with this Application instance during the login process.
     *
     * @return a valid NERDZ username (without any kind of escaping)
     */
    public abstract String getUsername();

    /**
     * Gets the current user ID.
     *
     * @return a valid user ID, representing this Application user.
     */
    public abstract int getUserID();

    /**
     * Returns a serializable class which can be stored and later used to recreate this application.
     *
     * @return a UserInfo implementation instance
     */
    public abstract UserInfo getUserInfo();

    /**
     * Returns features associated with this Application.
     */

    public abstract Features[] getSupportedFeatures();

    public boolean checkValidity() throws IOException, HttpException;

    /**
     * Indicates the different features that an Application can have.
     */
    public static enum Features {

        /**
         * Indicates that the application can only do login and retrieve UserInfo.
         */
        NONE,
        /**
         * Indicates that the application is an instance of Messenger.
         */
        MESSENGER,

        /**
         * Indicates that the application is a FullBlownApp.
         */
        FULLBLOWNAPP
    }

}
