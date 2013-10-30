package cs3744.graphics.common;

import cs3744.graphics.interfaces.IMaterial;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.GL;
import cs3744.graphics.interfaces.IColor;
import javax.media.opengl.GL2;

// -------------------------------------------------------------------------
/**
 * Creates a material that has an ambient component, diffuse component, specular
 * component, phong coefficient, and emissive component.
 *
 * @author Eric Hotinger
 * @version Aug 7, 2012
 */
public class Material
    extends Object
    implements cs3744.graphics.interfaces.IMaterial
{

    private IColor          ambientComponent;
    private IColor          diffuseComponent;
    private IColor          specularComponent;
    private Float           phongCoefficient;
    private IColor          emissiveComponent;

    /**
     * Black plastic
     */
    public static IMaterial BLACK_PLASTIC   = new Material(
                                                new Color(0, 0, 0),
                                                new Color(0.01f, 0.01f, 0.01f),
                                                new Color(0.5f, 0.5f, 0.5f),
                                                32f,
                                                null);
    /**
     * Brass
     */
    public static IMaterial BRASS           = new Material(new Color(
                                                0.329412f,
                                                0.223529f,
                                                0.027451f), new Color(
                                                0.780392f,
                                                0.568627f,
                                                0.113725f), new Color(
                                                0.992157f,
                                                0.941176f,
                                                0.807843f), 27.8974f, null);

    /**
     * Bronze
     */
    public static IMaterial BRONZE          = new Material(new Color(
                                                0.2125f,
                                                0.1275f,
                                                0.054f), new Color(
                                                0.714f,
                                                0.4284f,
                                                0.18144f), new Color(
                                                0.393548f,
                                                0.271906f,
                                                0.166721f), 25.6f, null);
    /**
     * Chrome
     */
    public static IMaterial CHROME          = new Material(new Color(
                                                0.25f,
                                                0.25f,
                                                0.25f), new Color(
                                                0.4f,
                                                0.4f,
                                                0.4f), new Color(
                                                0.774597f,
                                                0.774597f,
                                                0.774597f), 76.8f, null);
    /**
     * Copper
     */
    public static IMaterial COPPER          = new Material(new Color(
                                                0.19125f,
                                                0.0735f,
                                                00225f), new Color(
                                                0.7038f,
                                                0.27048f,
                                                0.0828f), new Color(
                                                0.256777f,
                                                0.137622f,
                                                0.086014f), 12.8f, null);
    /**
     * Gold
     */
    public static IMaterial GOLD            = new Material(new Color(
                                                0.24725f,
                                                0.1995f,
                                                0.0745f), new Color(
                                                0.75164f,
                                                0.60648f,
                                                0.22648f), new Color(
                                                0.628281f,
                                                0.555802f,
                                                0.366065f), 51.2f, null);
    /**
     * Pewter
     */
    public static IMaterial PEWTER          = new Material(new Color(
                                                0.10588f,
                                                0.058824f,
                                                0.113725f), new Color(
                                                0.427451f,
                                                0.470588f,
                                                0.541176f), new Color(
                                                0.3333f,
                                                0.3333f,
                                                0.521569f), 9.84615f, null);
    /**
     * Silver
     */
    public static IMaterial SILVER          = new Material(new Color(
                                                0.19225f,
                                                0.19225f,
                                                0.19225f), new Color(
                                                0.50754f,
                                                0.50754f,
                                                0.50754f), new Color(
                                                0.508273f,
                                                0.508273f,
                                                0.508273f), 51.2f, null);
    /**
     * Polished silver
     */
    public static IMaterial POLISHED_SILVER = new Material(new Color(
                                                0.23125f,
                                                0.23125f,
                                                0.23125f), new Color(
                                                0.2775f,
                                                0.2775f,
                                                0.2775f), new Color(
                                                0.773911f,
                                                0.773911f,
                                                0.773911f), 89.6f, null);


    // ----------------------------------------------------------
    /**
     * Creates a black material.
     */
    public Material()
    {
        this.ambientComponent = new Color(0, 0, 0);
        this.diffuseComponent = new Color(0.01f, 0.01f, 0.01f);
        this.specularComponent = new Color(0.5f, 0.5f, 0.5f);
        this.phongCoefficient = 32f;
        this.emissiveComponent = null;
    }


    // ----------------------------------------------------------
    /**
     * Creates a new Material with the provided ambient, diffuse, and specular
     * components with the provided phong coefficient.
     *
     * @param ambientComponent
     *            the ambient component
     * @param diffuseComponent
     *            the diffuse component
     * @param specularComponent
     *            the specular component
     * @param phongCoefficient
     *            the phong coefficient
     */
    public Material(
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        Float phongCoefficient)
    {
        this.ambientComponent = ambientComponent;
        this.diffuseComponent = diffuseComponent;
        this.specularComponent = specularComponent;
        this.phongCoefficient = phongCoefficient;
        this.emissiveComponent = null;
    }


    // ----------------------------------------------------------
    /**
     * Creates a new Material with the provided ambient, diffuse, specular, and
     * emissive components with the provided phong coefficient.
     *
     * @param ambientComponent
     *            the ambient component
     * @param diffuseComponent
     *            the diffuse component
     * @param specularComponent
     *            the specular component
     * @param phongCoefficient
     *            the phong coefficient
     * @param emissiveComponent
     *            the emissive component
     */
    public Material(
        IColor ambientComponent,
        IColor diffuseComponent,
        IColor specularComponent,
        Float phongCoefficient,
        IColor emissiveComponent)
    {
        this.ambientComponent = ambientComponent;
        this.diffuseComponent = diffuseComponent;
        this.specularComponent = specularComponent;
        this.phongCoefficient = phongCoefficient;
        this.emissiveComponent = emissiveComponent;
    }


    /**
     * Applies the attribute to the drawing context using calls to
     * gl2.glMaterialf and gl2.glMaterialfv.
     *
     * @param gl2
     *            The OpenGL state machine
     */
    public void applyAttribute(GL2 gl2)
    {

        float[] ambient = this.getAmbientComponent().toArray();

        float[] diffuse = this.getDiffuseComponent().toArray();

        float[] specular = this.getSpecularComponent().toArray();

        float shininess = this.getPhongCoefficient();

        gl2.glEnable(GLLightingFunc.GL_LIGHTING);

        gl2.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_AMBIENT, ambient, 0);
        gl2.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_DIFFUSE, diffuse, 0);
        gl2.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_SPECULAR, specular, 0);

        gl2.glMaterialf(GL.GL_FRONT, GLLightingFunc.GL_SHININESS, shininess);

        gl2.glDisable(GLLightingFunc.GL_LIGHTING);
    }


    /**
     * Returns the ambient component.
     *
     * @return the ambient component.
     */
    public IColor getAmbientComponent()
    {
        return this.ambientComponent;
    }


    /**
     * Returns the diffuse component.
     *
     * @return the diffuse component.
     */
    public IColor getDiffuseComponent()
    {
        return this.diffuseComponent;
    }


    /**
     * Returns the specular component.
     *
     * @return the specular component.
     */
    public IColor getSpecularComponent()
    {
        return this.specularComponent;
    }


    /**
     * Returns the Phong coefficient.
     *
     * @return the Phong coefficient.
     */
    public float getPhongCoefficient()
    {
        return this.phongCoefficient;
    }


    /**
     * Returns the emissive component.
     *
     * @return the emissive component.
     */
    public IColor getEmissiveComponent()
    {
        return this.emissiveComponent;
    }


    /**
     * Returns a string representation of the Material.
     *
     * @return a string representation of the Material
     */
    public String toString()
    {
        return "Material [ambientComponent=" + ambientComponent
            + ", diffuseComponent=" + diffuseComponent + ", specularComponent="
            + specularComponent + ", phongCoefficient=" + phongCoefficient
            + ", emissiveComponent=" + emissiveComponent + "]";
    }


    /**
     * Generates a custom hashcode for the Material.
     *
     * @return an integer hashcode for the Material
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
            prime
                * result
                + ((diffuseComponent == null) ? 0 : diffuseComponent.hashCode());
        result =
            prime
                * result
                + ((emissiveComponent == null) ? 0 : emissiveComponent
                    .hashCode());
        result =
            prime
                * result
                + ((phongCoefficient == null) ? 0 : phongCoefficient.hashCode());
        result =
            prime
                * result
                + ((specularComponent == null) ? 0 : specularComponent
                    .hashCode());
        return result;
    }


    /**
     * Compares a Material to this to see if they are equal.
     *
     * @param obj
     *            the object to compare to this to see if they're equal
     * @return true or false if the objects are equal
     */
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Material other = (Material)obj;
        if (ambientComponent == null)
        {
            if (other.ambientComponent != null)
                return false;
        }
        else if (!ambientComponent.equals(other.ambientComponent))
            return false;
        if (diffuseComponent == null)
        {
            if (other.diffuseComponent != null)
                return false;
        }
        else if (!diffuseComponent.equals(other.diffuseComponent))
            return false;
        if (emissiveComponent == null)
        {
            if (other.emissiveComponent != null)
                return false;
        }
        else if (!emissiveComponent.equals(other.emissiveComponent))
            return false;
        if (phongCoefficient == null)
        {
            if (other.phongCoefficient != null)
                return false;
        }
        else if (!phongCoefficient.equals(other.phongCoefficient))
            return false;
        if (specularComponent == null)
        {
            if (other.specularComponent != null)
                return false;
        }
        else if (!specularComponent.equals(other.specularComponent))
            return false;
        return true;
    }

}
