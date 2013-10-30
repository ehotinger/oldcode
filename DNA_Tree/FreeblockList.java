import java.lang.StringBuilder;
import java.lang.RuntimeException;
import java.util.NoSuchElementException;

/**
 * A circular, Doubly Linked List implementation that is restricted by size.
 *
 * @author Trevor M. Senior
 * @author Eric R. Hotinger
 * @version 1
 * @since 10/15/2012
 */
public class FreeblockList
{

    /**
     * A unique ID counter for every block that enters this class. Everytime a
     * new block is added to this list, it gets increased by one. This will
     * never decrement.
     */
    private static int uniqueId = 0;


    /**
     * A single block in this Freeblock list.
     */
    public class Block
    {
        /**
     *
     */
        public final int id       = uniqueId++;
        /**
         *
         */
        public int       blockSize     = -1;
        /**
         *
         */
        public byte[]    data     = null;
        /**
         *
         */
        public Block     previous = null;
        /**
         *
         */
        public Block     next     = null;


        /**
         * Creates a new Block with a specified size.
         *
         * @param size
         *            the size of the block
         */
        public Block(int size)
        {
            this.blockSize = size;
        }


        /**
         * @param block
         *            The block that we are copying the contents of
         */
        public Block(Block block)
        {
            this.blockSize = block.blockSize;
            this.data = block.data;
            this.next = block.next;
            this.previous = block.previous;
        }


        /**
         * Determines whether or not the Block is empty.
         *
         * @return true or false if the Block is empty
         */
        public boolean isEmpty()
        {
            return this.data == null;
        }


        /**
         * Represents the String representation of this block.
         *
         * @return String the String representation of this Block.
         * @Override preexisting toString function
         */
        public String toString()
        {
            return "[ Block:" + " id=" + this.id + " size=" + this.blockSize
                + " empty=" + isEmpty() + " ]";
        }


        /**
         * Determines whether or not two Blocks are equal to each other.
         *
         * @param obj
         *            The object we are comparing this block to.
         * @return boolean True if we have a match, false otherwise.
         * @Override preexisting equals function
         */
        public boolean equals(Object obj)
        {

            if (obj == null)
            {
                return false;
            }
            if (this.getClass() != obj.getClass())
            {
                return false;
            }

            final Block other = (Block)obj;

            if (this.id != other.id)
            {
                return false;
            }

            return true;
        }
    }

    private Block leftEdgeBlock  = null;
    private Block rightEdgeBlock = null;
    private Block currentBlock   = null;

    private int   totalBlocks    = -1;  // The "size" of this Freeblock List
    private int   size           = -1;


    // ----------------------------------------------------------
    /**
     * Create a new FreeblockList object.
     * @param size
     */
    public FreeblockList(int size)
    {
        currentBlock = new Block(size);
        this.totalBlocks = 1;
        this.size = size;
    }


