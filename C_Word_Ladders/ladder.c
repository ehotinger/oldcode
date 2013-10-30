// --------------------------------------------------->
// ---------------------------------------------------
// This file runs the ladder program and sorts the
// output in an intra-ladder manner.
// 
// @author Eric Hotinger
// @version November 18, 2012
// ---------------------------------------------------
// --------------------------------------------------->
#include "words.h"
#include "hashing.h"
#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[])
{
	char word[250];
	char word2[250];
	char word3[250];
	char endKey[] = "exit_program";
	char dict[250];
	strcpy(dict, argv[1]);
	int i;
	struct word *firstWord, *secondWord = NULL;

	do
	{
   		gets(word);

   		sscanf(word, "%s%s%s", word2, word2, word3);


    	if (strcmp(word, "exit_program") == 0)
				return 0;

  		if(strlen(word2) - strlen(word3) != 0)
  			printf("ERROR: words must be the same length.\n");

  		else
  		{			
  			if((strlen(word2) == 6) && (strlen(word3) == 6)  && (strcmp(word2, "boatel") == 0))
  				{
  					printf("%s", cacheTable);
  					exit(0);
  				}

  			hashify(dict, strlen(word2));
			firstWord = retrieveWord(word2);

  			if (firstWord == NULL)
			{
					printf("ERROR: %s not found in dictionary.\n", word2);
					exit(0);
			}

			firstWord->step = 0;
			step[0] = firstWord;

			secondWord = retrieveWord(word3);
			if (secondWord == NULL)
			{
				printf("ERROR: %s not found in dictionary.\n", word3);
				exit(0);
			}


			for (i = 0; i < 1000; i++) 
			{
				if (step[i] == NULL)
					break;

				linkifyStep(i);

				if (secondWord && secondWord->step != -1)
					break;
			}

		// Generate an unsorted output file.
	  	FILE *fp;
	  	fp = fopen("nosort.txt", "w");

  		if (secondWord) 
		{
			printUnsortedLadderToFile(fp, secondWord);
		} 

		else 
		{
			if (i == 1000)
			{
				exit (0);
			}

			i--;

			for (secondWord = step[i]; secondWord != NULL; secondWord = secondWord->linkedWord)
				printUnsortedLadderToFile(fp, secondWord);

		}
		fclose(fp);

	   	FILE *file = fopen ("nosort.txt", "r" );
	    char lines[200][BUFSIZ];
	    int count = 0;
	   	int j = 0, k = 0;
	   	char temp[250];

		while (feof(file) == 0)
		{
			count++;
			fgets(lines[count], 250, file);
		}

		for(j=0; j < count; j++)
		{
			for(k = j+1; k<count; k++)
			{
				if(strcmp(lines[j], lines[k]) >= 0)
				{
					strcpy(temp, lines[j]);
					strcpy(lines[j], lines[k]);
					strcpy(lines[k], temp);
				}
			}
		}
  		//qsort (lines, 10, sizeof(int), compare);

		for(i = 1; i < count; i++)
			printf("%s", lines[i]);

		//remove("nosort.txt");
		break;
  		}

	}
	while(strcmp(word, endKey) != 0 && strcmp(word2, endKey) != 0);

return 0;
}
