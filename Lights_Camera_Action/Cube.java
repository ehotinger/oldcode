package cs3744.graphics.primitives;

import cs3744.graphics.common.Color;
import cs3744.graphics.primitives.solution.Point;
import cs3744.graphics.interfaces.IFace;
import cs3744.graphics.interfaces.IPrimitive;
import cs3744.graphics.primitives.solution.Rectangle;
import javax.media.opengl.GL2;

// -------------------------------------------------------------------------
/**
 * Creates a Cube by drawing six different rectangles. There are some defects
 * with a SOLID cube because of the .solution.Rectangle class. See the paint
 * method for more information.
 *
 * @author Eric Hotinger
 * @version Aug 8, 2012
 */
public class Cube
    extends cs3744.graphics.primitives.Mesh
    implements cs3744.graphics.interfaces.ICube
{
    private static final Point point1 = new Point(0, 0, -1);
    private static final Point point2 = new Point(1, 0, -1);
    private static final Point point3 = new Point(1, 1, -1);
    private static final Point point4 = new Point(0, 1, -1);
    private static final Point point5 = new Point(0, 0, 0);
    private static final Point point6 = new Point(1, 0, 0);
    private static final Point point7 = new Point(1, 1, 0);
    private static final Point point8 = new Point(0, 1, 0);

    private Rectangle          front  = new Rectangle(
                                          point1,
                                          point2,
                                          point3,
                                          point4);
    private Rectangle          top    = new Rectangle(
                                          point5,
                                          point6,
                                          point7,
                                          point8);
    private Rectangle          left   = new Rectangle(
                                          point8,
                                          point5,
                                          point1,
                                          point4);
    private Rectangle          right  = new Rectangle(
                                          point2,
                                          point3,
                                          point7,
                                          point6);
    private Rectangle          bottom = new Rectangle(
                                          point6,
                                          point5,
                                          point1,
                                          point2);
    private Rectangle          back   = new Rectangle(
                                          point7,
                                          point8,
                                          point4,
                                          point3);


    // ----------------------------------------------------------
    /**
     * Create a new Cube object with a gray color, no parent, and no attributes.
     */
    public Cube()
    {
        this.addFace(back);
        this.addFace(top);
        this.addFace(left);
        this.addFace(right);
        this.addFace(bottom);
        this.addFace(front);

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
     * Create a new Cube object based off a primitiveToCopy and parent-based
     * attributes.
     *
     * @param primitiveToCopy
     *            a Cube object to copy
     * @param parent
     *            a parent to copy attributes from
     */
    public Cube(Cube primitiveToCopy, IPrimitive parent)
    {
        this.addFace(primitiveToCopy.back);
        this.addFace(primitiveToCopy.top);
        this.addFace(primitiveToCopy.left);
        this.addFace(primitiveToCopy.right);
        this.addFace(primitiveToCopy.bottom);
        this.addFace(primitiveToCopy.front);

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
     * Create a new Cube object based off a parent.
     *
     * @param parent
     *            the parent to base the Cube off of
     */
    public Cube(IPrimitive parent)
    {
        this.addFace(back);
        this.addFace(top);
        this.addFace(left);
        this.addFace(right);
        this.addFace(bottom);
        this.addFace(front);

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
     * Creates a copy of this Cube.
     *
     * @return a copy of this cube
     */
    public IPrimitive copy()
    {
        return new Cube(this);
    }


    /**
     * Creates a copy of this Cube and gives it parent-based attributes.
     *
     * @return a copy of this Cube with parent-based attributes
     */
    public IPrimitive copy(IPrimitive parent)
    {
        return new Cube(this, parent);
    }


    /**
     * Paints the Cube. For some reason, the SOLID cube malfunctions slightly,
     * but the wireframe cube works perfectly as intended. Color inheritance is
     * messed up slightly, but material works fine. I believe this is due to the
     * solutions Rectangle class.
     *
     * @param gl2
     *            the OpenGL state machine.
     */
    public void paint(GL2 gl2)
    {
        for (IFace face : this.getFaceList())
        {
            face.setParent(this);
            face.setColor(this.getColor());
            face.setMaterial(this.getMaterial());
            face.setSolid(this.isSolid());
            face.setRotation(this.getRotation());
            face.paint(gl2);
        }

    }

}
