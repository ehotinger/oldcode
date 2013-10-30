// -------------------------------------------------------------------------
/**
 * Interface class that describes how CharacterArray should correctly function.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 12, 2012
 */
public interface Handle
{

    // ----------------------------------------------------------
    /**
     * Returns the next character (currentPosition + 1).
     *
     * @return the next character in the character array
     */
    public char getNextCharacter();


    // ----------------------------------------------------------
    /**
     * Returns the previous character (currentPosition - 1)
     *
     * @return the previous character
     */
    public char getPreviousCharacter();


    // ----------------------------------------------------------
    /**
     * Returns the character at the current position - currentPosition.
     *
     * @return char the character at the current position - currentPosition
     */
    public char getCurrentCharacter();


    // ----------------------------------------------------------
    /**
     * Returns true if the current position isn't the starting position
     *
     * @return true or false if the current position is/isn't the starting
     *         position.
     */
    public boolean hasPreviousCharacter();


    // ----------------------------------------------------------
    /**
     * Returns true if the current position is less than it's length. E.g., it
     * has more characters remaining in it.
     *
     * @return true or false depending on if there are more characters remaining
     */
    public boolean hasNextCharacter();


    // ----------------------------------------------------------
    /**
     * Returns the unique handle identifier
     *
     * @return the unique handle identifier
     */
    public int getHandleId();


    // ----------------------------------------------------------
    /**
     * Returns the DNA sequence string
     *
     * @return the dna sequence string
     */
    public String getSequence();


    // ----------------------------------------------------------
    /**
     * Returns the length
     *
     * @return the length
     */
    public int getLength();


    // ----------------------------------------------------------
    /**
     * Determines whether this character array is a prefix of a given Handle.
     *
     * @param handle
     *            a given Handle
     * @return true or false depending on whether or not the handle is a prefix
     */
    public boolean isPrefixOf(Handle handle);


    // ----------------------------------------------------------
    /**
     * Given the character array, returns the averages of all the letters inside
     * of it based on the dna alphabet.
     *
     * @return the frequencies in string format
     */
    public String getFrequencies();
}
