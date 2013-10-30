import java.util.Scanner;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

// -------------------------------------------------------------------------
/**
 * Parses commands from an input file.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 11, 2012
 */
public class Parser
{

    private String         filePath;
    private Queue<Command> commands;


    // ----------------------------------------------------------
    /**
     * Create a new Parser object based on a given file path.
     *
     * @param filePath
     *            the file's path
     */
    public Parser(String filePath)
    {
        this.filePath = filePath;
    }


    // ----------------------------------------------------------
    /**
     * Returns the next token in the stringtokenizer.
     *
     * @return the next String in the tokenizer
     */
    private String getNext(StringTokenizer tokenizer)
    {
        return (tokenizer.hasMoreTokens()) ? tokenizer.nextToken() : null;
    }


    // ----------------------------------------------------------
    /**
     * Processes the command file given through the arguments in the DNATree. If
     * the command can be successfully executed (IE: it's in a correct format,
     * then it will run the respective methods). If it can't, a default error
     * message gets outputted.
     *
     * @throws Exception
     *             possibly exceptions -- such as file not opening
     */
    public void parse()
        throws Exception
    {
        commands = new LinkedList<Command>();

        Scanner fileScanner = new Scanner(new File(this.filePath));

        String line, argument;

        while (fileScanner.hasNextLine())
        {
            line = fileScanner.nextLine();

            // http://stackoverflow.com/questions/11600156/scanner-through-a-line-with-whitespace-and-comma/11602336#11602336
            StringTokenizer tokenizer = new StringTokenizer(line);

            if (tokenizer.hasMoreTokens())
            {
                String command = tokenizer.nextToken();

                switch (command)
                {
                    case "insert":
                        argument = getNext(tokenizer);
                        commands.add(new InsertCommand(argument));
                        break;

                    case "remove":
                        argument = getNext(tokenizer);
                        commands.add(new RemoveCommand(argument));
                        break;

                    case "print":
                        argument = getNext(tokenizer);
                        argument = (argument == null) ? "" : argument;

                        switch (argument)
                        {
                            case "":
                                commands.add(new PrintCommand());
                                break;
                            case "lengths":
                                commands.add(new PrintCommand(
                                    Util.printMode.LENGTH));
                                break;
                            case "stats":
                                commands.add(new PrintCommand(
                                    Util.printMode.STATS));
                                break;
                            default:
                                throw new Exception(
                                    "[PRINT ERROR] Unable to print " + argument);
                        }

                    case "search":
                        argument = getNext(tokenizer);
                        if (argument != null)
                        {
                            if (argument.endsWith("$"))
                            {
                                commands
                                    .add(new SearchCommand(
                                        argument.substring(
                                            0,
                                            argument.length() - 1),
                                        Util.searchMode.EXACT));
                            }
                            else
                            {
                                commands.add(new SearchCommand(argument));
                            }

                        }
                        break;

                    default:
                        throw new Exception("COMMAND UNKNOWN: " + command);
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Returns the command list used in the Parser
     *
     * @return the command list queue
     */
    public Queue<Command> getCommandList()
    {
        return commands;
    }

}
