// -------------------------------------------------------------------------
/**
 * Defines a basic interface for a Node to be implemented as a PriorityQueueNode
 * and used in the PriorityQueue. A Node has T data and can access/modify the
 * next and previous nodes relative to itself.
 *
 * @param <T>
 *            the data; T for Trevor
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 1, 2012
 */
public interface Node<T>
{
    // ----------------------------------------------------------
    /**
     * Returns the data of this Node.
     *
     * @return data of the Node
     */
    public T getData();


    // ----------------------------------------------------------
    /**
     * Changes the data of the Node.
     *
     * @param data
     *            new data of the Node
     */
    public void setData(T data);


    // ----------------------------------------------------------
    /**
     * Returns the next Node.
     *
     * @return the next Node
     */
    public Node<T> getNext();


    // ----------------------------------------------------------
    /**
     * Changes the next Node
     *
     * @param next
     *            the new next Node
     */
    public void setNext(Node<T> next);


    // ----------------------------------------------------------
    /**
     * Returns the previous Node.
     *
     * @return the previous Node
     */
    public Node<T> getPrevious();


    // ----------------------------------------------------------
    /**
     * Changes the previous Node.
     *
     * @param previous
     *            the previous Node
     */
    public void setPrevious(Node<T> previous);

}
