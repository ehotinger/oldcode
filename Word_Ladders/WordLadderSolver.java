import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// -------------------------------------------------------------------------
/**
 * Given two different String words of length 5 in the corresponding input.txt
 * file, determines all Word Ladders associated - or none if there aren't any.
 * Please see the README.txt file for more information.
 *
 * @author Eric Hotinger
 * @version Sep 9, 2012
 */
public class WordLadderSolver
{
    /**
     * A place to store the list of words. IE: the dictionary to base results
     * off of.
     */
    private Set<String>                    dictionary;

    /**
     * The start of the Word Ladder. IE: the first line in input.txt
     */
    private String                         start;

    /**
     * The end of the Word Ladder. IE: the second line in input.txt
     */
    private String                         end;

    /**
     * The length of the Word Ladder. It is fixed to 5 for Part I.
     */
    private static final int               LENGTH  = 5;

    /**
     * Results are a linked list of queues. In my case, there will only ever be
     * one queue because I only implemented Part I.
     */
    private LinkedList<Queue<String>>      results =
                                                       new LinkedList<Queue<String>>();

    /**
     * This map keeps track of words and their neighbors.
     */
    private Map<String, ArrayList<String>> wordMap;

    /**
     * This is the String-based output both for the System.out and for the
     * output.txt file.
     */
    private String                         output  = "";


    /**
     * Creates a new WordLadderSolver object.
     */
    public WordLadderSolver()
    {

        /**
         * Load the dictionary into a HashSet.
         */
        try
        {
            this.loadDictionary();
        }

        /**
         * If the dictionary isn't in the Project Folder, shoot out an error
         * message that it's missing.
         */
        catch (FileNotFoundException e)
        {
            System.out
                .println("The dictionary file can't be found. Please place it in the main Project's folder.");
            System.exit(1);
        }

        /**
         * Load the input into the input fields.
         */
        try
        {
            this.loadInput();
        }

        /**
         * If the input isn't in the Project Folder, shoot out an error message
         * that it's missing.
         */
        catch (FileNotFoundException e)
        {
            System.out
                .println("The input file can't be found. Please place it in the main Project's folder.");
            System.exit(1);
        }

        /**
         * Eliminates all of the excess dictionary words that aren't of length
         * 5.
         */
        this.eliminateExcessDictionaryWords();

        /**
         * Creates a new HashMap to store all of the words and their neighbors.
         */
        wordMap = new HashMap<String, ArrayList<String>>();

        /**
         * Puts each word and its associated neighbors in the HashMap.
         */
        for (String word : this.dictionary)
        {
            ArrayList<String> neighbors = new ArrayList<String>();

            for (String word2 : this.dictionary)
            {

                if (isANeighbor(word, word2))
                {
                    neighbors.add(word2);
                }
                wordMap.put(word, neighbors);
            }

        }

        /**
         * If there is a solution to the Word Ladder, we'll print it out.
         */
        if (this.hasLadder())
        {
            for (Queue<String> queue : this.results)
            {
                while (!queue.isEmpty())
                {
                    if (queue.size() > 1)
                    {
                        output += queue.poll() + ", ";
                    }

                    else
                    {
                        output += queue.poll();
                    }
                }
            }

            System.out.println(output);
        }

        /**
         * If there isn't a solution to the Word Ladder, we'll put some default
         * text.
         */
        else
        {
            output = "No word ladder found.";
            System.out.println(output);
        }

    }


    /**
     * Loads the given dictionary.txt file and stores its results in a HashSet
     * for fast look ups.
     *
     * @throws FileNotFoundException
     *             if the dictionary.txt file is missing or unable to be found
     */
    private void loadDictionary()
        throws FileNotFoundException
    {
        this.dictionary = new HashSet<String>();

        Scanner filescan = new Scanner(new File("dictionary.txt"));

        while (filescan.hasNext())
        {
            dictionary.add(filescan.nextLine().toLowerCase());
        }

    }


    /**
     * Loads the given input.txt file and stores its results in the various
     * input fields.
     *
     * @throws FileNotFoundException
     *             if the input.txt file is missing or unable to be found
     */
    private void loadInput()
        throws FileNotFoundException
    {
        Scanner filescan = new Scanner(new File("input.txt"));

        this.start = filescan.nextLine().toString();

        this.end = filescan.nextLine().toString();
    }


