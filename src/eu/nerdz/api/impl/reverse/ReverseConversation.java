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

package eu.nerdz.api.impl.reverse;

import java.util.Date;

import eu.nerdz.api.Conversation;

/**
 * Created by marco on 7/20/13.
 */
class ReverseConversation implements Conversation {

    private final String other;
    private int userID;
    private Date lastDate;

    ReverseConversation(String userName, int userID, Date lastDate) {
        this.other = userName;
        this.userID = userID;
        this.lastDate = lastDate;
    }

    @Override
    public int getOtherID() {

        return this.userID;

    }

    @Override
    public String getOtherName() {
        return this.other;
    }

    @Override
    public Date getLastDate() {
        return this.lastDate;
    }

    @Override
    public String toString() {
        return this.other + " (" + this.userID + ") , last contact on " + this.lastDate;
    }
}