    /**
     * Inserts a given peice of data into this FreeblockList and returns its
     * unique ID.
     *
     * @param insertSize
     *            The size of this block that we are inserting
     * @param insertData
     *            The data that we are inserting - gheesh.
     * @return int A unique ID associated with this Block of data, -1 if there
     *         was a problem with insertion
     */
    public int insert(byte[] insertData, int insertSize)
    {
        int ret = -1; // default return value

        boolean currentBlockIsValid = false;

        int emptyBlocks = 0;

        /**
         * loop through all the blocks in this FreeList and determine if any are
         * valid to insert into
         */
        for (int i = 0; i < totalBlocks; i++)
        {

            if (currentBlock.data == null)
            {
                emptyBlocks++;
            }

            /**
             * if we are trying to insert into a block that already has data in
             * it OR if the current block we are looking is at too small to hold
             * the given data, we then.
             */
            if (currentBlock.data != null || insertSize > currentBlock.blockSize)
            {
                // skip this block, and move on to the next block in the list.
                if (currentBlock.next != null)
                {
                    currentBlock = currentBlock.next;
                }
            }

            /**
             * else we can break out of the loop as the current block is a valid
             * block to insert into.
             */
            else
            {
                currentBlockIsValid = true;
                break;
            }
        }

        if (!currentBlockIsValid)
        {
            // Get the new block size
            int newBlockSize = 100;
            while(newBlockSize < insertSize) {
                newBlockSize += 100;
            }


            // Create a new block and append it to the freeblock list
            if (emptyBlocks == 0)
            {
                System.out.println("Cresting a new block");
                Block newBlock = new Block(newBlockSize);
                totalBlocks ++;
                newBlock.next = currentBlock.next;
                currentBlock.next = newBlock;
                currentBlock = newBlock;

            }
            // Search for an empty node and increase it by 100
            else
            {
                for (int i = 0; i < totalBlocks; i++)
                {
                    if (currentBlock.data != null)
                    {
                        if (currentBlock.next != null)
                        {
                            currentBlock = currentBlock.next;
                        }
                    } else {
                        break;
                    }

                }

                currentBlock.blockSize += newBlockSize;
            }

            currentBlockIsValid = true;

        }

        // if we did find a block that can hold our data.
        if (currentBlockIsValid)
        {

            ret = currentBlock.id; // set the return value to this blocks unique
// id

            // Case 1: The given block fits the list perfectly!
            if (insertSize == currentBlock.blockSize)
            {
                currentBlock.data = insertData;

                // Case 2: The block given is smaller than the one we are
// currently
                // looking at. We need to create a new one, and stick it in
// before the
                // currentBlock that we are looking at. Of course, increment the
// total
                // number of blocks as well!
            }
            else
            {

                int freeblockSize = currentBlock.blockSize - insertSize;
                Block newFreeBlock = new Block(freeblockSize);

                currentBlock.blockSize = insertSize;
                currentBlock.data = insertData;

                if (currentBlock.previous == null)
                {
                    currentBlock.previous = newFreeBlock;
                    leftEdgeBlock = currentBlock;
                }

                newFreeBlock.previous = currentBlock;

                if (currentBlock.next == null)
                {
                    newFreeBlock.next = currentBlock;
                    rightEdgeBlock = newFreeBlock;
                }
                else
                {
                    newFreeBlock.next = currentBlock.next;
                }

                if (currentBlock.equals(rightEdgeBlock))
                {
                    rightEdgeBlock = newFreeBlock;
                }

                currentBlock.next = newFreeBlock;

                currentBlock = newFreeBlock;
                totalBlocks++;
            }

            // else display a message to the user that their data is too large
// to fit
            // into this FreeblockList given the size constraints
        }
        else
        {
            throw new RuntimeException(
                "There isn't any more space in the FreeblockList "
                    + "to insert a size of " + insertSize + ".");
        }

        return ret;
    }


    /**
     * Searches through the FreeblockList for a specific block specified by a
     * unique ID.
     *
     * @param id
     *            int The unique ID that was assigned to this block when it was
     *            inserted into this FreeblockList.
     * @return Block The found block, or null if a Block with the given ID does
     *         not exist.
     */
    public Block get(int id)
    {

        Block ret = null;

        for (int i = 0; i < totalBlocks; i++)
        {
            // If we found the block with the matching ID, we can return it's
            // value
            if (currentBlock.id == id)
            {
                ret = currentBlock;
                break;
            }

            // Catch the case where we only have one element in the list!
            if (currentBlock.next != null)
            {
                currentBlock = currentBlock.next;
            }
            ;
        }

        if (ret == null)
        {
            throw new NoSuchElementException("A block with an id of " + id
                + " does not exist in this FreeblockList.");
        }
        return ret;
    }


    /**
     * Gets the current block that the FreeblockList is currently looking at
     * @return The Current block in this Freeblock List
     */
    public Block getCurrentBlock()
    {
        return currentBlock;
    }


