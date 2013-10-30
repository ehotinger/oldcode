package cs3744.model;

import cs3744.model.events.solution.DataChangeEvent;
import java.util.concurrent.locks.Lock;
import java.util.LinkedList;
import java.util.List;
import cs3744.model.events.interfaces.IDataChangeEvent;
import cs3744.model.events.interfaces.IDataChangeListener;
import cs3744.model.events.interfaces.IVetoableDataChangeListener;
import cs3744.model.exceptions.DataChangeVetoException;
import cs3744.model.interfaces.IGame;
import cs3744.model.interfaces.IGamePiece;
import cs3744.model.interfaces.IPlayer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Defines several aspects about a GamePiece such as a game, owner, position,
 * state, and listeners which affects the GamePiece's individual development
 * throughout the Game.
 *
 * @author Eric Hotinger
 * @version 1.0
 */
public class GamePiece
    implements IGamePiece
{

    private final IPlayer                     OWNER;
    private final IGame                       GAME;
    private Lock                              positionLock;
    private Lock                              stateLock;
    private List<IDataChangeListener>         listeners;
    private List<IVetoableDataChangeListener> vetoListeners;
    private int                               position;
    private GamePieceState                    state;
    private IDataChangeEvent                  myEvent;


    // ----------------------------------------------------------
    /**
     * Create a new GamePiece object with the specified owner and game.
     *
     * @param owner
     *            the owner of the GamePiece
     * @param game
     *            the Game being played
     */
    public GamePiece(IPlayer owner, IGame game)
    {
        this.OWNER = owner;
        this.GAME = game;
        this.position = 0;
        this.state = GamePieceState.OUT;
        this.positionLock = new ReentrantLock();
        this.stateLock = new ReentrantLock();
        this.listeners = new LinkedList<IDataChangeListener>();
        this.vetoListeners = new LinkedList<IVetoableDataChangeListener>();
    }


    /**
     * Adds the provided listener to the list of listeners.
     *
     * @param listener
     *            the listener to be added
     */
    public synchronized void addDataChangeListener(IDataChangeListener listener)
    {
        this.listeners.add(listener);
    }


    /**
     * Removes the provided listener from the list of listeners.
     *
     * @param listener
     *            the listener to be removed
     */
    public synchronized void removeDataChangeListener(
        IDataChangeListener listener)
    {
        this.listeners.remove(listener);
    }


    /**
     * Notifies all listeners of the event
     *
     * @param event
     *            the event
     */
    public synchronized void fireDataChangeEvent(IDataChangeEvent event)
    {
        for (IDataChangeListener listener : this.listeners)
        {
            listener.dataChanged(event);
        }
    }


    /**
     * Adds an IVetoableDataChangeListener to the list.
     *
     * @param listener
     *            the listener to be added to the list
     */
    public synchronized void addVetoableDataChangeListener(
        IVetoableDataChangeListener listener)
    {
        this.vetoListeners.add(listener);
    }


    /**
     * Removes an IVetoableDataChangeListener from the list.
     *
     * @param listener
     *            the listener to be removed from the list
     */
    public synchronized void removeVetoableDataChangeListener(
        IVetoableDataChangeListener listener)
    {
        this.vetoListeners.remove(listener);
    }


    /**
     * Notifies all IVetoableDataChangeListener objects in the list of a
     * DataChangeEvent.
     *
     * @param event
     *            a DataChangeEvent that all listener objects will be notified
     *            of
     * @throws DataChangeVetoException
     *             if the event is vetoed
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
     * Returns the owner of the GamePiece
     *
     * @return the owner of the GamePiece
     */
    public IPlayer getOwner()
    {
        return this.OWNER;
    }


    /**
     * Returns the Game being played. No need for a lock on this because there
     * is only a getter; no setter.
     *
     * @return the Game being played
     */
    public IGame getGame()
    {
        return this.GAME;
    }


    /**
     * Returns the position of the GamePiece.
     *
     * @return the position of the GamePiece
     */
    public int getPosition()
    {
        try
        {
            this.positionLock.lock();
            return this.position;
        }

        finally
        {
            this.positionLock.unlock();
        }
    }


    /**
     * Changes the position of this GamePiece.
     *
     * @param position
     *            the new position of the GamePiece
     * @throws DataChangeVetoException
     *             if the data change is vetoed
     */
    public void setPosition(int position)
        throws DataChangeVetoException
    {
        try
        {
            this.positionLock.lock();

            myEvent =
                new DataChangeEvent(
                    OWNER.toString(),
                    "position",
                    this.position,
                    position);

            this.fireVetoableDataChangeEvent(myEvent);
            this.position = position;
            this.fireDataChangeEvent(myEvent);
        }

        finally
        {
            this.positionLock.unlock();
        }
    }


    /**
     * Returns the state of this GamePiece.
     *
     * @return the state of the GamePiece
     */
    public GamePieceState getState()
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
     * Changes the state of this GamePiece.
     *
     * @param state
     *            the new state of the GamePiece
     * @throws DataChangeVetoException
     *             if the data change is vetoed
     */
    public void setState(GamePieceState state)
        throws DataChangeVetoException
    {
        try
        {
            this.stateLock.lock();
            myEvent =
                new DataChangeEvent(
                    OWNER.toString(),
                    "state",
                    this.state,
                    state);

            this.fireVetoableDataChangeEvent(myEvent);
            this.state = state;
            this.fireDataChangeEvent(myEvent);

            this.state = state;
        }

        finally
        {
            this.stateLock.unlock();
        }
    }


    /**
     * Returns a string representation of the GamePiece.
     *
     * @return a string representation of the game piece
     */
    public String toString()
    {
        String ret = "";

        if (this.getState() == GamePieceState.OUT)
            ret = "out";

        else if (this.getState() == GamePieceState.IN)
            ret = "in";

        else if (this.getState() == GamePieceState.GOAL)
            ret = "goal";

        return this.OWNER + "'s game piece at position (" + this.position
            + ") " + ret + " of play.";
    }

}
