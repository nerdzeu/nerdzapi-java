package eu.nerdz.api.messages;

import java.io.IOException;
import java.io.Serializable;

import eu.nerdz.api.ContentException;
import eu.nerdz.api.HttpException;

/**
 * An utility class for refined message fetching from conversations.
 */
public interface MessageFetcher extends Serializable, Iterable<Message>, Conversation {

    /**
     * Fetches <b>ten</b> messages, storing them internally, and moving the fetching index ahead of 10. They will be avaliable when iterating with this class.
     * @return the number of messages fetched; if lesser than 10, indicates that there is no messages left in the conversation. Any further calls to fetch will raise out of bound exceptions.
     * @throws IOException
     * @throws HttpException
     * @throws ContentException
     */
    public abstract int fetch() throws IOException, HttpException, ContentException;

    /**
     Fetches limit messages, storing them internally. They will be avaliable when iterating with this class.
     * @return the number of messages fetched; if lesser than 10, indicates that there is no messages left in the conversation. Any further calls to fetch will raise out of bound exceptions.
     * @throws IOException
     * @throws HttpException
     * @throws ContentException
     */
    public abstract int fetch(int limit) throws IOException, HttpException, ContentException;

    /**
     * Forces the start of iteration and fetching from the start-th message, starting from 0.
     * Per example, if fetch() is called after setStart(6), fetch() will fetch messages from 6 to 15 if possible.
     * If an illegal index is set a RuntimeException will be raised during fetch or iteration, so use it with caution.
     * @param start an integer number indicating the new start value
     */
    public abstract void setStart(int start);

    /**
     * Returns the current start value.
     * @return the current start value
     */
    public abstract int getStart();

    /**
     * Resets internal data, deleting from memory everything contained in this fetcher and setting fetch start to 0.
     */
    public abstract void reset();

    /**
     * Returns true if a new call to fetch() will effectively fetch something.
     * @return a boolean value indicating if a new call to fetch() will effectively fetch something
     */
    public Boolean hasMore();


}
