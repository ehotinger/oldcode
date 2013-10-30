package cs3744.graphics.common;

import cs3744.graphics.primitives.solution.Point;
import cs3744.graphics.interfaces.IPositionalLightSource;
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
 * A PositionalLightSource is a LightSource with a Point position.
 *
 * @author Eric Hotinger
 * @version Aug 7, 2012
 */
public class PositionalLightSource
    extends cs3744.graphics.common.LightSource
    implements cs3744.graphics.interfaces.IPositionalLightSource
{

    private IPoint     position;
    private final Lock positionLock;


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with white light components, no
     * attenuation, and no parent.
     *
     * @param primitiveToCopy
     *            the primitive to copy
     * @param parent
     *            the parent to base the light source off of
     */
    protected PositionalLightSource(
        ILightSource primitiveToCopy,
        IPrimitive parent)
    {
        super(primitiveToCopy, parent);

        this.positionLock = new ReentrantLock();

        this.position = new Point(0, 0, 1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with white light components, no
     * attenuation, and no parent.
     *
     * @param lightNumber
     *            the light number
     */
    public PositionalLightSource(int lightNumber)
    {
        super(lightNumber);

        this.positionLock = new ReentrantLock();

        this.position = new Point(0, 0, 1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with white light components, the
     * provided attenuation and no parent.
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
    protected PositionalLightSource(
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

        this.position = new Point(0, 0, 1);

    }


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with white light components, the
     * provided attenuation, and the provided parent.
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
    protected PositionalLightSource(
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

        this.position = new Point(0, 0, 1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with the provided light components,
     * no attenuation, and no parent.
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
    protected PositionalLightSource(
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

        this.position = new Point(0, 0, 1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with the provided light components,
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
    protected PositionalLightSource(
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

        this.position = new Point(0, 0, 1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with the provided light components,
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
    protected PositionalLightSource(
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

        this.position = null;
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with the provided light components,
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
     * @param position
     *            the position
     * @param parent
     *            the parent to base this directional light source off of
     */
    protected PositionalLightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor,
        IPoint position,
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

        this.position = position;
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with the provided light components,
     * no attenuation, and the provided parent.
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
    protected PositionalLightSource(
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

        this.position = new Point(0, 0, 1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PositionalLightSource with white light components, no
     * attenuation, and the provided parent..
     *
     * @param lightNumber
     *            the light number
     * @param parent
     *            the parent to base this light source off of
     */
    protected PositionalLightSource(int lightNumber, IPrimitive parent)
    {
        super(lightNumber, parent);

        this.positionLock = new ReentrantLock();

        this.position = new Point(0, 0, 1);
    }


    /**
     * Provides a copy of the current object.
     *
     * @return a copy of the current object
     */
    public IPositionalLightSource copy()
    {
        IPositionalLightSource lightSource =
            new PositionalLightSource(
                this.getLightNumber(),
                this.getAmbientComponent(),
                this.getDiffuseComponent(),
                this.getSpecularComponent(),
                this.getConstantAttenuationFactor(),
                this.getLinearAttenuationFactor(),
                this.getQuadraticAttenuationFactor(),
                this.getPosition(),
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
    public IPositionalLightSource copy(IPrimitive parent)
    {
        IPositionalLightSource lightSource =
            new PositionalLightSource(
                this.getLightNumber(),
                this.getAmbientComponent(),
                this.getDiffuseComponent(),
                this.getSpecularComponent(),
                this.getConstantAttenuationFactor(),
                this.getLinearAttenuationFactor(),
                this.getQuadraticAttenuationFactor(),
                this.getPosition(),
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
     * Paints the PositionalLightSource.
     */
    public void paint(GL2 gl2)
    {
        gl2.glEnable(GLLightingFunc.GL_LIGHTING);

        super.paint(gl2);

        gl2.glLightfv(this.getLightNumber(), GLLightingFunc.GL_POSITION, this
            .getPosition().toArray(), 0);

        gl2.glDisable(GLLightingFunc.GL_LIGHTING);

    }


    /**
     * Returns the light source type, Positional Light
     *
     * @return the light source type, Positional Light
     */
    public LightSourceType getLightSourceType()
    {
        return LightSourceType.POSITIONAL_LIGHT;
    }


    /**
     * Returns the position of this PositionalLightSource.
     *
     * @return the position of this PositionalLightSource
     */
    public IPoint getPosition()
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
     * Changes the position of this PositionalLightSource.
     *
     * @param position
     *            the new position of the light source
     */
    public void setPosition(IPoint position)
    {
        try
        {
            this.positionLock.lock();
            this.position = position;
        }

        finally
        {
            this.positionLock.unlock();
        }
    }

}
