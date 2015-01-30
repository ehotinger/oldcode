using System;
using System.Diagnostics;
using System.Linq;

namespace RDACompetition
{
    public class HotingerSolution
    {
        private static readonly string KEY = "RDA";

        public static void Main(string[] args)
        {
            string input = Console.ReadLine().Trim();

            //Stopwatch time = Stopwatch.StartNew();
            Console.WriteLine(Encrypt(input));
            //Console.WriteLine(time.ElapsedMilliseconds);
            //time.Stop();
            // 3130 ms for 15.2k characters.

            // Press Key To End...
            Console.ReadLine();
        }

        public static string Encrypt(string input)
        {
            int ROWS = (int)Math.Floor(Math.Sqrt(input.Length));
            int COLUMNS = (int)Math.Ceiling(Math.Sqrt(input.Length));
            string output = "";

            // We need to increase # rows by 1 if the input's length
            // is greater than the total area of our grid.
            if (input.Length > ROWS * COLUMNS)
                ROWS++;

            // Create the square code resulting from the input.
            for (int i = 0; i < COLUMNS; i++)
            {
                for (int j = 0; j < ROWS; j++)
                {
                    // The offset between letters.
                    int offset = j * COLUMNS + i;

                    // Keep track of the characters in the input if there still is one.
                    if (offset < input.Length)
                        output += input[offset];

                    else
                        break;
                }
            }

            output = Cipher(output, KEY);

            return output;
        }

        // Run a Vig. Cipher on a piece of text and key.
        public static string Cipher(string text, string key)
        {
            string encryptedStr = "";
            
            for (int i = 0, j = 0; i < text.Length; i++)
            {
                char c = text.ElementAt(i);

                if (c < 'A' || c > 'Z')
                    continue;

                // Example first run of ERICHOTINGER:
                // c => 'E' = 69
                // k => 'R' = 82.
                // 69 + 82 = 151;
                // A => 65
                // 151 - 2 * 65 = 21 % 26 => 21.
                // c + k - 2 * 'A' gives us the offset of the new character.
                // Then we mod 26 to make sure the offset is within the 0-25 range.
                // And we add 'A' to get the ASCII character combined with the offset
                // 21 + 65 = 86 = V
                encryptedStr += (char)((c + key.ElementAt(j) - 2 * 'A') % 26 + 'A');

                j = ++j % key.Length;
            }

            return encryptedStr;
        }
    }
}
