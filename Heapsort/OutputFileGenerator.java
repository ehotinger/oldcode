import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// -------------------------------------------------------------------------
/**
 * Given a String name, creates a new file and outputs the heap sort statistics
 * to it.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Nov 4, 2012
 */
public class OutputFileGenerator
{

    // ----------------------------------------------------------
    /**
     * Writes the statistics from StatisticsGenerator to a given file name.
     *
     * @param outputFileName
     *            the name of the output file
     */
    static void writeToFile(String outputFileName)
    {
        File outputFile = new File(outputFileName);

        try
        {
            outputFile.createNewFile();

            BufferedWriter statisticsOutputFile =
                new BufferedWriter(new FileWriter(outputFile, true));

            statisticsOutputFile.write(StatisticsGenerator
                .getFormattedStatistics());

            statisticsOutputFile.flush();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
