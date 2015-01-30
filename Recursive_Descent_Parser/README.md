Description
-----------
The Grammar shown below describes the bitwise manipulation of Hexidecimal numbers. It is 
left-recursive and indicates proper associativity and precedence. 

	E → E “|” A bitwise OR 
	E → A 
	A → A “^” B bitwise XOR 
	A → B 
	B → B “&” C bitwise AND 
	B → C 
	C → “<” C bitwise shift left 1 
	C → “>” C bitwise shift right 1 
	C → “~” C bitwise NOT 
	C → “(” E “)” 
	C → hex 

The corresponding right-recursive attribute Grammar is provided on the second page. You are 
to write a program using the designated Inherited and Synthesized attributes to compute correctly 
the expressions shown below. You cannot use more than 2 global / non-local variables, and they 
should be to hold the Operator and HexNumber as detected by the Lexical Analyzer. 

	f&a 
	b|3 
	f^1 
	~0 
	>>f 
	<1 
	3|6&c 
	(3|6)&c 
	(3|c)&6 
	~~f 
	f^>f 
	c&3&f 
	<3|3 
	~(e^7) 
	>>>>~(a^c) 
	~(>1|>2|>4|>8)^~5 
	(d^2|1)&(<<2|c) 
	((f&>9)|(~3^8)|(~c|b)) 
	>f|<f&1 
	(>(<1&>f)|8|9)^(~3&7) 
	~(><8|<>1) 

	E → A EE
	EE.st = A.val
	E.val = EE.val
	EE1 → | A EE2
	EE2.st = EE1.st | A.val ( “|” bitwise OR )
	EE1.val = EE2.val
	EE → ε
	EE.val = EE.st
	A → B AA
	AA.st = B.val
	A.val = AA.val
	AA1 → ^ B AA2
	AA2.st = AA1.st ^ B.val ( “^” bitwise XOR )
	AA1.val = AA2.val
	AA → ε
	AA.val = AA.st
	B → C BB
	BB.st = C.val
	B.val = BB.val
	BB1 → & C BB2
	BB2.st = BB1.st & C.val ( “&” bitwise AND )
	BB1.val = BB2.val
	BB → ε
	BB.val = BB.st
	C1 → <C2
	C1.val = C2.val << 1 ( “<<” bitwise shift left one )
	C1 → >C2
	C1.val = C2.val >> 1 ( “>>” bitwise shift right one )
	C1 → ~C2
	C1.val = ~C2.val ( “~” bitwise NOT )
	C → ( E )
	C.val = E.val
	C → hex
	C.val = hex.val

Notes
-----
The program should run all of the given input test cases perfectly.  Note that this program only handles 4 bits.
The sample input I've included with the program should be ran to generate the sample output I've provided.

If at any time the input file is modified, please note that the input file must end with a newline character, otherwise the last command in the file will not be ran!

Please also note that the input file must have \n characters, not \r\n or other types of  carriage returns.  This should pose no problem if the sample input I've provided is used.

Compilation
------------
It's simple! Type "make" to compile the program.


Running
------------------------------------------------
To execute and run the program, type:
./descent < input.txt

... results will appear in stdout.

To redirect the program's output to a file, type:
./descent < input.txt > output.txt

... results will appear in output.txt
