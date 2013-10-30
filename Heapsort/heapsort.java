// -------------------------------------------------------------------------
/**
 * Runs a heapsort on the <data-file-name> with <numb-buffers> and outputs
 * statistics to <stat-file-name>.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 6, 2012
 */
public class heapsort
{
    /**
     * The error message to print out if incorrect arguments are used in the
     * main function.
     */
    static final String USAGE_ERROR_MESSAGE =
                                                "Incorrect arguments used.\nUsage: heapsort <data-file-name> <numb-buffers> <stat-file-name>";


    // ----------------------------------------------------------
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
    // ----------------------------------------------------------
    /**
     * Based on the given arguments, sorts the specified input file with the
     * specified number of buffers, and outputs statistics to the specified
     * output file. Compiled using JAVA SE 1.7 on Windows 7 64 bit on 11/6/2012.
     *
     * @param args
     *            arguments
     */

    public static void main(String[] args)
    {
        if (args.length != 3)
            System.out.println(USAGE_ERROR_MESSAGE);

        else
        {
            final String inputFile = args[0];
            final String outputFile = args[2];

            final int bufferCount = Integer.parseInt(args[1]);

            if (bufferCount > 20 || bufferCount < 1)
            {
                System.out.println("<numb-buffers> must be between 1 and 20");
                System.exit(0);
            }

            RecordArray array = new RecordArray(bufferCount, inputFile);

            MaxHeap heap = new MaxHeap(array);

            StatisticsGenerator statGen = new StatisticsGenerator(array);

            StatisticsGenerator.setInputFileName(inputFile);

            // ----------------------------------------------------------
            // BEGIN SORTING
            // ----------------------------------------------------------
            StatisticsGenerator.setStartTime(System.currentTimeMillis());
            heap.sort();
            array.flush();
            // ----------------------------------------------------------
            // END SORTING
            // ----------------------------------------------------------
            OutputFileGenerator.writeToFile(outputFile);

            System.out.println(statGen.getKeysAndValues());
        }

    }

}
