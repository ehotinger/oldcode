// -------------------------------------------------------------------------
/**
 * Defines the Remove Command, which is an extension of a basic command. When
 * called, the RemoveCommand will remove a given handle based on a DNA string
 * sequence from the TreeNode. Furthermore, the RemoveCommand has the ability to
 * spit out a custom error message in the case that no such DNA string exists in
 * the memory manager.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 11, 2012
 */
public class RemoveCommand
    extends Command
{
    private Handle handle;


    // ----------------------------------------------------------
    /**
     * Creates a new RemoveCommand object.
     *
     * @param sequence
     *            a string of DNA
     * @throws Exception
     *             an exception if the DNA Sequence can't be removed (it is
     *             nonexistent).
     */
    public RemoveCommand(String sequence)
        throws Exception
    {
        handle = createHandle(sequence);
    }


    /**
     * Executes the remove command on the given TreeNode node.
     *
     * @param node
     *            the TreeNode to remove the handle from
     * @return the TreeNode executed on
     */
    public TreeNode execute(TreeNode node)
    {
        System.out.println(">>> remove " + handle.toString());
        DNATree.memoryManager.remove(handle.getHandleId());
        return node.remove(handle);
    }


    // ----------------------------------------------------------
    /**
     * Spits out an error message if a Handle that doesn't exist is trying to be
     * removed.
     *
     * @param handle
     *            the handle that is trying to be removed
     */
    public static void removeNonexistentHandle(Handle handle)
    {
        System.out.println("[REMOVE ERROR] Unable to remove sequence '"
            + handle + "'");
    }
}
