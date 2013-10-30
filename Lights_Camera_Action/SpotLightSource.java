package cs3744.graphics.common;

import cs3744.graphics.primitives.solution.Point;
import cs3744.graphics.interfaces.ISpotLightSource;
import cs3744.graphics.interfaces.IVector;
import cs3744.graphics.interfaces.IPoint;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.GL2;
import cs3744.graphics.interfaces.LightSourceType;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import cs3744.graphics.interfaces.IColor;
import cs3744.graphics.interfaces.ILightSource;
import cs3744.graphics.interfaces.IPrimitive;

// -------------------------------------------------------------------------
/**
 * A SpotLightSource is a LightSource with a Point position.
 *
 * @author Eric Hotinger
 * @version Aug 7, 2012
 */
public class SpotLightSource
    extends cs3744.graphics.common.LightSource
    implements cs3744.graphics.interfaces.ISpotLightSource
{

    private float      cutoffAngle;
    private float      exponent;
    private IPoint     position;
    private IVector    direction;

    private final Lock positionLock;
    private final Lock exponentLock;
    private final Lock cutoffAngleLock;
    private final Lock directionLock;


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with white light components, no
     * attenuation, and no parent.
     *
     * @param primitiveToCopy
     *            the primitive to copy
     * @param parent
     *            the parent to base the light source off of
     */
    protected SpotLightSource(ILightSource primitiveToCopy, IPrimitive parent)
    {
        super(primitiveToCopy, parent);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = (float)180.0;
        this.exponent = 0;
        this.position = new Point(0, 0, 1);
        this.direction = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with white light components, no
     * attenuation, and no parent.
     *
     * @param lightNumber
     *            the light number
     */
    protected SpotLightSource(int lightNumber)
    {
        super(lightNumber);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = (float)180.0;
        this.exponent = 0;
        this.position = new Point(0, 0, 1);
        this.direction = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with white light components, the provided
     * attenuation and no parent.
     *
     * @param lightNumber
     *            the light number
     * @param constantAttenuationFactor
     *            the constant attenuation factor
     * @param linearAttenuationFactor
     *            the linear attenuation factor
     * @param quadraticAttenuationFactor
     *            the quadratic attenuation factor
     */
    protected SpotLightSource(
        int lightNumber,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor)
    {
        super(
            lightNumber,
            constantAttenuationFactor,
            linearAttenuationFactor,
            quadraticAttenuationFactor);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = (float)180.0;
        this.exponent = 0;
        this.position = new Point(0, 0, 1);
        this.direction = new Vector(0, 0, -1);

    }


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with white light components, the provided
     * attenuation, and the provided parent.
     *
     * @param lightNumber
     *            the light number
     * @param constantAttenuationFactor
     *            the constant attenuation factor
     * @param linearAttenuationFactor
     *            the linear attenuation factor
     * @param quadraticAttenuationFactor
     *            the quadratic attenuation factor
     * @param parent
     *            the parent to base the light source off of
     */
    protected SpotLightSource(
        int lightNumber,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor,
        IPrimitive parent)
    {
        super(
            lightNumber,
            constantAttenuationFactor,
            linearAttenuationFactor,
            quadraticAttenuationFactor,
            parent);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = (float)180.0;
        this.exponent = 0;
        this.position = new Point(0, 0, 1);
        this.direction = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with the provided light components, no
     * attenuation, and no parent.
     *
     * @param lightNumber
     *            the light number
     * @param ambientComponent
     *            the ambient component
     * @param diffuseComponent
     *            the diffuse component
     * @param specularComponent
     *            the specular component
     */
    protected SpotLightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent)
    {
        super(
            lightNumber,
            ambientComponent,
            diffuseComponent,
            specularComponent);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = (float)180.0;
        this.exponent = 0;
        this.position = new Point(0, 0, 1);
        this.direction = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with the provided light components,
     * attenuation, and no parent.
     *
     * @param lightNumber
     *            the light number
     * @param ambientComponent
     *            the ambient component
     * @param diffuseComponent
     *            the diffuse component
     * @param specularComponent
     *            the specular component
     * @param constantAttenuationFactor
     *            the constant attenuation factor
     * @param linearAttenuationFactor
     *            the linear attenuation factor
     * @param quadraticAttenuationFactor
     *            the quadratic attenuation factor
     */
    protected SpotLightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor)
    {
        super(
            lightNumber,
            ambientComponent,
            diffuseComponent,
            specularComponent,
            constantAttenuationFactor,
            linearAttenuationFactor,
            quadraticAttenuationFactor);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = (float)180.0;
        this.exponent = 0;
        this.position = new Point(0, 0, 1);
        this.direction = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with the provided light components,
     * attenuation, and parent.
     *
     * @param lightNumber
     *            the light number
     * @param ambientComponent
     *            the ambient component
     * @param diffuseComponent
     *            the diffuse component
     * @param specularComponent
     *            the specular component
     * @param constantAttenuationFactor
     *            the constant attenuation factor
     * @param linearAttenuationFactor
     *            the linear attenuation factor
     * @param quadraticAttenuationFactor
     *            the quadratic attenuation factor
     * @param parent
     *            the parent to base this light source off of
     */
    protected SpotLightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor,
        IPrimitive parent)
    {
        super(
            lightNumber,
            ambientComponent,
            diffuseComponent,
            specularComponent,
            constantAttenuationFactor,
            linearAttenuationFactor,
            quadraticAttenuationFactor,
            parent);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = (float)180.0;
        this.exponent = 0;
        this.position = new Point(0, 0, 1);
        this.direction = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with the provided light components,
     * attenuation, direction and parent.
     *
     * @param lightNumber
     *            the light number
     * @param ambientComponent
     *            the ambient component
     * @param diffuseComponent
     *            the diffuse component
     * @param specularComponent
     *            the specular component
     * @param constantAttenuationFactor
     *            the constant attenuation factor
     * @param linearAttenuationFactor
     *            the linear attenuation factor
     * @param quadraticAttenuationFactor
     *            the quadratic attenuation factor
     * @param direction
     *            the direction of the SpotLightSource
     * @param position
     *            the position
     * @param cutoffAngle
     *            the cutoff angle
     * @param spotExponent
     *            the spot exponent
     * @param parent
     *            the parent to base this directional light source off of
     */
    protected SpotLightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor,
        IVector direction,
        IPoint position,
        float cutoffAngle,
        float spotExponent,
        IPrimitive parent)
    {
        super(
            lightNumber,
            ambientComponent,
            diffuseComponent,
            specularComponent,
            constantAttenuationFactor,
            linearAttenuationFactor,
            quadraticAttenuationFactor,
            parent);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = cutoffAngle;
        this.exponent = spotExponent;
        this.position = position;
        this.direction = direction;
    }


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with the provided light components, no
     * attenuation, and the provided parent.
     *
     * @param lightNumber
     *            the light number
     * @param ambientComponent
     *            the ambient component
     * @param diffuseComponent
     *            the diffuse component
     * @param specularComponent
     *            the specular component
     * @param parent
     *            the parent to base this light source off of
     */
    protected SpotLightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        IPrimitive parent)
    {
        super(
            lightNumber,
            ambientComponent,
            diffuseComponent,
            specularComponent,
            parent);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = (float)180.0;
        this.exponent = 0;
        this.position = new Point(0, 0, 1);
        this.direction = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new SpotLightSource with white light components, no
     * attenuation, and the provided parent.
     *
     * @param lightNumber
     *            the light number
     * @param parent
     *            the parent to base this light source off of
     */
    protected SpotLightSource(int lightNumber, IPrimitive parent)
    {
        super(lightNumber, parent);

        this.positionLock = new ReentrantLock();
        this.exponentLock = new ReentrantLock();
        this.cutoffAngleLock = new ReentrantLock();
        this.directionLock = new ReentrantLock();

        this.cutoffAngle = (float)180.0;
        this.exponent = 0;
        this.position = new Point(0, 0, 1);
        this.direction = new Vector(0, 0, -1);
    }


    /**
     * Provides a copy of the current object.
     *
     * @return a copy of the current object
     */
    public ISpotLightSource copy()
    {

        ISpotLightSource lightSource =
            new SpotLightSource(
                this.getLightNumber(),
                this.getAmbientComponent(),
                this.getDiffuseComponent(),
                this.getSpecularComponent(),
                this.getConstantAttenuationFactor(),
                this.getLinearAttenuationFactor(),
                this.getQuadraticAttenuationFactor(),
                this.getSpotDirection(),
                this.getSpotPosition(),
                this.getCutoffAngle(),
                this.getSpotExponent(),
                this.getParent());

        lightSource.setParent(this.getParent());
        lightSource.setColor(this.getParent().getColor());
        lightSource.setMaterial(this.getParent().getMaterial());
        lightSource.setRotation(this.getParent().getRotation());
        lightSource.setScaling(this.getParent().getScaling());
        lightSource.setShear(this.getParent().getShear());
        lightSource.setSolid(this.getParent().isSolid());
        lightSource.setTranslation(this.getParent().getTranslation());

        return lightSource;
    }


    /**
     * Provides a copy of the current object and sets its parent.
     *
     * @return a copy of the current object with a specific parent
     */
    public ISpotLightSource copy(IPrimitive parent)
    {
        ISpotLightSource lightSource =
            new SpotLightSource(
                this.getLightNumber(),
                this.getAmbientComponent(),
                this.getDiffuseComponent(),
                this.getSpecularComponent(),
                this.getConstantAttenuationFactor(),
                this.getLinearAttenuationFactor(),
                this.getQuadraticAttenuationFactor(),
                this.getSpotDirection(),
                this.getSpotPosition(),
                this.getCutoffAngle(),
                this.getSpotExponent(),
                parent);

        lightSource.setParent(parent);
        lightSource.setColor(parent.getColor());
        lightSource.setMaterial(parent.getMaterial());
        lightSource.setRotation(parent.getRotation());
        lightSource.setScaling(parent.getScaling());
        lightSource.setShear(parent.getShear());
        lightSource.setSolid(parent.isSolid());
        lightSource.setTranslation(parent.getTranslation());

        return lightSource;
    }


    /**
     * Paints the SpotLightSource.
     */
    public void paint(GL2 gl2)
    {
        gl2.glEnable(GLLightingFunc.GL_LIGHTING);

        super.paint(gl2);

        gl2.glLightfv(this.getLightNumber(), GLLightingFunc.GL_POSITION, this
            .getSpotPosition().toArray(), 0);

        gl2.glLightfv(
            this.getLightNumber(),
            GLLightingFunc.GL_SPOT_DIRECTION,
            this.getSpotDirection().toArray(),
            0);

        gl2.glLightf(
            this.getLightNumber(),
            GLLightingFunc.GL_SPOT_EXPONENT,
            this.getSpotExponent());

        gl2.glLightf(
            this.getLightNumber(),
            GLLightingFunc.GL_SPOT_CUTOFF,
            this.getCutoffAngle());

        gl2.glDisable(GLLightingFunc.GL_LIGHTING);

    }


    /**
     * Returns the light source type, Positional Light
     *
     * @return the light source type, Positional Light
     */
    public LightSourceType getLightSourceType()
    {
        return LightSourceType.SPOTLIGHT;
    }


    /**
     * Returns the cutoff angle.
     *
     * @return the cutoff angle
     */
    public float getCutoffAngle()
    {
        try
        {
            this.cutoffAngleLock.lock();
            return this.cutoffAngle;
        }

        finally
        {
            this.cutoffAngleLock.unlock();
        }
    }


    /**
     * Changes the cutoff angle.
     *
     * @param cutoffAngle
     *            the new cutoff angle
     */
    public void setCutoffAngle(float cutoffAngle)
    {
        try
        {
            this.cutoffAngleLock.lock();
            this.cutoffAngle = cutoffAngle;
        }

        finally
        {
            this.cutoffAngleLock.unlock();
        }
    }


    /**
     * Returns the spot exponent.
     *
     * @return the spot exponent
     */
    public float getSpotExponent()
    {
        try
        {
            this.exponentLock.lock();
            return this.exponent;
        }

        finally
        {
            this.exponentLock.unlock();
        }
    }


    /**
     * Changes the spot exponent to a new value.
     *
     * @param spotExponent
     *            the new spot exponent
     */
    public void setSpotExponent(float spotExponent)
    {
        try
        {
            this.exponentLock.lock();
            this.exponent = spotExponent;
        }

        finally
        {
            this.exponentLock.unlock();
        }
    }


    /**
     * Returns the spot direction.
     *
     * @return the spot direction
     */
    public IVector getSpotDirection()
    {
        try
        {
            this.directionLock.lock();
            return this.direction;
        }

        finally
        {
            this.directionLock.unlock();
        }
    }


    /**
     * Changes the spot direction.
     *
     * @param spotDirection
     *            the new spot direction
     */
    public void setSpotDirection(IVector spotDirection)
    {
        try
        {
            this.directionLock.lock();
            this.direction = spotDirection;
        }

        finally
        {
            this.directionLock.unlock();
        }
    }


    /**
     * Returns the spot position.
     *
     * @return the spot position
     */
    public IPoint getSpotPosition()
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
     * Changes the spot position to a new value.
     *
     * @param spotPosition
     *            the new spot position
     */
    public void setSpotPosition(IPoint spotPosition)
    {
        try
        {
            this.positionLock.lock();
            this.position = spotPosition;
        }

        finally
        {
            this.positionLock.unlock();
        }
    }

}
