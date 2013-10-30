// -------------------------------------------------------------------------
/**
 * A command is a primitive abstract class which defines the createSequence
 * method, wherein we represent a given DNA string as a Handle.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 11, 2012
 */
public abstract class Command
{
    // ----------------------------------------------------------
    /**
     * Each command can execute a given "command" on a TreeNode.
     *
     * @param node
     *            the given TreeNode to execute commands on
     * @return the TreeNode after it's been executed on
     */
    public abstract TreeNode execute(TreeNode node);


    // ----------------------------------------------------------
    /**
     * Creates a Handle from a given dnaSequence String and returns a new
     *
     * @param dnaSequence
     *            the String of DNA
     * @return a new CharacterArray with the given handle
     * @throws Exception
     *             any potential exceptions
     */
    protected Handle createHandle(String dnaSequence)
        throws Exception
    {

        if (dnaSequence == null)
            throw new Exception();

        byte[] encodedDNA = DNAEncoder.encodeDNA(dnaSequence);
        int handleId = DNATree.memoryManager.insert(encodedDNA);

        return new CharacterArray(handleId);
    }
}
