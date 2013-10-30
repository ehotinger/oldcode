import java.util.Locale;
import java.util.Formatter;

// -------------------------------------------------------------------------
/**
 * Maintains the various statistics as the program runs and generates both the
 * System.out information and file output information.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 4, 2012
 */
public class StatisticsGenerator
{
    /**
     * The starting time of the heap sort.
     */
    public static long   startTime   = 0;

    /**
     * The input file's string name.
     */
    public static String inputFileName;

    /**
     * The number of disk reads.
     */
    public static int    diskReads   = 0;

    /**
     * The number of disk writes.
     */
    public static int    diskWrites  = 0;

    /**
     * The number of cache misses.
     */
    public static int    cacheMisses = 0;

    /**
     * The number of cache hits.
     */
    public static int    cacheHits   = 0;

    /**
     * The number of records, fixed at 1024.
     */
    private final int    numRecs     = 1024;

    /**
     * An associated RecordArray.
     */
    private RecordArray  recordArray;


    // ----------------------------------------------------------
    /**
     * Create a new StatisticsGenerator object with a given RecordArray.
     *
     * @param array
     *            a record array
     */
    public StatisticsGenerator(RecordArray array)
    {
        this.recordArray = array;
    }


    // ----------------------------------------------------------
    /**
     * Returns a string representation of the keys and values found in the input
     * file for System.out.
     *
     * @return string representation of the keys and values found in the input
     *         file
     */
    public String getKeysAndValues()
    {
        String ret = "";

        for (int i = 0; i < (this.recordArray.size() / numRecs); i++)
        {
            int index = i * numRecs;

            Formatter formatter = new Formatter(Locale.US);

            /**
             * %5d ensures proper spacing since keys and values are in the range
             * 0 to 30,000.
             */
            formatter.format(
                "%5d %5d ",
                this.recordArray.getKey(index),
                this.recordArray.getValue(index));

            ret += formatter.toString();

            /**
             * If there are more than 8 keys/values, create a new line to put
             * the output on.
             */
            if ((i + 1) % 8 == 0)
                ret += "\n";
        }

        return ret;
    }


    // ----------------------------------------------------------
    /**
     * Formats the various statistics and returns a string representation of
     * them all. Used in the output file.
     *
     * @return formatted string representation of the statistics
     */
    public static String getFormattedStatistics()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("------Statistics------\n");
        sb.append("Input file name: " + inputFileName + "\n");
        sb.append("Cache hits: " + cacheHits + "\n");
        sb.append("Cache misses: " + cacheMisses + "\n");
        sb.append("Disk reads: " + diskReads + "\n");
        sb.append("Disk writes: " + diskWrites + "\n");
        sb.append("Sort execution time: "
            + (System.currentTimeMillis() - startTime) + "ms" + "\n\n");

        return sb.toString();
    }


    // ----------------------------------------------------------
    /**
     * Returns the start time.
     *
     * @return the start time
     */
    public static long getStartTime()
    {
        return startTime;
    }


    // ----------------------------------------------------------
    /**
     * Changes the start time.
     *
     * @param startTime
     *            the new start time
     */
    public static void setStartTime(long startTime)
    {
        StatisticsGenerator.startTime = startTime;
    }


    // ----------------------------------------------------------
    /**
     * Returns the name of the input file.
     *
     * @return the data file name
     */
    public static String getInputFileName()
    {
        return inputFileName;
    }


    // ----------------------------------------------------------
    /**
     * Changes the name of the input data file.
     *
     * @param dataFileName
     *            the new name for the data file name
     */
    public static void setInputFileName(String dataFileName)
    {
        StatisticsGenerator.inputFileName = dataFileName;
    }
}