    /**
     * Clears all blocks fro mthis list and removes all data.
     *
     * @Postcondition Sets this FreeblockList back to its initial state.
     */
    public void clear()
    {
        currentBlock.blockSize = this.size;
        currentBlock.data = null;
        currentBlock.previous = null;
        currentBlock.next = null;

        leftEdgeBlock = null;
        rightEdgeBlock = null;

        totalBlocks = 1;
    }


    /**
     * Merges the current block with the block to the left of it.
     *
     * @Precondition currentBlock can't be null
     */
    private void mergePrevious()
    {
        if (totalBlocks <= 2)
        {
            clear();
        }
        else
        {
            currentBlock.previous.blockSize =
                currentBlock.previous.blockSize + currentBlock.blockSize;
            currentBlock.previous.next = currentBlock.next;
            currentBlock.next.previous = currentBlock.previous;
            currentBlock = currentBlock.previous;
            totalBlocks--;
        }
    }


    /**
     * Merges the current block with the block to the right of it.
     *
     * @Precondition currentBlock can't be null
     */
    private void mergeNext()
    {
        if (totalBlocks <= 2)
        {
            clear();
        }
        else
        {
            if (currentBlock.next.equals(rightEdgeBlock))
            {
                rightEdgeBlock = currentBlock;
            }
            currentBlock.blockSize = currentBlock.blockSize + currentBlock.next.blockSize;
            currentBlock.next = currentBlock.next.next;
            currentBlock.next.previous = currentBlock;
            totalBlocks--;
        }
    }


    /**
     * Searches through the FreeblockList for a specific block specified by a
     * unique ID and deletes and returns the Block if found.
     *
     * @param id
     *            int The unique ID that was assigned to this block when it was
     *            inserted into this FreeblockList.
     */
    public void remove(int id)
    {
        boolean success = false;

        for (int i = 0; i < totalBlocks; i++)
        {
            // If we found the block with the matching ID, we can then
// deallocate it
            if (currentBlock.id == id)
            {

                success = true;
                currentBlock.data = null; // remove all data tied with this
// block

                // If the previous block isn't null, the data is null, and we're
// not
                // on the left edge of the list we can then merge to the left
                boolean canMergePrevious =
                    (currentBlock.previous != null)
                        && (currentBlock.previous.data == null)
                        && (!currentBlock.equals(leftEdgeBlock));
                // If the next block isn't null, the data is null, and we're not
// on
                // the edge of the list we can then merge to the right
                boolean canMergeNext =
                    (currentBlock.next != null)
                        && (currentBlock.next.data == null)
                        && (!currentBlock.equals(rightEdgeBlock));

                // CASE 1: We have a double merge.
                if (canMergePrevious && canMergeNext)
                {
                    mergeNext();
                    mergePrevious();

                    // CASE 2: A single merge to the right.
                }
                else if (canMergeNext)
                {
                    mergeNext();

                    // CASE 3: A single merge to the left.
                }
                else if (canMergePrevious)
                {
                    mergePrevious();
                }

                break; // no need to continue our search once found & removed
            }

            // Catch the case where we only have one element in the list!
            if (currentBlock.next != null)
            {
                currentBlock = currentBlock.next;
            }
            ;
        }

        if (!success)
        {
            throw new RuntimeException("Element with an id of " + id
                + " does not exist in this FreeblockList.");
        }
    }


    /**
     * Returns the total number of blocks that are in the Freeblock List.
     *
     * @return The total number of blocks that are currently in this list.
     */
    public int getTotalBlocks()
    {
        return this.totalBlocks;
    }


    /**
     * Returns a String representation of the Freeblock List.
     *
     * @return a String representation of this FreeblockList
     * @Override the preexisting toString function
     */
    public String toString()
    {
        StringBuilder list = new StringBuilder();
        list.append("\n");
        list.append("FreeblockList (top block is the current one):\n");

        for (int i = 0; i < totalBlocks; i++)
        {
            list.append(currentBlock + "\n");
            // Catch the case where we only have one element in the list!
            if (currentBlock.next != null)
            {
                currentBlock = currentBlock.next;
            }
            ;
        }

        return list.toString();
    }

}
