Scenario: 
---------
You are to write a lisp program that a robot can use to get from point A to point B in a 
deterministic (non backtracking) way.

Specifications: 
---------------
Your program, navigate, will have two input parameters: 
	(1) a destination represented as an integer, and 
	(2) a "map" that represents all paths the robot can follow. 

The "map" is in the form of a binary search tree. Each node in the tree is of the form 
(position (left subtree) (right subtree)). 

Because the paths are represented as a BINARY SEARCH TREE, comparing the given destination 
to the "current" position will enable you to determine whether you want to turn right (dest < curr 
pos) or left (dest > curr pos) to get to your destination, if it exists. 

Deliverables
------------
Your program will print out 
(1) (not found: #), if the destination "#" does not exist, 
(2) (found: #), if the destination is the root node (# is root node number), 
(3) (found: # T # T ... # T #), if your destination is in the tree but not the root (# is a position 
number and T is either L or R indicating that you took a Left or Right turn at position #.

Example
-------
Suppose, for example, the set of possible paths looked like the following:

								  ROBOT 
									V 
					Right Turn 				Left Turn 
									9 
				 +------------------^------------------+ 
				 | 									   | 
				 7 									  26 
		  +------^------+ 					   +-------^-------+ 
		  | 			| 					   | 			   | 
		  () 			8 					  20 			   31 
				  +-----^------+           +---^----+ 		+---^---+ 
				  |            | 		   | 		| 		| 		| 
				 () 		   () 		  17 		() 		() 		() 
			  +---^---+ 
			  |       | 
			  ()     () 

The corresponding Sexpression given to navigate as the path argument would be: 
 (9 (7 () (8 () ())) 
 (26 (20 (17 () ()) 
 ()) 
 (31 () ()))) 

(navigate 17 "path") 
would then return "(found: 9 L 26 R 20 R 17)"

indicating that the robot must travel to position 9, turn right, go to position 26, turn left, go to 
position 20, turn left, and then go to position 17, its destination. 

BEWARE!!! Traveling down the LEFT subtree implies a RIGHT turn, and vice-versa.

(navigate 9 "path") 
would return "(found: 9)" 
indicating that the robot could proceed directly to position 9 and that it is actually the root of the binary search tree. 

(navigate 30 "path") 
would return "(not found: 30)" 
indicating that the given destination could not be found.

Explanation of Implementation
-----------------------------
- "path" is the S-Expression which we'll use as our path to 
navigate through (defined at the top)

- "found_list" is a simple list which stores visited nodes
as we traverse the path.

- "appendList" is an implementation of the append function;
all it does is append a list to another list.

- "search" does all the real work; using recursion it traverses
the path and checks the node it's on, which falls into 4 cases:

	1. the node is NULL -- meaning the value isn't in the BST.

	2. the node is < the target value -- meaning we go down the left subtree (robot turns "right")

	3. the node is > the target value -- meaning we go down the right subtree (robot turns "left")

	4. the node is = the target value -- meaning we've found the target value and the robot ends walking

- "navigate" just calls "search" with the "found_list" as an extra parameter so we can record the nodes
we find in a list.  The only reason I have this function is because in Dr. Arthur's original PDF,
he gives us the examples of:
(navigate 17 "path") 
(navigate 9 "path") 
(navigate 30 "path") 

	... by limiting our "navigate" function to two parameters like this and using the helper function, we can directly match with what we were given -- that's all!
