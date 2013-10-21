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

package eu.nerdz.api.messages;


import java.io.Serializable;
import java.util.Date;

/**
 * An interface that acknowledges that a conversation between two users exists.
 * Every operation on it must be made with a ConversationHandler and the object itself must be created by it.
 */
public interface Conversation extends Serializable {

    /**
     * Returns the numeric ID of the other person in the conversation (being the first the user that is using this application)
     *
     * @return a numeric ID
     */
    public abstract int getOtherID();

    /**
    * Returns the username of the other person in the conversation (being the first the user that is using this application)
    *
    * @return a numeric ID
    */
    public abstract String getOtherName();

    public abstract Date getLastDate();

    /**
     * Returns true if this conversation has new messages, false otherwise.
     * @return true if this conversation has new messages, false otherwise.
     */
    public abstract boolean hasNewMessages();

    /**
     * Toggles new messages value.
     */
    public abstract void toggleHasNewMessages();

    /**
     * Sets new messages value to newStatus.
     * @param newStatus
     */
    public abstract void setHasNewMessages(boolean newStatus);

    /**
     * Updates the conversation with the info from the given message. If the message does not own to this
     * conversation, it throws a ContentException.
     * @param message
     */
    public abstract void updateConversation(Message message);
}
