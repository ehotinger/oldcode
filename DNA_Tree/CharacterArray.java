import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.NoSuchElementException;

// -------------------------------------------------------------------------
/**
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 11, 2012
 */
public class CharacterArray
    implements Handle
{
    private int    currentPosition = 0;
    private char[] characters;
    private int    handleId;


    /**
     * Constructs a CharactcerArray and associates it with an integer handle so
     * that we can pull its related DNA sequence from the Memory Manager.
     *
     * @param handleId
     *            a unique handle identifier
     */
    public CharacterArray(int handleId)
    {
        this.handleId = handleId;

        byte[] encodedDNA = DNATree.memoryManager.get(handleId);
        String decodedDNA = DNAEncoder.decodeDNA(encodedDNA);

        characters = decodedDNA.toCharArray();
    }


    /**
     * Returns true if the current position in the character array is less than
     * it's length. E.g., it has more characters remaining in it.
     *
     * @return true or false depending on if there are more characters remaining
     */
    public boolean hasNextCharacter()
    {
        return (currentPosition < characters.length);
    }


    /**
     * Returns the character at the current position - currentPosition.
     *
     * @return char the character at the current position - currentPosition
     * @throws NoSuchElementException
     *             if there isn't a previous character
     */
    public char getCurrentCharacter()
        throws NoSuchElementException
    {
        if (!hasNextCharacter())
            throw new NoSuchElementException("No current character");

        return characters[currentPosition];
    }


    /**
     * Returns the previous character (currentPosition - 1) in the character
     * array.
     *
     * @return the previous character in the character array
     * @throws NoSuchElementException
     *             if there isn't a previous character
     */
    public char getPreviousCharacter()
        throws NoSuchElementException
    {
        if (!hasPreviousCharacter())
            throw new NoSuchElementException("No previous character");

        currentPosition--;

        return characters[currentPosition];
    }


    /**
     * Returns true if the current position isn't the starting position in the
     * character array.
     *
     * @return true or false if the current position is/isn't the starting
     *         position.
     */
    public boolean hasPreviousCharacter()
    {
        return currentPosition > 0;
    }


    /**
     * Converts the character array into a String so that we can see it on
     * screen
     *
     * @return the string representation of the character array
     */
    public String toString()
    {
        return new String(characters);
    }


    /**
     * Returns the next character (currentPosition + 1) in the character array.
     *
     * @return the next character in the character array
     */
    public char getNextCharacter()
    {
        if (!hasNextCharacter())
            throw new NoSuchElementException("No next character");

        return characters[currentPosition++];
    }


    /**
     * Determines whether or not two CharacterArray objects are equal.
     *
     * @param obj
     *            the character array to compare to this one
     * @return true or false if the objects are equal
     */
    public boolean equals(Object obj)
    {
        String representation = this.toString();
        String object = ((Handle)obj).toString();

        return (obj instanceof Handle) ? representation.equals(object) : super
            .equals(obj);
    }


    /**
     * Returns the length of the Character Array.
     *
     * @return the length of the character array
     */
    public int getLength()
    {
        return characters.length;
    }


    /**
     * Changes the current position of the Character Array.
     *
     * @param newPosition
     *            the new position of the character array
     */
    public void setPosition(int newPosition)
    {
        this.currentPosition = newPosition;
    }


    /**
     * Returns the DNA sequence string of the character array.
     *
     * @return the dna sequence string of the character array
     */
    public String getSequence()
    {
        return characters.toString();
    }


    /**
     * Returns the currentPosition of the Character Array.
     *
     * @return the current position of the character array
     */
    public int getPosition()
    {
        return currentPosition;
    }


    /**
     * Returns the array of characters.
     *
     * @return the array of characters
     */
    public char[] getCharacterArray()
    {
        return characters;
    }


    /**
     * Determines whether this character array is a prefix of a given Handle.
     *
     * @param handle
     *            a given Handle
     */
    public boolean isPrefixOf(Handle handle)
    {
        return handle.toString().startsWith(new String(characters));
    }


    /**
     * Returns the unique handle identifier of this character array.
     *
     * @return the unique handle identifier of this character array
     */
    public int getHandleId()
    {
        return this.handleId;
    }


    /**
     * Given the character array, returns the averages of all the letters inside
     * of it based on the dna alphabet.
     *
     * @return the frequencies in string format
     */
    public String getFrequencies()
    {

        char[] dnaAlphabet = { 'A', 'C', 'G', 'T' };
        float[] averages = new float[dnaAlphabet.length];
        String[] formatted = new String[dnaAlphabet.length];

        NumberFormat numberFormatter = new DecimalFormat("0.00");

        int totalCharacters = this.characters.length;

        for (char character : this.characters)
        {
            for (int i = 0; i < dnaAlphabet.length; i++)
            {
                if (character == dnaAlphabet[i])
                {
                    averages[i] += 1;
                    break;
                }
            }
        }

        for (int i = 0; i < averages.length; i++)
        {
            averages[i] = (averages[i] / totalCharacters) * 100;
            formatted[i] =
                dnaAlphabet[i] + "(" + numberFormatter.format(averages[i])
                    + ")";
        }

        return Util.join(formatted, ", ");
    }

}
