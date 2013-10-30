package cs3744.graphics.models;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import cs3744.graphics.models.interfaces.IGamePiece;
import cs3744.graphics.primitives.solution.Cylinder;
import cs3744.graphics.interfaces.IPrimitive;
import javax.media.opengl.GL2;

// -------------------------------------------------------------------------
/**
 * A GamePiece3D is a Cylinder with a color and positioning on a board.
 *
 * @author Eric Hotinger
 * @version Aug 8, 2012
 */
public class GamePiece3D
    extends cs3744.graphics.primitives.Primitive
    implements cs3744.graphics.models.interfaces.IGamePiece
{
    private Cylinder   cylinder;
    private int        xPosition;
    private int        yPosition;
    private final Lock xLock = new ReentrantLock();
    private final Lock yLock = new ReentrantLock();


    // ----------------------------------------------------------
    /**
     * Constructs a new GamePiece at position (0, 0) on the grid.
     */
    public GamePiece3D()
    {
        this.cylinder = new Cylinder();

        this.cylinder.setParent(this);
        this.cylinder.setColor(this.getColor());
        this.cylinder.setRotation(this.getRotation());
        this.cylinder.setScaling(this.getScaling());
        this.cylinder.setShear(this.getShear());
        this.cylinder.setSolid(true);
        this.cylinder.setTranslation(this.getTranslation());

        this.xPosition = 0;
        this.yPosition = 0;
    }


    // ----------------------------------------------------------
    /**
     * Creates a copy of the game piece.
     *
     * @param primitiveToCopy
     *            a primitive to copy
     * @param parent
     *            the parent to base the game piece off of
     */
    public GamePiece3D(IGamePiece primitiveToCopy, IPrimitive parent)
    {
        this.cylinder = new Cylinder();

        this.cylinder.setParent(parent);
        this.cylinder.setColor(parent.getColor());
        this.cylinder.setRotation(parent.getRotation());
        this.cylinder.setScaling(parent.getScaling());
        this.cylinder.setShear(parent.getShear());
        this.cylinder.setSolid(parent.isSolid());
        this.cylinder.setTranslation(parent.getTranslation());

        this.xPosition = 0;
        this.yPosition = 0;

    }


    // ----------------------------------------------------------
    /**
     * Constructs a new GamePiece at position (xPosition, yPosition) on the
     * grid.
     *
     * @param xPosition
     *            the x-position
     * @param yPosition
     *            the y-position
     */
    public GamePiece3D(int xPosition, int yPosition)
    {
        this.cylinder = new Cylinder();

        this.cylinder.setRotation(this.getRotation());
        this.cylinder.setScaling(this.getScaling());
        this.cylinder.setShear(this.getShear());
        this.cylinder.setSolid(true);
        this.cylinder.setTranslation(this.getTranslation());

        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }


    // ----------------------------------------------------------
    /**
     * Constructs a new GamePiece at position (xPosition, yPosition) on the grid
     * and sets its parent.
     *
     * @param xPosition
     *            the x-position
     * @param yPosition
     *            the y-position
     * @param parent
     *            the parent to base the game piece off of
     */
    public GamePiece3D(int xPosition, int yPosition, IPrimitive parent)
    {
        this.cylinder = new Cylinder();

        this.cylinder.setParent(parent);
        this.cylinder.setRotation(parent.getRotation());
        this.cylinder.setScaling(parent.getScaling());
        this.cylinder.setShear(parent.getShear());
        this.cylinder.setSolid(parent.isSolid());
        this.cylinder.setTranslation(parent.getTranslation());

        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }


    // ----------------------------------------------------------
    /**
     * Constructs a new GamePiece at position (0, 0) on the grid and sets its
     * parent.
     *
     * @param parent
     */
    public GamePiece3D(IPrimitive parent)
    {
        this.cylinder = new Cylinder();

        this.cylinder.setParent(parent);
        this.cylinder.setRotation(parent.getRotation());
        this.cylinder.setScaling(parent.getScaling());
        this.cylinder.setShear(parent.getShear());
        this.cylinder.setSolid(parent.isSolid());
        this.cylinder.setTranslation(parent.getTranslation());

        this.xPosition = 0;
        this.yPosition = 0;
    }


    /**
     * Returns a copy of this GamePiece3D
     *
     * @return a copy of this GamePiece3D
     */
    public IPrimitive copy()
    {
        return new GamePiece3D(this);
    }


    /**
     * Returns a copy of this GamePiece3D with an associated parent.
     *
     * @return a copy with a parent
     */
    public IPrimitive copy(IPrimitive parent)
    {
        return new GamePiece3D(this, parent);
    }


    /**
     * Paints the GamePiece3D.
     *
     * @param gl2
     *            the OpenGL state machine
     */
    public void paint(GL2 gl2)
    {
        if (this.isSolid())
        {
            this.cylinder.setSolid(true);
            this.cylinder.setColor(this.getColor());
            this.cylinder.paint(gl2);
        }

        else
        {
            this.cylinder.setSolid(false);
            this.cylinder.setColor(this.getColor());
            this.cylinder.paint(gl2);
        }
    }


    /**
     * Returns the x-position of the GamePiece3D.
     *
     * @return the x position
     */
    public int getXPosition()
    {
        try
        {
            this.xLock.lock();
            return this.xPosition;
        }

        finally
        {
            this.xLock.unlock();
        }
    }


    /**
     * Changes the x position of the piece.
     *
     * @param xPosition
     *            the new x position
     */
    public void setXPosition(int xPosition)
    {
        try
        {
            this.xLock.lock();
            this.xPosition = xPosition;
        }

        finally
        {
            this.xLock.unlock();
        }
    }


    /**
     * Returns the y position of the piece.
     *
     * @return the y position
     */
    public int getYPosition()
    {
        try
        {
            this.yLock.lock();
            return this.yPosition;
        }

        finally
        {
            this.yLock.unlock();
        }
    }


    /**
     * Changes the y position of the piece.
     *
     * @param yPosition
     *            the new y-position
     */
    public void setYPosition(int yPosition)
    {
        try
        {
            this.yLock.lock();
            this.yPosition = yPosition;
        }

        finally
        {
            this.yLock.unlock();
        }
    }

}
