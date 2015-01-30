/* FACTS: ONLY BROTHER, SISTER, HUSBAND, FATHER */

brother(claude, alice).
brother(edward, grace).
brother(edward, florence).


% Make the assumption that florence is a girl.
sister(alice, claude).
sister(grace, edward).
sister(grace, florence).
sister(florence, grace).
sister(florence, edward).

husband(claude, dianna).
husband(bob, alice).
husband(edward, jennifer).
husband(keith, grace).

father(bob, florence).
father(bob, edward).
father(bob, grace).
father(keith, harry).
father(edward, iona).


/* RULES: 	WIFE, MOTHER, PARENT, SIBLING, GRANDFATHER, GRANDMOTHER	*/
/*			GRANDPARENT, AUNT, UNCLE, GREATAUNT, GREATUNCLE 		*/

% X is a wife to Y if Y is a husband to X.
wife(X, Y) :- husband(Y, X).

% Mother if there's a father for the child and the father is a husband to the woman.
mother(W, C) :- father(F, C), husband(F, W).

% Person is a parent of a Child if Person is a Father or Mother to Child.
parent(P, C) :- father(P, C) ; mother(P, C).

% X is a sibling to Y if X is a sister or brother to Y.
sibling(X, Y) :- sister(X, Y) ; brother(X, Y).

% X is a grandfather to Y if Z is a parent to Y and X is a father to Z.
grandfather(X, Y) :- parent(Z, Y), father(X, Z).

% X is a grandmother to Y if Z is a parent to Y and X is a mother to Z.
grandmother(X, Y) :- parent(Z, Y), mother(X, Z).

% X is a grandparent to Y if X is a grandfather or grandmother to Y.
grandparent(X, Y) :- grandfather(X, Y) ; grandmother(X, Y).

% Woman is aunt to Child if Person is parent of Child and Woman is sister to Person, OR if Person is parent to Child, Brother is brother to Person, and Woman is wife to Brother.
aunt(W, C) :- (parent(P, C), sister(W, P)) ; (parent(P, C), brother(B, P), wife(W, B)).

% Man is uncle to Child if Person is parent of Child and Man is brother to Person, OR if Person is parent to Child, Sister is sister to Person, and Man is husband to Sister.
uncle(M, C) :- (parent(P, C), brother(M, P)) ; (parent(P, C), sister(S, P), husband(M, S)).

% Same as aunt but with grandparent
greataunt(W, C) :- (grandparent(P, C), sister(W, P)) ; (grandparent(P, C), brother(B, P), wife(W, B)).

% same as uncle but with grandparent
greatuncle(M, C) :- (grandparent(P, C), brother(M, P)) ; (grandparent(P, C), sister(S, P), husband(M, S)).