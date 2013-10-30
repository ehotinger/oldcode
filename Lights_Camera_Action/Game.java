package cs3744.model;

import cs3744.model.events.solution.CollectionChangeOperation;
import cs3744.model.events.solution.DataChangeEvent;
import cs3744.model.events.solution.CollectionChangeEvent;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.List;
import java.util.LinkedList;
import cs3744.model.events.interfaces.ICollectionChangeEvent;
import cs3744.model.events.interfaces.ICollectionChangeListener;
import cs3744.model.events.interfaces.IDataChangeEvent;
import cs3744.model.events.interfaces.IDataChangeListener;
import cs3744.model.events.interfaces.IVetoableCollectionChangeListener;
import cs3744.model.events.interfaces.IVetoableDataChangeListener;
import cs3744.model.exceptions.CollectionChangeVetoException;
import cs3744.model.exceptions.DataChangeVetoException;
import cs3744.model.interfaces.IGame;
import cs3744.model.interfaces.IGamePiece;
import cs3744.model.interfaces.IMessageChannel;
import cs3744.model.interfaces.IPlayer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The <CODE>IGame</CODE> interface provides a contract for a class representing
 * a game with game pieces, players, and a message channel.
 *
 * @author Eric Hotinger
 * @version 1.0
 */
public class Game
    implements IGame
{

    private final Integer                           gameID;
    private final IPlayer                           OWNER;
    private GameState                               state;
    private Lock                                    stateLock;
    private Lock                                    gamePiecesLock;
    private Lock                                    playerLock;
    private List<IDataChangeListener>               dataChangeListeners;
    private List<IVetoableCollectionChangeListener> vetoCollectionListeners;
    private List<ICollectionChangeListener>         collectionListeners;
    private List<IVetoableDataChangeListener>       vetoListeners;
    private List<IGamePiece>                         gamePieces;
    private List<IPlayer>                           players;
    private ICollectionChangeEvent                  collectionEvent;
    private IDataChangeEvent                        dataEvent;
    private final IMessageChannel                   CHANNEL;


    // ----------------------------------------------------------
    /**
     * Create a new Game object with a Game ID and Owner.
     *
     * @param gameID
     *            the game's ID
     * @param owner
     *            the game's owner
     */
    public Game(Integer gameID, IPlayer owner)
    {
        this.gameID = gameID;
        this.CHANNEL = new MessageChannel();
        this.OWNER = owner;
        this.dataChangeListeners = new LinkedList<IDataChangeListener>();
        this.vetoListeners = new LinkedList<IVetoableDataChangeListener>();
        this.vetoCollectionListeners =
            new LinkedList<IVetoableCollectionChangeListener>();
        this.collectionListeners = new LinkedList<ICollectionChangeListener>();
        this.players = new LinkedList<IPlayer>();
        this.gamePieces = new LinkedList<IGamePiece>();
        this.gamePiecesLock = new ReentrantLock();
        this.playerLock = new ReentrantLock();
    }


    /**
     * Adds the specified listener to the list of data change listeners.
     *
     * @param listener
     *            the specified listener to add to the list of listeners
     */
    public synchronized void addDataChangeListener(IDataChangeListener listener)
    {
        this.dataChangeListeners.add(listener);
    }


    /**
     * Removes the specified listener from the list of data change listeners.
     *
     * @param listener
     *            the specified listener to remove
     */
    public synchronized void removeDataChangeListener(
        IDataChangeListener listener)
    {
        this.dataChangeListeners.remove(listener);
    }


    /**
     * Notifies all data change listeners of the event.
     *
     * @param event
     *            the event for all listeners to be notified of
     */
    public synchronized void fireDataChangeEvent(IDataChangeEvent event)
    {
        for (IDataChangeListener listener : this.dataChangeListeners)
        {
            listener.dataChanged(event);
        }
    }


    /**
     * Adds a vetoable listener to the list of listeners.
     *
     * @param listener
     *            the listener to add to the list
     */
    public synchronized void addVetoableDataChangeListener(
        IVetoableDataChangeListener listener)
    {
        this.vetoListeners.add(listener);
    }


    /**
     * Removes a vetoable listener from the list of listeners.
     *
     * @param listener
     *            the listener to remove from the list
     */
    public synchronized void removeVetoableDataChangeListener(
        IVetoableDataChangeListener listener)
    {
        this.vetoListeners.remove(listener);
    }


    /**
     * Notifies all vetoable listeners of the event.
     *
     * @param event
     *            the event to notify the vetoable listeners of
     */
    public synchronized void fireVetoableDataChangeEvent(IDataChangeEvent event)
        throws DataChangeVetoException
    {
        for (IVetoableDataChangeListener listener : this.vetoListeners)
        {
            listener.vetoableDataChange(event);
        }
    }


    /**
     * Returns an unmodifiable list of players in the Game.
     *
     * @return an unmodifiable list of players in the Game
     */
    public List<IPlayer> getPlayers()
    {
        try
        {
            this.playerLock.lock();
            return Collections.unmodifiableList(this.players);
        }

        finally
        {
            this.playerLock.unlock();
        }

    }


    /**
     * Adds the specified collection change listener to the list of listeners.
     *
     * @param listener
     *            the specified listener to add to the list
     */
    public synchronized void addCollectionChangeListener(
        ICollectionChangeListener listener)
    {
        this.collectionListeners.add(listener);
    }


    /**
     * Removes the specified collection change listener from the list of
     * listeners.
     *
     * @param listener
     *            the specified listener to remove from the list
     */
    public synchronized void removeCollectionChangeListener(
        ICollectionChangeListener listener)
    {
        this.collectionListeners.remove(listener);
    }


    /**
     * Notifies all collection listeners of an added event.
     *
     * @param event
     *            the event for all listeners to be notified of
     */
    public synchronized void fireElementAddedEvent(ICollectionChangeEvent event)
    {
        for (ICollectionChangeListener listener : this.collectionListeners)
        {
            listener.elementAdded(event);
        }
    }


    /**
     * Notifies all collection listeners of a removed event.
     *
     * @param event
     *            the event to notify listeners of
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
     * Notifies all collection listeners of a cleared event.
     *
     * @param event
     *            the event to notify listeners of
     */
    public synchronized void fireCollectionClearedEvent(
        ICollectionChangeEvent event)
    {
        for (ICollectionChangeListener listener : this.collectionListeners)
        {
            listener.collectionCleared(event);
        }
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
     * Notifies all of the vetoable collection change listeners in the list of
     * an added event.
     *
     * @param event
     *            the added event
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
     * Notifies all of the vetoable collection change listeners in the list of a
     * removed event.
     *
     * @param event
     *            the removed event
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
     * Notifies all of the vetoable collection change listeners in the list of a
     * cleared event.
     *
     * @param event
     *            the cleared event
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
     * Adds a player to the list of players.
     *
     * @param player
     *            the player to add to the list of players
     * @throws CollectionChangeVetoException
     *             if the player cannot be added
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
     * Removes a player from the list of players.
     *
     * @param player
     *            the player to remove from the list of players
     * @throws CollectionChangeVetoException
     *             if the player cannot be removed
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
     * Clears the list of players.
     *
     * @throws CollectionChangeVetoException
     *             if the list cannot be cleared
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
     * Returns the state of the Game.
     *
     * @return the state of the game
     */
    public GameState getState()
    {
        try
        {
            this.stateLock.lock();
            return this.state;
        }

        finally
        {
            this.stateLock.unlock();
        }
    }


    /**
     * Changes the state of the Game.
     *
     * @param state
     *            the new state of the Game
     * @throws DataChangeVetoException
     *             if state change is rejected
     */
    public void setState(GameState state)
        throws DataChangeVetoException
    {

        try
        {
            this.stateLock.lock();

            dataEvent = new DataChangeEvent(this, "state", this.state, state);

            this.fireVetoableDataChangeEvent(dataEvent);
            this.state = state;
            this.fireDataChangeEvent(dataEvent);

        }

        finally
        {
            this.stateLock.unlock();
        }

    }


    /**
     * Returns the owner of the Game.
     *
     * @return the owner of the Game
     */
    public IPlayer getOwner()
    {
        return this.OWNER;
    }


    /**
     * Returns an unmodifiable list of game pieces.
     *
     * @return an unmodifiable list of game pieces
     */
    public List<IGamePiece> getGamePieces()
    {
        try
        {
            this.gamePiecesLock.lock();
            return Collections.unmodifiableList(this.gamePieces);
        }

        finally
        {
            this.gamePiecesLock.unlock();
        }

    }


    /**
     * Returns the role of a player. This means Player 1 through 4 for the first
     * four players, and observer for the remaining players.
     *
     * @return the player role of a player
     */
    public PlayerRole getPlayerRole(IPlayer player)
    {
        PlayerRole role;

        switch (this.getPlayers().indexOf(player))
        {
            case 0:
                role = PlayerRole.PLAYER_ONE;
                break;

            case 1:
                role = PlayerRole.PLAYER_TWO;
                break;

            case 2:
                role = PlayerRole.PLAYER_THREE;
                break;

            case 3:
                role = PlayerRole.PLAYER_FOUR;
                break;

            default:
                role = PlayerRole.PLAYER_ONE;
                break;
        }

        return role;
    }


    /**
     * Adds a gamePiece to the list of game pieces.
     *
     * @param gamePiece
     *            the game piece being added
     * @throws CollectionChangeVetoException
     *             if add is rejected
     */
    public void addGamePiece(IGamePiece gamePiece)
        throws CollectionChangeVetoException
    {
        try
        {
            this.gamePiecesLock.lock();
            collectionEvent =
                new CollectionChangeEvent(
                    this,
                    "gamePieces",
                    gamePiece,
                    CollectionChangeOperation.ADD);

            this.fireVetoableElementAddedEvent(collectionEvent);
            this.gamePieces.add(gamePiece);
            this.fireElementAddedEvent(collectionEvent);
        }

        finally
        {
            this.gamePiecesLock.unlock();
        }
    }


    /**
     * Removes a specified game piece from the list of game pieces.
     *
     * @param gamePiece
     *            the game piece being removed
     * @throws CollectionChangeVetoException
     *             if remove is rejected
     */
    public void removeGamePiece(IGamePiece gamePiece)
        throws CollectionChangeVetoException
    {
        try
        {
            this.gamePiecesLock.lock();
            collectionEvent =
                new CollectionChangeEvent(
                    this,
                    "gamePieces",
                    gamePiece,
                    CollectionChangeOperation.REMOVE);

            this.fireVetoableElementRemovedEvent(collectionEvent);
            this.gamePieces.remove(gamePiece);
            this.fireElementRemovedEvent(collectionEvent);
        }

        finally
        {
            this.gamePiecesLock.unlock();
        }
    }


    /**
     * Clears the list of game pieces.
     *
     * @throws CollectionChangeVetoException
     *             if the list cannot be cleared
     */
    public void clearGamePieces()
        throws CollectionChangeVetoException
    {
        try
        {
            this.gamePiecesLock.lock();
            collectionEvent =
                new CollectionChangeEvent(
                    this,
                    "gamePieces",
                    this.gamePieces,
                    CollectionChangeOperation.CLEAR);

            this.fireVetoableCollectionClearedEvent(collectionEvent);
            this.gamePieces.clear();
            this.fireCollectionClearedEvent(collectionEvent);
        }

        finally
        {
            this.gamePiecesLock.unlock();
        }
    }


    /**
     * Returns the channel of the Game.
     *
     * @return the channel of the Game
     */
    public IMessageChannel getMessageChannel()
    {
        return this.CHANNEL;
    }


    /**
     * Returns a custom toString of the Game class.
     *
     * @return the Owner's name and his game ID
     */
    public String toString()
    {
        return this.OWNER + "'s Game: " + this.gameID;
    }

}
