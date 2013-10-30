// -------------------------------------------------------------------------
/**
 * Represents a pool of BufferADT instances. Has the ability to return a
 * specific Buffer based on an index and the ability to return its size.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 2, 2012
 */
public interface BufferPoolADT
{

    // ----------------------------------------------------------
    /**
     * Returns a specific BufferADT instance based on an index.
     *
     * @param index
     *            the index of a Buffer
     * @return the Buffer at the given index
     */
    public BufferADT getBuffer(int index);


    // ----------------------------------------------------------
    /**
     * Returns the size of the BufferPoolADT.
     *
     * @return the size of the BufferPoolADT
     */
    public int size();


    // ----------------------------------------------------------
    /**
     * This is a total clear; it makes sure that everything we have done so far
     * gets shipped out to disk and is properly written to file.
     */
    public void flush();

}
