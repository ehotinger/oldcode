Substitution Permutation Network
================================
This is an example of a Substitution-Permutation Network (SPN). It is a series of linked mathematical operations: a substitution, permutation, and XOR.

The main purpose of this particular SPN is to be able to trace the algorithm as it goes along; partial sample output follows.

Initializing program with key <89AC36B5> and plain-text

>>> Beginning round 1 with text <778A> and key <9AC3>

Sbox:	 778A --> 8836 = 1000100000110110

Permutation:	1000100000110110 --> 1100000100110010 = C132

0101101111110001 = 5BF1

... [more code] ...

>>> Cipher text: 1000101111101110 (8BEE)

This cipher is a simpler version of one such as the Rijndael or AES (which is also posted).