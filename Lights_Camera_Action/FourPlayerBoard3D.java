package cs3744.graphics.models;

import cs3744.graphics.common.Material;
import cs3744.graphics.common.Translation;
import cs3744.graphics.interfaces.IColor;
import cs3744.graphics.common.Color;
import cs3744.graphics.common.Scaling;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import cs3744.graphics.primitives.solution.Cube;
import cs3744.graphics.models.interfaces.IGamePiece;
import java.util.List;
import cs3744.graphics.interfaces.IPrimitive;
import javax.media.opengl.GL2;

// -------------------------------------------------------------------------
/**
 * Creates the playing board with game pieces a 560x560x70 playing area.
 *
 * @author Eric Hotinger
 * @version Aug 8, 2012
 */
public class FourPlayerBoard3D
    extends cs3744.graphics.primitives.Primitive
    implements cs3744.graphics.models.interfaces.IGame
{

    private Cube             board;
    private List<IGamePiece> gamePieces = new LinkedList<IGamePiece>();
    private Lock             gamePieceLock;


    // ----------------------------------------------------------
    /**
     * Create a new FourPlayerBoard3D object.
     */
    public FourPlayerBoard3D()
    {
        this.setUpBoard();
        this.gamePieceLock = new ReentrantLock();

        for (int i = 0; i < 13; i++)
        {
            for (int j = 0; j < 13; j++)
            {
                GamePiece3D gamePiece = new GamePiece3D(j, i);
                this.addGamePiece(gamePiece);
            }
        }

        this.setSolid(true);
    }


    // ----------------------------------------------------------
    /**
     * Create a new FourPlayerBoard3D object.
     *
     * @param primitiveToCopy
     * @param parent
     */
    public FourPlayerBoard3D(IPrimitive primitiveToCopy, IPrimitive parent)
    {
        this.gamePieceLock = new ReentrantLock();
        this.setUpBoard();

        for (int i = 0; i < 13; i++)
        {
            for (int j = 0; j < 13; j++)
            {
                GamePiece3D gamePiece = new GamePiece3D(j, i);
                this.addGamePiece(gamePiece);
            }
        }

        this.setParent(parent);
        this.setColor(parent.getColor());
        this.setParent(parent);
        this.setRotation(parent.getRotation());
        this.setScaling(parent.getScaling());
        this.setShear(parent.getShear());
        this.setSolid(parent.isSolid());
        this.setTranslation(parent.getTranslation());
    }


    // ----------------------------------------------------------
    /**
     * Create a new FourPlayerBoard3D object.
     *
     * @param parent
     */
    public FourPlayerBoard3D(IPrimitive parent)
    {
        this.gamePieceLock = new ReentrantLock();
        this.setUpBoard();

        for (int i = 0; i < 13; i++)
        {
            for (int j = 0; j < 13; j++)
            {
                GamePiece3D gamePiece = new GamePiece3D(j, i);
                this.addGamePiece(gamePiece);
            }
        }

        this.setParent(parent);
        this.setColor(parent.getColor());
        this.setParent(parent);
        this.setRotation(parent.getRotation());
        this.setScaling(parent.getScaling());
        this.setShear(parent.getShear());
        this.setSolid(parent.isSolid());
        this.setTranslation(parent.getTranslation());
    }


    /**
     * Handles the creation of the Rectangular board.
     */
    public void setUpBoard()
    {
        Scaling scaling = new Scaling(560, 560, -70);

        this.board = new Cube();
        this.board.setScaling(scaling);
        this.board.setColor(Color.YELLOW);
        this.board.setMaterial(Material.GOLD);
        this.board.setParent(this);
    }


    /**
     * Returns a copy of this game board.
     *
     * @return a copy of this game board
     */
    public IPrimitive copy()
    {
        return new FourPlayerBoard3D(this);
    }


    /**
     * Returns a copy of this game board with a parent.
     *
     * @return a copy of this gmae board with a parent
     */
    public IPrimitive copy(IPrimitive parent)
    {
        return new FourPlayerBoard3D(this, parent);
    }


    /**
     * Paints the FourPlayerBoard.
     *
     * @param gl2
     *            The OpenGL state machine.
     */
    public void paint(GL2 gl2)
    {
        board.paint(gl2);

        int xPosition = 25;
        int yPosition = 25;

        for (IGamePiece gamePiece : this.getGamePieces())
        {

            IColor color =
                ColorLookupTable.fourPlayerBoardGridColor(
                    gamePiece.getXPosition(),
                    gamePiece.getYPosition());

            gamePiece.setParent(this);

            Translation translate = new Translation(xPosition, yPosition, 0);
            gamePiece.setTranslation(translate);

            gamePiece.setColor(color);

            Scaling scaling2 = new Scaling((float)12.5, (float)12.5, 0);

            gamePiece.setScaling(scaling2);

            // If there isn't a color, don't paint the object...
            if (color != null)
            {

                if (this.isSolid())
                {
                    gamePiece.setSolid(true);
                }

                else
                {
                    gamePiece.setSolid(false);
                }

                gamePiece.paint(gl2);

            }

            // Move to the right...
            if (xPosition < 500)
            {
                xPosition += 40;
            }

            // Move up...
            else
            {
                xPosition = 25;
                yPosition += 40;
            }
        }
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
            this.gamePieceLock.lock();
            return Collections.unmodifiableList(this.gamePieces);
        }

        finally
        {
            this.gamePieceLock.unlock();
        }
    }


    /**
     * Adds the specified game piece to the board.
     *
     * @param gamePiece
     *            the game piece to be added
     */
    public void addGamePiece(IGamePiece gamePiece)
    {
        try
        {
            this.gamePieceLock.lock();
            this.gamePieces.add(gamePiece);
        }

        finally
        {
            this.gamePieceLock.unlock();
        }
    }


    /**
     * Removes the specified game piece from the board.
     *
     * @param gamePiece
     *            the game piece to be removed
     */
    public void removeGamePiece(IGamePiece gamePiece)
    {
        try
        {
            this.gamePieceLock.lock();
            this.gamePieces.remove(gamePiece);
        }

        finally
        {
            this.gamePieceLock.unlock();
        }
    }


    /**
     * Clears the list of GamePieces.
     */
    public void clearGamePieces()
    {
        try
        {
            this.gamePieceLock.lock();
            this.gamePieces.clear();
        }

        finally
        {
            this.gamePieceLock.unlock();
        }
    }

}
