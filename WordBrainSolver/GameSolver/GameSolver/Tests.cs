using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace GameSolver
{

    [TestClass]
    public class Tests
    {

        #region IsPossible

        [TestMethod]
        public void TrowelIsPossible()
        {
            var board = new[,] {
             {"t", "n", "l", "e"},
             {"m", "b", "o", "w"},
             {"a", "o", "r", "t"},
             {"j", "o", "i", "t"}
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "trowel"));
        }

        [TestMethod]
        public void EntireBoardIsPossible()
        {
            var board = new[,] {
             {"t", "n", "l", "e"},
             {"m", "b", "o", "w"},
             {"a", "o", "r", "t"},
             {"j", "o", "i", "t"}
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "tnlewobmaorttioj"));
        }

        [TestMethod]
        public void MostlyOs()
        {
            var board = new[,] {
                {"o", "o", "o", "o"},
                {"o", "l", "o", "o"},
                {"l", "l", "o", "o"},
                {"o", "l", "o", "l"}
            };
            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "oooooololloololo"));
        }

        [TestMethod]
        public void AllAs()
        {
            var board = new[,] {
                {"a", "a", "a", "a"},
                {"a", "a", "a", "a"},
                {"a", "a", "a", "a"},
                {"a", "a", "a", "a"}
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "aaaaaaaaaaaaaaaa"));
        }

        [TestMethod]
        public void BEndings()
        {
            var board = new[,] {
                {"b", "a", "a", "a"},
                {"a", "a", "a", "a"},
                {"a", "a", "a", "a"},
                {"a", "a", "a", "b"}
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "aaabaaaaaaaaaaab"));
        }


        [TestMethod]
        public void DiagonalBs()
        {
            var board = new[,] {
                {"b", "a", "a", "a"},
                {"a", "b", "a", "a"},
                {"a", "a", "b", "a"},
                {"a", "a", "a", "b"}
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "aababaaabaaaaab"));
        }

        [TestMethod]
        public void BananaIsPossible()
        {
            var board = new[,] {
                {"b", "a", "n", "a"},
                {"a", "a", "a", "n"},
                {"a", "a", "a", "a"},
                {"a", "a", "a", "b"}
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "banana"));
        }

        [TestMethod]
        public void DoorbellIsPossible()
        {
            var board = new[,] {
                {"r", "b", "r", "b"},
                {"o", "e", "o", "e"},
                {"o", "l", "o", "l"},
                {"l", "d", "r", "t"}
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "doorbell"));
        }

        [TestMethod]
        public void CemeteryIsPossible()
        {
            var board = new[,] {
                {"y", "r", "s", "h"},
                {"e", "t", "f", "i"},
                {"e", "c", "l", "d"},
                {"e", "m", "o", "g"}
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "cemetery"));
        }

        [TestMethod]
        public void GoldFishIsPossible()
        {
            var board = new[,] {
                {"y", "r", "s", "h"},
                {"e", "t", "f", "i"}, 
                {"e", "c", "l", "d"}, 
                {"e", "m", "o", "g"}
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "goldfish"));
        }

        [TestMethod]
        public void CannonIsPossible()
        {
            var board = new[,] {
                { "n", "o", "n", "c" }, 
                { "p", "n", "t", "m" }, 
                { "e", "c", "a", "a" }, 
                { "r", "e", "n", "j" } 
            };

            Assert.IsTrue(Solver.IsPossibleFromBoard(board, "cannon"));
        }

        #endregion

        #region IsNotPossible

        [TestMethod]
        public void PercentNotPossible()
        {
            var board = new[,] {
                { "n", "o", "n", "c" }, 
                { "p", "n", "t", "m" }, 
                { "e", "c", "a", "a" }, 
                { "r", "e", "n", "j" } 
            };

            Assert.IsFalse(Solver.IsPossibleFromBoard(board, "percent"));
        }

        [TestMethod]
        public void BrbNotPossible()
        {
            var board = new[,] {
                {"r", "b", "r", "o"},
                {"o", "e", "o", "e"},
                {"o", "l", "o", "l"},
                {"l", "d", "r", "t"}
            };

            Assert.IsFalse(Solver.IsPossibleFromBoard(board, "brb"));
        }

        [TestMethod]
        public void FishyIsNotPossible()
        {
            var board = new[,] {
                {"y", "r", "s", "h"},
                {"e", "t", "f", "i"},
                {"e", "c", "l", "d"},
                {"e", "m", "o", "g"}
            };

            Assert.IsFalse(Solver.IsPossibleFromBoard(board, "fishy"));
        }

        #endregion
    }
}