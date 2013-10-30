package cs3744.graphics.common;

import cs3744.vectorAlgebra.Tuple4f;
import cs3744.vectorAlgebra.Matrix4f;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import cs3744.graphics.common.solution.ProjectionMode;
import cs3744.graphics.interfaces.IPoint;
import cs3744.graphics.interfaces.IPrimitive;
import cs3744.graphics.interfaces.ITransformation;
import cs3744.graphics.interfaces.IVector;
import javax.media.opengl.GL2;

// -------------------------------------------------------------------------
/**
 * Defines a camera that can look around a scene.
 *
 * @author Eric Hotinger
 * @version Aug 8, 2012
 */
public class Camera
    extends cs3744.graphics.primitives.Primitive
    implements cs3744.graphics.interfaces.ICamera
{
    private IPoint         eyePosition        = null;
    private IPoint         lookAtPoint        = null;
    private IVector        upGuess            = null;
    private ProjectionMode projectionMode     = ProjectionMode.ORTHOGRAPHIC_3D;
    private Float          viewAngle          = (float)0;
    private Float          aspectRatio        = (float)0;
    private Float          nearDistance       = (float)0;
    private Float          farDistance        = (float)0;
    private Float          left               = (float)0;
    private Float          right              = (float)0;
    private Float          top                = (float)0;
    private Float          bottom             = (float)0;

    private final Lock     projectionModeLock = new ReentrantLock();
    private final Lock     aspectRatioLock    = new ReentrantLock();
    private final Lock     viewAngleLock      = new ReentrantLock();
    private final Lock     nearDistanceLock   = new ReentrantLock();
    private final Lock     farDistanceLock    = new ReentrantLock();


    // ----------------------------------------------------------
    /**
     * Create a new Camera object.
     */
    public Camera()
    {
        this.setParent(null);
        this.setColor(this.getColor());
        this.setMaterial(this.getMaterial());
        this.setProjectionMode(ProjectionMode.ORTHOGRAPHIC_3D);
        this.setRotation(this.getRotation());
        this.setScaling(this.getScaling());
        this.setShear(this.getShear());
        this.setSolid(this.isSolid());
        this.setTranslation(this.getTranslation());
    }


    // ----------------------------------------------------------
    /**
     * Create a new Camera object.
     *
     * @param viewAngle
     * @param aspectRatio
     * @param nearDistance
     * @param farDistance
     */
    public Camera(
        Float viewAngle,
        Float aspectRatio,
        Float nearDistance,
        Float farDistance)
    {
        this.viewAngle = viewAngle;
        this.aspectRatio = aspectRatio;
        this.nearDistance = nearDistance;
        this.farDistance = farDistance;

        this.setParent(null);
        this.setColor(this.getColor());
        this.setMaterial(this.getMaterial());
        this.setProjectionMode(ProjectionMode.ORTHOGRAPHIC_3D);
        this.setRotation(this.getRotation());
        this.setScaling(this.getScaling());
        this.setShear(this.getShear());
        this.setSolid(this.isSolid());
        this.setTranslation(this.getTranslation());
    }


    // ----------------------------------------------------------
    /**
     * Create a new Camera object.
     *
     * @param eyePosition
     * @param lookAtPoint
     * @param upGuess
     */
    public Camera(IPoint eyePosition, IPoint lookAtPoint, IVector upGuess)
    {
        this.eyePosition = eyePosition;
        this.lookAtPoint = lookAtPoint;
        this.upGuess = upGuess;

        this.setParent(null);
        this.setColor(this.getColor());
        this.setMaterial(this.getMaterial());
        this.setProjectionMode(ProjectionMode.ORTHOGRAPHIC_3D);
        this.setRotation(this.getRotation());
        this.setScaling(this.getScaling());
        this.setShear(this.getShear());
        this.setSolid(this.isSolid());
        this.setTranslation(this.getTranslation());
    }


    // ----------------------------------------------------------
    /**
     * Create a new Camera object.
     *
     * @param eyePosition
     *            the eye position
     * @param lookAtPoint
     *            the look at point
     * @param upGuess
     *            the up guess
     * @param viewAngle
     *            the view angle
     * @param aspectRatio
     *            aspect ratio
     * @param nearDistance
     *            near distance
     * @param farDistance
     *            far distance
     */
    public Camera(
        IPoint eyePosition,
        IPoint lookAtPoint,
        IVector upGuess,
        Float viewAngle,
        Float aspectRatio,
        Float nearDistance,
        Float farDistance)
    {
        this.eyePosition = eyePosition;
        this.lookAtPoint = lookAtPoint;
        this.upGuess = upGuess;
        this.viewAngle = viewAngle;
        this.aspectRatio = aspectRatio;
        this.nearDistance = nearDistance;
        this.farDistance = farDistance;

        this.setParent(null);
        this.setColor(this.getColor());
        this.setMaterial(this.getMaterial());
        this.setProjectionMode(ProjectionMode.ORTHOGRAPHIC_3D);
        this.setRotation(this.getRotation());
        this.setScaling(this.getScaling());
        this.setShear(this.getShear());
        this.setSolid(this.isSolid());
        this.setTranslation(this.getTranslation());

    }


    // ----------------------------------------------------------
    /**
     * Create a new Camera object.
     *
     * @param eyePosition
     *            eye position
     * @param lookAtPoint
     *            look-at point
     * @param upGuess
     *            up guess
     * @param left
     *            left
     * @param right
     *            right
     * @param bottom
     *            bottom
     * @param top
     *            top
     * @param nearDistance
     *            near distance
     * @param farDistance
     *            far distance
     */
    public Camera(
        IPoint eyePosition,
        IPoint lookAtPoint,
        IVector upGuess,
        Float left,
        Float right,
        Float bottom,
        Float top,
        Float nearDistance,
        Float farDistance)
    {
        this.eyePosition = eyePosition;
        this.lookAtPoint = lookAtPoint;
        this.upGuess = upGuess;
        this.nearDistance = nearDistance;
        this.farDistance = farDistance;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;

        this.setParent(null);
        this.setColor(this.getColor());
        this.setMaterial(this.getMaterial());
        this.setProjectionMode(ProjectionMode.ORTHOGRAPHIC_3D);
        this.setRotation(this.getRotation());
        this.setScaling(this.getScaling());
        this.setShear(this.getShear());
        this.setSolid(this.isSolid());
        this.setTranslation(this.getTranslation());
    }


    // ----------------------------------------------------------
    /**
     * Create a new Camera object.
     *
     * @param parent
     *            a parent to base the Camera off of
     */
    public Camera(IPrimitive parent)
    {
        this.setParent(parent);
        this.setColor(parent.getColor());
        this.setMaterial(parent.getMaterial());
        this.setRotation(parent.getRotation());
        this.setScaling(parent.getScaling());
        this.setShear(parent.getShear());
        this.setTranslation(parent.getTranslation());
    }


    // ----------------------------------------------------------
    /**
     * Create a new Camera object.
     *
     * @param primitiveToCopy
     *            a primitive to copy
     * @param parent
     *            a parent to base the camera off of
     */
    public Camera(IPrimitive primitiveToCopy, IPrimitive parent)
    {
        this.setParent(parent);
        this.setColor(parent.getColor());
        this.setMaterial(parent.getMaterial());
        this.setRotation(parent.getRotation());
        this.setScaling(parent.getScaling());
        this.setShear(parent.getShear());
        this.setTranslation(parent.getTranslation());
    }


    /**
     * Returns a copy of the Camera
     *
     * @return a copy of the Camera
     */
    public IPrimitive copy()
    {
        return new Camera(this);
    }


    /**
     * Returns a copy with a parent attached.
     *
     * @return a copy with a parent attached
     */
    public IPrimitive copy(IPrimitive parent)
    {
        return new Camera(this, parent);
    }


    /**
     * Returns the eye position.
     *
     * @return the eye position
     */
    public IPoint getEyePosition()
    {
        return this.eyePosition;
    }


    /**
     * Returns the look at point.
     *
     * @return the look at point
     */
    public IPoint getLookAtPoint()
    {
        return this.lookAtPoint;
    }


    /**
     * Returns the up guess vector.
     *
     * @return the up guess vector
     */
    public IVector getUpGuessVector()
    {
        return this.upGuess;
    }


    /**
     * Returns the projection mode.
     *
     * @return the projection mode
     */
    public ProjectionMode getProjectionMode()
    {
        try
        {
            this.projectionModeLock.lock();
            return this.projectionMode;
        }

        finally
        {
            this.projectionModeLock.unlock();
        }

    }


    /**
     * Changes the projection mode.
     *
     * @param projectionMode
     *            the new projection mode
     */
    public void setProjectionMode(ProjectionMode projectionMode)
    {
        try
        {
            this.projectionModeLock.lock();
            this.projectionMode = projectionMode;
        }

        finally
        {
            this.projectionModeLock.unlock();
        }

    }


    /**
     * Returns the view angle.
     *
     * @return the view angle
     */
    public Float getViewAngle()
    {
        try
        {
            this.viewAngleLock.lock();
            return this.viewAngle;
        }

        finally
        {
            this.viewAngleLock.unlock();
        }
    }


    /**
     * Changes the view angle.
     *
     * @param viewAngle
     *            the new view angle
     */
    public void setViewAngle(Float viewAngle)
    {
        try
        {
            this.viewAngleLock.lock();
            this.viewAngle = viewAngle;
        }

        finally
        {
            this.viewAngleLock.unlock();
        }

    }


    /**
     * Returns the aspect ratio.
     *
     * @return the aspect ratio
     */
    public Float getAspectRatio()
    {
        try
        {
            this.aspectRatioLock.lock();
            return this.aspectRatio;
        }

        finally
        {
            this.aspectRatioLock.unlock();
        }
    }


    /**
     * Changes the aspect ratio.
     *
     * @param aspectRatio
     *            the new aspect ratio
     */
    public void setAspectRatio(Float aspectRatio)
    {
        try
        {
            this.aspectRatioLock.lock();
            this.aspectRatio = aspectRatio;
        }

        finally
        {
            this.aspectRatioLock.unlock();
        }
    }


    /**
     * Returns the near distance.
     *
     * @return the near distance
     */
    public Float getNearDistance()
    {
        try
        {
            this.nearDistanceLock.lock();
            return this.nearDistance;
        }

        finally
        {
            this.nearDistanceLock.unlock();
        }
    }


    /**
     * Changes the nearDistance.
     *
     * @param nearDistance
     *            the new near distance
     */
    public void setNearDistance(Float nearDistance)
    {
        try
        {
            this.nearDistanceLock.lock();
            this.nearDistance = nearDistance;
        }

        finally
        {
            this.nearDistanceLock.unlock();
        }

    }


    /**
     * Returns the far distance.
     *
     * @return the far distance
     */
    public Float getFarDistance()
    {
        try
        {
            this.farDistanceLock.lock();
            return this.farDistance;
        }

        finally
        {
            this.farDistanceLock.unlock();
        }
    }


    /**
     * Changes the far distance.
     *
     * @param farDistance
     *            the new far distance
     */
    public void setFarDistance(Float farDistance)
    {
        try
        {
            this.farDistanceLock.lock();
            this.farDistance = farDistance;
        }

        finally
        {
            this.farDistanceLock.unlock();
        }

    }


    /**
     * Returns the viewing transformation of the camera.
     *
     * @return the viewing transformation of the camera
     */
    public ITransformation getViewingTransformation()
    {
        Tuple4f first = new Tuple4f(-(right - left) / (left - right), 0, 0, 0);
        Tuple4f second = new Tuple4f(0, -(top - bottom) / (bottom - top), 0, 0);
        Tuple4f third =
            new Tuple4f(0, 0, -(this.farDistance - this.nearDistance)
                / (nearDistance - farDistance), 0);
        Tuple4f fourth =
            new Tuple4f(
                0,
                0,
                -(this.nearDistance - (this.farDistance - this.nearDistance)
                    / (this.nearDistance - this.farDistance)),
                1);

        Matrix4f matrix = new Matrix4f(first, second, third, fourth);

        ITransformation viewingTransformation = new Transformation(matrix);

        return viewingTransformation;
    }


    /**
     * Returns the projection transformation of the camera.
     *
     * @return the projection transformation of the camera
     */
    public ITransformation getProjectionTransformation()
    {
        float a =
            -(this.farDistance + this.nearDistance)
                / (this.farDistance - this.nearDistance);
        float b =
            (this.farDistance * this.nearDistance)
                / (this.farDistance - this.nearDistance);

        Tuple4f first =
            new Tuple4f((2 * this.nearDistance) / (right - left), 0, 0, 0);
        Tuple4f second =
            new Tuple4f(0, (2 * this.nearDistance) / (top - bottom), 0, 0);
        Tuple4f third = new Tuple4f(0, 0, 1 + a, 0);
        Tuple4f fourth =
            new Tuple4f((left + right) / (left - right), -(right + left)
                / (right - left), -b, -(left + right) / (left - right));

        Matrix4f matrix = new Matrix4f(first, second, third, fourth);

        ITransformation projectionTransformation = new Transformation(matrix);

        return projectionTransformation;
    }


    /**
     * Paints the Camera.
     *
     * @param gl2
     *            the opengl state machine
     */
    public void paint(GL2 gl2)
    {
        this.setParent(this.getParent());

        gl2.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl2.glPushMatrix();
        gl2.glLoadIdentity();

        gl2.glLoadMatrixf(this.getProjectionTransformation()
            .toColumnMajorArray(), 0);

        gl2.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl2.glPushMatrix();
        gl2.glLoadIdentity();

        gl2.glLoadMatrixf(
            this.getViewingTransformation().toColumnMajorArray(),
            0);
    }

}
