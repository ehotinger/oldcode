package cs3744.graphics.common;

import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.GL2;
import cs3744.graphics.interfaces.LightSourceType;
import cs3744.graphics.interfaces.IDirectionalLightSource;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import cs3744.graphics.interfaces.IColor;
import cs3744.graphics.interfaces.ILightSource;
import cs3744.graphics.interfaces.IPrimitive;
import cs3744.graphics.interfaces.IVector;

// -------------------------------------------------------------------------
/**
 * A DirectionalLightSource is a LightSource with a direction vector.
 *
 * @author Eric Hotinger
 * @version Aug 7, 2012
 */
public class DirectionalLightSource
    extends cs3744.graphics.common.LightSource
    implements cs3744.graphics.interfaces.IDirectionalLightSource
{

    private IVector    directionVector;
    private final Lock vectorLock;


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with white light components, no
     * attenuation, and no parent.
     *
     * @param primitiveToCopy
     *            the primitive to copy
     * @param parent
     *            the parent to base the light source off of
     */
    protected DirectionalLightSource(
        ILightSource primitiveToCopy,
        IPrimitive parent)
    {
        super(primitiveToCopy, parent);

        this.vectorLock = new ReentrantLock();

        this.directionVector = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with white light components, no
     * attenuation, and no parent.
     *
     * @param lightNumber
     *            the light number
     */
    protected DirectionalLightSource(int lightNumber)
    {
        super(lightNumber);

        this.vectorLock = new ReentrantLock();

        this.directionVector = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with white light components, the
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
    protected DirectionalLightSource(
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

        this.vectorLock = new ReentrantLock();

        this.directionVector = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with white light components, the
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
    protected DirectionalLightSource(
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

        this.vectorLock = new ReentrantLock();

        this.directionVector = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with the provided light components,
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
    protected DirectionalLightSource(
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

        this.vectorLock = new ReentrantLock();

        this.directionVector = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with the provided light components,
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
    protected DirectionalLightSource(
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

        this.vectorLock = new ReentrantLock();

        this.directionVector = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with the provided light components,
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
    protected DirectionalLightSource(
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

        this.vectorLock = new ReentrantLock();

        this.directionVector = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with the provided light components,
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
     *            the direction
     * @param parent
     *            the parent to base this directional light source off of
     */
    protected DirectionalLightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor,
        IVector direction,
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

        this.vectorLock = new ReentrantLock();

        this.directionVector = direction;
    }


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with the provided light components,
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
    protected DirectionalLightSource(
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

        this.vectorLock = new ReentrantLock();

        this.directionVector = new Vector(0, 0, -1);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new DirectionalLightSource with white light components, no
     * attenuation, and the provided parent..
     *
     * @param lightNumber
     *            the light number
     * @param parent
     *            the parent to base this light source off of
     */
    protected DirectionalLightSource(int lightNumber, IPrimitive parent)
    {
        super(lightNumber, parent);

        this.vectorLock = new ReentrantLock();

        this.directionVector = new Vector(0, 0, -1);
    }


    /**
     * Provides a copy of the current object.
     *
     * @return a copy of the current object
     */
    public IDirectionalLightSource copy()
    {
        IDirectionalLightSource lightSource =
            new DirectionalLightSource(
                this.getLightNumber(),
                this.getAmbientComponent(),
                this.getDiffuseComponent(),
                this.getSpecularComponent(),
                this.getConstantAttenuationFactor(),
                this.getLinearAttenuationFactor(),
                this.getQuadraticAttenuationFactor(),
                this.getDirection(),
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
    public IDirectionalLightSource copy(IPrimitive parent)
    {
        IDirectionalLightSource lightSource =
            new DirectionalLightSource(
                this.getLightNumber(),
                this.getAmbientComponent(),
                this.getDiffuseComponent(),
                this.getSpecularComponent(),
                this.getConstantAttenuationFactor(),
                this.getLinearAttenuationFactor(),
                this.getQuadraticAttenuationFactor(),
                this.getDirection(),
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
     * Paints the DirectionalLightSource.
     */
    public void paint(GL2 gl2)
    {
        gl2.glEnable(GLLightingFunc.GL_LIGHTING);

        super.paint(gl2);

        gl2.glLightfv(this.getLightNumber(), GLLightingFunc.GL_POSITION, this
            .getDirection().toArray(), 0);

        gl2.glDisable(GLLightingFunc.GL_LIGHTING);

    }


    /**
     * Returns the light source type, Directional Light
     *
     * @return the light source type, Directional Light
     */
    public LightSourceType getLightSourceType()
    {
        return LightSourceType.DIRECTIONAL_LIGHT;
    }


    /**
     * Returns the direction of the DirectionalLightSource.
     *
     * @return the Vector direction of the light source
     */
    public IVector getDirection()
    {
        try
        {
            this.vectorLock.lock();
            return this.directionVector;
        }

        finally
        {
            this.vectorLock.unlock();
        }
    }


    /**
     * Changes the direction of the DirectionalLightSource.
     *
     * @param direction
     *            the new direction of the DirectionalLightSource
     */
    public void setDirection(IVector direction)
    {
        try
        {
            this.vectorLock.lock();
            this.directionVector = direction;
        }

        finally
        {
            this.vectorLock.unlock();
        }
    }

}
