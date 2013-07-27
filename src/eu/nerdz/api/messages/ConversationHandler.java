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
import java.util.List;

import eu.nerdz.api.BadStatusException;
import eu.nerdz.api.ContentException;
import eu.nerdz.api.HttpException;
import eu.nerdz.api.messages.Conversation;
import eu.nerdz.api.messages.Message;

/**
 * Created by marco on 7/21/13.
 */
public interface ConversationHandler {
    public List<Conversation> getConversations() throws IOException, HttpException, ContentException;
    public List<Message> getMessagesFromConversation(Conversation conversation) throws IOException, HttpException, ContentException;
    public List<Message> getMessagesFromConversation(Conversation conversation, int start, int howMany) throws IOException, HttpException, ContentException;
    public void deleteConversation(Conversation conversation) throws IOException, HttpException, BadStatusException, ContentException;

}
