// --------------------------------------------------->
// ---------------------------------------------------
// This file stores all hashing-related functions.
// 
// @see hashing.h
// @see words.h
// @author Eric Hotinger
// @version November 18, 2012
// ---------------------------------------------------
// --------------------------------------------------->
#include "hashing.h"
#include "words.h"
#include <ctype.h>
#include <stdlib.h>
#include <string.h>

// -------------------------------------------------->
// Turns a character pointer into a hashed integer.
// -------------------------------------------------->
int hash(char *ptr)
{
	long hashed = 0;

	// ---------------------------------------------->
	// NOTE: A - B * (A / B) => C = A % B.
	// For some reason, % wasn't working during
	// the writing of this function...
	// ---------------------------------------------->
	while (*ptr)
	{
		hashed = (hashed * 26 + *ptr++ - 'a') % 10001;
	}

return (int)hashed;
}

// ------------------------------------------------->
// Makes a hashmap of a dictionary file based on 
// a word's length.
// ------------------------------------------------->
void hashify(char *dictionary, int wordLength)
{
	int hashing;
	FILE *file;
	char word[100];
	char *nextWord;
	struct word *newWord;

	// --------------------------------------------->
	// If the dictionary isn't NULL, open it for
	// writing.  If it is, print an error.
	// --------------------------------------------->
	if(dictionary != NULL)
		file = freopen(dictionary, "rw", stdin);

	if (file == NULL)
	{
		printf("Unable to open dictionary.\n");
		exit(0);
	}

	// ------------------------------------------------->
	// 1. Loop through all the words in the dictionary.
	// 2. If the word is uppercase, ignore it.
	// 3. If the word isn't the same as our word's
	// length, ignore it.
	// 4. Allocate space for the word, and then place it
	// as our word's next word.
	// ------------------------------------------------->
	while(fscanf(file, "%s", word) !=EOF)
	{
		nextWord = word;

		while (*nextWord)
		{
			if (!islower(*nextWord))
				break;

			nextWord++;
		}

		if (nextWord - word != wordLength)
			continue;

		if (*nextWord == '\n')
			*nextWord = '\0';

		else if (*nextWord)
			continue;

		newWord = initializeWord(word);

		hashing = hash(word);

		newWord->nextWord = hashMap[hashing];

		hashMap[hashing] = newWord;
	}

	fclose(file);

	int i = 0;
	struct word *linkedWord;

	for(i = 0; i < 10000; i++)
	{
		for(linkedWord = hashMap[i]; linkedWord; linkedWord = linkedWord->nextWord)
			markNearby(linkedWord);
	}
}

// ------------------------------------------------->
// Allocates memory for a word and initializes it.
// ------------------------------------------------->
struct word *initializeWord(char *givenWord)
{
	struct word *word = makeSpace(sizeof(struct word));

	int wordLength = strlen(givenWord);

	char *name = makeSpace(wordLength + 1);
	
	strcpy(name, givenWord);

	word->name = name;
	word->step = -1;
	word->numNearby = 0;
	word->length = wordLength;

	word->linkedWord = NULL;
	word->nearby = NULL;
	word->ladder = NULL;
	word->nextWord = NULL;

	return word;
}

// ------------------------------------------------->
// Allocates memory for the specified amount of
// memory space.
// ------------------------------------------------->
void *makeSpace(int space)
{
	void *outerSpace;

	char *buffer = NULL;
	int bufferSize = -1;

	if (space > bufferSize) 
	{
		buffer = malloc(bufferSize = 100000);

		if (buffer == NULL)
		{
			printf("No valid word ladders could be found with the given input.\n");
			exit(0);
		}
	}


	outerSpace = buffer;

	buffer += space;

	bufferSize -= space;

	return outerSpace;
}
