import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 * Defines the Search Command, which is an extension of a basic Command. When
 * called, the Search Command will search the nodes and increment the number of
 * nodes visited until it finds the correct dna sequence, or doesn't find one
 * and then reports its results to standard system out.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 11, 2012
 */
public class SearchCommand
    extends Command
{

    private Handle            handle;

    private int               numVisited;

    private Util.searchMode   searchMode;

    private ArrayList<Handle> handlesMatched;


    // ----------------------------------------------------------
    /**
     * Creates a new SearchCommand object.
     *
     * @param sequence
     *            a string of DNA
     * @throws Exception
     *             an exception if a new Handle can't be created from the DNA
     *             string sequence
     */
    public SearchCommand(String sequence)
        throws Exception
    {
        handle = createHandle(sequence);
        searchMode = Util.searchMode.PREFIX;
    }


    // ----------------------------------------------------------
    /**
     * Create a new SearchCommand object.
     *
     * @param sequence
     *            a DNA string sequence
     * @param searchMode
     *            the enum for the searchMode
     * @throws Exception
     *             if a handle can't be created based on the DNA string sequence
     */
    public SearchCommand(String sequence, Util.searchMode searchMode)
        throws Exception
    {
        handle = createHandle(sequence);
        this.searchMode = searchMode;
    }


    // ----------------------------------------------------------
    /**
     * Increases the counter (for the # of TreeNodes visited) by one; can be
     * accessed publically so other TreeNodes can call it for ease of access.
     */
    public void increaseNumVisited()
    {
        this.numVisited++;
    }


    /**
     * Executes the search command on the given TreeNode node.
     *
     * @param node
     *            the TreeNode to search for the Handle
     */
    public TreeNode execute(TreeNode node)
    {
        numVisited = 0;

        handlesMatched = new ArrayList<Handle>();
        node.search(this);

        if (searchMode == Util.searchMode.PREFIX)
            System.out.println(">>> search " + this.getHandle().toString());
        else if (searchMode == Util.searchMode.EXACT)
            System.out.println(">>> search " + this.getHandle().toString()
                + "$");

        printResults();

        System.out.print("\n");

        return node;
    }


    /**
     * Returns the Handle associated with this search command.
     *
     * @return the Handle associated with the search
     */
    public Handle getHandle()
    {
        return handle;
    }


    /**
     * Returns true or false if the search mode is set equal to EXACT mode.
     *
     * @return true or false if the search mode is EXACT.
     */
    public boolean isMatchingExact()
    {
        return (searchMode == Util.searchMode.EXACT);
    }


    /**
     * Reports a matching Handle has been found by adding it to the ArrayList.
     *
     * @param matchingHandle
     *            the matching Handle
     */
    public void reportMatchFound(Handle matchingHandle)
    {
        this.handlesMatched.add(matchingHandle);
    }


    /**
     * Prints the results found from the searching onto the standard output.
     * Formatting is based on the PDF outline provided on the assignments page.
     */
    public void printResults()
    {
        System.out.println("# of nodes visited: " + this.numVisited);

        if (!handlesMatched.isEmpty())
            for (Handle matchedSequence : handlesMatched)
                System.out.println("sequence: " + matchedSequence.toString());

        else
            System.out.println("no sequence found");
    }

}
