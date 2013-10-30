import java.util.Queue;

// -------------------------------------------------------------------------
/**
 * The main Tree class.
 *
 * @author Eric Hotinger
 * @author Trevor Senior
 * @version Oct 11, 2012
 */
public class Tree
{
    /**
     * Sets the root of the tree equal to null.
     */
    private TreeNode root = null;


    /**
     * Constructs a new Tree object and sets the root equal to a flyweight.
     */
    public Tree()
    {
        root = EmptyTreeNode.getInstanceOf();
    }


    /**
     * Executes a given queue of commands in first in first out order.
     *
     * @param commands
     *            a queue of commands to execute
     */
    public void executeCommands(Queue<Command> commands)
    {
        if (commands != null && commands.size() >= 1)
            for (Command operation : commands)
                root = operation.execute(root);

    }
}
