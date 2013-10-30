package cs3744.model;

import cs3744.model.events.solution.CollectionChangeOperation;
import cs3744.model.events.solution.CollectionChangeEvent;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.List;
import java.util.LinkedList;
import cs3744.model.events.interfaces.ICollectionChangeEvent;
import cs3744.model.events.interfaces.ICollectionChangeListener;
import cs3744.model.events.interfaces.IVetoableCollectionChangeListener;
import cs3744.model.exceptions.CollectionChangeVetoException;
import cs3744.model.interfaces.IGame;
import cs3744.model.interfaces.IGameLobby;
import cs3744.model.interfaces.IMessageChannel;
import cs3744.model.interfaces.IPlayer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * At this revision, the <CODE>ILobby</CODE> interface only contains a list of
 * <CODE>IPlayer</CODE>s as well as an <CODE>IMessageChannel</CODE>. All changes
 * to the player list as well as the message channel are <b>vetoable</b>.
 *
 * @author Eric Hotinger
 * @version 1.0
 */
public class Lobby
    implements IGameLobby
{

    private List<IPlayer>                           players;
    private List<IGame>                             gameList;
    private Lock                                    gameLock;
    private List<IVetoableCollectionChangeListener> vetoCollectionListeners;
    private List<ICollectionChangeListener>         collectionListeners;
    private Lock                                    playerLock;
    private final IMessageChannel                   CHANNEL;
    private ICollectionChangeEvent                  collectionEvent;


    /**
     * Creates a new Lobby
     */
    public Lobby()
    {
        this.CHANNEL = new MessageChannel();
        this.gameLock = new ReentrantLock();
        this.playerLock = new ReentrantLock();
        this.collectionListeners = new LinkedList<ICollectionChangeListener>();
        this.vetoCollectionListeners =
            new LinkedList<IVetoableCollectionChangeListener>();
        this.players = new LinkedList<IPlayer>();
        this.gameList = new LinkedList<IGame>();
    }


    /**
     * Retrieves the message channel of the message channel container.
     *
     * @return the channel of the Lobby
     */
    public IMessageChannel getMessageChannel()
    {
        return this.CHANNEL;
    }


    /**
     * Returns an unmodifiable list of players.
     *
     * @return the list of IPlayers
     */
    public List<IPlayer> getPlayers()
    {
        return Collections.unmodifiableList(this.players);
    }


    /**
     * Adds a player to the list.
     *
     * @param player
     *            the player to add to the list
     * @throws CollectionChangeVetoException
     *             if the player can't be added
     */
    public void addPlayer(IPlayer player)
        throws CollectionChangeVetoException
    {
        try
        {
            this.playerLock.lock();
            collectionEvent =
                new CollectionChangeEvent(
                    this,
                    "players",
                    player,
                    CollectionChangeOperation.ADD);

            this.fireVetoableElementAddedEvent(collectionEvent);
            this.players.add(player);
            this.fireElementAddedEvent(collectionEvent);

        }

        finally
        {
            this.playerLock.unlock();
        }

    }


    /**
     * Removes a player from the list.
     *
     * @param player
     *            the player to remove from the list
     * @throws CollectionChangeVetoException
     *             if the player can't be removed
     */
    public void removePlayer(IPlayer player)
        throws CollectionChangeVetoException
    {

        try
        {
            this.playerLock.lock();
            collectionEvent =
                new CollectionChangeEvent(
                    this,
                    "players",
                    player,
                    CollectionChangeOperation.REMOVE);

            this.fireVetoableElementRemovedEvent(collectionEvent);
            this.players.remove(player);
            this.fireElementRemovedEvent(collectionEvent);

        }

        finally
        {
            this.playerLock.unlock();
        }

    }


    /**
     * Clears the player list.
     *
     * @throws CollectionChangeVetoException
     *             if the list can't be cleared
     */
    public void clearPlayers()
        throws CollectionChangeVetoException
    {
        try
        {
            this.playerLock.lock();
            collectionEvent =
                new CollectionChangeEvent(
                    this,
                    "players",
                    this.players,
                    CollectionChangeOperation.CLEAR);

            this.fireVetoableCollectionClearedEvent(collectionEvent);
            this.players.clear();
            this.fireCollectionClearedEvent(collectionEvent);

        }

        finally
        {
            this.playerLock.unlock();
        }
    }


    /**
     * Returns an unmodifiable list of the Games.
     *
     * @return an unmodifiable list of Games.
     */
    public List<IGame> getGames()
    {
        try
        {
            this.gameLock.lock();
            return Collections.unmodifiableList(this.gameList);
        }

        finally
        {
            this.gameLock.unlock();
        }
    }


    /**
     * Adds a game to the list of games.
     *
     * @param game
     *            the game to be added to the list of games
     * @throws CollectionChangeVetoException
     *             if the game can't be added
     */
    public void addGame(IGame game)
        throws CollectionChangeVetoException
    {
        try
        {
            this.gameLock.lock();
            collectionEvent =
                new CollectionChangeEvent(
                    this,
                    "games",
                    game,
                    CollectionChangeOperation.ADD);

            this.fireVetoableElementAddedEvent(collectionEvent);
            this.gameList.add(game);
            this.fireElementAddedEvent(collectionEvent);
        }

        finally
        {
            this.gameLock.unlock();
        }
    }


    /**
     * Removes a game from the list of games.
     *
     * @param game
     *            the game to be removed
     * @throws CollectionChangeVetoException
     *             if the game can't be removed
     */
    public void removeGame(IGame game)
        throws CollectionChangeVetoException
    {
        try
        {
            this.gameLock.lock();
            collectionEvent =
                new CollectionChangeEvent(
                    this,
                    "games",
                    game,
                    CollectionChangeOperation.REMOVE);

            this.fireVetoableElementRemovedEvent(collectionEvent);
            this.gameList.remove(game);
            this.fireElementRemovedEvent(collectionEvent);
        }

        finally
        {
            this.gameLock.unlock();
        }
    }


    /**
     * Clears the game list.
     *
     * @throws CollectionChangeVetoException
     *             if the list can't be cleared
     */
    public void clearGames()
        throws CollectionChangeVetoException
    {
        try
        {
            this.gameLock.lock();
            collectionEvent =
                new CollectionChangeEvent(
                    this,
                    "games",
                    this.gameList,
                    CollectionChangeOperation.CLEAR);

            this.fireVetoableCollectionClearedEvent(collectionEvent);
            this.gameList.clear();
            this.fireCollectionClearedEvent(collectionEvent);

        }

        finally
        {
            this.gameLock.unlock();
        }

    }


    /**
     * Returns a toString representation of the Lobby.
     *
     * @return a String representing the Lobby
     */
    public String toString()
    {
        return "Lobby" + " " + this.CHANNEL.toString();
    }


    /**
     * Adds a vetoable collection change listener to the list of listeners.
     *
     * @param listener
     *            the listener to add to the list
     */
    public synchronized void addVetoableCollectionChangeListener(
        IVetoableCollectionChangeListener listener)
    {
        this.vetoCollectionListeners.add(listener);
    }


    /**
     * Removes a vetoable collection change listener from the list of listeners.
     *
     * @param listener
     *            the listener to remove from the list
     */
    public synchronized void removeVetoableCollectionChangeListener(
        IVetoableCollectionChangeListener listener)
    {
        this.vetoCollectionListeners.remove(listener);
    }


    /**
     * Notifies all collection change listeners of an added event.
     *
     * @param event
     *            the added event
     * @throws CollectionChangeVetoException
     *             if the addition is rejected
     */
    public synchronized void fireVetoableElementAddedEvent(
        ICollectionChangeEvent event)
        throws CollectionChangeVetoException
    {
        for (IVetoableCollectionChangeListener listener : this.vetoCollectionListeners)
        {
            listener.vetoableElementAdded(event);
        }
    }


    /**
     * Notifies all collection change listeners of a removed event.
     *
     * @param event
     *            the removed event
     * @throws CollectionChangeVetoException
     *             if the removal is rejected
     */
    public synchronized void fireVetoableElementRemovedEvent(
        ICollectionChangeEvent event)
        throws CollectionChangeVetoException
    {
        for (IVetoableCollectionChangeListener listener : this.vetoCollectionListeners)
        {
            listener.vetoableElementRemoved(event);
        }
    }


    /**
     * Notifies all collection change listeners of a cleared event.
     *
     * @param event
     *            the cleared event
     * @throws CollectionChangeVetoException
     *             if the clear is rejected
     */
    public synchronized void fireVetoableCollectionClearedEvent(
        ICollectionChangeEvent event)
        throws CollectionChangeVetoException
    {
        for (IVetoableCollectionChangeListener listener : this.vetoCollectionListeners)
        {
            listener.vetoableCollectionCleared(event);
        }
    }


    /**
     * Adds the specified collection change listener to the list.
     *
     * @param listener
     *            the collection change lister to be added to the list
     */
    public synchronized void addCollectionChangeListener(
        ICollectionChangeListener listener)
    {
        this.collectionListeners.add(listener);
    }


    /**
     * Removes the specified collection change listener from the list.
     *
     * @param listener
     *            the collection change lister to be removed from the list
     */
    public synchronized void removeCollectionChangeListener(
        ICollectionChangeListener listener)
    {
        this.collectionListeners.remove(listener);
    }


    /**
     * Notifies all collection change listeners of the event added.
     *
     * @param event
     *            the event added
     */
    public synchronized void fireElementAddedEvent(ICollectionChangeEvent event)
    {
        for (ICollectionChangeListener listener : this.collectionListeners)
        {
            listener.elementAdded(event);
        }
    }


    /**
     * Notifies all collection change listeners of the event removed.
     *
     * @param event
     *            the removed event
     */
    public synchronized void fireElementRemovedEvent(
        ICollectionChangeEvent event)
    {
        for (ICollectionChangeListener listener : this.collectionListeners)
        {
            listener.elementRemoved(event);
        }
    }


    /**
     * Notifies all collection change listeners of the event cleared.
     *
     * @param event
     *            the cleared event
     */
    public synchronized void fireCollectionClearedEvent(
        ICollectionChangeEvent event)
    {
        for (ICollectionChangeListener listener : this.collectionListeners)
        {
            listener.collectionCleared(event);
        }
    }

}
