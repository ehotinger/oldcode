package cs3744.model;

import cs3744.model.events.solution.DataChangeEvent;
import cs3744.model.events.interfaces.IDataChangeEvent;
import cs3744.model.events.interfaces.IDataChangeListener;
import cs3744.model.events.interfaces.IVetoableDataChangeListener;
import cs3744.model.exceptions.DataChangeVetoException;
import cs3744.model.interfaces.IPlayer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* Defines a Player object that has a name, relative listeners, games won, and
* games lost.
*
* @author Eric Hotinger
* @version 1.0
*/
public class Player
   implements IPlayer
{

   private final String                      NAME;
   private List<IVetoableDataChangeListener> vetoList;
   private List<IDataChangeListener>         listeners;
   private Lock                              lostLock;
   private Lock                              wonLock;
   private Integer                           gamesWon;
   private Integer                           gamesLost;
   private IDataChangeEvent                  myEvent;


   /**
    * Creates a new instance of the Player class
    *
    * @param name
    */
   public Player(String name)
   {
       this.NAME = name;
       this.gamesLost = 0;
       this.gamesWon = 0;
       this.vetoList = new LinkedList<IVetoableDataChangeListener>();
       this.listeners = new LinkedList<IDataChangeListener>();
       this.lostLock = new ReentrantLock();
       this.wonLock = new ReentrantLock();
   }


   /**
    * Returns the player's name
    *
    * @return the player's name
    */
   public String toString()
   {
       return this.NAME;
   }


   /**
    * Creates a custom hashCode method for the Player class.
    *
    * @return a hashcode for a Player
    */
   public int hashCode()
   {
       final int prime = 31;
       int result = 1;
       result = prime * result + ((NAME == null) ? 0 : NAME.hashCode());
       result =
           prime * result + ((gamesLost == null) ? 0 : gamesLost.hashCode());
       result =
           prime * result + ((gamesWon == null) ? 0 : gamesWon.hashCode());
       return result;
   }


   /**
    * Custom equals method for the Player class. Determines whether or not two
    * players are equal based on their name, gamesLost, and gamesWon.
    *
    * @return true or false depending on whether or not two Players are equal
    */
   public boolean equals(Object obj)
   {
       if (this == obj)
           return true;
       if (obj == null)
           return false;
       if (getClass() != obj.getClass())
           return false;
       Player other = (Player)obj;
       if (NAME == null)
       {
           if (other.NAME != null)
               return false;
       }
       else if (!NAME.equals(other.NAME))
           return false;
       if (gamesLost == null)
       {
           if (other.gamesLost != null)
               return false;
       }
       else if (!gamesLost.equals(other.gamesLost))
           return false;
       if (gamesWon == null)
       {
           if (other.gamesWon != null)
               return false;
       }
       else if (!gamesWon.equals(other.gamesWon))
           return false;
       return true;
   }


   /**
    * Adds a new listener to the list of vetoable listeners.
    *
    * @param listener
    *            the listener to add to the list
    */
   public synchronized void addVetoableDataChangeListener(
       IVetoableDataChangeListener listener)
   {
       this.vetoList.add(listener);
   }


   /**
    * Removes a listener from the list of vetoable listeners.
    *
    * @param listener
    *            the listener to remove from the list
    */
   public synchronized void removeVetoableDataChangeListener(
       IVetoableDataChangeListener listener)
   {
       this.vetoList.remove(listener);
   }


   /**
    * Notifies all listeners of the event.
    *
    * @param event
    *            the event
    * @throws DataChangeVetoException
    *             if the event is rejected
    */
   public synchronized void fireVetoableDataChangeEvent(IDataChangeEvent event)
       throws DataChangeVetoException
   {
       for (IVetoableDataChangeListener listener : this.vetoList)
       {
           listener.vetoableDataChange(event);
       }
   }


   /**
    * Adds a listener to the list of data change listeners.
    *
    * @param listener
    *            the listener added to the list
    */
   public synchronized void addDataChangeListener(IDataChangeListener listener)
   {
       this.listeners.add(listener);
   }


   /**
    * Removes a listener from the list of data change listeners.
    *
    * @param listener
    *            the listener removed from the list
    */
   public synchronized void removeDataChangeListener(
       IDataChangeListener listener)
   {
       this.listeners.remove(listener);
   }


   /**
    * Notifies all listeners of the data change event.
    */
   public synchronized void fireDataChangeEvent(IDataChangeEvent event)
   {
       for (IDataChangeListener listener : this.listeners)
       {
           listener.dataChanged(event);
       }
   }


   /**
    * Returns the name of the Player. No need for a lock here because the
    * Player's name is never set.
    *
    * @return the Player's name
    */
   public String getName()
   {
       return this.NAME;
   }


   /**
    * Returns the number of games won for the Player.
    *
    * @return the number of games won
    */
   public Integer getGamesWon()
   {
       try
       {
           this.wonLock.lock();
           return this.gamesWon;
       }

       finally
       {
           this.wonLock.unlock();
       }
   }


   /**
    * Changes the number of games won for the Player.
    *
    * @param gamesWon
    *            the new number of games won for the Player
    * @throws DataChangeVetoException
    *             if the change is rejected
    */
   public void setGamesWon(Integer gamesWon)
       throws DataChangeVetoException
   {
       try
       {
           this.wonLock.lock();

           myEvent =
               new DataChangeEvent(this, "gamesWon", this.gamesWon, gamesWon);

           this.fireVetoableDataChangeEvent(myEvent);
           this.gamesWon = (Integer)myEvent.getNewValue();
           this.fireDataChangeEvent(myEvent);

       }

       finally
       {
           this.wonLock.unlock();
       }

   }


   /**
    * Returns the number of games lost for the Player.
    *
    * @return the number of games lost
    */
   public Integer getGamesLost()
   {
       try
       {
           this.lostLock.lock();
           return this.gamesLost;
       }

       finally
       {
           this.lostLock.unlock();
       }
   }


   /**
    * Changes the number of games lost for the Player to a new value.
    *
    * @param gamesLost
    *            the new number of games the Player has lost
    * @throws DataChangeVetoException
    *             if the change is rejected
    */
   public void setGamesLost(Integer gamesLost)
       throws DataChangeVetoException
   {
       try
       {
           this.lostLock.lock();

           myEvent =
               new DataChangeEvent(
                   this,
                   "gamesLost",
                   this.gamesLost,
                   gamesLost);

           this.fireVetoableDataChangeEvent(myEvent);
           this.gamesLost = gamesLost;
           this.fireDataChangeEvent(myEvent);

       }

       finally
       {
           this.lostLock.unlock();
       }

   }
}
