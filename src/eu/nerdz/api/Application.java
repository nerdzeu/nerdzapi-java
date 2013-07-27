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

/**
 * Interface representing a basic NerdzApi application.
 * The Application contains login information, and takes care internally of validating access to the NERDZ website.
 *
 * @author Marco Cilloni
 * @version 0.1
 */
public interface Application {

    /**
     * Gets the username associated with this Application instance during the login process.
     * @return a valid NERDZ username (without any kind of escaping)
     */
    public String getUsername();

    /**
     * Gets the current user ID.
     * @return a valid user ID, representing this Application user.
     */
    public int getUserID();

}
