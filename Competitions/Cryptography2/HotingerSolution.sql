SELECT P.num AS P, 
A.num AS A,
Y.num AS Y, 
F.num AS F, 
O.num AS O, 
R.num AS R, 
L.num AS L, 
C.num AS C,
K.num AS K

FROM nums AS P
JOIN nums AS A ON A.num NOT IN (P.num) 
JOIN nums AS Y ON Y.num NOT IN (P.num, A.num) 
JOIN nums AS F ON F.num NOT IN (P.num, A.num, Y.num) 
JOIN nums AS O ON O.num NOT IN (P.num, A.num, Y.num, F.num) 
JOIN nums AS R ON R.num NOT IN (P.num, A.num, Y.num, F.num, O.num) 
JOIN nums AS L ON L.num NOT IN (P.num, A.num, Y.num, F.num, O.num, R.num) 
JOIN nums AS C ON C.num NOT IN (P.num, A.num, Y.num, F.num, O.num, R.num, L.num)
JOIN nums AS K ON K.num NOT IN (P.num, A.num, Y.num, F.num, O.num, R.num, L.num, C.num)

WHERE P.num * 100 + A.num * 10 + Y.num + F.num * 100 + O.num * 10 + R.num = L.num * 1000 + O.num * 100 + C.num * 10 + K.num
AND P.num = 1
AND A.num = 2
AND Y.num = 5;