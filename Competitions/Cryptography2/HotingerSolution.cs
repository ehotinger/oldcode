using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HotingerCrypto2
{
    public class HotingerSolution
    {
        public static void Main(string[] args)
        {
            // We are given the following information from the problem statement:
            // P = 1
            // A = 2
            // Y = 5

            // Since L can only be a 0 or 1 because it is in the thousands position 
            // and we already know that 1 is taken by P, we assume L is 0.
            // Hence, the only remaining possibilities for the other digits are 3, 4, 6, 7, 8, and 9.
            int[] RemainingPossibilities = { 3, 4, 6, 7, 8, 9 };

            // We need to solve for F, O, R, C, and K still.
            //Stopwatch timer = new Stopwatch();
            //timer.Start();
            Solve(RemainingPossibilities);
            //timer.Stop();
            //Console.WriteLine("Ellapsed time (in ms): " + timer.ElapsedMilliseconds);

            Console.ReadLine(); // Press any key to end.
        }

        public static void Solve(int[] RemainingPossibilities)
        {
            foreach (int F in RemainingPossibilities)
            {
                foreach (int O in RemainingPossibilities)
                {
                    if (O == F)
                        continue;

                    foreach (int R in RemainingPossibilities)
                    {
                        if (R == O || R == F)
                            continue;

                        foreach (int C in RemainingPossibilities)
                        {
                            if (C == R || C == O || C == F)
                                continue;

                            foreach (int K in RemainingPossibilities)
                            {
                                if (K == C || K == R || K == O || K == F)
                                    continue;

                                int PAY = 125;
                                int FOR = F * 100 + O * 10 + R;
                                int LOCK = O * 100 + C * 10 + K; // L is 0 from above.

                                if (PAY + FOR == LOCK)
                                {
                                    StringBuilder sb = new StringBuilder();

                                    sb.AppendLine("P: 1");
                                    sb.AppendLine("A: 2");
                                    sb.AppendLine("Y: 5");
                                    sb.AppendLine("F: " + F);
                                    sb.AppendLine("O: " + O);
                                    sb.AppendLine("R: " + R);
                                    sb.AppendLine("L: 0");
                                    sb.AppendLine("C: " + C);
                                    sb.Append("K: " + K);

                                    Console.WriteLine(sb.ToString());
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
