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

package eu.nerdz.api.impl.reverse.messages;

import java.util.Date;

import eu.nerdz.api.messages.Message;

/**
 * Created by marco on 7/18/13.
 */
class ReverseMessage implements Message {

    /**
     * 
     */
    private static final long serialVersionUID = 2838062641195124239L;
    private String senderName, content;
    private int senderID;
    private Date date;

    public ReverseMessage(String senderName, String content, int senderID, Date date) {

        this.senderName = senderName;
        this.content = content;
        this.senderID = senderID;
        this.date = date;

    }

    @Override
    public String getSenderName() {
        return this.senderName;
    }

    @Override
    public int getSenderID() {
        return this.senderID;
    }

    @Override
    public boolean read() {
        return false;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return this.senderName + " (" + this.date + "): " + this.content;
    }
}
