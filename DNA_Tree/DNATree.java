// -------------------------------------------------------------------------
/**
 * The DNA Tree which runs the program.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 11, 2012
 */
public class DNATree
{
    /**
     * Global static memory manager that can be accessed by all files.
     */
    public static MemoryManager memoryManager = new MemoryManager(100);


    // -------------------------------------------------------------
    // On my honor:
    //
    // - I have not used source code obtained from another student,
    // or any other unauthorized source, either modified or
    // unmodified.
    //
    // - All source code and documentation used in my program is
    // either my original work, or was derived by me from the
    // source code published in the textbook for this course.
    //
    // - I have not discussed coding details about this project with
    // anyone other than my partner (in the case of a joint
    // submission), instructor, ACM/UPE tutors or the TAs assigned
    // to this course. I understand that I may discuss the concepts
    // of this program with other students, and that another student
    // may help me debug my program so long as neither of us writes
    // anything during the discussion or modifies any computer file
    // during the discussion. I have violated neither the spirit nor
    // letter of this restriction.
    // -------------------------------------------------------------
    /**
     * @author Eric Hotinger
     * @author Trevor Senior
     *
     * Compiled on 10/15/2012 on Windows 7 using JavaSE1.7.
     *
     * Runs the program.
     * @param arg
     *            the filePath
     */
    public static void main(String[] arg)
    {
        String filePath = arg[0];

        Parser parser = new Parser(filePath);

        /**
         * Try to parse the commands given in the filePath & Catch any errors
         * and print them out.
         */
        try { parser.parse(); }
        catch (Exception e) { System.out.println(e.getMessage()); }

        Tree tree = new Tree();

        tree.executeCommands(parser.getCommandList());
    }
}
