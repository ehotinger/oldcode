Cryptography 2
==============

###Part 1 (5 points for correctness, 5 points for speed)###

You ignored Dave's warning about the crypto locker virus and now all of your documents are encrypted with an unknown password.

The authors of the virus have issued a challenge to you; if you can solve it, they will provide the key to unencrypt all of your documents.

You must solve the following summation, where each letter represents a unique digit (0-9):

```
  PAY
+ FOR
------
 LOCK
```

Luckily, Dave is a master at cryptography and has already deduced that `P = 1`, `A = 2`, and `Y = 5` but he needs your help to finish the work.

You can use any programming language you want; expected output is each letter followed by its value.

###Sample Output:###

```
P: 1
A: 2
Y: 5
F:
O:
R:
L:
C:
K:
```

###Part 2 (5 points for correctness, 5 points for speed)###
Now, instead of using any particular programming language, you must solve the same problem but using any variant of SQL you want.

You are limited to at most three tables, which can have any schema and any number of tuples you want.

Include your `CREATE TABLE` and any `INSERT` statements as well as the actual query to find the result.

###Sample Output:###

```
| P | A | Y | F | O | R | L | C | K |
-------------------------------------
| 1 | 2 | 5 |   |   |   |   |   |   |
-------------------------------------
```

###Due Date:###

```
11/13/2013 by 9:00 AM
```
