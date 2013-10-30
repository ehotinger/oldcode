import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

// -------------------------------------------------------------------------
/**
 * The LRUBufferPool represents a pool of LRUBuffer instances by utilizing the
 * LRU-based PriorityQueue. It has the ability to return a specific LRUBuffer
 * based on an index and the ability to return its size, which represents the
 * block size.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 3, 2012
 */
public class LRUBufferPool
    implements BufferPoolADT
{
    /**
     * The number of BufferADT instances specified at the start of heapsort.
     */
    private final int                numBuffers;

    /**
     * An array of BufferADT instances.
     */
    private BufferADT[]              bufferArray;

    /**
     * The size of the block.
     */
    private int                      blockSize;

    /**
     * An associated PriorityQueue of BufferADT instances.
     */
    private PriorityQueue<BufferADT> priorityQueue;

    /**
     * The RandomAccessFile that is used for reading and writing in the
     * LRUBuffer objects.
     */
    private RandomAccessFile         outputFile;


    // ----------------------------------------------------------
    /**
     * Creates a new LRUBufferPool object based on the number of BufferADTs,
     * block size, and RandomAccessFile.
     *
     * @param numBuffers
     *            the number of buffers
     * @param blockSize
     *            the size of the block
     * @param file
     *            The associated RandomAccessFile of this LRUBufferPool, used
     *            for reading and writing purposes.
     * @throws IOException
     *             if the RandomACceessFile can't be read or written to
     */
    public LRUBufferPool(int numBuffers, int blockSize, File file)
        throws IOException
    {
        this.blockSize = blockSize;

        this.outputFile = new RandomAccessFile(file, "rw");

        this.numBuffers = (int)(this.outputFile.length() / blockSize);

        this.bufferArray = new BufferADT[this.numBuffers];

        this.priorityQueue = new PriorityQueue<BufferADT>(numBuffers);
    }


    // ----------------------------------------------------------
    /**
     * Returns the Buffer associated with the given index. If one doesn't exist,
     * generate one and return it. Similar to the acquireBuffer(int block)
     * method included in the distributed code handout, but with more ternaries.
     *
     * @param index
     *            an index of a BufferADT
     * @return a BufferADT associated with the given index
     */
    public BufferADT getBuffer(int index)
    {
        return (this.bufferArray[index] =
            ((this.bufferArray[index] == null)
                ? generateNewBuffer(index)
                : this.bufferArray[index]));
    }


    // ----------------------------------------------------------
    /**
     * Generates a new BufferADT based on a given index.
     *
     * @param index
     *            an index
     * @return a BufferADT based on the given index and the instance of this
     *         LRUBufferPool
     */
    private BufferADT generateNewBuffer(int index)
    {
        /**
         * Calculate the offset of the BufferADT.
         */
        int offset = index * this.blockSize;

        return new LRUBuffer(this.outputFile, this, this.blockSize, offset);
    }


    // ----------------------------------------------------------
    /**
     * Returns the size of the LRUBufferPool, which is equal to the number of
     * buffers.
     *
     * @return the size of the LRUBufferPool (number of BufferADTs)
     */
    public int size()
    {
        return this.numBuffers;
    }


    // ----------------------------------------------------------
    /**
     * Inserts a specified BufferADT into the PriorityQueue. If the inserted
     * BufferADT isn't null, it will flush it. In essence, it will try to write
     * to file if the BufferADT is dirty, and do nothing otherwise.
     *
     * @param buffer
     *            a specified BufferADT
     */
    public void bufferUsed(BufferADT buffer)
    {
        BufferADT insertedBuffer = this.priorityQueue.insert(buffer);

        if (insertedBuffer != null)
            insertedBuffer.flush();
    }


    // ----------------------------------------------------------
    /**
     * Iterates through every BufferADT in this LRUBufferPool and flushes each
     * individual BufferADT.
     */
    public void flush()
    {
        for (int i = 0; i < this.numBuffers; i++)
            this.bufferArray[i].flush();
    }

}
