Cryptography
============
Two classic methods for crafting secret messages are **square codes** and the [**Vigenère cipher**](http://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher)

In a square code, all spaces are removed from a set of English text and the characters 
are written into a square or rectangle.

The width and height of the rectangle have the constraint:

```
floor(sqrt(word.Length)) <= width, height <= ceil(sqrt(word.Length))
```

Among the possible squares or rectangles, choose the one with the **minimum area**.

In the case of a rectangle, the number of rows should be kept smaller than the number of columns.

For example, the sentence `ERICHOTINGER` is 12 characters long so it is written as a rectangle with 3 rows and 4 columns.

```
ERIC
HOTI
NGER
```

The coded message is obtained by reading down the columns going left to right and forming a string without spaces. 
For example, the message above is then encoded as: `EHNROGITECIR`

The Vigenère cipher consists of multiple Caesar ciphers with different shift values.

Suppose our plaintext is the encoded `EHNROGITECIR` that we found above and our keyword is `RDA`

The person sending the message takes the keyword and repeats it until it matches the length of the plaintext 
and then adds them modulus 26, where the letters A-Z are taken to be the numbers 0-25 respectively.

For example,

```
EHNROGITECIR
RDARDARDARDA
------------
VKNIRGZWETLR
```

Explanation:

```
E=4,  R=17 -> (21)mod26 = 21 = V
H=7,  D=3  -> (10)mod26 = 10 = K
N=13, A=0  -> (13)mod26 = 13 = N
R=17, R=17 -> (34)mod26 = 8  = I
...
```


You will be given an input which is a message in English with no spaces between the words and every letter will be uppercase.  
The maximum plain-text length is 80 characters long.

Print the encoded message after applying a Vigenère cipher to a square code based on the original input.

Read from `STDIN` and print from `STDOUT`.

###Sample Input:###

```
ERICHOTINGER
```

###Sample Output:###

```
VKNIRGZWETLR
```

###Sample Input:###

```
ORIOLES
```

###Sample Output:###

```
FRSIOIV
```

###Explanation:###
After the square encoding, we see that `ORIOLES` is transformed into

```
ORI
OLE
S
```

From the block we get a new string: `OOSRLIE`

We take the new string and encrypt it with the key `RDA`:

```
OOSRLIE
RDARDAR
-------
FRSIOIV
```
