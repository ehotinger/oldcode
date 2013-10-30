package cs3744.graphics.common;

import javax.media.opengl.fixedfunc.GLLightingFunc;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import cs3744.graphics.interfaces.ILightSource;
import cs3744.graphics.interfaces.IColor;
import cs3744.graphics.interfaces.IPrimitive;
import cs3744.graphics.interfaces.LightSourceType;
import javax.media.opengl.GL2;

// -------------------------------------------------------------------------
/**
 * Creates a LightSource from a LightSourceType, ambient component, diffuse
 * component, specular component, light number, constant attenuation factor,
 * quadratic attenuation factor, and linear attenuation factor. All
 * thread-friendly!
 *
 * @author Eric Hotinger
 * @version Aug 7, 2012
 */
public class LightSource
    extends cs3744.graphics.primitives.Primitive
    implements cs3744.graphics.interfaces.ILightSource
{

    private IColor                ambientComponent;
    private IColor                diffuseComponent;
    private IColor                specularComponent;
    private final Integer         lightNumber;
    private float                 constantAttenuationFactor;
    private float                 quadraticAttenuationFactor;
    private float                 linearAttenuationFactor;
    private final LightSourceType lightSourceType;

    private final Lock            ambientLock;
    private final Lock            diffuseLock;
    private final Lock            specularLock;
    private final Lock            constantAttenuationLock;
    private final Lock            quadraticAttenuationLock;
    private final Lock            linearAttenuationLock;


    // ----------------------------------------------------------
    /**
     * Create a new LightSource object based on a copy object.
     *
     * @param primitiveToCopy
     *            a copy object
     * @param parent
     *            the parent of the LightSource
     */
    protected LightSource(ILightSource primitiveToCopy, IPrimitive parent)
    {
        this.ambientLock = new ReentrantLock();
        this.diffuseLock = new ReentrantLock();
        this.specularLock = new ReentrantLock();
        this.constantAttenuationLock = new ReentrantLock();
        this.quadraticAttenuationLock = new ReentrantLock();
        this.linearAttenuationLock = new ReentrantLock();

        this.lightSourceType = primitiveToCopy.getLightSourceType();

        this.lightNumber = primitiveToCopy.getLightNumber();

        this.setAmbientComponent(primitiveToCopy.getAmbientComponent());
        this.setDiffuseComponent(primitiveToCopy.getDiffuseComponent());
        this.setSpecularComponent(primitiveToCopy.getSpecularComponent());

        this.setConstantAttenuationFactor(primitiveToCopy
            .getConstantAttenuationFactor());
        this.setQuadraticAttenuationFactor(primitiveToCopy
            .getQuadraticAttenuationFactor());
        this.setLinearAttenuationFactor(primitiveToCopy
            .getLinearAttenuationFactor());

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
     * Creates a new LightSource with white light components, no attenuation,
     * and no parent.
     *
     * @param lightNumber
     *            the light number
     */
    protected LightSource(int lightNumber)
    {
        this.ambientLock = new ReentrantLock();
        this.diffuseLock = new ReentrantLock();
        this.specularLock = new ReentrantLock();
        this.constantAttenuationLock = new ReentrantLock();
        this.quadraticAttenuationLock = new ReentrantLock();
        this.linearAttenuationLock = new ReentrantLock();

        this.lightSourceType = LightSourceType.DIRECTIONAL_LIGHT;

        this.lightNumber = lightNumber;

        this.setAmbientComponent(Color.WHITE);
        this.setDiffuseComponent(Color.WHITE);
        this.setSpecularComponent(Color.WHITE);

        this.setConstantAttenuationFactor(1);
        this.setQuadraticAttenuationFactor(0);
        this.setLinearAttenuationFactor(0);

        this.setParent(null);
        this.setColor(null);
        this.setMaterial(null);
        this.setRotation(null);
        this.setScaling(null);
        this.setShear(null);
        this.setSolid(null);
        this.setTranslation(null);

    }


    /**
     * Creates a new LightSource with white light components, the provided
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
    protected LightSource(
        int lightNumber,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor)
    {
        this.ambientLock = new ReentrantLock();
        this.diffuseLock = new ReentrantLock();
        this.specularLock = new ReentrantLock();
        this.constantAttenuationLock = new ReentrantLock();
        this.quadraticAttenuationLock = new ReentrantLock();
        this.linearAttenuationLock = new ReentrantLock();

        this.lightSourceType = LightSourceType.DIRECTIONAL_LIGHT;

        this.lightNumber = lightNumber;

        this.setAmbientComponent(Color.WHITE);
        this.setDiffuseComponent(Color.WHITE);
        this.setSpecularComponent(Color.WHITE);

        this.setConstantAttenuationFactor(constantAttenuationFactor);
        this.setQuadraticAttenuationFactor(linearAttenuationFactor);
        this.setLinearAttenuationFactor(quadraticAttenuationFactor);

        this.setParent(null);
        this.setColor(null);
        this.setMaterial(null);
        this.setRotation(null);
        this.setScaling(null);
        this.setShear(null);
        this.setSolid(null);
        this.setTranslation(null);
    }


    /**
     * Creates a new LightSource with white light components, the provided
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
     *            the parent of the LightSource
     */
    protected LightSource(
        int lightNumber,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor,
        IPrimitive parent)
    {
        this.ambientLock = new ReentrantLock();
        this.diffuseLock = new ReentrantLock();
        this.specularLock = new ReentrantLock();
        this.constantAttenuationLock = new ReentrantLock();
        this.quadraticAttenuationLock = new ReentrantLock();
        this.linearAttenuationLock = new ReentrantLock();

        this.lightSourceType = LightSourceType.DIRECTIONAL_LIGHT;

        this.lightNumber = lightNumber;

        this.setAmbientComponent(Color.WHITE);
        this.setDiffuseComponent(Color.WHITE);
        this.setSpecularComponent(Color.WHITE);

        this.setConstantAttenuationFactor(constantAttenuationFactor);
        this.setQuadraticAttenuationFactor(linearAttenuationFactor);
        this.setLinearAttenuationFactor(quadraticAttenuationFactor);

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
     * Creates a new LightSource with the provided light components, no
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
    protected LightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent)
    {
        this.ambientLock = new ReentrantLock();
        this.diffuseLock = new ReentrantLock();
        this.specularLock = new ReentrantLock();
        this.constantAttenuationLock = new ReentrantLock();
        this.quadraticAttenuationLock = new ReentrantLock();
        this.linearAttenuationLock = new ReentrantLock();

        this.lightSourceType = LightSourceType.DIRECTIONAL_LIGHT;

        this.lightNumber = lightNumber;

        this.setAmbientComponent(ambientComponent);
        this.setDiffuseComponent(diffuseComponent);
        this.setSpecularComponent(specularComponent);

        this.setConstantAttenuationFactor(1);
        this.setQuadraticAttenuationFactor(0);
        this.setLinearAttenuationFactor(0);

        this.setParent(null);
        this.setColor(null);
        this.setMaterial(null);
        this.setRotation(null);
        this.setScaling(null);
        this.setShear(null);
        this.setSolid(null);
        this.setTranslation(null);
    }


    /**
     * Creates a new LightSource with the provided light components,
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
    protected LightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor)
    {
        this.ambientLock = new ReentrantLock();
        this.diffuseLock = new ReentrantLock();
        this.specularLock = new ReentrantLock();
        this.constantAttenuationLock = new ReentrantLock();
        this.quadraticAttenuationLock = new ReentrantLock();
        this.linearAttenuationLock = new ReentrantLock();

        this.lightSourceType = LightSourceType.DIRECTIONAL_LIGHT;

        this.lightNumber = lightNumber;

        this.setAmbientComponent(ambientComponent);
        this.setDiffuseComponent(diffuseComponent);
        this.setSpecularComponent(specularComponent);

        this.setConstantAttenuationFactor(constantAttenuationFactor);
        this.setQuadraticAttenuationFactor(linearAttenuationFactor);
        this.setLinearAttenuationFactor(quadraticAttenuationFactor);

        this.setParent(null);
        this.setColor(null);
        this.setMaterial(null);
        this.setRotation(null);
        this.setScaling(null);
        this.setShear(null);
        this.setSolid(null);
        this.setTranslation(null);
    }


    /**
     * Creates a new LightSource with the provided light components,
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
     *            the parent of the LightSource
     */
    protected LightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        float constantAttenuationFactor,
        float linearAttenuationFactor,
        float quadraticAttenuationFactor,
        IPrimitive parent)
    {
        this.ambientLock = new ReentrantLock();
        this.diffuseLock = new ReentrantLock();
        this.specularLock = new ReentrantLock();
        this.constantAttenuationLock = new ReentrantLock();
        this.quadraticAttenuationLock = new ReentrantLock();
        this.linearAttenuationLock = new ReentrantLock();

        this.lightSourceType = LightSourceType.DIRECTIONAL_LIGHT;

        this.lightNumber = lightNumber;

        this.setAmbientComponent(ambientComponent);
        this.setDiffuseComponent(diffuseComponent);
        this.setSpecularComponent(specularComponent);

        this.setConstantAttenuationFactor(constantAttenuationFactor);
        this.setQuadraticAttenuationFactor(linearAttenuationFactor);
        this.setLinearAttenuationFactor(quadraticAttenuationFactor);

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
     * Creates a new LightSource with the provided light components, no
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
     *            the parent of the LightSource
     */
    protected LightSource(
        int lightNumber,
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        IPrimitive parent)
    {
        this.ambientLock = new ReentrantLock();
        this.diffuseLock = new ReentrantLock();
        this.specularLock = new ReentrantLock();
        this.constantAttenuationLock = new ReentrantLock();
        this.quadraticAttenuationLock = new ReentrantLock();
        this.linearAttenuationLock = new ReentrantLock();

        this.lightSourceType = LightSourceType.DIRECTIONAL_LIGHT;

        this.lightNumber = lightNumber;

        this.setAmbientComponent(ambientComponent);
        this.setDiffuseComponent(diffuseComponent);
        this.setSpecularComponent(specularComponent);

        this.setConstantAttenuationFactor(1);
        this.setQuadraticAttenuationFactor(0);
        this.setLinearAttenuationFactor(0);

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
     * Creates a new LightSource with white light components, no attenuation,
     * and the provided parent..
     *
     * @param lightNumber
     * @param parent
     */
    protected LightSource(int lightNumber, IPrimitive parent)
    {
        this.ambientLock = new ReentrantLock();
        this.diffuseLock = new ReentrantLock();
        this.specularLock = new ReentrantLock();
        this.constantAttenuationLock = new ReentrantLock();
        this.quadraticAttenuationLock = new ReentrantLock();
        this.linearAttenuationLock = new ReentrantLock();

        this.lightNumber = lightNumber;
        this.lightSourceType = LightSourceType.DIRECTIONAL_LIGHT;
    }


    /**
     * Provides a copy of the current object.
     *
     * @return a copy of the current object
     */
    public IPrimitive copy()
    {
        LightSource lightSource =
            new LightSource(
                this.getLightNumber(),
                this.getAmbientComponent(),
                this.getDiffuseComponent(),
                this.getSpecularComponent(),
                this.getConstantAttenuationFactor(),
                this.getLinearAttenuationFactor(),
                this.getQuadraticAttenuationFactor(),
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
     * Generates a String representation of the LightSource.
     *
     * @return a String representation of the LightSource
     */
    public String toString()
    {
        return "LightSource [ambientComponent=" + ambientComponent
            + ", diffuseComponent=" + diffuseComponent + ", specularComponent="
            + specularComponent + ", lightNumber=" + lightNumber
            + ", constantAttenuationFactor=" + constantAttenuationFactor
            + ", quadraticAttenuationFactor=" + quadraticAttenuationFactor
            + ", linearAttenuationFactor=" + linearAttenuationFactor
            + ", lightSourceType=" + lightSourceType + ", ambientLock="
            + ambientLock + ", diffuseLock=" + diffuseLock + ", specularLock="
            + specularLock + ", constantAttenuationLock="
            + constantAttenuationLock + ", quadraticAttenuationLock="
            + quadraticAttenuationLock + ", linearAttenuationLock="
            + linearAttenuationLock + "]";
    }


    /**
     * Generates a hashcode for the LightSource based on its fields.
     *
     * @return a hashcode for the LightSource
     */
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result =
            prime
                * result
                + ((ambientComponent == null) ? 0 : ambientComponent.hashCode());
        result =
            prime * result
                + ((ambientLock == null) ? 0 : ambientLock.hashCode());
        result =
            prime * result + Float.floatToIntBits(constantAttenuationFactor);
        result =
            prime
                * result
                + ((constantAttenuationLock == null)
                    ? 0
                    : constantAttenuationLock.hashCode());
        result =
            prime
                * result
                + ((diffuseComponent == null) ? 0 : diffuseComponent.hashCode());
        result =
            prime * result
                + ((diffuseLock == null) ? 0 : diffuseLock.hashCode());
        result =
            prime * result
                + ((lightNumber == null) ? 0 : lightNumber.hashCode());
        result =
            prime * result
                + ((lightSourceType == null) ? 0 : lightSourceType.hashCode());
        result = prime * result + Float.floatToIntBits(linearAttenuationFactor);
        result =
            prime
                * result
                + ((linearAttenuationLock == null) ? 0 : linearAttenuationLock
                    .hashCode());
        result =
            prime * result + Float.floatToIntBits(quadraticAttenuationFactor);
        result =
            prime
                * result
                + ((quadraticAttenuationLock == null)
                    ? 0
                    : quadraticAttenuationLock.hashCode());
        result =
            prime
                * result
                + ((specularComponent == null) ? 0 : specularComponent
                    .hashCode());
        result =
            prime * result
                + ((specularLock == null) ? 0 : specularLock.hashCode());
        return result;
    }


    /**
     * Compares this LightSource object to another object and tests whether or
     * not they are equal.
     *
     * @param obj
     *            the object to test against
     * @return true or false depending on whether or not the objects are equal
     */
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LightSource other = (LightSource)obj;
        if (ambientComponent == null)
        {
            if (other.ambientComponent != null)
                return false;
        }
        else if (!ambientComponent.equals(other.ambientComponent))
            return false;
        if (ambientLock == null)
        {
            if (other.ambientLock != null)
                return false;
        }
        else if (!ambientLock.equals(other.ambientLock))
            return false;
        if (Float.floatToIntBits(constantAttenuationFactor) != Float
            .floatToIntBits(other.constantAttenuationFactor))
            return false;
        if (constantAttenuationLock == null)
        {
            if (other.constantAttenuationLock != null)
                return false;
        }
        else if (!constantAttenuationLock.equals(other.constantAttenuationLock))
            return false;
        if (diffuseComponent == null)
        {
            if (other.diffuseComponent != null)
                return false;
        }
        else if (!diffuseComponent.equals(other.diffuseComponent))
            return false;
        if (diffuseLock == null)
        {
            if (other.diffuseLock != null)
                return false;
        }
        else if (!diffuseLock.equals(other.diffuseLock))
            return false;
        if (lightNumber == null)
        {
            if (other.lightNumber != null)
                return false;
        }
        else if (!lightNumber.equals(other.lightNumber))
            return false;
        if (lightSourceType != other.lightSourceType)
            return false;
        if (Float.floatToIntBits(linearAttenuationFactor) != Float
            .floatToIntBits(other.linearAttenuationFactor))
            return false;
        if (linearAttenuationLock == null)
        {
            if (other.linearAttenuationLock != null)
                return false;
        }
        else if (!linearAttenuationLock.equals(other.linearAttenuationLock))
            return false;
        if (Float.floatToIntBits(quadraticAttenuationFactor) != Float
            .floatToIntBits(other.quadraticAttenuationFactor))
            return false;
        if (quadraticAttenuationLock == null)
        {
            if (other.quadraticAttenuationLock != null)
                return false;
        }
        else if (!quadraticAttenuationLock
            .equals(other.quadraticAttenuationLock))
            return false;
        if (specularComponent == null)
        {
            if (other.specularComponent != null)
                return false;
        }
        else if (!specularComponent.equals(other.specularComponent))
            return false;
        if (specularLock == null)
        {
            if (other.specularLock != null)
                return false;
        }
        else if (!specularLock.equals(other.specularLock))
            return false;
        return true;
    }


    /**
     * Provides a copy of the current object and sets its parent.
     *
     * @return a copy of the current object with a given parent
     */
    public IPrimitive copy(IPrimitive parent)
    {
        LightSource lightSource =
            new LightSource(
                this.getLightNumber(),
                this.getAmbientComponent(),
                this.getDiffuseComponent(),
                this.getSpecularComponent(),
                this.getConstantAttenuationFactor(),
                this.getLinearAttenuationFactor(),
                this.getQuadraticAttenuationFactor(),
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
     * Paints the LightSource.
     */
    public void paint(GL2 gl2)
    {
        float[] ambient = this.getAmbientComponent().toArray();

        float[] diffuse = this.getDiffuseComponent().toArray();

        float[] specular = this.getSpecularComponent().toArray();

        gl2.glEnable(GLLightingFunc.GL_LIGHTING);

        gl2.glLightfv(
            this.getLightNumber(),
            GLLightingFunc.GL_AMBIENT,
            ambient,
            0);
        gl2.glLightfv(
            this.getLightNumber(),
            GLLightingFunc.GL_DIFFUSE,
            diffuse,
            0);
        gl2.glLightfv(
            this.getLightNumber(),
            GLLightingFunc.GL_SPECULAR,
            specular,
            0);

        gl2.glLightf(
            this.getLightNumber(),
            GLLightingFunc.GL_CONSTANT_ATTENUATION,
            this.getConstantAttenuationFactor());
        gl2.glLightf(
            this.getLightNumber(),
            GLLightingFunc.GL_LINEAR_ATTENUATION,
            this.getLinearAttenuationFactor());
        gl2.glLightf(
            this.getLightNumber(),
            GLLightingFunc.GL_QUADRATIC_ATTENUATION,
            this.getQuadraticAttenuationFactor());

        gl2.glDisable(GLLightingFunc.GL_LIGHTING);

    }


    /**
     * Return the light's number.
     *
     * @return the light's number
     */
    public Integer getLightNumber()
    {
        return this.lightNumber;
    }


    /**
     * Returns the type of this LightSource.
     *
     * @return the type of this LightSource.
     */
    public LightSourceType getLightSourceType()
    {
        return this.lightSourceType;
    }


    /**
     * Returns the ambient component.
     *
     * @return the ambient component
     */
    public IColor getAmbientComponent()
    {
        try
        {
            this.ambientLock.lock();
            return this.ambientComponent;
        }

        finally
        {
            this.ambientLock.unlock();
        }
    }


    /**
     * Changes the ambient component.
     *
     * @param ambientComponent
     *            the new ambient component
     */
    public void setAmbientComponent(IColor ambientComponent)
    {
        try
        {
            this.ambientLock.lock();
            this.ambientComponent = ambientComponent;
        }

        finally
        {
            this.ambientLock.unlock();
        }
    }


    /**
     * Returns the diffuse component of the LightSource.
     *
     * @return the diffuse component
     */
    public IColor getDiffuseComponent()
    {
        try
        {
            this.diffuseLock.lock();
            return this.diffuseComponent;
        }

        finally
        {
            this.diffuseLock.unlock();
        }
    }


    /**
     * Changes the diffuse component of the LightSource.
     *
     * @param diffuseComponent
     *            the new diffuse component for the LightSource
     */
    public void setDiffuseComponent(IColor diffuseComponent)
    {
        try
        {
            this.diffuseLock.lock();
            this.diffuseComponent = diffuseComponent;
        }

        finally
        {
            this.diffuseLock.unlock();
        }

    }


    /**
     * Returns the specular component of the LightSource.
     *
     * @return the specular component of the LightSource
     */
    public IColor getSpecularComponent()
    {
        try
        {
            this.specularLock.lock();
            return this.specularComponent;
        }

        finally
        {
            this.specularLock.unlock();
        }
    }


    /**
     * Changes the specular component of the LightSource.
     *
     * @param specularComponent
     *            the new specularComponent for the LightSource
     */
    public void setSpecularComponent(IColor specularComponent)
    {
        try
        {
            this.specularLock.lock();
            this.specularComponent = specularComponent;
        }

        finally
        {
            this.specularLock.unlock();
        }

    }


    /**
     * Returns the constant attentuation factor.
     *
     * @return the constant attentuation factor
     */
    public float getConstantAttenuationFactor()
    {
        try
        {
            this.constantAttenuationLock.lock();
            return this.constantAttenuationFactor;
        }

        finally
        {
            this.constantAttenuationLock.unlock();
        }
    }


    /**
     * Changes the constant attentuation factor.
     *
     * @param constantAttenuationFactor
     *            the new constant attenuation factor
     */
    public void setConstantAttenuationFactor(float constantAttenuationFactor)
    {
        try
        {
            this.constantAttenuationLock.lock();
            this.constantAttenuationFactor = constantAttenuationFactor;
        }

        finally
        {
            this.constantAttenuationLock.unlock();
        }

    }


    /**
     * Returns the linear attenuation factor.
     *
     * @return the linear attenuation factor
     */
    public float getLinearAttenuationFactor()
    {
        try
        {
            this.linearAttenuationLock.lock();
            return this.linearAttenuationFactor;
        }

        finally
        {
            this.linearAttenuationLock.unlock();
        }
    }


    /**
     * Changes the linear attenuation factor.
     *
     * @param linearAttenuationFactor
     *            the new linear attenuation factor
     */
    public void setLinearAttenuationFactor(float linearAttenuationFactor)
    {
        try
        {
            this.linearAttenuationLock.lock();
            this.linearAttenuationFactor = linearAttenuationFactor;
        }

        finally
        {
            this.linearAttenuationLock.unlock();
        }
    }


    /**
     * Returns the quadratic attenuation factor.
     *
     * @return the quadratic attenuation factor
     */
    public float getQuadraticAttenuationFactor()
    {
        try
        {
            this.quadraticAttenuationLock.lock();
            return this.quadraticAttenuationFactor;
        }

        finally
        {
            this.quadraticAttenuationLock.unlock();
        }

    }


    /**
     * Changes the quadratic attenuation factor.
     *
     * @param quadraticAttenuationFactor
     *            the new quadratic attenuation factor
     */
    public void setQuadraticAttenuationFactor(float quadraticAttenuationFactor)
    {
        try
        {
            this.quadraticAttenuationLock.lock();
            this.quadraticAttenuationFactor = quadraticAttenuationFactor;
        }

        finally
        {
            this.quadraticAttenuationLock.unlock();
        }
    }

}
