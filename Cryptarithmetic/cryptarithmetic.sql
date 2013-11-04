SELECT S.num, E.num, N.num, D.num, M.num, O.num, R.num, Y.num 
FROM nums as S, nums as E, nums as N, nums as D, nums as M, nums as O, nums as R, nums as Y 
WHERE M.num = 1
AND S.num * 1000 + E.num * 100 + N.num * 10 + D.num + M.num * 
1000 + O.num * 100 + R.num * 10 + E.num = M.num * 10000 + O.num * 1000 + N.num * 
100 + E.num * 10 + Y.num
AND S.num != 0 AND S.num != 1 AND E.num != 1 AND N.num != 1 AND D.num != 1 
AND O.num != 1 AND R.num != 1 AND Y.num != 1 AND S.num != E.num 
AND S.num != N.num AND S.num != D.num AND S.num != O.num AND S.num != R.num 
AND S.num != Y.num AND E.num != N.num AND E.num != D.num AND E.num != O.num 
AND E.num != R.num AND E.num != Y.num AND N.num != D.num AND N.num != O.num 
AND N.num != R.num AND N.num != Y.num AND D.num != O.num AND D.num != R.num 
AND D.num != Y.num AND O.num != R.num AND O.num != Y.num AND R.num != Y.num;
