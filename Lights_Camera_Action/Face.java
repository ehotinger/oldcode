package cs3744.graphics.primitives;

import cs3744.graphics.common.Vector;
import java.util.ArrayList;
import java.util.Collections;
import cs3744.graphics.interfaces.IMaterial;
import cs3744.graphics.interfaces.IColor;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import cs3744.graphics.interfaces.IPoint;
import cs3744.graphics.interfaces.IVector;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import cs3744.graphics.interfaces.IFace;
import cs3744.graphics.interfaces.IPrimitive;

// -------------------------------------------------------------------------
/**
 * Defines the basic aspects of a Face such as vertices and surface normals.
 * Then, it adds the vertices and normals to a map for easy access.
 *
 * @author Eric Hotinger
 * @version Aug 7, 2012
 */
public abstract class Face
    extends cs3744.graphics.primitives.Primitive
    implements cs3744.graphics.interfaces.IFace
{

    private IVector              surfaceNormal;
    private boolean              isFlat;
    private Set<IPoint>          vertices;
    private Collection<IVector>  normals;
    private Map<IPoint, IVector> vertexNormalMap;

    private final Lock           surfaceNormalLock;
    private final Lock           isFlatLock;
    private final Lock           setLock;
    private final Lock           mapLock;


    // ----------------------------------------------------------
    /**
     * Creates an empty face.
     */
    protected Face()
    {

        this.surfaceNormalLock = new ReentrantLock();
        this.isFlatLock = new ReentrantLock();
        this.setLock = new ReentrantLock();
        this.mapLock = new ReentrantLock();

        this.vertexNormalMap = new LinkedHashMap<IPoint, IVector>();
        this.normals = new ArrayList<IVector>();
    }


    // ----------------------------------------------------------
    /**
     * Copy constructor.
     *
     * @param primitiveToCopy
     * @param parent
     */
    protected Face(IFace primitiveToCopy, IPrimitive parent)
    {
        this.surfaceNormalLock = new ReentrantLock();
        this.isFlatLock = new ReentrantLock();
        this.setLock = new ReentrantLock();
        this.mapLock = new ReentrantLock();

        this.vertexNormalMap = primitiveToCopy.getVertexNormalMap();
    }


    // ----------------------------------------------------------
    /**
     * Constructs a new Face with the provided parent.
     *
     * @param parent
     */
    protected Face(IPrimitive parent)
    {
        this.surfaceNormalLock = new ReentrantLock();
        this.isFlatLock = new ReentrantLock();
        this.setLock = new ReentrantLock();
        this.mapLock = new ReentrantLock();
    }


    /**
     * Adds a vertex without an associated IVector.
     *
     * @param vertex
     *            the vertex to add
     */
    public void addVertex(IPoint vertex)
    {
        try
        {
            this.setLock.lock();
            this.vertices.add(vertex);
        }

        finally
        {
            this.setLock.unlock();
        }

    }


    /**
     * Adds a vertex with an associated normal.
     *
     * @param vertex
     *            a vertex
     * @param normal
     *            the normal
     */
    public void addVertex(IPoint vertex, IVector normal)
    {
        try
        {
            this.mapLock.lock();
            this.vertexNormalMap.put(vertex, normal);
        }

        finally
        {
            this.mapLock.unlock();
        }
    }


    /**
     * Calculates the vertex normal based on the crossproduct of a point with
     * the two adjacent points.
     */
    public void calculateAndSetVertexNormals()
    {
        /**
         * There is actually no need for this method because it is never used in
         * our implementation.
         */
    }


    /**
     * Calculates the normal of the surface based on the robust method suggested
     * by Newell.
     */
    public void calculateAndSetSurfaceNormal()
    {
        float x = 0;
        float y = 0;
        float z = 0;

        for (int i = 0; i < ((IPoint[])this.vertices.toArray()).length; i++)
        {
            Point currentPoint = (Point)((IPoint[])this.vertices.toArray())[i];
            Point nextPoint =
                (Point)((IPoint[])this.vertices.toArray())[(i + 1)
                    % ((IPoint[])this.vertices.toArray()).length];
            x +=
                (currentPoint.getY() - nextPoint.getY())
                    * (currentPoint.getZ() + nextPoint.getZ());
            y +=
                (currentPoint.getZ() - nextPoint.getZ())
                    * (currentPoint.getX() + nextPoint.getX());
            z +=
                (currentPoint.getX() - nextPoint.getX())
                    * (currentPoint.getY() + nextPoint.getY());
        }
        Vector normal = new Vector(x, y, z);

        this.setSurfaceNormal(normal);

    }


    /**
     * Clears the vertices.
     */
    public void clearVertices()
    {
        try
        {
            this.setLock.lock();
            this.vertices.clear();
        }

        finally
        {
            this.setLock.unlock();
        }
    }


    /**
     * Returns the vertex-IVectors.
     */
    public Collection<IVector> getNormals()
    {
        return Collections.unmodifiableCollection(this.normals);

    }


    /**
     * Returns the surface normal.
     *
     * @return the surface normal
     */
    public IVector getSurfaceNormal()
    {
        try
        {
            this.surfaceNormalLock.lock();
            return this.surfaceNormal;
        }

        finally
        {
            this.surfaceNormalLock.unlock();
        }
    }


    /**
     * Returns the vertex normal map.
     *
     * @return the vertex normal map
     */
    public Map<IPoint, IVector> getVertexNormalMap()
    {
        try
        {
            this.mapLock.lock();
            return Collections.unmodifiableMap(this.vertexNormalMap);
        }

        finally
        {
            this.mapLock.unlock();
        }
    }


    /**
     * Returns the vertices (points) of the face.
     */
    public Set<IPoint> getVertices()
    {
        try
        {
            this.setLock.lock();
            return Collections.unmodifiableSet(this.vertices);
        }

        finally
        {
            this.setLock.unlock();
        }
    }


    /**
     * Returns whether or not this face is flat.
     *
     * @return true or false if this face is flat.
     */
    public Boolean isFlat()
    {
        try
        {
            this.isFlatLock.lock();
            return this.isFlat;
        }

        finally
        {
            this.isFlatLock.unlock();
        }

    }


    /**
     * Sets whether or not the surface is flat.
     *
     * @param isFlat
     *            whether or not the Face is flat.
     */
    public void setFlat(boolean isFlat)
    {
        try
        {
            this.isFlatLock.lock();
            this.isFlat = isFlat;
        }

        finally
        {
            this.isFlatLock.unlock();
        }

    }


    /**
     * Sets the surface normal.
     *
     * @param surfaceNormal
     *            the new surfaceNormal
     */
    public void setSurfaceNormal(IVector surfaceNormal)
    {
        try
        {
            this.surfaceNormalLock.lock();
            this.surfaceNormal = surfaceNormal;
        }

        finally
        {
            this.surfaceNormalLock.unlock();
        }
    }


    /**
     * Changes the material of the Face.
     *
     * @param material
     *            the material
     */
    public void setMaterial(IMaterial material)
    {
        super.setMaterial(material);
    }


    /**
     * Changes the color of the face.
     *
     * @param color
     *            the color
     */
    public void setColor(IColor color)
    {
        super.setColor(color);
    }


    /**
     * Removes a vertex.
     *
     * @param vertex
     *            the vertex to be removed
     */
    public void removeVertex(IPoint vertex)
    {
        try
        {
            this.setLock.lock();
            this.vertices.remove(vertex);
        }

        finally
        {
            this.setLock.unlock();
        }
    }

}
