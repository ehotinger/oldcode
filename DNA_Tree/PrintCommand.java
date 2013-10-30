// -------------------------------------------------------------------------
/**
 * Defines the Print Command, which is an extension of a basic command. When
 * called, the PrintCommand will print the representation of a TreeNode.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 12, 2012
 */
public class PrintCommand
    extends Command
{
    private Util.printMode printMode;

    private String         freeBlockPrintout;


    // ----------------------------------------------------------
    /**
     * Creates a new PrintCommand object with a default print mode.
     */
    public PrintCommand()
    {
        printMode = Util.printMode.DEFAULT;
        freeBlockPrintout = DNATree.memoryManager.toString();
    }


    // ----------------------------------------------------------
    /**
     * Creates a new PrintCommand object with a given print mode.
     *
     * @param printMode
     *            the given print mode
     */
    public PrintCommand(Util.printMode printMode)
    {
        this.printMode = printMode;

        freeBlockPrintout = DNATree.memoryManager.toString();

    }


    /**
     * Executes the print command on a given TreeNode.
     *
     * @param node
     *            the TreeNode to print from.
     */
    public TreeNode execute(TreeNode node)
    {

        if (printMode == Util.printMode.DEFAULT)
            System.out.println(">>> print");
        else if (printMode == Util.printMode.LENGTH)
            System.out.println(">>> print lengths");
        else if (printMode == Util.printMode.STATS)
            System.out.println(">>> print stats");

        node.print(0, printMode);
        System.out.println(freeBlockPrintout);

        return node;
    }


    // ----------------------------------------------------------
    /**
     * Places an indented print according to the depth and string representation
     * of the Tree Node.
     *
     * @param depth
     *            the depth of the Tree
     * @param treeNodeRepresentation
     *            the string representation of the TreeNode
     */
    protected static void indentedTernaryPrintGod(
        int depth,
        String treeNodeRepresentation)
    {
        String indent = "";

        while ((indent = (indent.length() < depth) ? (" " + indent) : indent)
            .length() < depth)
            ;

        System.out.println(indent + treeNodeRepresentation);
    }


    // ----------------------------------------------------------
    /**
     * Prints the representation of an empty Tree Node based on its depth in the
     * Tree.
     *
     * @param depth
     *            the depth of the tree
     */
    public static void printEmptyTreeNode(int depth)
    {
        indentedTernaryPrintGod(depth, "E");
    }


    // ----------------------------------------------------------
    /**
     * Prints the representation of an internal Tree Node based on its depth in
     * the Tree.
     *
     * @param depth
     *            the depth of the Tree
     */
    public static void printInternalTreeNode(int depth)
    {
        indentedTernaryPrintGod(depth, "I");
    }


    // ----------------------------------------------------------
    /**
     * Prints the representation of a Handle Leaf Node based on its depth in the
     * tree and its DNA sequence.
     *
     * @param depth
     *            the depth of the Tree
     * @param sequence
     *            the DNA sequence
     */
    public static void printHandleLeafNode(int depth, String sequence)
    {
        indentedTernaryPrintGod(depth, sequence);
    }

}
