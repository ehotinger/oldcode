import java.util.NoSuchElementException;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 14, 2012
 */
public class InternalTreeNode
    implements TreeNode
{

    /**
     * Represents an 'A' in the DNA Alphabet.
     */
    private TreeNode A;

    /**
     * Represents a 'C' in the DNA Alphabet.
     */
    private TreeNode C;

    /**
     * Represents a 'G' in the DNA Alphabet.
     */
    private TreeNode G;

    /**
     * Represents a 'T' in the DNA Alphabet.
     */
    private TreeNode T;

    /**
     * Represents a '$' in the DNA Alphabet.
     */
    private TreeNode dollar;


    // ----------------------------------------------------------
    /**
     * Constructs an InternalTreeNode given a preexisting HandleLeafNode and a
     * new Handle. Handles the case where the preexisting HandleLeafNode should
     * be inserted into the "dollar" TreeNode, or the new HandleLeafNode should
     * be inserted into the "dollar" TreeNode.
     *
     * @param handleLeafNode
     *            the existing HandleLeafNode
     * @param handle
     *            a new handle
     */
    public InternalTreeNode(HandleLeafNode handleLeafNode, Handle handle)
    {

        A = C = G = T = dollar = EmptyTreeNode.getInstanceOf();

        final Handle currentHandle = handleLeafNode.getHandle();

        /**
         * @see above
         */
        final Handle[] handleArray =
            {
                ((currentHandle.getLength() < handle.getLength())
                    ? handle
                    : currentHandle),

                ((currentHandle.getLength() < handle.getLength())
                    ? currentHandle
                    : handle) };

        insert(handleArray[0]);

        if (handleArray[1].isPrefixOf(handleArray[0]))
            insertDollar(handleArray[1]);

        else
            insert(handleArray[1]);
    }


    // ----------------------------------------------------------
    /**
     * Prints out the InternalTreeNode according to the given print mode and a
     * given depth. Prints from "left to right," in the sense that A-C-G-T-$
     * have order.
     *
     * @param depth
     *            the depth of the tree
     * @param printMode
     *            the printing mode
     */
    public void print(int depth, Util.printMode printMode)
    {
        PrintCommand.printInternalTreeNode(depth);

        A.print(depth + 1, printMode);
        C.print(depth + 1, printMode);
        G.print(depth + 1, printMode);
        T.print(depth + 1, printMode);
        dollar.print(depth + 1, printMode);
    }


    // ----------------------------------------------------------
    /**
     * Responsible for inserting the dollar correctly.
     *
     * @param handle
     *            the handle that is a dollar
     */
    public void insertDollar(Handle handle)
    {
        /**
         * If the dollar is empty, or a flyweight, we can simply insert the
         * dollar straight into the Tree.
         */
        if (dollar instanceof EmptyTreeNode)
            dollar = dollar.insert(handle);

        /**
         * If there are two dollars, we need to remove the duplicate before we
         * can insert it.
         */
        else if (handle.equals(((HandleLeafNode)dollar).getHandle()))
            InsertCommand.removeDuplicateHandle(handle);

        /**
         * Otherwise, the dollar is in the wrong position and we need to change
         * it.
         */
        else
            insert(changeDollar(handle));
    }


    /**
     * Swaps the old handle with the dollar and sets the dollar's handle to the
     * new handle. Returns the old handle.
     *
     * @param handle
     *            the new handle
     * @return the old handle
     */
    private Handle changeDollar(Handle handle)
    {
        /**
         * The old handle is the dollar's current handle.
         */
        Handle oldHandle = ((HandleLeafNode)dollar).getHandle();

        /**
         * Set the dollar to the new handle
         */
        ((HandleLeafNode)dollar).setHandle(handle);

        /**
         * Return the previous (old) handle
         */
        return oldHandle;
    }


    /**
     * If the prefix for the specified handle is an instance of a
     * HandleLeafNode, the dollar's DNA sequence length is greater than the
     * handle's sequence length, AND the handle is a prefix of the dollar's
     * handle, then the prefix is empty. OTHERWISE: it's not.
     *
     * @param handle
     *            the specified handle
     * @return true or false based on these aforementioned conditions
     */
    private boolean isPrefixEmpty(Handle handle)
    {
        if (dollar instanceof HandleLeafNode)
            if (((HandleLeafNode)dollar).getHandle().getLength() > handle
                .getLength())
                if (handle.isPrefixOf(((HandleLeafNode)dollar).getHandle()))
                    return true;

        return false;
    }


    /**
     * Determines whether or not the handle has a prefix based on the childNode.
     *
     * @param a
     *            handle
     * @param childNode
     *            a child node
     * @return true or false depending on whether or not it has a prefix
     */
    private boolean hasPrefix(Handle handle, TreeNode childNode)
    {
        if (!handle.hasNextCharacter() && (countEmptyTreeNodeChildren() < 2))
            if (childNode instanceof HandleLeafNode)
                return true;

        return false;
    }


    /**
     * Counts the number of empty tree node children and returns the count.
     *
     * @return the number of empty tree node children
     */
    private int countEmptyTreeNodeChildren()
    {
        int emptyChilds = 0;

        for (TreeNode treeNodeChild : new TreeNode[] { this.A, this.C, this.G,
            this.T })
            emptyChilds += (treeNodeChild instanceof HandleLeafNode) ? 1 : 0;

        return emptyChilds;
    }


    /**
     * Returns the TreeNode based on a given dna sequence.
     *
     * @param character
     *            a given dna sequence
     * @return the TreeNode
     */
    private TreeNode getTreeNodeBasedOnCharacter(char character)
    {
        if (character == 'A')
            return this.A;
        else if (character == 'C')
            return this.C;
        else if (character == 'G')
            return this.G;
        else if (character == 'T')
            return this.T;

        return null;
    }


    /**
     * Determines whether or not the TreeNode is a child based on a given
     * character.
     *
     * @param character
     *            a specified Character
     * @param treeNodeChild
     *            a given TreeNode child.
     * @return true or false depending on whether or not it's a child TreeNode
     */
    public boolean isChildTreeNodeBasedOnCharacter(
        char character,
        TreeNode treeNodeChild)
    {

        if (character == 'A')
        {
            this.A = treeNodeChild;
            return true;
        }

        else if (character == 'C')
        {
            this.C = treeNodeChild;
            return true;
        }

        else if (character == 'G')
        {
            this.G = treeNodeChild;
            return true;
        }

        else if (character == 'T')
        {
            this.T = treeNodeChild;
            return true;
        }

        return false;
    }


    /**
     * Inserts a TreeNode into a TreeNode that is a child of the
     * InternalTreeNode, or itself if there are no children and prefixes.
     *
     * @param handle
     *            the handle
     * @return the TreeNode inserted
     */
    public TreeNode insert(Handle handle)
    {

        if (handle.hasNextCharacter())
        {
            char nextChar = handle.getNextCharacter();

            TreeNode childNode = getTreeNodeBasedOnCharacter(nextChar);

            /**
             * If the prefix isn't empty, then we can just insert the dollar.
             */
            if (hasPrefix(handle, childNode))
                insertDollar(handle);

            /**
             * If the prefix is empty, then we need to change the dollar and
             * then insert it.
             */
            else if (isPrefixEmpty(handle))
                insert(changeDollar(handle));

            /**
             * Otherwise, change the child of the TreeNode.
             */
            else
            {
                isChildTreeNodeBasedOnCharacter(
                    nextChar,
                    childNode.insert(handle));

            }
        }

        else
            insertDollar(handle);

        return this;
    }


    /**
     * Removes a Handle from an InternalTreeNode's child TreeNode.
     *
     * @param handle
     *            a given handle
     * @return the TreeNode that is removed (replaced), or itself
     */
    public TreeNode remove(Handle handle)
    {
        TreeNode newTreeNode = null;

        if (handle.hasNextCharacter())
        {
            char character = handle.getNextCharacter();

            TreeNode treeNodeChild = getTreeNodeBasedOnCharacter(character);

            isChildTreeNodeBasedOnCharacter(
                character,
                treeNodeChild.remove(handle));
        }

        else
            dollar = dollar.remove(handle);

        for (TreeNode treeNodeChild : new TreeNode[] { this.A, this.C, this.G,
            this.T, this.dollar })
        {

            /**
             * If the treeNodeChild is an InternalTreeNode, we just return it.
             */
            if (treeNodeChild instanceof InternalTreeNode)
            {
                return this;
            }

            /**
             * If the treeNodeChild is a HandleLeafNode, then we need to check
             * whether or not it's null. If it's null, then we need to change it
             * into a new TreeNode, and if it isn't then we can just return the
             * treeNodeChild.
             */
            else if (treeNodeChild instanceof HandleLeafNode)
            {
                if (newTreeNode == null)
                    newTreeNode = treeNodeChild;

                else
                    return this;
            }
        }

        /**
         * Decrements the Handle's current position by 1.
         *
         * @see CharacterArray.java
         */
        try
        {
            ((HandleLeafNode)newTreeNode).getHandle().getPreviousCharacter();
        }

        catch (NoSuchElementException e)
        {
            /**
             * This ... shouldn't error. but just in case
             * System.out.println("ERROR: " + e.getMessage());
             */
        }

        return newTreeNode;
    }


    /**
     * Searches the InternalTreeNode based on a given search command and
     * traverses over its children, searching them as well if the correct child
     * is found.
     *
     * @param searchCommand
     *            a given search command
     */
    public void search(SearchCommand searchCommand)
    {
        searchCommand.increaseNumVisited();

        Handle handle = searchCommand.getHandle();

        if (handle.hasNextCharacter())
        {

            TreeNode treeNodeChild =
                getTreeNodeBasedOnCharacter(handle.getNextCharacter());

            treeNodeChild.search(searchCommand);
        }

        else if (searchCommand.isMatchingExact())
        {
            dollar.search(searchCommand);
        }

        else
        {
            for (TreeNode treeNodeChild : new TreeNode[] { this.A, this.C,
                this.G, this.T, this.dollar })
                treeNodeChild.search(searchCommand);
        }
    }

}
