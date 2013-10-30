package cs3744.model;

import cs3744.model.events.solution.CollectionChangeEvent;
import cs3744.model.events.solution.CollectionChangeOperation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import cs3744.model.events.interfaces.ICollectionChangeEvent;
import cs3744.model.events.interfaces.ICollectionChangeListener;
import cs3744.model.events.interfaces.IVetoableCollectionChangeListener;
import cs3744.model.exceptions.CollectionChangeVetoException;
import cs3744.model.interfaces.IMessage;
import cs3744.model.interfaces.IMessageChannel;

/**
 * Message Channel provides management for chat messages. Changes to the list of
 * messages are <b>vetoable</b>.
 *
 * @author Eric Hotinger
 * @version 1.0
 * @see IMessage
 */
public class MessageChannel
    implements IMessageChannel
{

    private List<ICollectionChangeListener>         listeners;
    private List<IVetoableCollectionChangeListener> vetoListeners;
    private List<IMessage>                          messages;
    private ICollectionChangeEvent                  myEvent;


    /**
     * Creates a new MessageChannel with String name.
     */
    public MessageChannel()
    {
        this.listeners = new LinkedList<ICollectionChangeListener>();
        this.vetoListeners =
            new LinkedList<IVetoableCollectionChangeListener>();
        this.messages = new LinkedList<IMessage>();
    }


    /**
     * Adds the provided listener to the list of listeners.
     *
     * @param listener
     *            the listener to be added
     */
    public synchronized void addVetoableCollectionChangeListener(
        IVetoableCollectionChangeListener listener)
    {
        this.vetoListeners.add(listener);
    }


    /**
     * Removes the provided listener from the list of listeners.
     *
     * @param listener
     *            the listener to be removed
     */
    public synchronized void removeVetoableCollectionChangeListener(
        IVetoableCollectionChangeListener listener)
    {
        this.vetoListeners.remove(listener);
    }


    /**
     * Notifies all listeners of the added event.
     *
     * @param event
     *            the event
     * @throws CollectionChangeVetoException
     *             if the change is rejected
     */
    public synchronized void fireVetoableElementAddedEvent(
        ICollectionChangeEvent event)
        throws CollectionChangeVetoException
    {
        for (IVetoableCollectionChangeListener listener : this.vetoListeners)
        {
            listener.vetoableElementRemoved(event);
        }
    }


    /**
     * Notifies all listeners of the removed event.
     *
     * @param event
     *            the event
     * @throws CollectionChangeVetoException
     *             if the change is rejected
     */
    public synchronized void fireVetoableElementRemovedEvent(
        ICollectionChangeEvent event)
        throws CollectionChangeVetoException
    {
        for (IVetoableCollectionChangeListener listener : this.vetoListeners)
        {
            listener.vetoableElementRemoved(event);
        }
    }


    /**
     * Notifies all listeners of the cleared event.
     *
     * @param event
     *            the event
     * @throws CollectionChangeVetoException
     *             if the change is rejected
     */
    public synchronized void fireVetoableCollectionClearedEvent(
        ICollectionChangeEvent event)
        throws CollectionChangeVetoException
    {
        for (IVetoableCollectionChangeListener listener : this.vetoListeners)
        {
            listener.vetoableCollectionCleared(event);
        }
    }


    /**
     * Adds the specified listener to the list of listeners.
     *
     * @param listener
     *            the listener to be added to the list of listeners
     */
    public synchronized void addCollectionChangeListener(
        ICollectionChangeListener listener)
    {
        this.listeners.add(listener);

    }


    /**
     * Removes the specified listener from the list of listeners.
     *
     * @param listener
     *            the listener to be removed from the list of listeners.
     */
    public synchronized void removeCollectionChangeListener(
        ICollectionChangeListener listener)
    {
        this.listeners.remove(listener);
    }


    /**
     * Notifies all of the CollectionChangeEvent listeners of an added event.
     *
     * @param event
     *            the event added
     */
    public synchronized void fireElementAddedEvent(ICollectionChangeEvent event)
    {
        for (ICollectionChangeListener listener : this.listeners)
        {
            listener.elementAdded(event);
        }
    }


    /**
     * Notifies all of the CollectionChangeEvent listeners of a removed event.
     *
     * @param event
     *            the event removed
     */
    public synchronized void fireElementRemovedEvent(
        ICollectionChangeEvent event)
    {
        for (ICollectionChangeListener listener : this.listeners)
        {
            listener.elementRemoved(event);
        }
    }


    /**
     * Notifies all of the CollectionChangeEvent listeners of a cleared event.
     *
     * @param event
     *            the event cleared
     */
    public synchronized void fireCollectionClearedEvent(
        ICollectionChangeEvent event)
    {
        for (ICollectionChangeListener listener : this.listeners)
        {
            listener.collectionCleared(event);
        }
    }


    /**
     * Returns the name of the MessageChannel. No need for a lock because there
     * is no setter for the name.
     *
     * @return the name of the MessageChannel
     */
    public String getName()
    {
        return "Message Channel";
    }


    /**
     * Adds the message to the channel's list of messages.
     *
     * @param message
     *            the message
     * @throws CollectionChangeVetoException
     *             if the message cannot be added
     */
    public void addMessage(IMessage message)
        throws CollectionChangeVetoException
    {
        myEvent =
            new CollectionChangeEvent(
                message,
                "messages",
                message,
                CollectionChangeOperation.ADD);

        this.fireVetoableElementAddedEvent(myEvent);
        this.messages.add(message);
        this.fireElementAddedEvent(myEvent);
    }


    /**
     * Returns a read-only list of messages received by this message channel.
     *
     * @return a list of messages received by this message channel
     */
    public List<IMessage> getMessages()
    {
        return Collections.unmodifiableList(this.messages);
    }


    /**
     * Returns a string representation of the MessageChannel.
     *
     * @return a String representation of the MessageChannel
     */
    public String toString()
    {
        return "Message Channel";
    }
}
