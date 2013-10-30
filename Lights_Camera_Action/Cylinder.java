package cs3744.graphics.primitives;

import cs3744.graphics.common.Color;
import cs3744.graphics.interfaces.IFace;
import cs3744.graphics.interfaces.ICylinder;
import cs3744.graphics.interfaces.IPrimitive;
import javax.media.opengl.GL2;
import cs3744.graphics.primitives.solution.Circle;

// -------------------------------------------------------------------------
/**
 * Draws Cylinders by using two circles and connecting them together.
 *
 * @author Eric Hotinger
 * @version Aug 8, 2012
 */
public class Cylinder
    extends cs3744.graphics.primitives.Mesh
    implements cs3744.graphics.interfaces.ICylinder
{
    private Circle top;


    // ----------------------------------------------------------
    /**
     * Create a new Cylinder object.
     */
    public Cylinder()
    {
        this.top = new Circle();

        this.setParent(null);
        this.setColor(Color.GRAY);
        this.setMaterial(null);
        this.setRotation(null);
        this.setScaling(null);
        this.setShear(null);
        this.setSolid(false);
        this.setTranslation(null);
    }


    // ----------------------------------------------------------
    /**
     * Create a new Cylinder object based on a primitive and a parent.
     *
     * @param primitiveToCopy
     *            primitive to copy
     * @param parent
     *            parent to base attributes on
     */
    public Cylinder(ICylinder primitiveToCopy, IPrimitive parent)
    {
        this.clearFaces();
        for (IFace face : primitiveToCopy.getFaceList())
        {
            this.addFace(face.copy());
        }

        this.setColor(parent.getColor());
        this.setMaterial(parent.getMaterial());
        this.setParent(parent);
        this.setRotation(parent.getRotation());
        this.setScaling(parent.getScaling());
        this.setShear(parent.getShear());
        this.setSolid(parent.isSolid());
        this.setTranslation(parent.getTranslation());
    }


    // ----------------------------------------------------------
    /**
     * Create a new Cylinder object based on a parent.
     *
     * @param parent
     *            the parent to base attributes on
     */
    public Cylinder(IPrimitive parent)
    {
        this.setColor(parent.getColor());
        this.setMaterial(parent.getMaterial());
        this.setParent(parent);
        this.setRotation(parent.getRotation());
        this.setScaling(parent.getScaling());
        this.setShear(parent.getShear());
        this.setSolid(parent.isSolid());
        this.setTranslation(parent.getTranslation());
    }


    /**
     * Returns a copy of this Cylinder
     */
    public IPrimitive copy()
    {
        return new Cylinder(this);
    }


    /**
     * Returns a copy of this cylinder and bases it on a parent.
     *
     * @return a copy of this cylinder with a parent
     */
    public IPrimitive copy(IPrimitive parent)
    {
        return new Cylinder(this, parent);
    }


    /**
     * Paints the cylinder.
     *
     * @param gl2
     *            the OpenGL state machine
     */
    public void paint(GL2 gl2)
    {
        this.top = new Circle();

        this.addFace(top);

        /**
         * This code isn't working because of Cast conversion errors... it is impossible to create the
         * correct cylinder as a result...
         */
//        IPoint[] points = (IPoint[])top.getVertices().toArray();
//
//        for (int i = 0; i < points.length; i++)
//        {
//            IPoint currentBottom =
//                new Point(Operations.add(
//                    points[i],
//                    new Vector(0.0f, 0.0f, 0.5f)));
//            this.bottom.addVertex(currentBottom);
//            this.addFace(bottom);
//        }

        for (IFace face : this.getFaceList())
        {

            face.setParent(this);
            face.setColor(this.getColor());
            face.setMaterial(this.getMaterial());
            face.setRotation(this.getRotation());
            face.setShear(this.getShear());
            face.setSolid(this.isSolid());

            face.paint(gl2);
        }
    }

}
