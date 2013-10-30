// -------------------------------------------------------------------------
/**
 * Defines the Insert Command, which is an extension of a basic Command. When
 * called, the InsertCommand will create a handle based on a DNA string
 * sequence. Furthermore, the InsertCommand has the ability to removeDuplicate
 * handles in the case that there exists the same DNA sequence in the Tree
 * structure. An insertion occurs naturally at the root of the tree.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 11, 2012
 */
public class InsertCommand
    extends Command
{
    private Handle handle;


    // ----------------------------------------------------------
    /**
     * Creates a new InsertCommand object.
     *
     * @param sequence
     *            a DNA String sequence
     * @throws Exception
     *             an exception if the DNA Sequence can't be inserted (should
     *             never happen -- unless it's a duplicate)
     */
    public InsertCommand(String sequence)
        throws Exception
    {
        handle = createHandle(sequence);
    }


    /**
     * Executes the Insert Command on the given TreeNode root.
     *
     * @param root
     *            the TreeNode to insert the handle into
     */
    public TreeNode execute(TreeNode root)
    {
        System.out.println(">>> insert " + this.handle.toString());

        return root.insert(handle);
    }


    // ----------------------------------------------------------
    /**
     * If the Handle is a duplicate, remove it from the DNATree's memory manager
     * and print out an error message to the standard output.
     *
     * @param handle
     *            a given handle to test whether or not it's a duplicate
     */
    public static void removeDuplicateHandle(Handle handle)
    {
        DNATree.memoryManager.remove(handle.getHandleId());
        System.out.println("[INSERT ERROR] The DNA sequence '"
            + handle.toString() + "' already exists in the DNA Tree.");
    }

}
