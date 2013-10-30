// --------------------------------------------------->
// ---------------------------------------------------
// This file stores headers for
// all hashing-related functions.
// 
// @see hashing.c
// @author Eric Hotinger
// @version November 18, 2012
// ---------------------------------------------------
// --------------------------------------------------->


// --------------------------------------------------->
// Hash map of words; stores a maximum of 10k words.
// --------------------------------------------------->
struct word *hashMap[10001];

// --------------------------------------------------->
// Hashes a character pointer and returns an integer
// representation of the hash.
// --------------------------------------------------->
int hash(char *ptr);

// --------------------------------------------------->
// Takes a dictionary and a wordLength then hashes the
// entire dictionary's words that have the same length
// as the specified word length.
// --------------------------------------------------->
void hashify(char *dictionary, int wordLength);

// --------------------------------------------------->
// Given an word, intiializes it and allocates space
// for the word to be stored in memory.
// --------------------------------------------------->
struct word *initializeWord(char *word);

// --------------------------------------------------->
// Given an amount of space, allocates enough memory
// to hold the specified space.
// --------------------------------------------------->
void *makeSpace(int space);