General Knowledge: 
------------------
Your aunt or uncle is a sibling, or the spouse of a sibling, of one of your parents. Your greataunt 
(or greatuncle) is the sister (or brother) of one of your grandparents or the spouse of the brother (or 
sister) of one of your grandparents. Siblings are any two people who share the same parents. 

Known Genealogical Facts: 
-------------------------
At a recent family gathering, the oldest people present were the twins, Alice and Claude. Alice is 
married to Bob, and Claude is married to Dianna. All of Alice and Bob's children were in 
attendance: Edward, Florence and Grace. Grace brought her husband Keith and their child Harry. 
Grace's sister-in-law, Jennifer (Edward's wife), came too. Edward brought his child, Iona. 

Assignment: 
-----------
Write a prolog program which represents the gathering. You should make clear the meaning of any 
assertions (or predicates) that you use. For example, does "(parent X Y)" mean that "X is the 
parent of Y" or that "Y is the parent of X"?

Your are welcome to create any rules to infer relationships if you find them useful, as long as they 
are logically equivalent to the ones alluded to above. Use a minimal set of facts that allow you to 
infer all other relationship. Those facts are stated in the gathering, e.g. brother, sister, husband and 
father relationships. These four are all the facts that you need. You can write rule to infer sibling, 
wife, parent, grandfather, grandmother aunt, uncle, greataunt and greatuncle. 

Your program will need some common-sense knowledge that is not given in the problem statement. 
For example, if X is married to Y, then Y is married to X. But, if M is the brother of N, N is not 
necessarily the brother of M. 

Your program should be able to handle queries that involve brother, sister, sibling, father, mother, 
parent, grandfather, grandmother, aunt, uncle, greataunt or greatuncle. In particular, you are to ask 
your prolog system who is the greataunt of Harry. 
