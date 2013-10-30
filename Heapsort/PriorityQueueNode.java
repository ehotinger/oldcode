// -------------------------------------------------------------------------
/**
 * Provides the implementation of the Node interface as a PriorityQueueNode.
 * PriorityQueueNodes are used in the PriorityQueue.
 *
 * @param <E>
 *            the data; E for Eric
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 1, 2012
 */
public class PriorityQueueNode<E>
    implements Node<E>
{
    private E       data;
    private Node<E> previous;
    private Node<E> next;


    // ----------------------------------------------------------
    /**
     * Create a new PriorityQueueNode object with specified data.
     *
     * @param data
     *            the data of the Node
     */
    public PriorityQueueNode(E data)
    {
        this.data = data;
    }


    // ----------------------------------------------------------
    /**
     * Returns the data of this Node.
     *
     * @return data of the Node
     */
    public E getData()
    {
        return this.data;
    }


    // ----------------------------------------------------------
    /**
     * Changes the data of the Node.
     *
     * @param data
     *            new data of the Node
     */
    public void setData(E data)
    {
        this.data = data;
    }


    // ----------------------------------------------------------
    /**
     * Returns the next Node.
     *
     * @return the next Node
     */
    public Node<E> getNext()
    {
        return this.next;
    }


    // ----------------------------------------------------------
    /**
     * Changes the next Node
     *
     * @param next
     *            the new next Node
     */
    public void setNext(Node<E> next)
    {
        this.next = next;
    }


    // ----------------------------------------------------------
    /**
     * Returns the previous Node.
     *
     * @return the previous Node
     */
    public Node<E> getPrevious()
    {
        return this.previous;
    }


    // ----------------------------------------------------------
    /**
     * Changes the previous Node.
     *
     * @param previous
     *            the previous Node
     */
    public void setPrevious(Node<E> previous)
    {
        this.previous = previous;
    }

}