    /**
     * Searches for Word Ladders between two given words and a given dictionary.
     *
     * @return true or false if a Word Ladder exists between the two given words
     */
    private boolean hasLadder()
    {
        Queue<String> path = new LinkedList<String>();

        /**
         * If the dictionary doesn't contain the start or end words, it's
         * impossible to have a Word Ladder.
         */
        if (!dictionary.contains(start) || !dictionary.contains(end))
        {
            return false;
        }

        /**
         * If the start and end are equal to each other, then the Word Ladder
         * will immediately output "No Ladder Found."
         */
        else if (start.equals(end) && end.equals(start))
        {
            return false;
        }

        /**
         * Otherwise, we're going to make a map of the words and their neighbors
         * (words differing by 1 character), and then find the shortest possible
         * wordLadder.
         */
        else
        {
            path.add(start);

            Map<String, String> endMap = new HashMap<String, String>();

            Queue<String> backwardsLadder = new LinkedList<String>();

            /**
             * Add the start to the endMap.
             */
            endMap.put(start, null);

            while (path.size() != 0)
            {
                String currentWord = path.remove();

                /**
                 * Loop through all the potential words.
                 */
                for (String word : wordMap.get(currentWord))
                {
                    /**
                     * If the current word equals the final word, add it to the
                     * endMap and then spit out the final results of the word
                     * ladder.
                     */
                    if (word.equals(end))
                    {
                        path.add(end);
                        endMap.put(end, currentWord);

                        /**
                         * Begin creating the output (from the end to the
                         * start).
                         */
                        while (endMap.get(end) != null)
                        {
                            backwardsLadder.add(end);
                            end = endMap.get(end);
                        }
                        backwardsLadder.add(start);

                        this.results.add(reverseQueue(backwardsLadder));
                        return true;
                    }

                    if (!endMap.keySet().contains(word))
                    {
                        path.add(word);
                        endMap.put(word, currentWord);
                    }
                }
            }
        }

        /**
         * Otherwise, the ladder doesn't exist.
         */
        return false;
    }


    /**
     * Given a queue, reverse the contents of the Queue.
     *
     * @param queue
     *            the queue to reverse
     * @return a reversed-order queue
     */
    private Queue<String> reverseQueue(Queue<String> backwardsLadder)
    {
        Queue<String> forwardsLadder = new LinkedList<String>();

        /**
         * Have to reverse our output here (for start-to-end form).
         */
        Stack<String> myStack = new Stack<String>();

        while (!backwardsLadder.isEmpty())
        {
            myStack.add(backwardsLadder.poll());
        }

        while (!myStack.isEmpty())
        {
            forwardsLadder.add(myStack.pop());
        }

        return forwardsLadder;
    }


    /**
     * Determines if a word is a neighbor to another word. A "neighbor" is a
     * word that differs by one character (letter).
     *
     * @param word
     *            the first word
     * @param word2
     *            the second word
     */
    private boolean isANeighbor(String word, String word2)
    {
        int charDifference = 0;

        for (int i = 0; i < word.length(); i++)
        {
            if (word.charAt(i) != word2.charAt(i))
            {
                charDifference++;
            }
        }

        return (charDifference == 1);
    }


    /**
     * Eliminates all excess dictionary words from the list. For example, if the
     * searched length is 5, it will remove all words that aren't length 5 from
     * the dictionary.
     */
    private void eliminateExcessDictionaryWords()
    {
        Set<String> newDictionary = new HashSet<String>();

        for (String word : this.dictionary)
        {
            if (word.length() == LENGTH)
            {
                newDictionary.add(word);
            }
        }

        this.dictionary = newDictionary;
    }


    /**
     * Exports the results found from the hasLadder() function into an output
     * file, "part1.txt"
     */
    private void exportResults()
    {
        try
        {
            FileWriter fstream = new FileWriter("part1.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(output);
            out.close();
        }

        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // ----------------------------------------------------------
    /**
     * Reads a given input file, input.txt, and attempts to find various
     * information based on what is provided in the input.txt file.
     *
     * @param args
     *            A list of String arguments
     */
    public static void main(String[] args)
    {
        WordLadderSolver mySolver = new WordLadderSolver();

        mySolver.exportResults();
    }

}
