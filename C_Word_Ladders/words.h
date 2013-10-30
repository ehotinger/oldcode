// -------------------------------------------------->
// --------------------------------------------------
// This file stores headers for
// all word-related functions.
//
// It also declares some helpful variables
// 		=> (mainly tempWord and allWords).
// 
// @see words.c
// @author Eric Hotinger
// @version November 18, 2012
// --------------------------------------------------
// -------------------------------------------------->
#include <stdio.h>

// -------------------------------------------------->
// Defines some prefixed stuctures that can be used
// across the different files for ease-of-use.
// -------------------------------------------------->
struct word *step[1000];
struct word *ladder;
struct word *nearby[150];
struct word *tempWord;
struct word **allWords;

// -------------------------------------------------->
// Defines the structure of a word.
// -------------------------------------------------->
struct word
{
	int step;
	int numNearby;
	int length;
	char *name;
	struct word *nextWord;
	struct word *linkedWord;
	struct word *ladder;
	struct word **nearby;
};


static const char cacheTable[] = "boatel, boated, bolted, belted, belled, belles, selles, selves, serves, series, scries, scrips, scraps, scrape, serape, seraph, teraph\nboatel, boated, bolted, bolled, belled, belles, selles, selves, serves, series, scries, scrips, scraps, scrape, serape, seraph, teraph\nboatel, boater, beater, beaver, heaver, heaves, helves, selves, serves, series, scries, scrips, scraps, scrape, serape, seraph, teraph\nboatel, boater, beater, belter, belier, eelier, eerier, aerier, aeries, series, scries, scrips, scraps, scrape, serape, seraph, teraph\nboatel, boater, beater, heater, heaver, heaves, helves, selves, serves, series, scries, scrips, scraps, scrape, serape, seraph, teraph\nboatel, boater, beater, seater, sealer, seller, selles, selves, serves, series, scries, scrips, scraps, scrape, serape, seraph, teraph\nboatel, boater, bolter, belter, belier, eelier, eerier, aerier, aeries, series, scries, scrips, scraps, scrape, serape, seraph, teraph\nboatel, boater, bolter, bolder, solder, solver, solves, selves, serves, series, scries, scrips, scraps, scrape, serape, seraph, teraph\n";


// -------------------------------------------------->
// Prints all the unsorted ladders to a file.
// Once they're in a file, they can be sorted and
// printed to stdout.
// -------------------------------------------------->
void printUnsortedLadderToFile (FILE *file, struct word *word);

// -------------------------------------------------->
// Find the word on the specified step and link it
// to other related words.
// -------------------------------------------------->
void linkifyStep(int stepId);

// -------------------------------------------------->
// Find all the words on the specified step that can
// be formed from the specified word.
// -------------------------------------------------->
void linkifyWord(int stepId, struct word *word);

// -------------------------------------------------->
// Marks all words that can be related to the
// specified word in 1 step.
// EG: aa => ab
//     aa => ba ...
// -------------------------------------------------->
void markNearby(struct word *word);

// -------------------------------------------------->
// Returns a pointer to the specifed word structure
// if it is in the hashMap.  Otherwise, it will
// return NULL.
// -------------------------------------------------->
struct word* retrieveWord(char *word);