// -------------------------------------------------------------------------
/**
 * Represents a simple MaxHeap with Nodes. Loosely based on the code
 * distribution for the course in "../MaxHeap/MaxHeap.java".
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 4, 2012
 */
public class MaxHeap
{
    /**
     * An associated RecordArray.
     */
    private RecordArray recordArray;

    /**
     * The MaxHeap's size.
     */
    private int         heapSize;


    // ----------------------------------------------------------
    /**
     * Creates a new MaxHeap object with an associated RecordArray and
     * automatically heapifies it.
     *
     * @param array
     *            an associated Record Array
     */
    public MaxHeap(RecordArray array)
    {
        this.recordArray = array;
        this.heapSize = recordArray.size();

        this.heapify();
    }


    // ----------------------------------------------------------
    /**
     * Determines whether or not the index is a leaf.
     *
     * @param index
     *            an index
     * @return true or false depending on whether or not the index is a leaf
     */
    public boolean isLeaf(int index)
    {
        return ((index >= this.size() / 2) && (index < this.size()));
    }


    // ----------------------------------------------------------
    /**
     * Returns the left child of a specified index.
     *
     * @param index
     *            an index
     * @return the left child index of a specified index
     */
    private int leftChild(int index)
    {
        return (2 * index) + 1;
    }


    // ----------------------------------------------------------
    /**
     * Returns the size of the MaxHeap.
     *
     * @return the size of the heap
     */
    public int size()
    {
        return this.heapSize;
    }


    // ----------------------------------------------------------
    /**
     * Heapifies the MaxHeap through siftdown iteration.
     */
    private void heapify()
    {
        for (int i = (this.size() / 2) - 1; i >= 0; i--)
            this.siftdown(i);
    }


    // ----------------------------------------------------------
    /**
     * Sifts down the MaxHeap to move the Node into a proper position.
     *
     * @param index
     *            the index of the Node
     */
    private void siftdown(int index)
    {
        int currentIndex = index;

        while (!isLeaf(currentIndex))
        {
            int childIndex = this.leftChild(currentIndex);

            if ((childIndex < (heapSize - 1))
                && (this.recordArray.getKey(childIndex) < this.recordArray
                    .getKey(childIndex + 1)))
                childIndex++;

            if (this.recordArray.getKey(currentIndex) >= this.recordArray
                .getKey(childIndex))
                return;

            this.recordArray.exchange(currentIndex, childIndex);
            currentIndex = childIndex;
        }
    }


    // ----------------------------------------------------------
    /**
     * Removes the max (top Node) of the MaxHeap.
     */
    private void removeMax()
    {
        this.heapSize--;

        this.recordArray.exchange(0, heapSize);

        if (this.heapSize != 0)
            siftdown(0);
    }


    // ----------------------------------------------------------
    /**
     * Sorts the MaxHeap by iterating through the entire Heap and removing the
     * Max.
     */
    public void sort()
    {
        int currentSize = this.heapSize;

        for (int i = 0; i < currentSize; i++)
            this.removeMax();
    }

}
