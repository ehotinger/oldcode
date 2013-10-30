// -------------------------------------------------------------------------
/**
 * The HandleLeafNode class represents a Leaf Node in the Tree.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 13, 2012
 */
public class HandleLeafNode
    implements TreeNode
{
    private Handle handle;


    // ----------------------------------------------------------
    /**
     * Creates a new HandleLeafNode object based on a Handle.
     *
     * @param handle
     *            a Handle to store in the HandleLeafNode object
     */
    public HandleLeafNode(Handle handle)
    {
        this.handle = handle;
    }


    // ----------------------------------------------------------
    /**
     * Returns the Handle stored in the HandleLeafNode.
     *
     * @return the Handle stored
     */
    public Handle getHandle()
    {
        return handle;
    }


    // ----------------------------------------------------------
    /**
     * Returns a String representation of this HandleLeafNode.
     *
     * @return a string representation of the TreeNode
     */
    public String toString()
    {
        return handle.toString();
    }


    // ----------------------------------------------------------
    /**
     * Inserts a given Handle into this HandleLeafNode. If the handle is the
     * same, return this HandleLeafNode.
     *
     * @param newHandle
     *            the Handle to insert into the TreeNode
     * @return the TreeNode inserted into
     */
    public TreeNode insert(Handle newHandle)
    {
        if (this.handle.equals(newHandle))
        {
            InsertCommand.removeDuplicateHandle(newHandle);
            return this;
        }

        else
            return new InternalTreeNode(this, newHandle);
    }


    // ----------------------------------------------------------
    /**
     * Removes a given Handle from this HandleLeafNode. If the handle is the
     * same, return an instance of EmptyTreeNode.
     *
     * @param newHandle
     *            the Handle to be removed
     * @return the TreeNode where the Handle was removed
     */
    public TreeNode remove(Handle newHandle)
    {
        if (this.handle.equals(newHandle))
        {
            return EmptyTreeNode.getInstanceOf();
        }

        else
        {
            RemoveCommand.removeNonexistentHandle(newHandle);
            return this;
        }
    }


    // ----------------------------------------------------------
    /**
     * Prints out the HandleLeafNode according to a given print mode and a given
     * depth.
     *
     * @param depth
     *            the depth of the Tree
     * @param printMode
     *            the printing mode
     */
    public void print(int depth, Util.printMode printMode)
    {
        String representation = handle.toString();

        representation +=
            (printMode == Util.printMode.STATS) ? ": "
                + handle.getFrequencies() : "";

        representation +=
            (printMode == Util.printMode.LENGTH) ? ": length "
                + handle.getLength() : "";

        PrintCommand.printHandleLeafNode(depth, representation);
    }


    // ----------------------------------------------------------
    /**
     * Searches the HandleLeafNode given a specific search command.
     *
     * @param searchCommand
     *            the given command
     */
    public void search(SearchCommand searchCommand)
    {
        searchCommand.increaseNumVisited();

        if (this.isSame(searchCommand))
            searchCommand.reportMatchFound(this.handle);

        else
            searchCommand.reportMatchFound(this.handle);
    }


    // ----------------------------------------------------------
    /**
     * Changes the Handle of the LeafNode.
     *
     * @param handle
     *            the new handle
     */
    public void setHandle(Handle handle)
    {
        this.handle = handle;
    }


    // ----------------------------------------------------------
    /**
     * Determines whether or not the two commands are the same.
     *
     * @param searchCommand
     *            another search command
     */
    private boolean isSame(SearchCommand searchCommand)
    {
        return searchCommand.isMatchingExact()
            && this.handle.equals(searchCommand.getHandle());
    }
}
