import java.io.IOException;
import java.io.RandomAccessFile;

// -------------------------------------------------------------------------
/**
 * The LRUBuffer is a container for byte data. It has a size, an offset, an
 * associated bufferPool, an associated RandomAccessFile, and two booleans to
 * denote whether or note the Buffer is loaded or dirty.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 3, 2012
 */
public class LRUBuffer
    implements BufferADT
{
    /**
     * The associated BufferPoolADT storing this LRUBuffer.
     */
    private LRUBufferPool    bufferPool;

    /**
     * The associated RandomAccessFile of this LRUBuffer, used for reading and
     * writing purposes.
     */
    private RandomAccessFile randomAccessFile;

    /**
     * The offset of this LRUBuffer in regards to the RandomAccessFile.
     */
    private int              offset;

    /**
     * The size of this LRUBuffer.
     */
    private int              size;

    /**
     * The byte array associated with the LRUBuffer.
     */
    private byte[]           data;

    /**
     * Determines whether or not the LRUBuffer is dirty.
     */
    private boolean          isDirty;

    /**
     * Determines whether or not the LRUBuffer is loaded.
     */
    private boolean          isLoaded;


    // ----------------------------------------------------------
    /**
     * Creates a new LRUBuffer object based on a RandomAccessFile,
     * BufferPoolADT, size, and offset.
     *
     * @param randomAccessFile
     *            The associated RandomAccessFile of this LRUBuffer, used for
     *            reading and writing purposes.
     * @param bufferPool
     *            The associated BufferPoolADT storing this LRUBuffer.
     * @param size
     *            The size of the LRUBuffer.
     * @param offset
     *            The offset of this LRUBuffer in regards to the
     *            RandomAccessFile.
     */
    public LRUBuffer(
        RandomAccessFile randomAccessFile,
        LRUBufferPool bufferPool,
        int size,
        int offset)
    {
        /**
         * Set this information based on the given information.
         */
        this.randomAccessFile = randomAccessFile;
        this.bufferPool = bufferPool;
        this.size = size;
        this.offset = offset;

        /**
         * By default, the LRUBuffer is neither dirty nor loaded.
         */
        this.isDirty = false;
        this.isLoaded = false;
    }


    // ----------------------------------------------------------
    /**
     * Returns the size of the LRUBuffer.
     *
     * @return the size of the LRUBuffer
     */
    public int getSize()
    {
        return this.size;
    }


    // ----------------------------------------------------------
    /**
     * Writes a specified byte array of data to the LRUBuffer. Marks the
     * LRUBuffer as dirty and loaded, and marks the LRUBuffer in the
     * BufferPoolADT as used.
     *
     * @param givenData
     *            specified data to write
     */
    public void write(byte[] givenData)
    {
        /**
         * Mark the LRUBuffer as dirty and loaded; mark the LRUBuffer as used in
         * the BufferPoolADT.
         */
        this.bufferPool.bufferUsed(this);

        this.isLoaded = true;
        this.isDirty = true;

        /**
         * Avoid re-writing the given data by cloning it.
         */
        this.data = givenData.clone();
    }


    // ----------------------------------------------------------
    /**
     * Attempts to read a byte of data from the RandomAccessFile. Marks the
     * LRUBuffer as clean and loaded and increases the number of disk reads.
     * Automatically handles errors in case the RandomAccessFile can't be found
     * or written to.
     */
    private void readFromFile()
    {
        this.data = new byte[this.size];

        try
        {
            StatisticsGenerator.diskReads++;

            this.randomAccessFile.seek(this.offset);
            this.randomAccessFile.read(this.data);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.isDirty = false;
        this.isLoaded = true;
    }


    // ----------------------------------------------------------
    /**
     * Attempts to read with the LRUBuffer. Depending on whether or not the
     * LRUBuffer is loaded, it will increase the number of cache hits and
     * misses.
     *
     * @return the read byte data
     */
    public byte[] read()
    {
        this.bufferPool.bufferUsed(this);

        /**
         * If the LRUBuffer isn't loaded, increase the number of cache misses in
         * the StatisticsGenerator, and attempt to read from the
         * RandomAccessFile again.
         */
        if (!this.isLoaded)
        {
            StatisticsGenerator.cacheMisses++;
            this.readFromFile();
        }

        /**
         * Increase the number of cache hits, since the LRUBuffer is loaded.
         */
        else
            StatisticsGenerator.cacheHits++;

        /**
         * Avoid re-writing the read data by cloning it.
         */
        byte[] clonedData = this.data.clone();

        return clonedData;
    }


    // ----------------------------------------------------------
    /**
     * Attempts to write the data of this LRUBuffer to the RandomAccessFile.
     * Marks the LRUBuffer as clean and increases the count of disk writes.
     * Automatically handles errors in case the RandomAccessFile can't be found
     * or written to.
     */
    private void writeToFile()
    {
        try
        {
            StatisticsGenerator.diskWrites++;

            this.randomAccessFile.seek(this.offset);
            this.randomAccessFile.write(this.data);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.isDirty = false;
    }


    // ----------------------------------------------------------
    /**
     * Clears the LRUBuffer of data and marks it as unloaded. If it is dirty,
     * the LRUBuffer will write to file so it is clean.
     */
    public void flush()
    {
        if (this.isDirty)
            this.writeToFile();

        this.data = null;
        this.isLoaded = false;
    }

}
