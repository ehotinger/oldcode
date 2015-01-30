Beautiful Arrays
================

A Beautiful Array is an array containing at least one integer.

A smaller Beautiful Array can be extracted from a larger Beautiful Array, but all of the elements extracted must be contiguous within the larger Beautiful array.

Given a Beautiful Array of integers as input, your task is to find the largest sum formed from a Beautiful Array and a count of all the distinct Beautiful Arrays containing the largest possible sum.

You will be given an integer N, followed by N integers, one on each line.  At least one of the following N integers will be positive.

###Constraints:###
`3 <= N <= 150000`

You should output the largest sum followed by a count of the total number of distinct Beautiful Arrays which contain the largest possible sum.

Use `STDIN` for all input and `STDOUT` for all output.

================================

###Sample Input 1:###

```
3
1
2
3
```


###Sample Output 1:###

```
6
1
```

###Explanation:###

```
The array [1, 2, 3] is a Beautiful Array since it contains 3 elements.  It also contains the largest sum, which is 6.
```


============================

###Sample Input 2:###

```
8
100
100
-40000
100
100
-40000
50
150
```


###Sample Output 2:###

```
200
2
```

###Explanation:###

```
There are three Beautiful Arrays which contain the largest contiguous sum, which is 200: [100, 100], [100, 100], and [50, 150].

However, since one of these arrays is duplicated, we remove the duplicate and end up with 2: [100, 100] and [50, 150].
```

======================


###Due Date:###

```
12/18/2013 by 9:00 AM
```
