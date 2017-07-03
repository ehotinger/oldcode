using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameSolver
{
    public class Solver
    {
        /// <summary>
        /// Stores a list of the letter occurences for a-z where a = letterOccurences[0] and z = letterOccurences[25]
        /// </summary>
        private static int[] letterOccurrences = new int[26];

        /// <summary>
        /// The main driver for the program.
        /// </summary>
        /// <param name="args">unused, everything goes through STDIN</param>
        public static void Main(string[] args)
        {
            var words = new HashSet<string>();

            Console.WriteLine("Input the word lengths needed to be solved for with spaces in between.");
            Console.WriteLine("Example: 8 4 4");

            var boardLetters = Console.ReadLine();
            if (boardLetters != null)
            {
                var lengths = boardLetters.Split(' ').Select(int.Parse).ToList();

                Console.WriteLine("Input all letters as a single string representing the square board.");
                Console.WriteLine("Example: bata");

                var possibleLetters = boardLetters;

                var boardLength = (int)Math.Sqrt(possibleLetters.Length);
                var WordBoard = MakeBoard(boardLength, possibleLetters);

                // Read in the dictionary and eliminate words that don't map
                using (var sr = new StreamReader("dictionary.txt"))
                {
                    while (!sr.EndOfStream)
                    {
                        var readLine = sr.ReadLine();
                        if (readLine != null)
                        {
                            var currLine = readLine.ToLower();

                            // If we arent looking exactly for this length, skip it.
                            if (!lengths.Contains(currLine.Length))
                                continue;

                            // Eliminate words that don't map to the occurences of letters we have.
                            if (IsPossibleFromLettersAndOccurrences(lengths, letterOccurrences, currLine))
                                words.Add(currLine);
                        }
                    }
                }

                // Print out the grid of possible solutions.
                Console.WriteLine("These could be words:");
                var output = words.OrderBy(m => m.Length).ThenBy(m => m).ToList();
                foreach (var word in output.Where(word => IsPossibleFromBoard(WordBoard, word)))
                {
                    Console.WriteLine(word);
                }

                while (true)
                {
                    // We found no words, end.
                    if (words.Count == 0)
                    {
                        Console.WriteLine("We found no words remaining.");
                        break;
                    }

                    Console.WriteLine("------------------------");
                    Console.WriteLine("What word worked? Type q to exit.");

                    string foundWord = boardLetters;

                    if (foundWord == "q")
                        break;

                    // Update the length counter.
                    lengths.Remove(foundWord.Length);

                    Console.WriteLine("Enter the new board state... Use 0s to fill in empty spaces. E.g, 0fl00y000");
                    var newBoardLetters = boardLetters;

                    var newBoard = MakeBoard(boardLength, newBoardLetters);

                    // Update the possible word list based on the letters.
                    foreach (var word in words.ToList().Where(word => !IsPossibleFromLettersAndOccurrences(lengths, letterOccurrences, word)))
                    {
                        words.Remove(word);
                    }

                    // Print out remaining words.
                    Console.WriteLine("After eliminating, we found these remaining:");
                    foreach (var word in words.OrderBy(m => m.Length).ThenBy(m => m).ToList().Where(word => IsPossibleFromBoard(newBoard, word)))
                    {
                        Console.WriteLine(word);
                    }
                }
            }

            Console.WriteLine("Press any key to exit.");
            Console.ReadLine();
        }

        /// <summary>
        /// Makes a square board at the specified length and keeps track of the possible letters.
        /// </summary>
        /// <param name="boardLength">The board's height and width</param>
        /// <param name="possibleLetters">A word which represents all the possible letters</param>
        /// <returns>A board with the possible letters at each position</returns>
        public static string[,] MakeBoard(int boardLength, string possibleLetters)
        {
            const double epsilon = 0.001;

            if (Math.Abs(Math.Pow(boardLength, 2) - possibleLetters.Length) > epsilon)
                throw new ArgumentException("The possible letters must match 1:1 to the board's length squared.");

            var board = new string[boardLength, boardLength];
            letterOccurrences = new int[26]; // Wipe out the letter occurrences.

            // Enter the word board's initial state based on the provided information.
            var currColumn = 0;
            var currRow = 0;
            foreach (var letter in possibleLetters)
            {
                board[currRow, currColumn] = letter.ToString();

                // Determine where to store the letter in the letter occurences
                var index = letter - 'a';

                if (index > -1 && index < 26)
                    letterOccurrences[index]++;

                currColumn++;

                // Keep going through each column until we're ready to skip to the next row
                if (currColumn % boardLength != 0)
                    continue;

                // Reset the column to 0 and increment the row
                currColumn = 0;
                currRow++;
            }

            return board;
        }

        /// <summary>
        /// Determine whether or not the word is possible based on the remaining word lengths
        /// we have and the remaining letter occurrences
        /// </summary>
        /// <param name="wordLengths">The remaining word lengths we're searching for</param>
        /// <param name="letterOccurrenceCounts">An array of letter occurences</param>
        /// <param name="word">The word we're searching for</param>
        /// <returns>true or false if the specified word is possible</returns>
        public static bool IsPossibleFromLettersAndOccurrences(List<int> wordLengths, int[] letterOccurrenceCounts, string word)
        {
            // If the word isn't in the valid lengths, it isn't possible.
            if (!wordLengths.Contains(word.Length))
                return false;

            // Make a clone so that we don't overwrite the original array.
            var clone = new int[26];
            Array.Copy(letterOccurrenceCounts, clone, 26);

            // Check to see if we can make the word based on the given letters from the board.
            foreach (var idx in word.Select(c => (int)c - 97))
            {
                clone[idx]--;

                if (clone[idx] < 0)
                    return false;
            }

            return true;
        }

        /// <summary>
        /// Determine whether or not the specified word can be made on the board.
        /// </summary>
        /// <param name="board">A board to search</param>
        /// <param name="word">A word to find in the board</param>
        /// <returns>true or false if it's possible to make the board.</returns>
        public static bool IsPossibleFromBoard(string[,] board, string word)
        {
            // Scan the entire board for points where the word could begin and for each point
            // determine if starting there could build the word
            var firstChar = word[0].ToString();
            for (var i = 0; i < board.GetLength(0); i++)
            {
                for (var j = 0; j < board.GetLength(0); j++)
                {
                    if (board[j, i] == firstChar)
                    {
                        if (DFSWord(board, j, i, word))
                            return true;
                    }
                }
            }

            return false;
        }

        /// <summary>
        /// Perform a DFS on the board starting at the specified row/col.
        /// </summary>
        /// <param name="board">The board to search</param>
        /// <param name="row">The starting row</param>
        /// <param name="col">The starting column</param>
        /// <param name="word">The word to search for</param>
        /// <returns>true if the word is found, false otherwise</returns>
        private static bool DFSWord(string[,] board, int row, int col, string word)
        {
            var stack = new Stack<Tuple<int, int, int>>();
            var alreadySearched = new HashSet<Tuple<int, int, int>>();

            var currPoint = new Tuple<int, int, int>(row, col, 0);

            stack.Push(currPoint);

            while (stack.Count != 0)
            {
                currPoint = stack.Pop();
                var index = currPoint.Item3;

                // Make it so that the distance to reuse the character is quite high
                // this is a bit of fuzzy searching
                var searched = alreadySearched.Any(pt => currPoint.Item1 == pt.Item1 && currPoint.Item2 == pt.Item2
                    && Math.Abs(currPoint.Item3 - pt.Item3) <= 5);

                // Skip if we searched this point already
                if (searched)
                    continue;

                if (index == word.Length - 1)
                    return true;

                alreadySearched.Add(currPoint);

                var nextLetter = word[index + 1].ToString();

                var tuples = NearbyLetters(board, currPoint, nextLetter);

                foreach (var tuple in tuples)
                    stack.Push(tuple);
            }

            return false;
        }

        /// <summary>
        /// Fetches a list of nearby letters from the board for the specified point and the letter we're looking for.
        /// </summary>
        /// <param name="board">The board to search</param>
        /// <param name="currPoint">The current point / letter we're on</param>
        /// <param name="nextLetter">The letter we're looking for</param>
        /// <returns>A list of points (letters) from the board based on our search criteria</returns>
        private static IEnumerable<Tuple<int, int, int>> NearbyLetters(string[,] board, Tuple<int, int, int> currPoint, string nextLetter)
        {
            var nearbyLetters = new List<Tuple<int, int, int>>();
            var boardLength = board.GetLength(0);

            var row = currPoint.Item1;
            var col = currPoint.Item2;

            for (var i = -1; i <= 1; i++)
            {
                for (var j = -1; j <= 1; j++)
                {
                    var calcRow = i + row;
                    var calcCol = j + col;

                    // the position of the actual letter, skip it
                    if (i == 0 && j == 0)
                        continue;

                    if (calcRow >= 0 && calcCol >= 0 && calcRow < boardLength && calcCol < boardLength)
                    {
                        // Make the index of the letter to be + 1 from the previous so we can track its relative position in the word
                        if (board[calcRow, calcCol] == nextLetter)
                            nearbyLetters.Add(new Tuple<int, int, int>(calcRow, calcCol, currPoint.Item3 + 1));
                    }
                }
            }

            return nearbyLetters;
        }
    }
}
