package cs3744.model;

import cs3744.model.interfaces.IMessage;
import cs3744.model.interfaces.IPlayer;
import cs3744.model.interfaces.ITimestamp;

/**
 * A message is a string with a sender and a timestamp.
 *
 * @author Eric Hotinger
 * @version 1.0
 */
public class Message
    implements IMessage
{

    private final IPlayer    SENDER;
    private final String     MESSAGE;
    private final ITimestamp TIMESTAMP;


    /**
     * Creates a new Message object with a player, message, and timestamp as
     * parameters.
     *
     * @param sender
     *            The sender
     * @param message
     *            A message
     * @param timestamp
     *            A time stamp
     */
    public Message(IPlayer sender, String message, ITimestamp timestamp)
    {
        this.SENDER = sender;
        this.MESSAGE = message;
        this.TIMESTAMP = timestamp;
    }


    /**
     * Getter for the message field. No need for a lock here because there is no
     * setter.
     *
     * @return the message
     */
    public String getMessage()
    {
        return this.MESSAGE;
    }


    /**
     * Getter for the timestamp field. No need for a lock here because there is
     * no setter.
     *
     * @return the timestamp
     */
    public ITimestamp getTimestamp()
    {
        return this.TIMESTAMP;
    }


    /**
     * Getter for the sender field. No need for a lock here because there is no
     * setter.
     *
     * @return the sender
     */
    public IPlayer getSender()
    {
        return this.SENDER;
    }


    /**
     * Gives the message a specific toString formatting
     *
     * @return the message in a String format, listing its Timestamp, Sender,
     *         and Message
     */
    public String toString()
    {
        try
        {
            return this.getTimestamp().toString() + ": <"
                + this.getSender().toString() + "> " + '"' + this.getMessage()
                + '"';
        }

        catch (NullPointerException e)
        {
            throw new NullPointerException("Error: null message found");
        }
    }


    /**
     * Custom hashCode method based on MESSAGE, SENDER, and TIMESTAMP.
     *
     * @return hashcode based on MESSAGE, SENDER, and TIMESTAMP
     */
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((MESSAGE == null) ? 0 : MESSAGE.hashCode());
        result = prime * result + ((SENDER == null) ? 0 : SENDER.hashCode());
        result =
            prime * result + ((TIMESTAMP == null) ? 0 : TIMESTAMP.hashCode());
        return result;
    }


    /**
     * Custom equals method based on MESSAGE, SENDER, and TIMESTAMP. Determines
     * whether or not another object is equal to this Message.
     *
     * @return true or false depending on if an object is equal to the current
     *         message
     */
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null)
        {
            return false;
        }

        if (getClass() != obj.getClass())
        {
            return false;
        }

        Message other = (Message)obj;

        if (MESSAGE == null)
        {
            if (other.MESSAGE != null)
                return false;
        }

        else if (!MESSAGE.equals(other.MESSAGE))
        {
            return false;
        }

        if (SENDER == null)
        {
            if (other.SENDER != null)
            {
                return false;
            }
        }

        else if (!SENDER.equals(other.SENDER))
        {
            return false;
        }

        if (TIMESTAMP == null)
        {
            if (other.TIMESTAMP != null)
            {
                return false;
            }
        }

        else if (!TIMESTAMP.equals(other.TIMESTAMP))
        {
            return false;
        }

        return true;
    }

}
