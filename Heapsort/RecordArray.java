import java.io.IOException;
import java.io.File;
import java.nio.ByteBuffer;

// -------------------------------------------------------------------------
/**
 * The RecordArray maintains the BufferPoolADT and specifies the default record
 * size and block size. The RecordArray processes BufferADTs
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 4, 2012
 */
public class RecordArray
{

    /**
     * An associated BufferPoolADT.
     */
    private BufferPoolADT    bufferPool;

    /**
     * The record size, always set to 4.
     */
    private final int        recordSize = 4;

    /**
     * The block size, always set to 4096.
     */
    private final int        blockSize  = 4096;

    /**
     * The current key, e.g., the first short.
     */
    private short            currentKey;

    /**
     * The current value, e.g., the second short.
     */
    private short            currentValue;

    /**
     * The difference in offsets between the two shorts is 2 as specified in the
     * spec.
     */
    private static final int DIFFERENCE = 2;


    // ----------------------------------------------------------
    /**
     * Creates a new RecordArray object with an associated LRUBufferPool based
     * on the number of buffers, block size, and file name.
     *
     * @param numBuffers
     *            the number of buffers
     * @param fileName
     *            the file name
     */
    public RecordArray(int numBuffers, String fileName)
    {
        /**
         * Try to create a new LRUBufferPool based on the specified fileName.
         * May cause an IOException if unable to create File.
         */
        try
        {
            this.bufferPool =
                new LRUBufferPool(
                    numBuffers,
                    this.blockSize,
                    new File(fileName));
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * Given an index, calculate the offset (of a BufferADT).
     *
     * @param index
     *            an index
     * @return offset the offset
     */
    public int calculateOffset(int index)
    {
        return (index * this.recordSize) % this.blockSize;
    }


    // ----------------------------------------------------------
    /**
     * Given an index, calculate the array index (of a BufferADT).
     *
     * @param index
     *            an index
     * @return array index of the BufferADT
     */
    public int calculateArrayIndex(int index)
    {
        return (index * this.recordSize) / this.blockSize;
    }


    // ----------------------------------------------------------
    /**
     * Returns the size of the RecordArray. The size of the RecordArray is equal
     * to the size of the associated BufferPoolADT * 1024.
     *
     * @return size the size of the RecordArray
     */
    public int size()
    {
        int bufferPoolSize = this.bufferPool.size();

        int size = bufferPoolSize * (this.blockSize / this.recordSize);

        return size;
    }


    // ----------------------------------------------------------
    /**
     * Returns the key (first short) associated with a specified index. The only
     * difference between the key and the value is that the offset is different
     * by 2 (as specified in the spec).
     *
     * @param index
     *            an index
     * @return key the key (first short) associated with a specified index
     */
    public short getKey(int index)
    {
        int offset = this.calculateOffset(index);
        int arrayIndex = this.calculateArrayIndex(index);

        byte[] bufferADTByteArray = bufferPool.getBuffer(arrayIndex).read();

        this.currentKey = ByteBuffer.wrap(bufferADTByteArray).getShort(offset);

        return this.currentKey;
    }


    // ----------------------------------------------------------
    /**
     * Returns the value (second short) associated with a specified index. The
     * only difference between the key and the value is that the offset is
     * different by 2 (as specified in the spec).
     *
     * @param index
     * @return value
     */
    public short getValue(int index)
    {
        int offset = this.calculateOffset(index);
        int arrayIndex = this.calculateArrayIndex(index);

        byte[] bufferADTByteArray = bufferPool.getBuffer(arrayIndex).read();

        this.currentValue =
            ByteBuffer.wrap(bufferADTByteArray).getShort(offset + DIFFERENCE);

        return this.currentValue;
    }


    /**
     * Given two different BufferADT objects and their offsets, copies and
     * writes the associated byte records.
     */
    private void copyAndWrite(
        BufferADT buffer1,
        BufferADT buffer2,
        int bufferOffset1,
        int bufferOffset2)
    {
        byte[] systemBuffer;

        byte[] byteRecord1 = new byte[this.recordSize];
        byte[] byteRecord2 = new byte[this.recordSize];

        systemBuffer = buffer1.read();

        System.arraycopy(
            systemBuffer,
            bufferOffset1,
            byteRecord1,
            0,
            this.recordSize);

        systemBuffer = buffer2.read();

        System.arraycopy(
            systemBuffer,
            bufferOffset2,
            byteRecord2,
            0,
            this.recordSize);

        System.arraycopy(
            byteRecord1,
            0,
            systemBuffer,
            bufferOffset2,
            this.recordSize);

        buffer2.write(systemBuffer);

        systemBuffer = buffer1.read();

        System.arraycopy(
            byteRecord2,
            0,
            systemBuffer,
            bufferOffset1,
            this.recordSize);

        buffer1.write(systemBuffer);
    }


    // ----------------------------------------------------------
    /**
     * Given two different array indexes, finds the associated BufferADTs,
     * exchanges them, and writes them.
     *
     * @param arrayIndex1
     *            first index
     * @param arrayIndex2
     *            second index
     */
    public void exchange(int arrayIndex1, int arrayIndex2)
    {

        int bufferIndex1 = this.calculateArrayIndex(arrayIndex1);
        int bufferOffset1 = this.calculateOffset(arrayIndex1);

        int bufferIndex2 = this.calculateArrayIndex(arrayIndex2);
        int bufferOffset2 = this.calculateOffset(arrayIndex2);

        BufferADT buffer1 = this.bufferPool.getBuffer(bufferIndex1);
        BufferADT buffer2 = this.bufferPool.getBuffer(bufferIndex2);

        this.copyAndWrite(buffer1, buffer2, bufferOffset1, bufferOffset2);
    }


    // ----------------------------------------------------------
    /**
     * This is a total clear; it makes sure that everything we have done so far
     * gets shipped out to disk and is properly written to file.
     */
    public void flush()
    {
        this.bufferPool.flush();
    }

}
