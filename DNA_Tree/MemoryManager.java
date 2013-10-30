/**
 * Responsible for storing variable length records. It accomplishes this by
 * using a FreeblockList along with a Record Array to store and keep track of
 * elements respectivly.
 *
 * @author Trevor M. Senior
 * @author Eric R. Hotinger
 * @version 1
 * @since 10/15/2012
 */
public class MemoryManager
{

    // The freeblock list datastructure that this memory manager will use to
    // hold information
    private FreeblockList freeblockList = null;


    /**
     * Initializes the FreeblockList.
     *
     * @param poolSize
     *            The size of the pool we want this freeblock list to be.
     */
    public MemoryManager(int poolSize)
    {
        freeblockList = new FreeblockList(poolSize);
    }


    /**
     * Gets an encoded DNA byte array at a specific block.
     *
     * @param id
     *            The unique ID assigned to a Block in the FreeblockArray.
     * @return A byte array representing the encoded DNA sequence.
     */
    public byte[] get(int id)
    {
        return freeblockList.get(id).data;
    }


    /**
     * Prints a representation of a Block in the Freeblock List.
     *
     * @param id
     *            A specific block id that we want to print.
     * @return String representation of the block.
     */
    public String print(int id)
    {
        return freeblockList.get(id).toString();
    }


    /**
     * Prints a representation of all the Blocks in the Freeblock List.
     *
     * @return A string representing all blocks that we have stored thus far.
     */
    public String print()
    {
        return freeblockList.toString();
    }


    /**
     * Inserts a block into the freeblockList, and returns a unique ID that
     * represents that specific block in the list.
     *
     * @param bytes
     *            the encoded DNA byte array
     * @return The unique ID associated with the inserted block
     */
    public int insert(byte[] bytes)
    {
        return freeblockList.insert(bytes, bytes.length);
    }


    /**
     * Removes a block from the Freeblock List.
     *
     * @param id
     *            The blocks id that we wish to remove
     */
    public void remove(int id)
    {
        freeblockList.remove(id);
    }


    /**
     * Returns a string representation of this Memory Manager.
     *
     * @Override the default toString
     * @return a string representation of the Memory Manager.
     */
    public String toString()
    {
        return print();
    }

}
