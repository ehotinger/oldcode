/**
 * Recursive Descent Parser for Bit Manipulation.
 *
 * CS 3304 - Comparative Languages - 3/30/2013
 *
 * By: Eric Hotinger
 *
 * This program can be ran using the command: ./descent < input.txt
 *
 */
#include <stdio.h>
#include <stdbool.h>
#include <ctype.h>
#include <stdlib.h>

void scan();
unsigned int E();
unsigned int A();
unsigned int B();
unsigned int C();
unsigned int D(bool);

// -------------------------------------
// 		Global variables
//
// OP: contains current operator
//
// Num: contains current computed value
// -------------------------------------
char OP;
unsigned int Num;

/* MAIN */
int main(){

	// Scan until the EOF
	while(OP != ';'){
		scan();
		unsigned int current = E();

		// Print out computed final value after recursive descent
		if(OP != ';')
			printf(" = %x\n", current & 0xf);
	}

	return 0;
}

/**
 * E in the grammar 
 */
unsigned int E(){
	unsigned int current = A();

	while(OP == '|'){
		scan();
		current |= A();
	}

	return current;
}

/**
 * A in the grammar
 */
unsigned int A(){
	unsigned int current = B();

	while(OP == '^'){
		scan();
		current ^= B(current);
	}

	return current;
}

/**
 * B in the grammar
 */
unsigned int B(){
	unsigned int current = C();
	
	while(OP == '&'){
		scan();
		current &= C();
	} 

	return current;
}

/**
 * C in the grammar which funnels parentheses into D()
 */
unsigned int C(){
	unsigned int current = 0;
	int numNot = 0;
	int numLeft = 0;
	int numRight = 0;
	bool needsNot;

	// Not operator; count the number of nots	
	while(OP == '~'){
		scan();
		numNot++;
	}

	// If there are an even number of nots, no need
	// to negate; if odd, we need to negate.
	needsNot = (numNot % 2 != 0);

	// Following the ~, if there's a > or < we
	// need to throw in the not.
	if(OP != '<' && OP != '>')
		current = D(needsNot);

	// Handle the rest of left bit shifts
	while(OP == '<'){
		scan();
		numLeft++;

		if(OP == 'N')
			current = D(0) << numLeft;

		else if(OP == '~'){
			scan();
			current = C() << numLeft;
		}

		else if(OP == '(')
			current = D(0) << numLeft;

		else if(OP == '>')
			current = C() << (numLeft - 1);
	}

	// Handle the rest of right bit shifts
	while(OP == '>'){
		scan();
		numRight++;

		if(OP == 'N')
			current = D(0) >> numRight;

		else if(OP == '~'){
			scan();
			current = C() >> numRight;
		}

		else if(OP == '(')
			current = D(0) >> numRight;

		else if(OP == '<')
			current = C() >> (numRight - 1);
	}

	return current;
}

/**
 * D isn't in the provided grammar, but it 
 * handles parentheses and negation, which is
 * channeled through C()
 */
unsigned int D(bool needsNot){
	unsigned int current = 0;

	if(OP == 'N'){
		if(needsNot)
			current = ~Num;

		else
			current = Num;

		scan();
	}

	else if(OP == '('){
		scan();

		if(needsNot)
			current = ~E();

		else
			current = E();

		scan();
	}

	return current;
}

/**
 * Scans for tokens and changes
 * the global OP variable to the associated
 * token that was processsed or tells the program
 * to end once it hits EOF.
 */
void scan(){
	char token;

	// Skip past all blank spaces
	while(isblank(token = getchar()));

	// Handle digits (0 - 9) and change the operator to 'N' for 'hex number'
	if(token >= '0' && token <= '9'){
		OP = 'N';
		Num = token - '0';
	}

	// Handle digits (a - f) and change the operator to 'N' for 'hex number'
    else if (token >= 'a' && token <= 'f'){
    	OP = 'N';
    	Num = token - 'a' + 10;
    }

	else if(token == '|') // Bitwise OR
		OP = '|';

	else if(token == '^') // Bitwise XOR
		OP = '^';

	else if(token == '&') // Bitwise AND
		OP = '&';

	else if(token == '~') // Bitwise NOT
		OP = '~';

	else if(token == '<') // Bitwise Shift Left 1
		OP = '<';

	else if(token == '>') // Bitwise Shift Right 1
		OP = '>';

	else if(token == '(') // Left parentheses
		OP = '(';

	else if(token == ')') // Right parentheses
		OP = ')';

	else if(token == '\n') // New line
		return;

	else if(token == EOF) // End of file
		OP = ';';

	// Print out the entire parsed expression as we go along
	if(token != EOF)
		printf("%c", token);
}