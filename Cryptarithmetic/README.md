Cryptarithmetic SQL
=====================

There's a pretty famous cryptarithmetic problem which has us solve SEND + MORE = MONEY, where each letter is a unique digit.

For an out-of-the-box solution, I was able to use SQL to solve the cryptarithmetic, using only one table.

The strategy is to create one table with one column; a column of integers from 0 to 9 which represent the values each letter could be stored as.

We then take the cartesian product of the same table multiple times (once for each letter) and eliminate all cases where a certain letter is equal to another letter, in order to find all of the possible permutations.