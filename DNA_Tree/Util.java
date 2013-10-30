import java.lang.StringBuilder;

// -------------------------------------------------------------------------
/**
 * A basic util class containing methods that can be used to format strings and
 * other various operations!
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 12, 2012
 */
public class Util
{
    // -------------------------------------------------------------------------
    /**
     * Enums to be used when searching. Two different modes: exact and prefix.
     *
     * @author Eric Hotinger
     * @author Trevor Senior
     * @version Oct 12, 2012
     */
    public enum searchMode
    {
        /**
         * The Exact search mode.
         */
        EXACT,

        /**
         * The Prefix search mode.
         */
        PREFIX
    };


    // -------------------------------------------------------------------------
    /**
     * Enums to be used when printing. Three different modes: length, stats, and
     * default (normal).
     *
     * @author Eric Hotinger
     * @author Trevor Senior
     * @version Oct 12, 2012
     */
    public enum printMode
    {
        /**
         * The default print mode.
         */
        DEFAULT,
        /**
         * Print stats mode.
         */
        STATS,
        /**
         * Print length mode.
         */
        LENGTH
    };


    // ----------------------------------------------------------
    /**
     * Joins an array of strings with a specified joiner. Similar to PHP's join
     * function.
     *
     * @param arrayString
     *            a given array string
     * @param joiner
     *            a given joiner string
     * @return the joined string
     */
    public static String join(String[] arrayString, String joiner)
    {
        if (arrayString == null || arrayString.length == 0)
            return null;

        /**
         * Create a new String Builder object to append string to
         */
        StringBuilder sb = new StringBuilder();
        sb.append(arrayString[0]); // Add the first element
        for (int i = 1; i < arrayString.length; i++)
        {
            sb.append(joiner); // Append what we're joining with
            sb.append(arrayString[i]); // Append the actual String
        }

        return sb.toString();
    }
}
