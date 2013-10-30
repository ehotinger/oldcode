// -------------------------------------------------------------------------
/**
 * A buffer is a container for data of a specific primitive type. It has a size,
 * which is the capacity of the Buffer.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 2, 2012
 */
public interface BufferADT
{

    // ----------------------------------------------------------
    /**
     * This is the equivalent of a clear on the Buffer. It cleans the Buffer and
     * makes it ready to read.
     */
    public void flush();


    // ----------------------------------------------------------
    /**
     * Attempt to read with the Buffer. Returns a byte array associated with the
     * read.
     *
     * @return a byte array associated with the read from the Buffer
     */
    public byte[] read();


    // ----------------------------------------------------------
    /**
     * Attempts to write with the Buffer given a byte array of data.
     *
     * @param data
     *            the data to write with the Buffer
     */
    public void write(byte[] data);


    // ----------------------------------------------------------
    /**
     * Returns the size, or capacity of the Buffer.
     *
     * @return the size of the Buffer
     */
    public int getSize();

}
