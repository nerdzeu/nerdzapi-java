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


import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import eu.nerdz.api.BadStatusException;
import eu.nerdz.api.ContentException;
import eu.nerdz.api.Conversation;
import eu.nerdz.api.ConversationHandler;
import eu.nerdz.api.HttpException;
import eu.nerdz.api.Message;
import eu.nerdz.api.Messenger;

/**
 * Created by marco on 7/18/13.
 */
public class ReverseMessenger extends ReverseApplication implements Messenger {

    private ConversationHandler conversationHandler;

    public ReverseMessenger(String user, String password) throws IOException, HttpException {
        super(user, password);
        this.conversationHandler = new ConversationHandler() {

            @Override
            public List<Conversation> getConversations() throws IOException, HttpException, ContentException {
                List<Conversation> conversations = null;

                List<String> rows = this.parseTableRows(ReverseMessenger.this.get("/pages/pm/inbox.html.php"));
                if (rows != null) {

                    conversations = new ArrayList<Conversation>(20);

                    for (String row : rows)
                        conversations.add(this.parseConversationRow(row));

                }

                return conversations;
            }

            @Override
            public List<Message> getMessagesFromConversation(Conversation conversation) throws IOException, HttpException, ContentException {
                return this.getMessagesFromConversation(conversation, 0, 10);
            }

            @Override
            public List<Message> getMessagesFromConversation(Conversation conversation, int start, int howMany) throws IOException, HttpException, ContentException {
                List<Message> messages = null;
                HashMap<String,String> form = new HashMap<String, String>(4);
                form.put("from", String.valueOf(conversation.getOtherID()));
                form.put("to", String.valueOf(ReverseMessenger.this.getUserID()));
                form.put("start", String.valueOf(start));
                form.put("num", String.valueOf(howMany));

                String body = ReverseMessenger.this.post("/pages/pm/read.html.php?action=conversation", form);

                int endOfList = body.indexOf("<form id=\"convfrm\"");

                switch(endOfList) {
                    case 0:
                        return null;
                    case -1:
                        throw new ContentException("malformed response: " + body);
                    default: {
                        int headOfList = body.indexOf("<div style=\"margin-top: 3px\" id=\"pm");
                        if (headOfList < 0)
                            throw new ContentException("malformed response: " + body);

                        messages = new LinkedList<Message>();
                        for (String messageString : this.splitMessages(body.substring(headOfList, endOfList)))
                            messages.add(new ReverseMessage(this.parseSender(messageString), this.parseMessage(messageString), this.parseSenderID(messageString), this.parseDate(messageString)));
                    }
                }

                return messages;
            }

            @Override
            public void deleteConversation(Conversation conversation) throws IOException, HttpException, BadStatusException, ContentException {

                HashMap<String,String> form = new HashMap<String, String>(2);

                form.put("from", String.valueOf(conversation.getOtherID()));
                form.put("to", String.valueOf(ReverseMessenger.this.getUserID()));

                try {
                    JSONObject response = new JSONObject(ReverseMessenger.this.post("/pages/pm/delete.json.php", form, ReverseApplication.NERDZ_DOMAIN_NAME + "/pm.php"));

                    if ( !( response.getString("status").equals("ok") && response.getString("message").equals("OK") ) )
                        throw new BadStatusException("wrong status couple (" + response.getString("status") + ", " + response.getString("message") + "), conversation not deleted" );
                } catch (JSONException e) {
                    throw new ContentException("Error while parsing JSON in new messages: " + e.getLocalizedMessage());
                }

            }

            private Date parseDate(String messageString) throws ContentException {

                int timestampPosition = messageString.indexOf("data-timestamp=\"");
                if (timestampPosition < 0)
                    throw new ContentException("malformed response: " + messageString);

                timestampPosition += 16;

                return new Date(Long.parseLong(messageString.substring(timestampPosition, messageString.indexOf('"', timestampPosition))) * 1000L);

            }

            private int parseSenderID(String messageString) throws ContentException {

                int fromIDPosition = messageString.indexOf("data-fromid=\"");
                if (fromIDPosition < 0)
                    throw new ContentException("malformed response: " + messageString);

                fromIDPosition += 13;

                return Integer.parseInt(messageString.substring(fromIDPosition, messageString.indexOf('"', fromIDPosition)));

            }

            private String parseSender(String messageString) throws ContentException {

                int closeLinkPosition = messageString.lastIndexOf("</a>", messageString.lastIndexOf("<time"));
                if (closeLinkPosition < 0)
                    throw new ContentException("malformed response: " + messageString);

                int nickStart = messageString.lastIndexOf('>', closeLinkPosition) + 1;
                if (nickStart < 0)
                    throw new ContentException("malformed response: " + messageString);

                return StringEscapeUtils.unescapeHtml4(messageString.substring(nickStart, closeLinkPosition));

            }

            private String parseMessage(String messageString) throws ContentException {

                int msgStart =  messageString.lastIndexOf("1pt solid #FFF\">") + 16;
                if (msgStart <= 0)
                    throw new ContentException("malformed message string: " + messageString);

                return this.removeTags(StringEscapeUtils.unescapeHtml4(messageString.substring(msgStart)));

            }

            private String removeTags(String msg) {

                return this.linkParse(this.ytParse(this.removeDivs(msg.replaceAll("<br />", "\n").replaceAll("<hr style=\"clear:both\" />", "\n"))));

            }

            private String linkParse(String msg) {

                int linkPos, hrefPos, hrefEnd, linkEnd;
                while ((linkPos = msg.indexOf("<a ")) >= 0) {
                    hrefPos = msg.indexOf("href=\"", linkPos) + 6;
                    hrefEnd = msg.indexOf('"', hrefPos);
                    linkEnd = msg.indexOf("</a>", hrefEnd) + 4;
                    msg = msg.substring(0, linkPos) + msg.substring(hrefPos, hrefEnd) + msg.substring(linkEnd);
                }
                return msg;

            }

            private String removeDivs(String msg) {

                int divPos, divEnd;
                while ((divPos = msg.indexOf("<div")) >= 0) {
                    divEnd = msg.indexOf('>', divPos) + 1;
                    msg = msg.substring(0, divPos) + msg.substring(divEnd);
                }
                return msg;

            }

            private String ytParse(String msg) {

                int iframePos, srcPos, srcEnd, iframeEnd;
                while ((iframePos = msg.indexOf("<iframe")) >= 0) {
                    srcPos = msg.indexOf("src=\"", iframePos) + 5;
                    srcEnd = msg.indexOf('"', srcPos);
                    iframeEnd = msg.indexOf("</iframe>", srcEnd) + 9;
                    msg = msg.substring(0, iframePos) + this.fixYTLink(msg.substring(srcPos, srcEnd)) + msg.substring(iframeEnd);
                }
                return msg;

            }

            private String fixYTLink(String link) {
                return "http://youtu.be/" + link.substring(link.lastIndexOf('/') + 1, link.lastIndexOf('?'));
            }

            private List<String> splitMessages(String list) throws ContentException {
                int lastCloseDivs, lastMessagePosition = 0;
                List<String> messages = new LinkedList<String>();
                while ((lastCloseDivs = list.indexOf("</div></div>", lastMessagePosition)) > 0) {
                    messages.add(list.substring(lastMessagePosition,lastCloseDivs));
                    lastMessagePosition = lastCloseDivs + 12;
                }
                return messages;
            }

            private List<String> parseTableRows(String table) {
                List<String> conversations = new ArrayList<String>(20);

                //If no conversation is present, return null
                int beginning = table.indexOf("<tr ");
                if (beginning < 0)
                    return null;

                //Everything except conversations table should be removed
                table = table.substring(beginning, table.indexOf("</table>"));
                int tdIndex, endTrIndex = 0;

                while ( (tdIndex = table.indexOf("<td", endTrIndex)) != -1) {

                    endTrIndex = table.indexOf("</tr>", endTrIndex + 5); //add 5 to last value found
                    conversations.add(table.substring(tdIndex,endTrIndex));

                }
                return conversations;
            }

            private Conversation parseConversationRow(String row) throws ContentException {

                int otherNamePosition = row.indexOf("<a href=");
                if (otherNamePosition < 0)
                    throw new ContentException("Malformed content \"" + row + "\"");

                String otherName = StringEscapeUtils.unescapeHtml4(row.substring(row.indexOf('>', otherNamePosition) + 1, row.indexOf("</a>")));

                int dataFromPosition = row.indexOf("data-from=\"");
                if (dataFromPosition < 0)
                    throw new ContentException("Malformed content \"" + row + "\"");
                dataFromPosition += 11;

                int dataFrom = Integer.parseInt(row.substring(dataFromPosition, row.indexOf('"',dataFromPosition)));

                int dataTimePosition = row.indexOf("data-timestamp=\"");
                if (dataTimePosition < 0)
                    throw new ContentException("Malformed content \"" + row + "\"");
                dataTimePosition += 16;

                Date lastDate = new Date(Long.parseLong(row.substring(dataTimePosition, row.indexOf('"',dataTimePosition))) * 1000L);

                return new ReverseConversation(otherName, dataFrom, lastDate);
            }


        };
    }





    @Override
    public ConversationHandler getConversationHandler() {
        return this.conversationHandler;
    }

    @Override
    public void sendMessage(String to, String message) throws IOException, HttpException, ContentException, BadStatusException {

        HashMap<String,String> form = new HashMap<String, String>(2);

        form.put("to", to);
        form.put("message", message);

        try {
            JSONObject response = new JSONObject(this.post("/pages/pm/send.json.php", form, ReverseApplication.NERDZ_DOMAIN_NAME + "/pm.php"));

            if ( !( response.getString("status").equals("ok") && response.getString("message").equals("OK") ) )
                throw new BadStatusException("wrong status couple (" + response.getString("status") + ", " + response.getString("message") + "), message not sent" );
        } catch (JSONException e) {
            throw new ContentException("Error while parsing JSON in new messages: " + e.getLocalizedMessage());
        }

    }

    @Override
    public int newMessages() throws IOException, HttpException, ContentException {
        try {
            return new JSONObject(this.get("/pages/pm/notify.json.php")).getInt("message");
        } catch (JSONException e) {
            throw new ContentException("Error while parsing JSON in new messages: " + e.getLocalizedMessage());
        }
    }
}
