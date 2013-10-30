// --------------------------------------------------->
// ---------------------------------------------------
// This file stores all word-related functions.
// 
// @see words.h
// @see hashing.h
// @author Eric Hotinger
// @version November 18, 2012
// ---------------------------------------------------
// --------------------------------------------------->
#include "words.h"
#include "hashing.h"
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// -------------------------------------------------->
// Returns a pointer to the specifed word structure
// if it is in the hashMap.  Otherwise, it will
// return NULL.
// -------------------------------------------------->
struct word *retrieveWord(char *aWord)
{
	int hashing = hash(aWord);

	struct word *word = hashMap[hashing];
	
	while (word)
	{
		if (strcmp(aWord, word->name) == 0)
			return word;

		word = word->nextWord;
	}

	return word;
}


// -------------------------------------------------->
// Marks all words that can be related to the
// specified word in 1 step.
// EG: aa => ab
//     aa => ba ...
// -------------------------------------------------->
void markNearby(struct word *givenWord)
{
	int i;
	int j;

	char word[100];
	int numNearby = 0;

	strcpy(word, givenWord->name);

	for (i = 0; i < givenWord->length; i++) 
	{
		// --------------------------------------------->
		// ONLY CONSIDER 'a' to 'z' in the name.
		// --------------------------------------------->
		for (j = 'a'; j <= 'z'; j++) 
		{
			if (j == givenWord->name[i])
				continue;

			word[i] = j;

			if ((tempWord = retrieveWord(word)) == 0)
				continue;

			nearby[numNearby++] = tempWord;
		}

		word[i] = givenWord->name[i];
	}

	// ------------------------------------------------>
	// If there aren't nearby words in the dictionary,
	// return.
	// ------------------------------------------------>
	if (numNearby == 0)
		return;

	allWords = makeSpace(numNearby * sizeof(struct word*));
	memcpy(allWords, nearby, numNearby * sizeof(struct word*));

	// ----------------------------------------------->
	// UPDATE the given word with what we found out.
	// ----------------------------------------------->
	givenWord->nearby = allWords;
	givenWord->numNearby = numNearby;
}

// -------------------------------------------------->
// Find all the words on the specified step that can
// be formed from the specified word.
// -------------------------------------------------->
void linkifyWord(int stepId, struct word *word)
{
	int i;

	stepId += 1;

	for (i = 0; i < word->numNearby; i++)
	{
		tempWord = word->nearby[i];

		// ---------------------------------------->
		// If the step is the last step. This is
		// the case for starting and ending words.
		// ---------------------------------------->
		if (tempWord->step == -1) 
		{
			tempWord->step = stepId;
			tempWord->linkedWord = step[stepId];
			step[stepId] = tempWord;
		}
	}
}

// -------------------------------------------------->
// Find the word on the specified step and link it
// to other related words.
// -------------------------------------------------->
void linkifyStep(int stepId)
{
	struct word *word = step[stepId];
	
	while (word)
	{
		linkifyWord(stepId, word);
		word = word->linkedWord;
	}
}

// -------------------------------------------------->
// Prints all the unsorted ladders to a file.
// Once they're in a file, they can be sorted and
// printed to stdout.
// -------------------------------------------------->
void printUnsortedLadderToFile (FILE *file, struct word *word)
{
	int i;
	int count = 0;

	word->ladder = ladder;
	ladder = word;

	if (word->step == 0) 
	{
		tempWord = word;

		while (tempWord) 
		{
			fprintf(file, "%s", tempWord->name);
			tempWord = tempWord->ladder;

			if (tempWord)
			{
				fprintf(file, ", ");	
			}
			count++;
		}
		fprintf(file, "\n");
	}

	else 
	{
		for (i = 0; i < word->numNearby; i++)
		{
			if (word->step == word->nearby[i]->step + 1)
			{
				printUnsortedLadderToFile(file, word->nearby[i]);
				count++;
			}
		}
	}

	ladder = word->ladder;

	if(count == 0)
	{
		fprintf(file, "No valid word ladders could be found with the given input.\n");
	}
}
