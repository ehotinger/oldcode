using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DictionaryHashSort
{
    /// <summary>
    /// Performs a simple unique sort on the dictionary file so that we can ensure that the dictionary file 
    /// we're using is unique.
    /// </summary>
    public class Program
    {
        public static void Main(string[] args)
        {
            HashSet<string> words = new HashSet<string>();

            using (StreamReader sr = new StreamReader("dictionary.txt"))
            {
                while (!sr.EndOfStream)
                {
                    string currLine = sr.ReadLine().ToLower();

                    words.Add(currLine);
                }
            }

            var sortedWords = words.OrderBy(m => m);
            using (StreamWriter sw = new StreamWriter("newDictionary.txt"))
            {
                foreach (string word in sortedWords)
                {
                    sw.WriteLine(word);
                }
            }

        }
    }
}
