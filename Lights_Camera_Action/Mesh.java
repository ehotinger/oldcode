package cs3744.graphics.primitives;

import java.util.LinkedList;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import cs3744.graphics.interfaces.IFace;
import java.util.List;
import cs3744.graphics.interfaces.IMesh;
import cs3744.graphics.interfaces.IPrimitive;

// -------------------------------------------------------------------------
/**
 * Describes a Mesh that has a List of Faces.
 *
 * @author Eric Hotinger
 * @version Aug 8, 2012
 */
public abstract class Mesh
    extends cs3744.graphics.primitives.Primitive
    implements cs3744.graphics.interfaces.IMesh
{
    private List<IFace> faceList;

    private final Lock  faceListLock;


    // ----------------------------------------------------------
    /**
     * Create a new Mesh object.
     */
    public Mesh()
    {
        this.faceListLock = new ReentrantLock();
        this.faceList = new LinkedList<IFace>();

        this.setColor(this.getColor());
        this.setParent(this);
        this.setRotation(this.getRotation());
        this.setScaling(this.getScaling());
        this.setShear(this.getShear());
        this.setSolid(this.isSolid());
        this.setTranslation(this.getTranslation());
        this.setMaterial(this.getMaterial());
    }


    // ----------------------------------------------------------
    /**
     * Create a new Mesh object based on a parent.
     *
     * @param parent
     *            the parent to base this Mesh object on
     */
    public Mesh(IPrimitive parent)
    {
        this.faceListLock = new ReentrantLock();

        this.faceList = new LinkedList<IFace>();

        this.setParent(parent);
        this.setColor(parent.getColor());
        this.setMaterial(parent.getMaterial());
        this.setRotation(parent.getRotation());
        this.setScaling(parent.getScaling());
        this.setShear(parent.getShear());
        this.setSolid(parent.isSolid());
        this.setTranslation(parent.getTranslation());
    }


    // ----------------------------------------------------------
    /**
     * Create a new Mesh object based on a copy and a parent.
     *
     * @param meshToCopy
     *            a mesh to copy
     * @param parent
     *            a parent to base this mesh's properties around
     */
    public Mesh(IMesh meshToCopy, IPrimitive parent)
    {
        this.faceListLock = new ReentrantLock();

        this.faceList = new LinkedList<IFace>();

        for (IFace face : meshToCopy.getFaceList())
        {
            this.faceList.add(face.copy());
        }

        this.setParent(parent);
        this.setColor(parent.getColor());
        this.setMaterial(parent.getMaterial());
        this.setRotation(parent.getRotation());
        this.setScaling(parent.getScaling());
        this.setShear(parent.getShear());
        this.setSolid(parent.isSolid());
        this.setTranslation(parent.getTranslation());

    }


    /**
     * Returns the face list of the mesh.
     *
     * @return the face list of the mesh
     */
    public List<IFace> getFaceList()
    {
        try
        {
            this.faceListLock.lock();
            return Collections.unmodifiableList(this.faceList);
        }

        finally
        {
            this.faceListLock.unlock();
        }

    }


    /**
     * Adds a face to the mesh.
     *
     * @param face
     *            the face to add to the mesh
     */
    public void addFace(IFace face)
    {
        try
        {
            this.faceListLock.lock();
            this.faceList.add(face);
        }

        finally
        {
            this.faceListLock.unlock();
        }
    }


    /**
     * Removes a face from the mesh.
     *
     * @param face
     *            the face to remove from the mesh
     */
    public void removeFace(IFace face)
    {
        try
        {
            this.faceListLock.lock();
            this.faceList.remove(face);
        }

        finally
        {
            this.faceListLock.unlock();
        }
    }


    /**
     * Clears the list of faces.
     */
    public void clearFaces()
    {
        try
        {
            this.faceListLock.lock();
            this.faceList.clear();
        }

        finally
        {
            this.faceListLock.unlock();
        }
    }

}
