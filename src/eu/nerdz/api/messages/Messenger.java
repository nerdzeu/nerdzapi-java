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


import java.io.IOException;

import eu.nerdz.api.Application;
import eu.nerdz.api.BadStatusException;
import eu.nerdz.api.ContentException;
import eu.nerdz.api.HttpException;
import eu.nerdz.api.UserNotFoundException;


/**
 * A Messenger is an Application which allows to manage the NERDZ Messaging services.
 * Messenger provides access to a ConversationHandler, which can be used to manage conversations and related operations.
 */
public interface Messenger extends Application {

    /**
     * Returns a ConversationHandler uniquely associated with this Messenger.
     *
     * @return a ConversationHandler uniquely associated with this Messenger
     */
    public abstract ConversationHandler getConversationHandler();

    /**
     * Sends message to the user to.
     *
     * @param to      a username
     * @param message some text to be sent
     * @return a new Message containing the message we've just sent
     * @throws IOException
     * @throws HttpException
     * @throws ContentException
     * @throws BadStatusException
     * @throws UserNotFoundException
     */
    public abstract Message sendMessage(String to, String message) throws UserNotFoundException, IOException, HttpException, ContentException, BadStatusException;

    /**
     * returns the number of unread messages in the inbox.
     *
     * @return an integer representing the number of unread messages in the inbox
     * @throws IOException
     * @throws HttpException
     * @throws ContentException
     */
    public abstract int newMessages() throws IOException, HttpException, ContentException;

}
