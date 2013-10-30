// -------------------------------------------------------------------------
/**
 * Defines a simple PriorityQueue which contains PriorityQueueNodes. The
 * PriorityQueue can insert or remove data and Nodes. This PriorityQueue is
 * meant for use in a LRU scheme.
 *
 * @param <T>
 *            the type of data contained in the PriorityQueueNodes
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 1, 2012
 */
public class PriorityQueue<T>
{
    /**
     * The head of the PriorityQueue.
     */
    private PriorityQueueNode<T> head;

    /**
     * The tail of the PriorityQueue.
     */
    private PriorityQueueNode<T> tail;


    // ----------------------------------------------------------
    /**
     * Creates a new PriorityQueue object with a specified size and initializes
     * it.
     *
     * @param size
     *            the size of the PriorityQueue
     */
    public PriorityQueue(int size)
    {
        this.setHead(new PriorityQueueNode<T>(null));
        this.setTail(new PriorityQueueNode<T>(null));

        this.getHead().setNext(this.getTail());
        this.getTail().setPrevious(this.getHead());

        this.initializePriorityQueue(size);
    }


    // ----------------------------------------------------------
    /**
     * Initializes the PriorityQueue based on a given size. Pads the
     * PriorityQueue with empty Nodes (Nodes containing no data).
     *
     * @param size
     *            the size of the PriorityQueue
     */
    private void initializePriorityQueue(int size)
    {
        for (int i = 0; i < size; i++)
        {
            PriorityQueueNode<T> emptyNode = new PriorityQueueNode<T>(null);

            this.getHead().getNext().setPrevious(emptyNode);

            emptyNode.setNext(this.getHead().getNext());
            emptyNode.setPrevious(this.getHead());

            this.getHead().setNext(emptyNode);
        }
    }


    // ----------------------------------------------------------
    /**
     * Removes a given node from the PriorityQueue.
     *
     * @param nodeToBeRemoved
     *            the node to be removed from the PriorityQueue
     */
    private void removeNode(Node<T> nodeToBeRemoved)
    {
        nodeToBeRemoved.getNext().setPrevious(nodeToBeRemoved.getPrevious());

        nodeToBeRemoved.getPrevious().setNext(nodeToBeRemoved.getNext());
    }


    // ----------------------------------------------------------
    /**
     * Inserts a Node at the head of the PriorityQueue after removing it from
     * its current position in the PriorityQueue.
     *
     * @param node
     *            the node to insert at the head of the PriorityQueue
     */
    private void insertAtHead(Node<T> node)
    {
        /**
         * Remove the node from its current position in the PriorityQueue.
         */
        this.removeNode(node);

        /**
         * Insert the node at the head of the PriorityQueue.
         */
        node.setPrevious(getHead());
        node.setNext(getHead().getNext());
        this.getHead().getNext().setPrevious(node);
        this.getHead().setNext(node);
    }


    // ----------------------------------------------------------
    /**
     * Given data, attempts to insert the data into the PriorityQueue.
     *
     * @param data
     *            data to insert
     * @return the data inserted
     */
    public T insert(T data)
    {
        /**
         * Try to put the data at the head if it isn't null.
         */
        if (data != null)
        {
            Node<T> currentNode = this.getHead();

            /**
             * Loop through the entire PriorityQueue and see if the data exists
             * already in the PriorityQueue. If it does, return null to avoid
             * duplication.
             */
            while ((currentNode = currentNode.getNext()) != this.getTail())
            {
                if (data.equals(currentNode.getData()))
                {
                    this.insertAtHead(currentNode);
                    return null;
                }
            }
        }

        /**
         * Create a new Node based on the given data; initialize it based on the
         * head of the PriorityQueue and insert it at the head.
         */
        Node<T> node = new PriorityQueueNode<T>(data);

        node.setPrevious(this.getHead());
        node.setNext(this.getHead().getNext());

        this.getHead().getNext().setPrevious(node);
        this.getHead().setNext(node);

        /**
         * Create a new Node that represents the least recently used Node and
         * insert it at the tail of the PriorityQueue. Return this Node's data.
         */
        Node<T> leastRecentlyUsedNode = this.getTail().getPrevious();

        leastRecentlyUsedNode.getPrevious().setNext(this.getTail());

        this.getTail().setPrevious(leastRecentlyUsedNode.getPrevious());

        return leastRecentlyUsedNode.getData();
    }


    // ----------------------------------------------------------
    /**
     * Removes a Node with specified data from the PriorityQueue.
     *
     * @param data
     *            data in the PriorityQueue
     */
    public void remove(T data)
    {
        /**
         * Set the current node equal to the head of the PriorityQueue.
         */
        Node<T> currentNode = this.getHead();

        /**
         * Loop through every Node in the PriorityQueue. If the current node's
         * data during the loop matches the specified data, remove it from the
         * PriorityQueue and insert an empty node (Node with no data) at that
         * location.
         */
        loop: while ((currentNode = currentNode.getNext()) != this.getTail())
        {
            if (data.equals(currentNode.getData()))
            {
                this.removeNode(currentNode);

                Node<T> emptyNode = new PriorityQueueNode<T>(null);

                emptyNode.setNext(getTail());
                emptyNode.setPrevious(getTail().getPrevious());

                getTail().getPrevious().setNext(emptyNode);
                getTail().setPrevious(emptyNode);

                break loop;
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Returns the head of the PriorityQueue.
     *
     * @return the head of the PriorityQueue
     */
    public PriorityQueueNode<T> getHead()
    {
        return this.head;
    }


    // ----------------------------------------------------------
    /**
     * Changes the head of the PriorityQueue.
     *
     * @param head
     *            the head to set
     */
    public void setHead(PriorityQueueNode<T> head)
    {
        this.head = head;
    }


    // ----------------------------------------------------------
    /**
     * Returns the tail of the PriorityQueue.
     *
     * @return the tail
     */
    public PriorityQueueNode<T> getTail()
    {
        return this.tail;
    }


    // ----------------------------------------------------------
    /**
     * Changes the tail of the PriorityQueue.
     *
     * @param tail
     *            the tail to set
     */
    public void setTail(PriorityQueueNode<T> tail)
    {
        this.tail = tail;
    }

}
